package me.Bokum.Supporter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.LineEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;

public class AreaCreator extends Ability{

	public Player ac;
	public Location healarea = null;
	public Location shieldarea;
	
	public AreaCreator(String playername, Player p){
		super(playername, "영역 생성자");
		ac = p;
		ItemStack item = new ItemStack(351, 1, (byte) 2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b영역 생성자 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §65");
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
	}
	
	public void description(){
		ac.sendMessage("§6============= §f[ §b영역 생성자 §f] - §e직업군 §7: §cSupporter §6=============\n"
				+ "§f- §a주스킬 §7: §d치유 영역\n§720초간 10칸내의 팀원들의 체력을 2초당 2회복시켜주는 영역을 생성합니다.\n"
				+ "§f- §a보조스킬 §7: §d삭제 영역\n§720초간 10칸내의 팀원들의 상태이상을 회복시켜줍니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d공격 영역\n§710초간 자신의 10칸내의 적들에게 초당 2의 데미지를 주는 영역을 생성합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d영역 확장\n§7모든 영역의 범위가 3칸 늘어납니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 30, true);
			if(healarea != null){
				Bukkit.getScheduler().cancelTask(timernum);
				healarea.getBlock().setType(Material.AIR);
				MobSystem.healarea.remove(healarea);
				healarea = null;
			}
			p.getLocation().getBlock().setType(Material.SPONGE);
			healarea = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			MobSystem.healarea.put(healarea, this);
			healarea.getWorld().playSound(healarea, Sound.DOOR_OPEN, 2.0f, 0.5f);
			p.sendMessage(Main.MS+"치유 영역 생성 완료");
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(healarea != null){
						Location l = new Location(healarea.getWorld(), healarea.getBlockX(), healarea.getBlockY(), healarea.getBlockZ());
						l.setY(l.getY()+1);
						ParticleEffect.HEART.display(0f, 0f, 0f, 0f, 1, l, 30);
					    HelixEffect he = new HelixEffect(Main.effectManager);
					    he.particle = de.slikey.effectlib.util.ParticleEffect.TOWN_AURA;
					    he.period = 1;
					    he.particles = 20;
					    he.radius = can_passive ? 10 : 8;
					    he.setLocation(l);
					    he.start();
						l.getWorld().playSound(l, Sound.ITEM_PICKUP, 1.0f, 2.0f);
						for(Player t : MobSystem.getTeam(p)){
							if(t.getLocation().distance(healarea) <= (can_passive ? 13 : 10) && Math.abs(t.getLocation().getY()-healarea.getY()) <= 3){
								Location l2 = p.getLocation();
								l2.setY(l2.getY()+2);
								MobSystem.Addhp(t, 2);
								ParticleEffect.HEART.display(0f, 0f, 0f, 0f, 1, l2, 30);
								t.getWorld().playSound(t.getLocation(), Sound.ITEM_PICKUP, 1.0f, 2.0f);
							}
						}
					} else {
						Bukkit.getScheduler().cancelTask(timernum);
					}

				}
			}, 20l, 40l);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 30, true);
			if(shieldarea != null){
				Bukkit.getScheduler().cancelTask(timernum);
				shieldarea.getBlock().setType(Material.AIR);
				MobSystem.shieldarea.remove(shieldarea);
				shieldarea = null;
			}
			p.getLocation().getBlock().setType(Material.SPONGE);
			shieldarea = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			MobSystem.shieldarea.put(shieldarea, this);
			p.sendMessage(Main.MS+"삭제 영역 생성 완료");
			shieldarea.getWorld().playSound(shieldarea, Sound.DOOR_OPEN, 2.0f, 0.5f);
			timernum1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(shieldarea != null){
						Location l = new Location(shieldarea.getWorld(), shieldarea.getBlockX(), shieldarea.getBlockY(), shieldarea.getBlockZ());
						l.setY(l.getY()+1);
						ParticleEffect.VILLAGER_HAPPY.display(0.1f, 0.1f, 0.1f, 0.1f, 15, l, 30);
					    HelixEffect he = new HelixEffect(Main.effectManager);
					    he.particle = de.slikey.effectlib.util.ParticleEffect.CRIT;
					    he.period = 1;
					    he.particles = 20;
					    he.radius = can_passive ? 10 : 8;
					    he.setLocation(l);
					    he.start();
						l.getWorld().playSound(l, Sound.IRONGOLEM_THROW, 1.0f, 0.5f);
						for(Player t : MobSystem.getTeam(p)){
							if(t.getLocation().distance(shieldarea) <= (can_passive ? 13 : 10) && Math.abs(t.getLocation().getY()-shieldarea.getY()) <= 3){
								Location l2 = p.getLocation();
								l2.setY(l2.getY()+2);
								t.removePotionEffect(PotionEffectType.SLOW);
								t.removePotionEffect(PotionEffectType.POISON);
								t.removePotionEffect(PotionEffectType.WITHER);
								t.removePotionEffect(PotionEffectType.CONFUSION);
								t.removePotionEffect(PotionEffectType.BLINDNESS);
								ParticleEffect.VILLAGER_HAPPY.display(0.1f, 0.1f, 0.1f, 0f, 15, l2, 30);
								t.getWorld().playSound(t.getLocation(), Sound.IRONGOLEM_THROW, 1.0f, 0.5f);
							}
						}
					} else {
						Bukkit.getScheduler().cancelTask(timernum1);
					}

				}
			}, 20l, 40l);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 135, true);
			p.sendMessage(Main.MS+"공격 영역 생성 완료");
			timertime2 = 11;
			timernum2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(--timertime2 > 0){
						p.setExp((float)timertime2/10);
						Location l = p.getLocation();
						l.setY(l.getY()+1);
						ParticleEffect.VILLAGER_ANGRY.display(0f, 0f, 0f, 0f, 1, l, 30);
					    HelixEffect he = new HelixEffect(Main.effectManager);
					    he.particle = de.slikey.effectlib.util.ParticleEffect.ENCHANTMENT_TABLE;
					    he.period = 1;
					    he.particles = 20;
					    he.radius = can_passive ? 10 : 8;
					    he.setLocation(l);
					    he.start();
						l.getWorld().playSound(l, Sound.BLAZE_HIT, 1.0f, 1.5f);
						for(Player t : MobSystem.getEnemy(p)){
							if(t.getLocation().distance(p.getLocation()) <= (can_passive ? 13 : 10) && Math.abs(t.getLocation().getY()-p.getLocation().getY()) <= 3){
								Location l2 = p.getLocation();
								l2.setY(l2.getY()+2);
								t.damage(2, p);
								ParticleEffect.VILLAGER_ANGRY.display(0f, 0f, 0f, 0f, 1, l2, 30);
								t.getWorld().playSound(t.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.5f);
							}
						}
					} else {
						Bukkit.getScheduler().cancelTask(timernum2);
						p.sendMessage(Main.MS+"공격 영역의 지속시간이 끝났습니다.");
						p.setExp(0);
					}

				}
			}, 20l, 20l);
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
			if(!can_Ultimate){ ac.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		e.setDamage(5);
	}
}




