package me.Bokum.MOB.Ability;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.Bokum.MOB.Main;
import me.Bokum.MOB.Game.MobSystem;

public class Ability
{
	public final String playerName;
	public final String abilityName;
	public boolean can_passive = false;
	public boolean can_Ultimate = false;
	public int timernum = 0;
	public int timertime = 0;
	public int timernum1 = 0;
	public int timertime1 = 0;
	public int timernum2 = 0;
	public int timertime2 = 0;
	public int corecatchnum = 0;
	public int corecatchtime = 0;
	public int reducedamage = 0;
	public boolean cc = false;
	public boolean ignore = false;
	public Location deathloc = null;
	public Ability(String playerName, String abilityName)
	{
		this.playerName=playerName;
		this.abilityName=abilityName;
		this.can_passive=false;
		this.can_Ultimate=false;
		this.timernum = 0;
		this.timertime = 0;
		this.timernum1 = 0;
		this.timertime1 = 0;
		this.timernum2 = 0;
		this.timertime2 = 0;
		this.reducedamage = 0;
		this.cc = false;
		this.ignore = false;
		this.deathloc = null;
	}
	
	public void description(){}
	
	public void onArrowHit(Player t){}
	
	public void Active(PlayerItemHeldEvent event){}
	
	public void onBlockBreak(BlockBreakEvent e){
		Block b = e.getBlock();
		Player p = e.getPlayer();
		Location loc = new Location(b.getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ());
		if(b.getType() == Material.SPONGE){
			if(MobSystem.healarea.containsKey(loc)){
				loc.getWorld().playSound(loc, Sound.DOOR_CLOSE, 2.0f, 0.5f);
				MobSystem.healarea.get(loc).ac.sendMessage(Main.MS+"치유 영역 생성기가 파괴되었습니다.");
				p.sendMessage(Main.MS+MobSystem.healarea.get(loc).ac.getName()+"님의 치유 영역 생성기를 파괴했습니다.");
				MobSystem.healarea.get(loc).healarea = null;
				loc.getBlock().setType(Material.AIR);
				MobSystem.healarea.remove(loc);
			}
			if(MobSystem.shieldarea.containsKey(loc)){
				loc.getWorld().playSound(loc, Sound.DOOR_CLOSE, 2.0f, 0.5f);
				MobSystem.shieldarea.get(loc).ac.sendMessage(Main.MS+"삭제 영역 생성기가 파괴되었습니다.");
				p.sendMessage(Main.MS+MobSystem.shieldarea.get(loc).ac.getName()+"님의 삭제 영역 생성기를 파괴했습니다.");
				MobSystem.shieldarea.get(loc).shieldarea = null;
				loc.getBlock().setType(Material.AIR);
				MobSystem.shieldarea.remove(loc);
			}
		}
		e.setCancelled(true);
	}
	
	public void onDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		deathloc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ());
	}
	
	public void onKill(PlayerDeathEvent event){
		event.getEntity().getKiller().getInventory().addItem(new ItemStack(396, 1));
	}
	
	public void onRegain(EntityRegainHealthEvent event){}
	
	public void onHit(EntityDamageByEntityEvent event){}
	
	public void onHitDamaged(EntityDamageByEntityEvent event){
		Player p = (Player) event.getEntity();
		for(Player t : MobSystem.getTeam(p)){
			if(t.getLocation().distance(p.getLocation()) <= 8 && MobSystem.ablist.get(t.getName()).abilityName.equalsIgnoreCase("쉴더")){
				if(t.getItemInHand() != null && t.getItemInHand().getType() == Material.IRON_INGOT){
					int amt = event.getDamage();
					int shield = (int) ((t.getExp()/0.005f));
					shield -= amt;
					if(shield < 0){
						event.setDamage(shield*-1);
						t.setExp(0);
					} else {
						t.setExp(shield*0.005f);
						event.setDamage(1);
					}
					return;
				}
			}
		}
		if(reducedamage > 0){
			event.setDamage(event.getDamage()-reducedamage <= 0 ? 1 : event.getDamage()-reducedamage);
		}
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {}
	
	public void onRespawn(PlayerRespawnEvent event) {
		final Player p = event.getPlayer();
		p.setExp(0);
		event.setRespawnLocation(MobSystem.bluelist.contains(p) ? Main.Bluedeath : Main.Reddeath);
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
			public void run(){
				p.getInventory().setContents(MobSystem.iteminven.get(p.getName()));
				p.getInventory().setArmorContents(MobSystem.armorinven.get(p.getName()));
			}
		}, 1l);
		}catch(Exception e){
			p.getInventory().setContents(MobSystem.backupinven.get(p.getName()));
			p.getInventory().setArmorContents(MobSystem.backuparmor.get(p.getName()));
			p.sendMessage(Main.MS+"인벤토리를 복구하는 중 오류가 발생해 백업을 불러옵니다.");
		}
		if(MobSystem.bluelist.contains(p)) MobSystem.bluedead.put(deathloc, p);
		if(MobSystem.redlist.contains(p)) MobSystem.reddead.put(deathloc, p);
		p.sendMessage(Main.MS+"10초 후 부활합니다.");
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
			public void run(){
				if(!MobSystem.plist.contains(p) || !(MobSystem.bluelist.contains(p) ? MobSystem.bluedead : MobSystem.reddead).containsValue(p)) return;
				p.sendMessage(Main.MS+"5초 후 부활합니다.");
			}
		}, 100l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
			public void run(){
				if(!MobSystem.plist.contains(p) || !(MobSystem.bluelist.contains(p) ? MobSystem.bluedead : MobSystem.reddead).containsValue(p)) return;
				p.sendMessage(Main.MS+"부활했습니다.");
				(MobSystem.bluelist.contains(p) ? MobSystem.bluedead : MobSystem.reddead).remove(deathloc);
				p.teleport(MobSystem.bluelist.contains(p) ? Main.Bluespawn : Main.Redspawn);
			}
		}, 200l);
	}
	
	public void Revive(Player p){
		p.teleport(deathloc);
		(MobSystem.bluelist.contains(p) ? MobSystem.bluedead : MobSystem.reddead).remove(p);
	}
	
	public void onMove(PlayerMoveEvent e){
		if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
				&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		final Player p = e.getPlayer();
		final Location l = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
		if(MobSystem.portalloc.containsKey(l)){
			Location l1 = MobSystem.portalloc.get(l);
			Location l2 = new Location(l1.getWorld(), l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
			l2.setPitch(p.getLocation().getPitch());
			l2.setYaw(p.getLocation().getYaw());
			p.teleport(l2);
			p.getWorld().playSound(l, Sound.ENDERMAN_TELEPORT, 2.0f, 1.0f);
			p.getWorld().playSound(l2, Sound.ENDERMAN_TELEPORT, 2.0f, 1.0f);
		}
	}

	public void onLaunch(ProjectileLaunchEvent event){}

	public void onProjectileHit(ProjectileHitEvent event){}
	
	public void onDamaged(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!MobSystem.plist.contains(p) || (!MobSystem.bluelist.contains(p) && !MobSystem.redlist.contains(p))) return;
		if((MobSystem.bluelist.contains(p) ? Main.Bluespawn : Main.Redspawn).distance(p.getLocation()) <= 40){
			e.setCancelled(true);
		}
	}
	
}
