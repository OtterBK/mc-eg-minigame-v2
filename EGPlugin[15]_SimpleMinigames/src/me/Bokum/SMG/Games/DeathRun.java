package me.Bokum.SMG.Games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Bokum.SMG.Main;
import me.Bokum.SMG.Messenger.Messenger;
import me.Bokum.SMG.Minigame.Minigame;
import me.Bokum.SMG.Utility.Cooldown;
import me.Bokum.SMG.Utility.Utility;
import net.milkbowl.vault.economy.EconomyResponse;

public class DeathRun extends Minigame{
	public Cooldown cooldown;
	public List<Location> changingList;
	public boolean isTimer;
	public ItemStack fireBallSkill;
	public ItemStack knockBackSkill;
	public ItemStack speedSkill;
	public ItemStack skillItem;
	public Inventory skillInventory;
	public List<Location> blockloc = new ArrayList<Location>();
	
	public DeathRun(String title, int max, int min){
		super(title, max, min);
		cooldown = new Cooldown(this);
		
		List<String> loreList = new ArrayList<String>();
		loreList.add("§f- §7우클릭시 전방에 화염구를 발사합니다.");
		loreList.add("§f- §7화염구가 땅에 닿으면 3x3의 범위를 즉시 없앱니다.");
		
		fireBallSkill = Utility.makeItem(280, 0, 1, "§f[ §e파이어볼 §f]", loreList);
		loreList.clear();
		
		loreList.add("§f- §7우클릭시 주위4칸내에 있는 플레이어를 튕겨냅니다.");
		knockBackSkill = Utility.makeItem(369, 0, 1, "§f[ §e넉백 §f]", loreList);
		
		loreList.add("§f- §73초간 신속2버프를 받습니다.");
		loreList.clear();
		speedSkill = Utility.makeItem(351, 12, 1, "§f[ §e이속 상승 §f]", loreList);
		
		loreList.add("§f- §7우클릭시 스킬을 선택합니다.");
		loreList.clear();
		skillItem = Utility.makeItem(340, 0, 1, "§f[ §e스킬 선택 §f]", loreList);
		
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
		
		skillInventory = Bukkit.createInventory(null, 9, "§c§l스킬 선택");
		
		skillInventory.setItem(1, fireBallSkill);
		skillInventory.setItem(4, knockBackSkill);
		skillInventory.setItem(7, speedSkill);
		
		changingList = new ArrayList<Location>();
		
		isTimer = false;
		
		loadConfig();
		
		RestoreBlock();
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
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("block");
			for(int j = 1; j <= Main.instance.getConfig().getInt("blockamt"); j++){
				  blockloc.add(new Location(Main.instance.getServer().getWorld(sec.getString("world")), sec.getInt("block_loc_"+j+"_x"), sec.getInt("block_loc_"+j+"_y"), sec.getInt("block_loc_"+j+"_z")));
			}
		}
		catch (Exception e){
			Main.instance.getLogger().info("블럭 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
	}
	
	public void setLoc(Player p, String args[]){
		if(args.length == 2){
			p.sendMessage("/smg set dr join");
			p.sendMessage("/smg set dr start");
			p.sendMessage("/smg set dr block");
		}else if(args.length >= 3){
			if(args[2].equalsIgnoreCase("test")){
				final int cur = Utility.Getcursch(this);
				tasktime[cur] = 5;
				tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
					public void run(){
						Bukkit.broadcastMessage("데스런");
					}
				}, 1l, 20l);
			}
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
			}else if(args[2].equalsIgnoreCase("block")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("block")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("block");
					Main.instance.saveConfig();
				}
					int x1 = Integer.valueOf(args[3]);
					int y1 = Integer.valueOf(args[4]);
					int z1 = Integer.valueOf(args[5]);
					int x2 = Integer.valueOf(args[6]);
					int y2 = Integer.valueOf(args[7]);
					int z2 = Integer.valueOf(args[8]);
					SaveBlock(p, x1, y1, z1, x2, y2, z2);
				Main.instance.saveConfig();
				loadConfig();
			}
		}
	}
	
	public void clearData(){
		for(int i = 0; i < tasknum.length; i++){
			if(tasknum[i] != -5){
				 Utility.Canceltask(minigame, i);
			}
		}
		for(Player p : playerList){
			Main.playingList.remove(p.getName());
		}
		RestoreBlock();
		playerList.clear();
		isStart = false;
		isLobbyStart = false;
		isTimer = false;
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
			Main.playingList.put(p.getName(), Main.deathrun);
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
		Bukkit.broadcastMessage(Messenger.MS+"§c§l데스런§f이 곧 시작됩니다.");
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tasktime[cur] > 0){
					Messenger.sendMessage(playerList, "게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.", true);
					tasktime[cur]--;
					if(tasktime[cur] == 3){
						Bukkit.broadcastMessage(Messenger.MS+"§a10초 후 §c데스런§f의 맵을 초기화합니다. §c렉에 주의해주세요!");
					}
					if(tasktime[cur] == 2){
						Bukkit.broadcastMessage(Messenger.MS+"§c데스런§f의 맵을 초기화합니다. §c렉주의!!!");
						RestoreBlock();
					}
				}
				else{
					Utility.Canceltask(minigame, cur);
					isStart = true;
					for(int i = 0; i < playerList.size(); i++){
						Player p = playerList.get(i);
						p.getInventory().clear();
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
						p.sendMessage(Messenger.MS+"게임이 시작 됐습니다.");
						p.getInventory().setItem(0, skillItem);
						p.teleport(startPos);
					}
					startWait();
				}
			}
		}, 200L, 200L);
	}
	
	public void startWait(){
		final int cur = Utility.Getcursch(this);
		tasktime[cur] = 25;
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(tasktime[cur] > 0){
					Messenger.sendMessage(playerList,tasktime[cur]+" 초 후 시작합니다. 스킬을 골라주세요.", true);
					tasktime[cur]--;
				}
				else{
					Utility.Canceltask(minigame, cur);
					timer();
				}
			}
		}, 60L, 20L);
	}
	
	public void RestoreBlock(){
		for(Location loc : blockloc){
			loc.getBlock().setTypeIdAndData(35, (byte) 0, true);
		}
	}
	
	public void SaveBlock(Player p, int x1, int y1, int z1, int x2, int y2, int z2){
		blockloc.clear();
		int amt = 0;
		if(x1 > x2){
			int tmpint = x1;
			x1 = x2;
			x2 = tmpint;
		}
		if(y1 > y2){
			int tmpint = y1;
			y1 = y2;
			y2 = tmpint;
		}		
		if(z1 > z2){
			int tmpint = z1;
			z1 = z2;
			z2 = tmpint;
		}
		Location pos1 = new Location(Main.instance.getServer().getWorld("world"), x1, y1, z1);
		Location pos2 = new Location(Main.instance.getServer().getWorld("world"), x2, y2, z2);
		ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("block");
		for(int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++){
			for(int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++){
				for(int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++){
					Location block_loc = new Location(Main.instance.getServer().getWorld("world"), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
					if(block_loc.getBlock().getType() == Material.WOOL){
						amt++;
						sec.set("world", block_loc.getWorld().getName());
						sec.set("block_loc_"+amt+"_x", block_loc.getBlockX());
						sec.set("block_loc_"+amt+"_y", block_loc.getBlockY());
						sec.set("block_loc_"+amt+"_z", block_loc.getBlockZ());			
						blockloc.add(block_loc);
					}
				}
			}
		}
		Main.instance.getConfig().set("blockamt", amt);
		Main.instance.saveConfig();
		loadConfig();
		p.sendMessage(Messenger.MS+"설정완료");
	}
	
	public void timer(){
		final int cur = Utility.Getcursch(this);
		isTimer = true;
		Messenger.sendMessage(playerList, "게임이 시작됐습니다!", true);
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
			public void run(){
				if(!isGameEnd){
					List<Location> locList = new ArrayList<Location>();
					for(int i = 0; i < changingList.size(); i++){
						locList.add(changingList.get(i));
					}
					for(Location l : locList){
						if(l.getBlock().getData() == 0) l.getBlock().setTypeIdAndData(35, (byte) 4, true);
						else if(l.getBlock().getData() == 4) l.getBlock().setTypeIdAndData(35, (byte) 1, true);
						else if(l.getBlock().getData() == 1) l.getBlock().setTypeIdAndData(35, (byte) 14, true);
						else if(l.getBlock().getData() == 14){
							l.getBlock().setType(Material.AIR);
							changingList.remove(l);
						} else if(l.getBlock().getType() == Material.AIR) changingList.remove(l);
					}
				}
				else{
					Utility.Canceltask(minigame, cur);
				}
			}
		}, 0L, 15L);
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
				p.sendMessage("§7게임 이름 §f: §c데스런");
				p.sendMessage("§f게임이 시작된 후 25초간 능력을 선택하실 수 있습니다.");
				p.sendMessage("§f25초가 지난후에는 자신의 발 밑에있는 블럭이");
			 	p.sendMessage("§f사라지게 되고 다른 플레이어를 떨어뜨려");
			 	p.sendMessage("§f마지막까지 살아남으면 승리합니다.");
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
				Bukkit.broadcastMessage(Messenger.MS+"§c§l데스런§f이 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	forceStop();
	    }
	}
	
	public void selectSkill(Player p, int slot){
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.5f, 0.5f);
		switch(slot){
		case 1: p.getInventory().clear(); p.closeInventory(); p.getInventory().addItem(fireBallSkill); return;
		case 4: p.getInventory().clear(); p.closeInventory(); p.getInventory().addItem(knockBackSkill); return;
		case 7: p.getInventory().clear(); p.closeInventory(); p.getInventory().addItem(speedSkill); return;
		
		default: return;
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
	
	public void onPlayerMove(PlayerMoveEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		if(!isTimer) return;
		Location l = e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).getLocation();
		if(l.getBlock().getType() == Material.AIR || changingList.contains(l)) return;
		changingList.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
	}
	
	public void onQuit(PlayerQuitEvent e){
		if(!playerList.contains(e.getPlayer())) return;
		gameQuit(e.getPlayer());
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
				if(e.getItem().getType() == Material.STICK && cooldown.checkCooldown(p, "스킬") && isTimer){
					Utility.spawnFireball(p, 2);
					cooldown.setCooldown(p, "스킬", 10, false, 0);
				}else if(e.getItem().getType() == Material.COMPASS){
					p.openInventory(helpInventory);
				}else if(e.getItem().getType() == Material.BOOK){
					p.openInventory(skillInventory);
				}else if(e.getItem().getType() == Material.BLAZE_ROD && cooldown.checkCooldown(p, "스킬")&& isTimer){
					for(Player t : playerList){
						if(t.getLocation().distance(p.getLocation()) <= 4){
							if(t.getName() == p.getName()) continue;
								t.setVelocity(t.getLocation().getDirection().multiply(-1.3));
								t.getWorld().playSound(t.getLocation(), Sound.BAT_TAKEOFF, 1.5f, 0.2f);	
						}
					}
					cooldown.setCooldown(p, "스킬", 10, false, 0);
				}else if(e.getItem().getType() == Material.INK_SACK && cooldown.checkCooldown(p, "스킬")&& isTimer){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
					p.sendMessage(Messenger.MS+"3초간 속도가 향상됩니다.");
					cooldown.setCooldown(p, "스킬", 13, false, 0);
				}
			}
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
		}else if(e.getInventory().getTitle().equalsIgnoreCase("§c§l스킬 선택")){
			selectSkill(p, e.getSlot());
			e.setCancelled(true);
			p.updateInventory();
		}
	}
	
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!playerList.contains(p)) return;
		e.setCancelled(true);
	}
	
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity ent = event.getEntity();
		if (ent instanceof Fireball) {
			event.setCancelled(true);
			Location l = new Location(event.getLocation().getWorld(), event.getLocation().getBlockX(), 
					0, event.getLocation().getBlockZ());
			for(int x = 0; x < 3; x++){
				for(int z = 0; z < 3; z++){
					l.getBlock().getRelative(x, 0, z).setType(Material.AIR);
				}
			}
		}
	}
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		event.setFire(false);
		
		Entity ent = event.getEntity();
		if (ent instanceof Fireball)
			event.setRadius(2);
	}
}
