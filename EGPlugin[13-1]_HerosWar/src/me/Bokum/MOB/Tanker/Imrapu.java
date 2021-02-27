package me.Bokum.MOB.Tanker;

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
import de.slikey.effectlib.effect.SphereEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;
import me.Bokum.MOB.Utility.Vector3D;

public class Imrapu extends Ability{

	public Player imrapu;
	
	public Imrapu(String playername, Player p){
		super(playername, "임라프");
		imrapu = p;
		ItemStack item = new ItemStack(338, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b임라프 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §66~8");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1");
		lorelist.add("§f- §7공격 : §6★★★★☆");
		lorelist.add("§f- §7기동 : §6☆☆☆☆☆");
		lorelist.add("§f- §7지원 : §6★★★☆☆");
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
		imrapu.sendMessage("§6============= §f[ §b임라프 §f] - §e직업군 §7: §cTanker §6=============\n"
				+ "§f- §a주스킬 §7: §d그랩\n§715칸내의 적을 자신에게 끕니다. 끌어진 적은 3초간 침묵상태가됩니다.\n"
				+ "§f- §a보조스킬 §7: §d순환\n§73초의 시간을 들여 체력을 10회복합니다. §c사용중에는 이동불가, 스킬사용불가\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d블랙홀\n§715칸내의 블럭을 바로보고 사용이 가능합니다."
				+ "\n바라본 블럭에 블랙홀을  5초간 생성하여 1초에 한번씩 주위 10칸내의 적을 끌어당깁니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d더욱더 멀리!\n§7그랩스킬의 범위가 5칸 늘어납니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 16, true);
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 2.0f, 2.0f);
			final Player t = MobSystem.Getcorsurplayer(p, can_passive ? 20 : 15);
			if(t == null || MobSystem.getTeam(p).contains(t)){
				p.sendMessage(Main.MS+"범위내에 바라보는곳에 적이 없었습니다.");
		        final Player observer = p;
		        
		        Location observerPos = observer.getEyeLocation();
		        final Vector3D observerDir = new Vector3D(observerPos.getDirection());
		        final Vector3D observerStart = new Vector3D(observerPos);
	            Vector3D observerEnd = observerStart.add(observerDir.multiply(20));
	            Location loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y-1, observerEnd.z); 
				LineEffect le = new LineEffect(Main.effectManager);
				le.particle = de.slikey.effectlib.util.ParticleEffect.SNOW_SHOVEL;
				le.particles = 30;
				le.period = 2;
				le.iterations = 20;
				le.speed = 0f;
				le.particleOffsetX = 0;
				le.particleOffsetY = 0;
				le.particleOffsetZ = 0;
				le.setEntity(p);
				le.setTargetLocation(loc);
				le.start();
				
				return;
			}
			
			LineEffect le = new LineEffect(Main.effectManager);
			le.particle = de.slikey.effectlib.util.ParticleEffect.SNOW_SHOVEL;
			le.particles = 30;
			le.period = 2;
			le.iterations = 20;
			le.speed = 0f;
			le.particleOffsetX = 0;
			le.particleOffsetY = 0;
			le.particleOffsetZ = 0;
			le.setEntity(p);
			le.setTargetEntity(t);
			le.start();
			
	        MobSystem.CancelSkill(t, 3);
			
	        final Player observer = p;
	        
	        Location observerPos = observer.getEyeLocation();
	        final Vector3D observerDir = new Vector3D(observerPos.getDirection());
	        final Vector3D observerStart = new Vector3D(observerPos);
			timertime = (int) t.getLocation().distance(p.getLocation());
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(--timertime > 0){
						t.getWorld().playSound(t.getLocation(), Sound.SKELETON_DEATH, 1.5f, 3.0f);
				        Location loc;
			            Vector3D observerEnd = observerStart.add(observerDir.multiply(timertime));
			            loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y-1, observerEnd.z);       
				        loc.setPitch(t.getLocation().getPitch());
				        loc.setYaw(t.getLocation().getYaw());
				        t.teleport(loc);
					} else {
						Bukkit.getScheduler().cancelTask(timernum);
						t.getWorld().playSound(t.getLocation(), Sound.SKELETON_HURT, 1.5f, 2.0f);
					}
				}
			}, 10l, 1l);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 21, true);
			p.getWorld().playSound(p.getLocation(), Sound.BREATH, 1.5f, 0.6f);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 99));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 199));
			cc = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					MobSystem.Addhp(p, 10);
					p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 1.5f, 1.0f);
					ParticleEffect.VILLAGER_HAPPY.display(0.3f, 0.3f, 0.3f, 0.1f, 30, p.getLocation(), 20);
					cc = false;
				}
			}, 60l);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Block tb = null;
			try{
				tb = p.getTargetBlock(null, 15);
			}catch(Exception e){}
			if(tb == null){
				p.sendMessage(Main.MS+"오류 발생 다시 사용해주세요.");
				return;
			}
			Cooldown.Setcooldown(p, "궁극기", 168, true);
			final Location loc = new Location(tb.getWorld(), tb.getLocation().getBlockX(), tb.getLocation().getBlockY()+1, tb.getLocation().getBlockZ());
			loc.getWorld().playSound(loc, Sound.IRONGOLEM_HIT, 1.0f, 0.5f);
			SphereEffect se = new SphereEffect(Main.effectManager);
			se.setLocation(loc);
			se.radius = 0.1f;
			se.particles = 240;
			se.particleOffsetX = 1;
			se.particleOffsetY = 1;
			se.particleOffsetZ = 1;
			se.period = 5;
			se.iterations = 20;
			se.start();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					Ultimate(loc);
					loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 0.5f);
				}
			}, 20l);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					Ultimate(loc);
					loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 0.5f);
				}
			}, 40l);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					Ultimate(loc);
					loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 0.5f);
				}
			}, 60l);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					Ultimate(loc);
					loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 0.5f);
				}
			}, 80l);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					Ultimate(loc);
					p.sendMessage(Main.MS+"블랙홀이 사라졌습니다.");
				}
			}, 100l);
		}
	}
	
	public void Ultimate(final Location l){
		timertime1 = 10;
		timernum1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(--timertime1 > 0){
					for(Player t : MobSystem.getEnemy(imrapu)){
						if(t.getLocation().distance(l) <= 10){
							t.teleport(l);
						}
					}
				} else {
					Bukkit.getScheduler().cancelTask(timernum1);
				}
			}
		}, 10l, 1l);
	}
	
	public void onDamaged(EntityDamageEvent e){
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
			if(!can_Ultimate){ imrapu.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		e.setDamage(MobSystem.Getrandom(6, 8));
	}
}



