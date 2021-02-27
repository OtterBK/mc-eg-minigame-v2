package me.Bokum.SMG.Games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Bokum.SMG.Main;
import me.Bokum.SMG.Messenger.Messenger;
import me.Bokum.SMG.Minigame.Minigame;
import me.Bokum.SMG.Utility.Cooldown;
import me.Bokum.SMG.Utility.Utility;
import net.milkbowl.vault.economy.EconomyResponse;

public class Chamber extends Minigame{
	public Location blueSpawn;
	public Location redSpawn;
	public ItemStack redHelmet;
	public ItemStack redChestPlate;
	public ItemStack redLeggins;
	public ItemStack redBoots;
	public ItemStack blueHelmet;
	public ItemStack blueChestPlate;
	public ItemStack blueLeggins;
	public ItemStack blueBoots;
	public ItemStack teamChatItem;
	public int redKillCount = 0;
	public int blueKillCount = 0;
	public List<Player> redList = new ArrayList<Player>();
	public List<Player> blueList = new ArrayList<Player>();
	public List<String> teamChatList = new ArrayList<String>();
	public Cooldown cooldown;
	
	public Chamber(String title, int max, int min){
		super(title, max, min);
		cooldown = new Cooldown(this);
		
		redHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) redHelmet.getItemMeta();
		leatherArmorMeta.setColor(Color.RED);
		redHelmet.setItemMeta(leatherArmorMeta);
		
		redChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		leatherArmorMeta = (LeatherArmorMeta) redChestPlate.getItemMeta();
		leatherArmorMeta.setColor(Color.RED);
		redChestPlate.setItemMeta(leatherArmorMeta);
		
		redLeggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		leatherArmorMeta = (LeatherArmorMeta) redLeggins.getItemMeta();
		leatherArmorMeta.setColor(Color.RED);
		redLeggins.setItemMeta(leatherArmorMeta);
		
		redBoots = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		leatherArmorMeta = (LeatherArmorMeta) redBoots.getItemMeta();
		leatherArmorMeta.setColor(Color.RED);
		redBoots.setItemMeta(leatherArmorMeta);
		
		blueHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		leatherArmorMeta = (LeatherArmorMeta) blueHelmet.getItemMeta();
		leatherArmorMeta.setColor(Color.BLUE);
		blueHelmet.setItemMeta(leatherArmorMeta);
		
		blueChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		leatherArmorMeta = (LeatherArmorMeta) blueChestPlate.getItemMeta();
		leatherArmorMeta.setColor(Color.BLUE);
		blueChestPlate.setItemMeta(leatherArmorMeta);
		
		blueLeggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		leatherArmorMeta = (LeatherArmorMeta) blueLeggins.getItemMeta();
		leatherArmorMeta.setColor(Color.BLUE);
		blueLeggins.setItemMeta(leatherArmorMeta);
		
		blueBoots = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		leatherArmorMeta = (LeatherArmorMeta) blueBoots.getItemMeta();
		leatherArmorMeta.setColor(Color.BLUE);
		blueBoots.setItemMeta(leatherArmorMeta);
		
		teamChatItem = new ItemStack(Material.WATCH);
		ItemMeta meta = teamChatItem.getItemMeta();
		meta.setDisplayName("§f[ §a팀채팅 §f]");
		teamChatItem.setItemMeta(meta);
		
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
		
