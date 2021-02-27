package me.Bokum.MOB.Expert;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;
import me.Bokum.MOB.Utility.Vector3D;

public class Tracer extends Ability{
	
	public int Backtime_health[] = new int[25];
	public Location Backtime_loc[] = new Location[25];
	//public float Backtime_count[] = new float[25];
	public int backtimer_num = 0;
	public int backtimer_time = 0;
	public boolean Backtiming = false;
	public int exptime = 0;
	public Player tracer;
	
	public Tracer(String playername, Player p){
		super(playername, "트레이서");
		ItemStack item = new ItemStack(267, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b트레이서 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §64~6");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1.43배");
		lorelist.add("§f- §7공격 : §6★★☆☆☆");
		lorelist.add("§f- §7기동 : §6★★★★☆");
		lorelist.add("§f- §7지원 : §6☆☆☆☆☆");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		
		p.getInventory().setItem(0, item);
		
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
		
		tracer = p;
		for(int i = 0; i < Backtime_health.length; i++){
			Backtime_health[i] = p.getHealth();
		}
		for(int i = 0; i < Backtime_loc.length; i++){
			Backtime_loc[i] = p.getLocation();
		}
		/*for(int i = 0; i < Backtime_count.length; i++){
			Backtime_count[i] = p.getExp();
		}*/
		Cooldown.Setcooldown(p, "보조스킬", 7, true);
		p.setExp(1);
		Timer();
	}
	
	public void onDamaged(EntityDamageEvent e){
		if(Backtiming) {
			e.setDamage(0);
			return;
		}
		e.setDamage((int) (e.getDamage()*1.43));
	}
	
	public void Timer(){
		timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tracer.getExp() < 1 && ++exptime > (can_passive ? 12 : 15)){
					exptime = 0;
					tracer.setExp(tracer.getExp()+0.33f > 1 ? 1 : tracer.getExp()+0.33f);
					tracer.getWorld().playSound(tracer.getLocation(), Sound.SHOOT_ARROW, 1.0f, 2.0f);
				}
				if(!Backtiming){
						for(int i = 0; i < Backtime_health.length-1; i++){
							Backtime_health[i] = Backtime_health[i+1];
						}
						for(int i = 0; i < Backtime_loc.length-1; i++){
							Backtime_loc[i] = Backtime_loc[i+1];
						}
						/*for(int i = 0; i < Backtime_count.length-1; i++){
							Backtime_count[i] = Backtime_count[i+1];
						}*/
						Backtime_health[24] = tracer.getHealth();
						Backtime_loc[24] = tracer.getLocation();
						//Backtime_count[24] = tracer.getExp();
				}
			}
		}, 0l, 4l);
	}
	
	public void description(){
		tracer.sendMessage("§6============= §f[ §b트레이서 §f] - §e직업군 §7: §cExpert §6=============\n"
				+ "§f- §a주스킬 §7: §d점멸\n§7전방 5칸앞으로 이동합니다. §c벽을 뚫지는 못합니다. 3초마다 횟수가 1증가합니다.\n"
				+ "§f- §a보조스킬 §7: §d시간역행\n§75초전의 시간으로 이동합니다. §c체력 또한 5초전으로 복구됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d펄스폭탄\n§7자신의 위치에 범위는 작지만 강력한 폭탄을 설치합니다. 2.4초후 폭발합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d효율성 증가\n§7좀 더 효율적으로 점멸을 사용합니다. 점멸의 횟수가 회복되는 간격이 3초에서 2.4초로 감소합니다.");	
	}
	
	public void PrimarySkill(final Player p){
		if(!(p.getExp() >= 0.25f)){
			p.sendMessage(Main.MS+"점멸 카운트가 부족합니다.");
			p.getWorld().playSound(p.getLocation(), Sound.CLICK, 1.2f, 1.8f);
			 return;
		}
		p.setExp(p.getExp() - 0.33f < 0 ? 0 : p.getExp() - 0.33f);
		p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);
		ParticleEffect.SMOKE_LARGE.display(1, 1, 1, 0, 80, p.getLocation(), 15);
        final Player observer = p;
        
        Location observerPos = observer.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        
        Location loc = p.getLocation();
        
        for(int i = 1; i < 6; i++){
            Vector3D observerEnd = observerStart.add(observerDir.multiply(i));
            loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
        	if(loc.getBlock().getType() != Material.AIR){
        		if(i == 1){
        			loc = p.getLocation();

        		} else {
        			observerEnd = observerStart.add(observerDir.multiply(i-1));
                    loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
        		}
    			break;
        	}
        }         
        loc.setPitch(p.getLocation().getPitch());
        loc.setYaw(p.getLocation().getYaw());
        p.teleport(loc);
	}
	
	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 23, true);
			Backtiming = true;
			backtimer_time = 24;
			backtimer_num = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(backtimer_time < 0 || p.isDead()){
						Bukkit.getScheduler().cancelTask(backtimer_num);
						Backtime_loc[0].getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 3.0f);
						Backtiming = false;
					}
					ParticleEffect.DRIP_WATER.display(0.3f, 0.3f, 0.3f, 0, 20, p.getLocation(), 30);
					p.getWorld().playSound(p.getLocation(), Sound.SHOOT_ARROW, 2.0f, 3.0f);
					try{
					p.setHealth(Backtime_health[backtimer_time]);
					p.teleport(Backtime_loc[backtimer_time]);
					} catch(Exception e){
						Bukkit.getScheduler().cancelTask(backtimer_num);
						Backtime_loc[0].getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 3.0f);
						Backtiming = false;
					}
					backtimer_time--;
				}
			}, 1l, 1l);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 124, true);
			final Location l = p.getLocation();
			ParticleEffect.REDSTONE.display(1, 1, 1, 0.02f, 150, l, 50);
			l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
					ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1.2f, 1.2f, 0.03f, 35, l, 20);
				}
			}, 4l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
					ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1.2f, 1.2f, 0.03f, 35, l, 20);
				}
			}, 8l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
					ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1.2f, 1.2f, 0.03f, 35, l, 20);
				}
			}, 12l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
					ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1.2f, 1.2f, 0.03f, 35, l, 20);
				}
			}, 16l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					l.getWorld().playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
					ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1.2f, 1.2f, 0.03f, 35, l, 20);
				}
			}, 20l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					ParticleEffect.EXPLOSION_NORMAL.display(1.5f, 1.5f, 1.5f, 0.03f, 10, l, 20);
					l.getWorld().playSound(l, Sound.EXPLODE, 3.0f, 1.0f);
					for(Player t : MobSystem.getEnemy(tracer)){
						if(t.getLocation().distance(l) <= 4){
							ignore = true;
							t.damage((12-(int)t.getLocation().distance(l))*2, tracer);
						}
					}
				}
			}, 24l);
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(tracer.getInventory().getHeldItemSlot() == 0) e.setDamage(MobSystem.Getrandom(4, 6));
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
			if(!can_Ultimate){ tracer.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			UltimateSkill(e.getPlayer());
		}
	}
}
