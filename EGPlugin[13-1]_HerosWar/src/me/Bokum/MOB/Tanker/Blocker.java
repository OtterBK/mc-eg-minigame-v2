package me.Bokum.MOB.Tanker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.ConeEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;

public class Blocker extends Ability{

	public Player blocker;
	public boolean shielding = false;
	public Player shieldtarget = null;
	public boolean ultimate = false;
	
	public Blocker(String playername, Player p){
		super(playername, "블로커");
		blocker = p;
		ItemStack item = new ItemStack(351, 1, (byte) 4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b블로커 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §64~6");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1");
		lorelist.add("§f- §7공격 : §6★★☆☆☆");
		lorelist.add("§f- §7기동 : §6☆☆☆☆☆");
		lorelist.add("§f- §7지원 : §6★★★★★");
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
		
		Timer();
	}
	
	public void Timer(){
		timernum2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(!shielding && shieldtarget == null && blocker.getExp() < 1){
					if(can_passive){
						blocker.setExp(blocker.getExp()+0.15f > 1 ? 1 : blocker.getExp() + 0.15f);
					} else {
						blocker.setExp(blocker.getExp()+0.15f > 1 ? 1 : blocker.getExp() + 0.15f);
					}
					blocker.getWorld().playSound(blocker.getLocation(), Sound.DIG_STONE, 1.5f, 0.3f);
				}
			}
		}, 0l, 20l);
	}
	
	public void description(){
		blocker.sendMessage("§6============= §f[ §b블로커 §f] - §e직업군 §7: §cTanker §6=============\n"
				+ "§f- §a주스킬 §7: §d방벽 전개\n§7자신에게 방벽을 전개합니다. 한번 더 사용하면 취소됩니다. 방벽이 전개된 상태에서 받는 데미지는 3감소됩니다.\n"
				+ "§f- §a보조스킬 §7: §d방벽 전달\n§715칸내의 바라보는 팀원들에게 장벽을 전개합니다. 한번 더 사용하면 취소됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d접근 금지\n§712초간 모든 데미지가 2상승하며 타격된 적을 멀리 밀쳐냅니다. \n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d강화 방벽\n§7방벽 게이지 충전 속도가 1.5배가 됩니다.");
	}
	
	public void PrimarySkill(final Player p){
			if(shielding){
				Bukkit.getScheduler().cancelTask(timernum);
				blocker.sendMessage(Main.MS+"방벽전개가 취소됩니다.");
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
				reducedamage = 0;
				shielding = false;
			}else{
				shielding = true;
				p.sendMessage(Main.MS+"방벽이 활성화 되었습니다.");
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 2.0f);
				reducedamage = 3;
				timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
					public void run(){
						if(shielding){
							if(p.getExp() <= 0){
								Bukkit.getScheduler().cancelTask(timernum);
								blocker.sendMessage(Main.MS+"에너지가 부족하여 방벽전개가 취소됩니다.");
								p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
								reducedamage = 0;
								shielding = false;
							}else{
								p.setExp(p.getExp()-0.1f < 0 ? 0 : p.getExp() - 0.1f);
								blocker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1));
							      CircleEffect circleeffect = new CircleEffect(Main.effectManager);
							      circleeffect.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_WITCH;
							      circleeffect.setEntity(p);;
							      circleeffect.period = 1;
							      circleeffect.iterations = 20;
							      circleeffect.speed = 1;
							      circleeffect.particleOffsetY = 1;
							      circleeffect.start();
							}
						} else {
							Bukkit.getScheduler().cancelTask(timernum);
						}

					}
				}, 0l, 20l);
			}
			}

	public void SecondarySkill(final Player p){
			if(shieldtarget != null){
				Bukkit.getScheduler().cancelTask(timernum);
				blocker.sendMessage(Main.MS+"방벽전개가 취소됩니다.");
				shieldtarget.sendMessage(Main.MS+"방벽이 사라졌습니다.");
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
				shieldtarget.playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
				MobSystem.ablist.get(shieldtarget.getName()).reducedamage = 0;
				shieldtarget = null;
				shielding = false;
			}else{
				Player t = MobSystem.Getcorsurplayer(p, 15);
				if(t == null || MobSystem.getEnemy(p).contains(t)){
					p.sendMessage(Main.MS+"15칸내에 바라보는곳에 팀이 없습니다.");
					p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 2.0f, 3.0f);
					return;
				}
				shieldtarget = t;
				t.sendMessage(Main.MS+p.getName()+" 님에게 방벽을 받았습니다.");
				p.sendMessage(Main.MS+t.getName()+" 님에게 방벽을 부여합니다.");
				t.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 2.0f);
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 2.0f);
				MobSystem.ablist.get(shieldtarget.getName()).reducedamage = 3;
				shielding = true;
				timernum1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
					public void run(){
						if(shielding){
							if(p.getExp() <= 0){
								Bukkit.getScheduler().cancelTask(timernum1);
								blocker.sendMessage(Main.MS+"에너지가 부족하여 방벽전개가 취소됩니다.");
								shieldtarget.sendMessage(Main.MS+"방벽이 사라졌습니다.");
								p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
								shieldtarget.playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
								shieldtarget = null;
								shielding = false;
								MobSystem.ablist.get(shieldtarget.getName()).reducedamage = 0;
							}else{
								p.setExp(p.getExp()-0.1f < 0 ? 0 : p.getExp() - 0.1f);
								blocker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1));
							      CircleEffect circleeffect = new CircleEffect(Main.effectManager);
							      circleeffect.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_WITCH;
							      circleeffect.setEntity(shieldtarget);;
							      circleeffect.period = 1;
							      circleeffect.iterations = 20;
							      circleeffect.speed = 1;
							      circleeffect.particleOffsetY = 1;
							      circleeffect.start();
							}
						} else {
							Bukkit.getScheduler().cancelTask(timernum1);
						}

					}
				}, 0l, 20l);
			}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 143, true);
			p.sendMessage(Main.MS+"강화 됐습니다!");
			p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_SCREAM, 2.0f, 2.0f);
			ultimate = true;
			Cooldown.SkillCountdown(p, 12, "접근 금지 스킬");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					ultimate = false;
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
				}
			}, 240);
		}
	}
	
	public void onDamaged(EntityDamageEvent e){
		e.setDamage((int) (e.getDamage()*1));
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
			if(!can_Ultimate){ blocker.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(ultimate){
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			e.setDamage(MobSystem.Getrandom(6, 8));
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_RETRACT, 2.0f, 2.0f);
			ParticleEffect.EXPLOSION_LARGE.display(0.03f,0.03f, 0.03f, 0.02f, 5, p.getLocation(), 20);
			final Vector direction = d.getEyeLocation().getDirection().multiply(3);
			p.setVelocity(direction);
		} else {
			e.setDamage(MobSystem.Getrandom(4, 6));
		}
	}
}
