package me.Bokum.SMG;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Bokum.SMG.Games.AvoidAnvil;
import me.Bokum.SMG.Games.Chamber;
import me.Bokum.SMG.Games.DeathRun;
import me.Bokum.SMG.Games.TakeSeat;
import me.Bokum.SMG.Messenger.Messenger;
import me.Bokum.SMG.Minigame.Minigame;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener{
	public static Location lobby;
	public static AvoidAnvil avoidanvil;
	public static DeathRun deathrun;
	public static TakeSeat takeseat;
	public static Chamber chamber;
	public static HashMap<String, Minigame> playingList = new HashMap<String, Minigame>();
	public static Economy econ = null;
	public static Main instance;
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		instance = this;
		try{
			ConfigurationSection sec = getConfig().getConfigurationSection("lobby");
			lobby = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			getLogger().info("로비가 설정되지 않았습니다.");
		}
		avoidanvil = new AvoidAnvil("모루피하기", 7, 3);
		deathrun = new DeathRun("데스런", 8, 4);
		takeseat = new TakeSeat("자리뺏기", 8, 4);
		chamber = new Chamber("챔버", 16, 8);
        if (!setupEconomy() ) {
            getLogger().info("[ 버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
        }
		getLogger().info("[ SMG ] 간단한 미니게임 플러그인 로드 완료");
	}
	
	public void onDisable(){
		getLogger().info("[ SMG ] 간단한 미니게임 플러그인 언로드 완료");
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[]){
		if(talker instanceof Player){
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("smg") && p.isOp()){
				if(args.length == 0){
					commandHelpMessage(p);
					return true;
				}else if(args.length <= 1){
					if(args[0].equalsIgnoreCase("setlobby")){
						ConfigurationSection sec = getConfig().getConfigurationSection("lobby");
						sec.set("world", p.getWorld().getName());
						sec.set("x", p.getLocation().getBlockX());
						sec.set("y", p.getLocation().getBlockY());
						sec.set("z", p.getLocation().getBlockZ());
						saveConfig();
						try{
							lobby = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
						}catch(Exception e){
							getLogger().info("로비가 설정되지 않았습니다.");
						}
					} else if(args[0].equalsIgnoreCase("test")){
						Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
							public void run(){
								Bukkit.broadcastMessage("모루피하기");
							}
						}, 1l, 20l);
					} else{
						commandHelpMessage(p);
					}
					return true;
				}else if(args.length >= 2){
					if(args[0].equalsIgnoreCase("set")){
						if(args[1].equalsIgnoreCase("ava")){
							avoidanvil.setLoc(p, args);
						} else if(args[1].equalsIgnoreCase("dr")){
							deathrun.setLoc(p, args);
						}else if(args[1].equalsIgnoreCase("ts")){
							takeseat.setLoc(p, args);
						}
					}else if(args[0].equalsIgnoreCase("stop")){
						if(args[1].equalsIgnoreCase("ava")){
							avoidanvil.forceStop();
						}else if(args[1].equalsIgnoreCase("dr")){
							deathrun.forceStop();
						}else if(args[1].equalsIgnoreCase("ts")){
							takeseat.forceStop();
						}
					} 
					return true;
				}
			}
		}
		return false;
	}
	
	public void commandHelpMessage(Player p){
		p.sendMessage(Messenger.MS+"/smg setlobby");
		p.sendMessage(Messenger.MS+"/smg <set/stop> ava");
		p.sendMessage(Messenger.MS+"/smg <set/stop> dr");
		p.sendMessage(Messenger.MS+"/smg <set/stop> cb");
		p.sendMessage(Messenger.MS+"/smg <set/stop> ts");
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onPlayerMove(e);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onBlockPlace(e);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onBlockBreak(e);
	}
	
	
	@EventHandler
	public void onEntityDamge(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onEntityDamage(e);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onQuit(e);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onDropItem(e);
	}
	
	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onRegainHealth(e);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onPlayerDeath(e);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onInteract(e);
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onClickInventory(e);
	}
	
	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onEntityDamagedByEntity(e);
		
		Player damager = null;
		if(e.getDamager() instanceof Player){
			damager = (Player) e.getDamager();
		}else if(e.getDamager() instanceof Snowball){
			Snowball projectile = (Snowball) e.getDamager();
			if(projectile.getShooter() instanceof Player){
				damager = (Player) projectile.getShooter();
			}
		}else if(e.getDamager() instanceof Fireball){
			Fireball projectile = (Fireball) e.getDamager();
			if(projectile.getShooter() instanceof Player){
				damager = (Player) projectile.getShooter();
			}
		}else if(e.getDamager() instanceof Arrow){
			Arrow projectile = (Arrow) e.getDamager();
			if(projectile.getShooter() instanceof Player){
				damager = (Player) projectile.getShooter();
			}
		}
		if(damager != null && playingList.containsKey(p.getName())){
			playingList.get(p.getName()).onEntityHit(e);
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		Player p = null;
		if(e.getEntity() instanceof Player){
			p = (Player) e.getEntity();
		}else if(e.getEntity() instanceof Snowball){
			Snowball projectile = (Snowball) e.getEntity();
			if(projectile.getShooter() instanceof Player){
				p = (Player) projectile.getShooter();
			}
		}else if(e.getEntity() instanceof Fireball){
			Fireball projectile = (Fireball) e.getEntity();
			if(projectile.getShooter() instanceof Player){
				p = (Player) projectile.getShooter();
			}
		}
		if(p == null) return;
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onEntityExplode(e);
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent e) {
		Player p = null;
		if(e.getEntity() instanceof Player){
			p = (Player) e.getEntity();
		}else if(e.getEntity() instanceof Snowball){
			Snowball projectile = (Snowball) e.getEntity();
			if(projectile.getShooter() instanceof Player){
				p = (Player) projectile.getShooter();
			}
		}else if(e.getEntity() instanceof Fireball){
			Fireball projectile = (Fireball) e.getEntity();
			if(projectile.getShooter() instanceof Player){
				p = (Player) projectile.getShooter();
			}
		}
		if(p == null) return;
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onExplosionPrime(e);
	}
	
	@EventHandler
	public void onBreakVehicle(VehicleDestroyEvent e){
		if(!(e.getAttacker() instanceof Player)) return;
		Player p = (Player) e.getAttacker();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onBreakVehicle(e);
	}
	
	@EventHandler
	public void onPlayercommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(playingList.containsKey(p.getName()) && (e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
				|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ"))){
			try{
				playingList.get(p.getName()).gameQuit(p);
			}catch(Exception exception){
				
			}
		}
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e){
		Player p = e.getPlayer();
		if(!playingList.containsKey(e.getPlayer().getName())) return;
		playingList.get(p.getName()).onChat(e);
	}
	
	@EventHandler
	public void onEntityDamgedByBlock(EntityDamageByBlockEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playingList.containsKey(p.getName())) return;
		playingList.get(p.getName()).onEntityDamagedByBlock(e);
	}
}
