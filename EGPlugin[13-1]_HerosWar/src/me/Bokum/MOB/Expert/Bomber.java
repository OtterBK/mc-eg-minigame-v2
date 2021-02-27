package me.Bokum.MOB.Expert;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.slikey.effectlib.effect.LineEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;
import net.minecraft.server.v1_5_R3.DamageSource;

public class Bomber extends Ability{

	public Player bomber;
	public List<Location> bomblist = new ArrayList<Location>();
	
	public Bomber(String playername, Player p){
		super(playername, "붐버");
		bomber = p;
		ItemStack item = new ItemStack(318, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b붐버 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §63~4");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1");
		lorelist.add("§f- §7공격 : §6★★★★★");
		lorelist.add("§f- §7기동 : §6☆☆☆☆☆");
		lorelist.add("§f- §7지원 : §6★☆☆☆☆");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		p.getInventory().setItem(0 ,item);
		
		item = new ItemStack(265, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c주스킬 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(1 ,item);
		
		item = new ItemStack(264, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c보조스킬 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(2 ,item);
		
		item = new ItemStack(388, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c궁극기 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(3 ,item);
	}
	
	public void description(){
		bomber.sendMessage("§6============= §f[ §b붐버 §f] - §e직업군 §7: §cExpert §6=============\n"
				+ "§f- §a주스킬 §7: §d폭탄 설치\n§7폭탄을 설치합니다. §c최대 3개까지\n"
				+ "§f- §a보조스킬 §7: §d기폭\n§7설치된 폭탄을 폭파합니다. 최대8 데미지\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d로켓 런쳐\n§730칸내 바라보는 블럭을 향해 소형 미사일을 발사합니다. 최대18 데미지\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d자폭\n§7사망시 20%확률로 폭발합니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 3, true);
			p.getWorld().playSound(p.getLocation(), Sound.CHICKEN_HURT, 2.0f, 2.0f);
			ParticleEffect.SNOWBALL.display(0.2f, 0.2f, 0.2f, 0, 30, p.getLocation(), 30);
			p.sendMessage(Main.MS+"폭탄을 설치했습니다.");
			if(bomblist.size() >= 3){
				bomblist.get(0).getBlock().setType(Material.AIR);
				p.getLocation().getBlock().setType(Material.GOLD_PLATE);
				p.sendMessage(Main.MS+"폭탄 최대량을 넘어 1번째 폭탄을 해체했습니다.");
				bomblist.set(0, p.getLocation());
			} else {
				bomblist.add(p.getLocation());
				p.getLocation().getBlock().setType(Material.GOLD_PLATE);
			}
			p.setExp(1);
		}
	}

	public void SecondarySkill(final Player p){
		if(bomblist.size() <= 0){
			p.sendMessage(Main.MS+"설치된 폭탄이 없습니다.");
		} else {
			int amt = 1;
			for(final Location l : bomblist){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
					public void run(){
						l.getBlock().setType(Material.AIR);
						ParticleEffect.EXPLOSION_LARGE.display(1f, 1f, 1f, 0.1f, 30, l, 30);
						l.getWorld().playSound(l, Sound.EXPLODE, 2.0f, 1.0f);
						for(Player t : MobSystem.getEnemy(p)){
							if(t.getLocation().distance(l) < 6){
								ignore = true;
								t.damage(9-(int)t.getLocation().distance(l), p);
							}
						}
					}
				}, amt++*10);
			}
			bomblist.clear();
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			final Block b = p.getTargetBlock(null, 30);
			if(b == null || b.getType() == Material.AIR){
				p.sendMessage(Main.MS+"30칸내 블럭을 향해 사용하세요.");
				return;
			}
			Cooldown.Setcooldown(p, "궁극기", 127, true);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 0.1f);
			LineEffect le = new LineEffect(Main.effectManager);
			le.particles = 30;
			le.period = 5;
			le.iterations = 20;
			le.speed = 0f;
			le.particleOffsetX = 0;
			le.particleOffsetY = 0;
			le.particleOffsetZ = 0;
			le.setEntity(p);
			le.setTargetLocation(b.getLocation());
			le.start();
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 99));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 199));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getLocation().getWorld().playSound(p.getLocation(), Sound.CLICK, 1.0f, 0.5f);
					b.getWorld().playSound(b.getLocation(), Sound.BLAZE_HIT, 2.5f, 0.25f);
				}
			}, 0l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getLocation().getWorld().playSound(p.getLocation(), Sound.CLICK, 1.0f, 0.5f);
					b.getWorld().playSound(b.getLocation(), Sound.BLAZE_HIT, 2.5f, 0.25f);
				}
			}, 20l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getLocation().getWorld().playSound(p.getLocation(), Sound.CLICK, 1.0f, 0.5f);
					b.getWorld().playSound(b.getLocation(), Sound.BLAZE_HIT, 2.5f, 0.25f);
				}
			}, 40l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getLocation().getWorld().playSound(p.getLocation(), Sound.CLICK, 1.0f, 0.5f);
					b.getWorld().playSound(b.getLocation(), Sound.BLAZE_HIT, 2.5f, 0.25f);
				}
			}, 60l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					if(!p.isDead()){
						p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0f, 2.5f);
						ParticleEffect.EXPLOSION_HUGE.display(3f, 3f, 3f, 0.1f, 25, b.getLocation(), 30);
						b.getLocation().getWorld().playSound(b.getLocation(), Sound.EXPLODE, 2.0f, 1.0f);
						for(Player t : MobSystem.getEnemy(p)){
							if(t.getLocation().distance(b.getLocation()) < 13){
								ignore = true;
								t.damage(19-(int)t.getLocation().distance(b.getLocation()), p);
							}
						}
					}
				}
			}, 80l);
		}
	}
	
	public void Active(PlayerItemHeldEvent e){
		if(e.getNewSlot() == 1){
			e.getPlayer().getInventory().setHeldItemSlot(0);
			PrimarySkill(e.getPlayer());
		}
		if(e.getNewSlot() == 2){
			e.getPlayer().getInventory().setHeldItemSlot(0);
			SecondarySkill(e.getPlayer());
		}
		if(e.getNewSlot() == 3){
			if(!can_Ultimate){ bomber.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onDeath(PlayerDeathEvent e){
		if(can_passive && MobSystem.Getrandom(0, 9) == 0){
			Player p = e.getEntity();
			p.sendMessage(Main.MS+"폭발했습니다!");
			for(Player t : MobSystem.getEnemy(p)){
				if(t.getLocation().distance(p.getLocation()) < 7){
					t.damage(7-(int)t.getLocation().distance(p.getLocation()), p);
				}
			}
		}

	}
	
	public void onHit(EntityDamageByEntityEvent e){
		e.setDamage(MobSystem.Getrandom(3, 4));
	}
}


