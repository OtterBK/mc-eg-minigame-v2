package me.Bokum.MOB.Attacker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;

public class BowMaster extends Ability{
	
	public boolean Freezing_arrow = false;
	public boolean Speed_arrow = false;
	public boolean Explode_arrow = false;
	public Player bowmaster;
	
	public BowMaster(String playername, Player p){
		super(playername, "보우 마스터");
		bowmaster = p;
		ItemStack item = new ItemStack(261, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.setDisplayName("§f[ §b보우 마스터 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §61~10(시위를 당긴만큼)");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1배");
		lorelist.add("§f- §7공격 : §6★★★★☆");
		lorelist.add("§f- §7기동 : §6☆☆☆☆☆");
		lorelist.add("§f- §7지원 : §6★★☆☆☆");
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
		
		p.getInventory().setItem(6, new ItemStack(262, 64));
	}
	
	public void description(){
		bowmaster.sendMessage("§6============= §f[ §b 보우 마스터 §f] - §e직업군 §7: §cAttacker §6============= ");
		bowmaster.sendMessage("§f- §a주스킬 §7: §d얼음화살\n§7얼음화살을 날려 맞은 적의 이동속도를 6초간 현저히 떨어뜨립니다.\n§c4초간 아무도 맞추지 못하면 효력이 사라집니다.\n");	
		bowmaster.sendMessage("§f- §a보조스킬 §7: §d소나무 화살\n§7기존보다 6배 빠른 화살을 발사합니다. 중력의 영향을 거의 받지 않으며 데미지 또한 더 높습니다.\n§c3초간 아무도 맞추지 못하면 효력이 사라집니다.\n");
		bowmaster.sendMessage("§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d폭발화살\n§7폭탄이 부착된 화살을 발사합니다. 대상을 맞춘 후 2.5초후 폭발합니다.\n§c10초간 아무도 맞추지 못하면 효력이 사라집니다.\n");
		bowmaster.sendMessage("§f- §2패시브§f(§c구매시 사용가능§f) §7: §d신중한 조준\n§7좀 더 신중하게 쏩니다. 모든 스킬의 유효시간이 1.5배가 됩니다.");
	}
	
	public void PrimarySkill(final Player p){
		Long time = can_passive ? 160l : 80l;
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 13, true);
			Freezing_arrow = true;
			p.getWorld().playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
			ParticleEffect.WATER_SPLASH.display(1, 1, 1, 0, 70, p.getLocation(), 50);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					if(Freezing_arrow){
						p.sendMessage(Main.MS+"- §7얼음화살의 유효시간이 끝났습니다.");
						Freezing_arrow = false;
					}
				}
			}, time);
		}
	}
	
	public void SecondarySkill(final Player p){
		Long time = can_passive ? 120l : 60l;
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 22, true);
			Speed_arrow = true;
			p.getWorld().playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
			ParticleEffect.CLOUD.display(1, 1, 1, 0, 70, p.getLocation(), 50);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					if(Speed_arrow){
						p.sendMessage(Main.MS+"- §7무중력화살의 유효시간이 끝났습니다.");
						Speed_arrow = false;
					}
				}
			}, time);
		}
	}
	
	public void UltimateSkill(final Player p){
		Long time = can_passive ? 300l : 200l;
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 104, true);
			Explode_arrow = true;
			p.getWorld().playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
			ParticleEffect.REDSTONE.display(1, 1, 1, 0, 100, p.getLocation(), 50);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					if(Explode_arrow){
						p.sendMessage(Main.MS+"- §7폭발화살의 유효시간이 끝났습니다.");
						Explode_arrow = false;
					}
				}
			}, time);
		}
	}
	
	public void onArrowHit(Player t){
		if(MobSystem.getTeam(t).contains(bowmaster)) return;
		if(Freezing_arrow){
			Freezing_arrow = true;
			ParticleEffect.WATER_SPLASH.display(1, 1, 1, 0.02f, 100, t.getLocation(), 50);
			t.getWorld().playSound(t.getLocation(), Sound.DIG_SNOW, 2.0f, 1.0f);
			t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 49));
			t.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 199));
		}
		if(Explode_arrow){
			Explode_arrow = false;
			final Location l = t.getLocation();
			ParticleEffect.REDSTONE.display(1, 1, 1, 0.02f, 150, l, 50);
			l.getWorld().playSound(t.getLocation(), Sound.FUSE, 3.0f, 0.7f);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.FUSE, 3.0f, 0.8f);
					ParticleEffect.REDSTONE.display(1, 1, 1, 0.02f, 50, l, 50);
				}
			}, 10l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.FUSE, 3.0f, 0.8f);
					ParticleEffect.REDSTONE.display(1.2f, 1.2f, 1.2f, 0.02f, 200, l, 50);
				}
			}, 20l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.FUSE, 3.0f, 0.8f);
					ParticleEffect.REDSTONE.display(1.4f, 1.4f, 1.4f, 0.02f, 250, l, 50);
				}
			}, 30l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.FUSE, 3.0f, 0.9f);
					ParticleEffect.REDSTONE.display(1.6f, 1.6f, 1.6f, 0.02f, 300, l, 50);
				}
			}, 40l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					ParticleEffect.EXPLOSION_HUGE.display(5.0f, 5.0f, 5.0f, 0.03f, 10, l, 20);
					l.getWorld().playSound(l, Sound.EXPLODE, 3.0f, 1.0f);
					for(Player t : MobSystem.getEnemy(bowmaster)){
						if(t.getLocation().distance(l) <= 8){
							t.damage(9-(int)t.getLocation().distance(l), bowmaster);
						}
					}
				}
			}, 50l);
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
			e.getPlayer().getInventory().setHeldItemSlot(0);
			if(!can_Ultimate){ bowmaster.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onLauch(ProjectileLaunchEvent e){
		if(e.getEntity() instanceof Arrow){
			if(!Speed_arrow) return;
			Arrow arrow = (Arrow) e.getEntity();
			Player p = (Player) arrow.getShooter();
			Speed_arrow = false;
			Block target = p.getTargetBlock(null, 150);
			Vector velocity = (target.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize()).multiply(6);
			arrow.setVelocity(velocity);
		}
	}
}