		loadConfig();
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
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("redSpawn");
			this.redSpawn = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 레드 스폰 지점이 설정되지 않았습니다.");
		}
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("blueSpawn");
			this.blueSpawn = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 블루 스폰 지점이 설정되지 않았습니다.");
		}
	}
	
	public void setLoc(Player p, String args[]){
		if(args.length == 2){
			p.sendMessage("/smg set cb join");
			p.sendMessage("/smg set cb start");
			p.sendMessage("/smg set cb red");
			p.sendMessage("/smg set cb blue");
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
				sec.set("y", p.getLocation().getBlockY()+1);
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
				}ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("startPos");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY()+1);
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("red")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("redSpawn")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("redSpawn");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("pos1");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY());
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("blue")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("blueSpawn")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("blueSpawn");
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
		playerList.clear();
		isStart = false;
		isGameEnd = false;
		isLobbyStart = false;
		teamChatList.clear();
		redList.clear();
		blueList.clear();
		Forcestoptimer = 0;
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		cooldown.cooldownlist.clear();
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
	
	public void gameHelper(Player p, int slot){
		switch(slot){
			case 11:
				p.sendMessage("§7게임 이름 §f: §c챔버");
				p.sendMessage("§f게임이 시작되면 레드팀과 블루팀으로 나눠집니다.");
				p.sendMessage("§f기본적으로 활, 검, 화살1개가 주어지며");
			 	p.sendMessage("§f적을 죽일때마다 화살을 1개씩 받습니다.");
			 	p.sendMessage("§f먼저 70킬을 한 팀이 승리합니다.");
				p.closeInventory();
				return;
				  
			case 13:
				p.sendMessage("§f아이템은 버려지지 않습니다.");
				p.closeInventory();
				return;
				  
			default: return;
			}  
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
			Main.playingList.put(p.getName(), Main.avoidanvil);
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
		Bukkit.broadcastMessage(Messenger.MS+"§2§l챔버§f가 곧 시작됩니다.");
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tasktime[cur] > 0){
					Messenger.sendMessage(playerList, "게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.", true);
					tasktime[cur]--;
				} else{
					Utility.Canceltask(minigame, cur);
					isStart = true;
					for(int i = 0; i < playerList.size(); i++){
						Player p = playerList.get(i);
						p.getInventory().clear();
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
						p.sendMessage(Messenger.MS+"게임이 시작 됐습니다.");
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 72000, 0));
						if(redList.size() <= blueList.size()){
							redList.add(p);
							setRedInventory(p);
							p.teleport(redSpawn);
						}else{
							blueList.add(p);
							setBlueInventory(p);
							p.teleport(blueSpawn);
						}
					}
				}
			}
		}, 200L, 200L);
	}
	
	public void gameQuit(Player p){
		if(!playerList.contains(p)) return;
		Main.playingList.remove(p.getName());
		playerList.remove(p);
		if(!isStart) return;
		if(redList.contains(p)) redList.remove(p);
		if(blueList.contains(p)) blueList.remove(p);
		Messenger.sendMessage(playerList, p.getName()+" §f님이 퇴장하셨습니다.", true);
		if(redList.size() <= 0){
			try{
				redWin();
			}catch(Exception e){
				
			}
		}else if(blueList.size() <= 0){
			try{
				blueWin();
			}catch(Exception e){
				
			}
		}
	}
	
	public void setRedInventory(Player p){
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().setHelmet(redHelmet);
		p.getInventory().setChestplate(redChestPlate);
		p.getInventory().setLeggings(redLeggins);
		p.getInventory().setBoots(redBoots);
		p.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		p.getInventory().addItem(new ItemStack(Material.BOW, 1));
		p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
		p.getInventory().setItem(8, teamChatItem);
	}
	
	public void setBlueInventory(Player p){
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().setHelmet(blueHelmet);
		p.getInventory().setChestplate(blueChestPlate);
		p.getInventory().setLeggings(blueLeggins);
		p.getInventory().setBoots(blueBoots);
		p.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		p.getInventory().addItem(new ItemStack(Material.BOW, 1));
		p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
	}
	
	public void redWin(){
		isGameEnd = true;
		for(Player p : playerList){
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
			p.sendMessage(Messenger.MS+"§c레드팀§f이 승리했습니다!");
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
		}
		try{
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					for(Player p : playerList){
						if(redList.contains(p)){
							EconomyResponse r = Main.econ.depositPlayer(p.getName(), 1000);
							if (r.transactionSuccess()) {
								p.sendMessage(ChatColor.GOLD + "승리 보상으로 1000원을 받으셨습니다.");
							}
						}else{
							EconomyResponse r = Main.econ.depositPlayer(p.getName(), 500);
							if (r.transactionSuccess()) {
								p.sendMessage(ChatColor.GOLD + "패배 보상으로 500원을 받으셨습니다.");
							}
						}
						try{
							me.Bokum.EGM.Main.Spawn(p);
						} catch(Exception e){
							p.teleport(Main.lobby);
						}
					}
					clearData();
					Bukkit.broadcastMessage(Messenger.MS+"§2§l챔버§f가 §c레드팀§f의 승리로 종료 됐습니다.");
				}
			}, 140L);
		}catch(Exception e){
	    	forceStop();
	    }
	}
	
	public void useTeamChat(Player p){
		if(teamChatList.contains(p.getName())){
			teamChatList.remove(p.getName());
			p.sendMessage(Messenger.MS+"팀 채팅을 껐습니다.");
		} else {
			teamChatList.add(p.getName());
			p.sendMessage(Messenger.MS+"팀 채팅을 껐습니다.");
		}
	}
	
	public void blueWin(){
		isGameEnd = true;
		for(Player p : playerList){
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
			p.sendMessage(Messenger.MS+"§c블루팀§f이 승리했습니다!");
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
		}
		try{
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					for(Player p : playerList){
						if(blueList.contains(p)){
							EconomyResponse r = Main.econ.depositPlayer(p.getName(), 1000);
							if (r.transactionSuccess()) {
								p.sendMessage(ChatColor.GOLD + "승리 보상으로 1000원을 받으셨습니다.");
							}
						}else{
							EconomyResponse r = Main.econ.depositPlayer(p.getName(), 500);
							if (r.transactionSuccess()) {
								p.sendMessage(ChatColor.GOLD + "패배 보상으로 500원을 받으셨습니다.");
							}
						}
						try{
							me.Bokum.EGM.Main.Spawn(p);
						} catch(Exception e){
							p.teleport(Main.lobby);
						}
					}
					clearData();
					Bukkit.broadcastMessage(Messenger.MS+"§2§l챔버§f가 §b블루팀§f의 승리로 종료 됐습니다.");
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
	
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Arrow)) return;
		Arrow arrow = (Arrow) e.getDamager();
		if(!(arrow.getShooter() instanceof Player)) return;
		Player k = (Player) arrow.getShooter();
		Player p = (Player) e.getEntity();
		if(!playerList.contains(p) || !(playerList.contains(k))) return;
		e.setDamage(100);
		k.sendMessage(Messenger.MS+"화살을 맞추어 즉사시켰습니다.");
		p.sendMessage(Messenger.MS+"화살에 맞아 즉사하였습니다.");
	}
	
	public void onQuit(PlayerQuitEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		gameQuit(e.getPlayer());
	}

	public void onRegainHealth(EntityRegainHealthEvent e){
		e.setCancelled(true);
	}
	public void onDropItem(PlayerDropItemEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		e.setCancelled(true);
	}
	
	public void onPlayerDeath(PlayerDeathEvent e){
		if(!playerList.contains(e.getEntity())) return;
		if(!isStart) return;
		Player p = e.getEntity();
		if(redList.contains(p)){
			if(++blueKillCount >= 70){
				blueWin();
			}
		} else {
			if(++redKillCount >= 70){
				redWin();
			}
		}
		if(!(p.getKiller() instanceof Player)) return;
		Player k = (Player) p.getKiller();
		if(playerList.contains(k)) k.getInventory().addItem(new ItemStack(Material.ARROW, 1));
	}
	
	public void onInteract(PlayerInteractEvent e){
		final Player p = (Player) e.getPlayer();
		if(!playerList.contains(p)) return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getItem() != null){
				if(e.getItem().getType() == Material.COMPASS){
					p.openInventory(helpInventory);
				} else if(e.getItem().getType() == Material.WATCH){
					useTeamChat(p);
				}
			}
		}
	}
	
	public void onRespawn(PlayerRespawnEvent e){
		final Player p = (Player) e.getPlayer();
		e.setRespawnLocation(redList.contains(p) ? redSpawn : blueSpawn);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
			public void run(){
				if(redList.contains(p)){
					setRedInventory(p);
				} else {
					setBlueInventory(p);
				}
			}
		}, 10);
	}
	
	public void onChat(PlayerChatEvent e){
		final Player p = e.getPlayer();
		if(!playerList.contains(p)) return;
		if(teamChatList.contains(p)){
			Messenger.sendMessage(redList.contains(p) ? redList : blueList, "§f[ §9팀채팅 §f] §c"+p.getName()+
					" §7: §6"+e.getMessage(), false);
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
}
