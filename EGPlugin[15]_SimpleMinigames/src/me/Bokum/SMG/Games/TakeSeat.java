package me.Bokum.SMG.Games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Bokum.SMG.Main;
import me.Bokum.SMG.Messenger.Messenger;
import me.Bokum.SMG.Minigame.Minigame;
import me.Bokum.SMG.Utility.Utility;
import net.milkbowl.vault.economy.EconomyResponse;

public class TakeSeat extends Minigame{
	public Location pos1;
	public Location pos2;
	List<Minecart> minecartList = new ArrayList<Minecart>();
	
	public TakeSeat(String title, int max, int min){
		super(title, max, min);
		loadConfig();
		
		helpInventory = Bukkit.createInventory(null, 27, "§c§l도우미");
		
		ItemStack item = new ItemStack(34, 1);
		ItemMeta meta2 = item.getItemMeta();
		meta2.setDisplayName("§6장식");
		item.setItemMeta(meta2);
		for(int i = 0; i <= 9; i++){
			helpInventory.setItem(i, item);
		}
		for(int i = 17; i < 27; i++){
			helpInventory.setItem(i, item);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e플레이 방법");
		item.setItemMeta(meta2);
		helpInventory.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e게임 규칙");
		item.setItemMeta(meta2);
		helpInventory.setItem(13, item);
	}
	
	public void loadConfig(){
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("joinPos");
			this.joinPos = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 참여 지점이 설정되지 않았습니다.");
		}
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("startPos");
			this.startPos = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 시작 지점이 설정되지 않았습니다.");
		}
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("pos1");
			this.pos1 = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 pos1 지점이 설정되지 않았습니다.");
		}
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("pos2");
			this.pos2 = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 pos2 지점이 설정되지 않았습니다.");
		}
	}
	
	public void setLoc(Player p, String args[]){
		if(args.length == 2){
			p.sendMessage("/smg set ts join");
			p.sendMessage("/smg set ts start");
			p.sendMessage("/smg set ts pos1");
			p.sendMessage("/smg set ts pos2");
		}else if(args.length == 3){
			if(args[2].equalsIgnoreCase("join")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("joinPos")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("joinPos");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("joinPos");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY());
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("start")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("startPos")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("startPos");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("startPos");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY());
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("pos1")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("pos1")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("pos1");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("pos1");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY());
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("pos2")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("pos2")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("pos2");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("pos2");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY());
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}
		}
	}
	
	public void clearData(){
		for(int i = 0; i < tasknum.length; i++){
			if(tasknum[i] != -5) Utility.Canceltask(minigame, i);
		}
		for(Player p : playerList){
			Main.playingList.remove(p.getName());
		}
		for(Minecart cart : minecartList){
			cart.remove();
		}
		playerList.clear();
		isStart = false;
		isLobbyStart = false;
		isGameEnd = false;
		Forcestoptimer = 0;
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
	}
	
	public void forceStop(){
		Messenger.sendMessage(playerList, "게임이 강제 종료 되었습니다.", true);
		Bukkit.broadcastMessage(Messenger.MS+gameTitle+" 게임이 강제 종료 되었습니다.");
		for(Player p : playerList){
			try{
				me.Bokum.EGM.Main.Spawn(p);
			}catch(Exception e){
				p.teleport(Main.lobby);
			}
		}
		clearData();
	}
	
	public void gameJoin(Player p){
		if(playerList.contains(p)){
			p.sendMessage(Messenger.MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(playerList.size() >= maxPlayer){
			p.sendMessage(Messenger.MS+"이미 최대인원(+"+maxPlayer+")입니다.");
			return;
		}
		if(isStart){
			p.sendMessage(Messenger.MS+"이미 게임이 진행중입니다.");
			return;
		}
		else{
			playerList.add(p);
			p.teleport(joinPos);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpItem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), gameTitle);
			Main.playingList.put(p.getName(), Main.takeseat);
			Messenger.sendMessage(playerList, 
					p.getName()+" 님이 "+gameTitle+"에 참여하셨습니다. 인원 (§e "+playerList.size()+"§7 / §c"+minPlayer+" §f)"
					,true);
			if(!isLobbyStart && playerList.size() >= minPlayer){
				startGame();
			}
			Messenger.sendSound(playerList, Sound.NOTE_PLING, 2.0f, 0.5f);
		}
	}
	
	public void startGame(){
		isLobbyStart = true;
		final int cur = Utility.Getcursch(this);
		tasktime[cur] = 5;
		Bukkit.broadcastMessage(Messenger.MS+"§a§l자리뺏기§f가 곧 시작됩니다.");
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tasktime[cur] > 0){
					Messenger.sendMessage(playerList, "게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.", true);
					tasktime[cur]--;
				}
				else{
					Utility.Canceltask(minigame, cur);
					isStart = true;
					for(int i = 0; i < playerList.size(); i++){
						Player p = playerList.get(i);
						p.getInventory().clear();
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
						p.sendMessage(Messenger.MS+"게임이 시작 됐습니다.");
						p.teleport(startPos);
					}
					timer();
				}
			}
		}, 200L, 200L);
	}
	
	public void timer(){
		final int cur = Utility.Getcursch(this);
		tasktime[cur] = Utility.Getrandom(30, 150);
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(!isGameEnd){
					if(--tasktime[cur] > 0){
						String str = "§e노래§f(§c상상하세요§f) §f:§a ";
							switch(Utility.Getrandom(0, 4)){
							case 0: str += "따"; break;
							case 1: str += "라"; break;
							case 2: str += "란"; break;
							case 3: str += "딴"; break;
							case 4: str += "뜨"; break;
							}
							switch(Utility.Getrandom(0, 4)){
							case 0: str += "↗"; break;
							case 1: str += "↘"; break;
							case 2: str += "↑"; break;
							case 3: str += "→"; break;
							case 4: str += "↓"; break;
						}
						Messenger.sendMessage(playerList, str, false);
							switch(Utility.Getrandom(0, 6)){
							case 0: Messenger.sendSound(playerList, Sound.NOTE_PLING, 1.5f, 1.0f); break;
							case 1: Messenger.sendSound(playerList, Sound.NOTE_PIANO, 1.5f, 1.0f); break;
							case 2: Messenger.sendSound(playerList, Sound.NOTE_BASS, 1.5f, 1.0f); break;
							case 3: Messenger.sendSound(playerList, Sound.NOTE_BASS_GUITAR, 1.5f, 1.0f); break;
							case 4: Messenger.sendSound(playerList, Sound.NOTE_SNARE_DRUM, 1.5f, 1.0f); break;
							case 5: Messenger.sendSound(playerList, Sound.NOTE_STICKS, 1.5f, 1.0f); break;
							case 6: Messenger.sendSound(playerList, Sound.NOTE_BASS_DRUM, 1.5f, 1.0f); break;
						}
					}else{
						Utility.Canceltask(minigame, cur);
						spawnMineCart();
					}
				}else{
					Utility.Canceltask(minigame, cur);
				}
			}
		}, 120l, 4L);
	}
	
	public void spawnMineCart(){
		final int x1 = pos1.getBlockX() >= pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX();
		final int x2 = pos1.getBlockX() >= pos2.getBlockX() ? pos1.getBlockX() : pos2.getBlockX();
		final int y = pos1.getBlockY() >= pos2.getBlockY() ? pos1.getBlockY() : pos2.getBlockY();
		final int z1 = pos1.getBlockZ() >= pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ();
		final int z2 = pos1.getBlockZ() >= pos2.getBlockZ() ? pos1.getBlockZ() : pos2.getBlockZ();
		Messenger.sendMessage(playerList, "빨리 마인카트에 타세요!", true);
		for(int i = 0; i < playerList.size()-1; i++){
			Location l = new Location(pos1.getWorld(), Utility.Getrandom(x1, x2), 
					y, Utility.Getrandom(z1, z2));
			Minecart cart = l.getWorld().spawn(l, Minecart.class);
			minecartList.add(cart);
		}
		final int cur = Utility.Getcursch(this);
		tasktime[cur] = 20;
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tasktime[cur]-- > 0){
					for(Player p : playerList){
						p.setExp((float)tasktime[cur]/20);
					}
				}else{
					Utility.Canceltask(minigame, cur);
					List<Player> exitList = new ArrayList<Player>();
					for(int i = 0; i < playerList.size(); i++){
						Player p = playerList.get(i);
						if(!p.isInsideVehicle()){
							exitList.add(p);
						}
					}
					for(Player p : exitList){
						p.damage(100);
						p.sendMessage(Messenger.MS+"시간내에 마인카트에 타지못해 탈락하셨습니다!");
						gameQuit(p);
					}
					for(Minecart cart : minecartList){
						cart.remove();
					}
					timer();
				}
			}
		}, 0l, 10l);
	}
	
	public void gameQuit(Player p){
		if(!playerList.contains(p)) return;
		Main.playingList.remove(p.getName());
		playerList.remove(p);
		if(!isStart) return;
		Messenger.sendMessage(playerList, p.getName()+" §f님이 탈락되셨습니다.", true);
		if(playerList.size() <= 1){
			try{
				winPlayer(playerList.get(0));
			}catch(Exception e){
				
			}
		}
	}
	
	public void gameHelper(Player p, int slot){
		switch(slot){
			case 11:
				p.sendMessage("§7게임 이름 §f: §c자리뻇기");
				p.sendMessage("§f게임이 시작된 후 노래(?)가 시작됩니다.");
				p.sendMessage("§f노래가 끝나면 마인카트가 하늘에서 내려오며");
			 	p.sendMessage("§f카트의 개수는 플레이어의 수보다 1개 적습니다.");
				p.closeInventory();
				return;
				  
			case 13:
				p.sendMessage("§f아이템은 버려지지 않습니다.");
				p.sendMessage("§fPVP는 불가능합니다.");
				p.closeInventory();
				return;
				  
			default: return;
			}  
		}
	
	public void winPlayer(final Player p){
		isGameEnd = true;
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		Messenger.sendMessage(playerList, "당신이 최후로 남았습니다!", true);
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = Main.econ.depositPlayer(p.getName(), 500);
				if (r.transactionSuccess()) {
					p.sendMessage(ChatColor.GOLD + "승리 보상으로 500원을 받으셨습니다.");
				}
				try{
					me.Bokum.EGM.Main.Spawn(p);
					} catch(Exception e){
					p.teleport(Main.lobby);
					}
				clearData();
				Bukkit.broadcastMessage(Messenger.MS+"§a§l자리뺏기§f가 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	forceStop();
	    }
	}
	
	//이벤트
	
	public void onBlockPlace(BlockPlaceEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playerList.contains(p)) return;
		e.setCancelled(true);
	}
	
	public void onBlockBreak(BlockBreakEvent e){
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = e.getPlayer();
		if(!playerList.contains(p)) return;
		e.setCancelled(true);
	}
	
	public void onQuit(PlayerQuitEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		gameQuit(e.getPlayer());
	}
	
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playerList.contains(p)) return;
		e.setCancelled(true);
	}

	public void onDropItem(PlayerDropItemEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		e.setCancelled(true);
	}
	
	public void onPlayerDeath(PlayerDeathEvent e){
		if(!playerList.contains(e.getEntity())) return;
		gameQuit(e.getEntity());
	}
	
	public void onInteract(PlayerInteractEvent e){
		Player p = (Player) e.getPlayer();
		if(!playerList.contains(p)) return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getItem() != null){
				if(e.getItem().getType() == Material.COMPASS){
					p.openInventory(helpInventory);
				}
			}
		}else{
			e.setCancelled(true);
		}
	}
	
	public void onClickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(!playerList.contains(p)) return;
		if(e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
			gameHelper(p, e.getSlot());
			e.setCancelled(true);
			p.updateInventory();
		}
	}
	
	public void onBreakVehicle(VehicleDestroyEvent e){
		if(!(e.getAttacker() instanceof Player)) return;
		Player p = (Player) e.getAttacker();
		if(!playerList.contains(p)) return;
		e.setCancelled(true);
	}
}
