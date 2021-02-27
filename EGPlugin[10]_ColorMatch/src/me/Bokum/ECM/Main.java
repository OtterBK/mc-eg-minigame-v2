package me.Bokum.ECM;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static String MS = "§f[ §aEG §f] ";
	public static Location Lobby;
	public static Location joinpos;
	public static Main instance;
	public static List<Player> plist = new ArrayList<Player>();
	public static int tasknum[] = new int[5];
	public static int tasktime[] = new int[5];
	public static int timernum = 0;
	public static long timertime = 0;
	public static ItemStack helpitem;
	public static Inventory gamehelper;
	public static Economy econ = null;
	public static ItemStack wool[] = new ItemStack[12];
	public static List<Byte> woolbyte = new ArrayList<Byte>();
	public static List<Location> blockloc = new ArrayList<Location>(100);
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static boolean game_end = false;
	public static int Forcestoptimer = 0;
	
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("컬러매치 플러그인이 로드 되었습니다.");
		
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
		woolbyte.add((byte) 1);
		woolbyte.add((byte) 2);
		woolbyte.add((byte) 4);
		woolbyte.add((byte) 5);
		woolbyte.add((byte) 6);
		woolbyte.add((byte) 8);
		woolbyte.add((byte) 10);
		woolbyte.add((byte) 11);
		woolbyte.add((byte) 12);
		woolbyte.add((byte) 13);
		woolbyte.add((byte) 14);
		woolbyte.add((byte) 15);
		
		gamehelper = Bukkit.createInventory(null, 27, "§c§l도우미");
		
		ItemStack item = new ItemStack(34, 1);
		ItemMeta meta2 = item.getItemMeta();
		meta2.setDisplayName("§6장식");
		item.setItemMeta(meta2);
		for(int i = 0; i <= 9; i++){
			gamehelper.setItem(i, item);
		}
		for(int i = 17; i < 27; i++){
			gamehelper.setItem(i, item);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e플레이 방법");
		item.setItemMeta(meta2);
		gamehelper.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e게임 규칙");
		item.setItemMeta(meta2);
		gamehelper.setItem(13, item);
		
		List<String> lorelist = new ArrayList<String>();
		
		helpitem = new ItemStack(345, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		wool[0] = new ItemStack(Material.WOOL, 1, (byte) 1);
		wool[1] = new ItemStack(Material.WOOL, 1, (byte) 2);
		wool[2] = new ItemStack(Material.WOOL, 1, (byte) 4);
		wool[3] = new ItemStack(Material.WOOL, 1, (byte) 5);
		wool[4] = new ItemStack(Material.WOOL, 1, (byte) 6);
		wool[5] = new ItemStack(Material.WOOL, 1, (byte) 8);
		wool[6] = new ItemStack(Material.WOOL, 1, (byte) 10);
		wool[7] = new ItemStack(Material.WOOL, 1, (byte) 11);
		wool[8] = new ItemStack(Material.WOOL, 1, (byte) 12);
		wool[9] = new ItemStack(Material.WOOL, 1, (byte) 13);
		wool[10] = new ItemStack(Material.WOOL, 1, (byte) 14);
		wool[11] = new ItemStack(Material.WOOL, 1, (byte) 15);	
		
		Loadconfig();
		
		RestoreBlock();
		
        if (!setupEconomy() ) {
            getLogger().info("[버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
        }
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
    
	public void onDisable()
	{
		getLogger().info("컬러매치 플러그인이 언로드 되었습니다.");
	}
	
	public void Loadconfig()
	{
	  try
	  {
		joinpos = new Location(Bukkit.getWorld(getConfig().getString("Join_world")), getConfig().getInt("Join_x"), getConfig().getInt("Join_y"), getConfig().getInt("Join_z"));
	    Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("참여 지점 또는 로비가 설정되어 있지 않습니다");
	  }
		ConfigurationSection sec = getConfig();
		try
		{
			for(int j = 1; j <= getConfig().getInt("blockamt"); j++){
				  blockloc.add(new Location(getServer().getWorld("world"), getConfig().getInt("block_loc_"+j+"_x"), getConfig().getInt("block_loc_"+j+"_y"), getConfig().getInt("block_loc_"+j+"_z")));
			}
		}
		catch (Exception e)
		{
			 getLogger().info("시작 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("ecm") && p.isOp())
			{
				if(args.length <= 0)
				{
					Helpmessages(p);
					return true;
				}
				else
				{
					if(args[0].equalsIgnoreCase("set"))
					{
						Setloc(p, args);
						return true;
					}
					if(args[0].equalsIgnoreCase("join"))
					{
						GameJoin(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("quit"))
					{
						GameQuit(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("stop"))
					{
						Forcestop();
						return true;
					}
					if(args[0].equalsIgnoreCase("block"))
					{
						SaveBlock(p, args[1], args[2], args[3], args[4], args[5], args[6]);
						return true;
					}
					if(args[0].equalsIgnoreCase("restore"))
					{
						RestoreBlock();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void SaveBlock(Player p, String x1, String y1, String z1, String x2, String y2, String z2){
		blockloc.clear();
		int amt = 0;
		Location pos1 = new Location(getServer().getWorld("world"), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(z1));
		Location pos2 = new Location(getServer().getWorld("world"), Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(z2));
		for(int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++){
			for(int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++){
				for(int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++){
					Location block_loc = new Location(getServer().getWorld("world"), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
					if(block_loc.getBlock().getType() == Material.WOOL){
						amt++;
						getConfig().set("block_loc_"+amt+"_x", block_loc.getBlockX());
						getConfig().set("block_loc_"+amt+"_y", block_loc.getBlockY());
						getConfig().set("block_loc_"+amt+"_z", block_loc.getBlockZ());			
						blockloc.add(block_loc);
					}
				}
			}
		}
		getConfig().set("blockamt", amt);
		saveConfig();
		Loadconfig();
		p.sendMessage(MS+"설정완료");
	}
	
	public static void RestoreBlock(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
			public void run(){
				List<Byte> woollist = new ArrayList<Byte>();
				woollist.add((byte) 1);
				woollist.add((byte) 2);
				woollist.add((byte) 4);
				woollist.add((byte) 5);
				woollist.add((byte) 6);
				woollist.add((byte) 8);
				woollist.add((byte) 10);
				woollist.add((byte) 11);
				woollist.add((byte) 12);
				woollist.add((byte) 13);
				woollist.add((byte) 14);
				woollist.add((byte) 15);
				int base = 0;
				for(int i = 0; i < 5; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				base += 10;
				for(int i = 0; i < 5; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				base += 10;
				for(int i = 0; i < 2; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				woollist.clear();
				woollist.add((byte) 1);
				woollist.add((byte) 2);
				woollist.add((byte) 4);
				woollist.add((byte) 5);
				woollist.add((byte) 6);
				woollist.add((byte) 8);
				woollist.add((byte) 10);
				woollist.add((byte) 11);
				woollist.add((byte) 12);
				woollist.add((byte) 13);
				woollist.add((byte) 14);
				woollist.add((byte) 15);
				for(int i = 0; i < 3; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				base += 10;
				for(int i = 0; i < 5; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				base += 10;
				for(int i = 0; i < 4; i++){
					int rn = Getrandom(0, woollist.size()-1);
					blockloc.get(base).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+1).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+10).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					blockloc.get(base+11).getBlock().setTypeIdAndData(35, woollist.get(rn), true);
					woollist.remove(rn);
					base += 2;
				}
				blockloc.get(base).getBlock().setTypeIdAndData(35, (byte) 5, true);
				blockloc.get(base+1).getBlock().setTypeIdAndData(35, (byte) 5, true);
				blockloc.get(base+10).getBlock().setTypeIdAndData(35, (byte) 5, true);
				blockloc.get(base+11).getBlock().setTypeIdAndData(35, (byte) 5, true);
			}
		}, 40l);
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/ecm join");
		p.sendMessage(MS+"/ecm quit");
		p.sendMessage(MS+"/ecm set");
		p.sendMessage(MS+"/ecm reload");
		p.sendMessage(MS+"/ecm inv");
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"컬러매치가 강제 종료 되었습니다.");
		for(Player p : plist)
		{
	          try{
	        	  me.Bokum.EGM.Main.Spawn(p);
	          } catch(Exception e){
	        	  p.teleport(Main.Lobby);
	          }
		}
		Cleardata();
	}
	
	public static void Cleardata()
	{
		Bukkit.getScheduler().cancelTasks(instance);
		Forcestoptimer = 0;
		game_end = false;
		plist.clear();
		check_start = false;
		check_lobbystart = false;
		RestoreBlock();
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/ecm set lobby");
			p.sendMessage(MS+"/ecm set join");
			p.sendMessage(MS+"/ecm set start 1~15");
			return;
		}
		if(args.length <= 2)
		{
			if(args[1].equalsIgnoreCase("lobby"))
			{
			    getConfig().set("Lobby_world", p.getLocation().getWorld().getName());
			    getConfig().set("Lobby_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Lobby_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Lobby_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"로비 설정 완료");
			}
			else if(args[1].equalsIgnoreCase("join"))
			{
			    getConfig().set("Join_world", p.getLocation().getWorld().getName());
			    getConfig().set("Join_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Join_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Join_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"대기실 설정 완료");
			}
			return;
		}
	}
	
	public static void Sendmessage(String str)
	{
		for(Player p : plist)
		{
			p.sendMessage(str);
		}
	}
	
	public static void GameJoin(Player p)
	{
		if(plist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(plist.size() >= 6)
		{
			p.sendMessage(MS+"이미 최대인원(6)입니다.");
			return;
		}
		if(check_start)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다. 남은인원 : "+plist.size());
			return;
		}
		else
		{
			plist.add(p);
			p.teleport(joinpos);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "컬러매치");
			Sendmessage(MS+p.getName()+" 님이 컬러매치에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c2 §f)");
			if(!check_lobbystart && plist.size() >= 2)
			{
				Startgame();
			}
			for(Player t : plist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
	}
	
	public void GameQuit(Player p)
	{
		if(!plist.contains(p))
		{
			return;
		}
		plist.remove(p);
		if(!check_start)
		{
			return;
		}
		Sendmessage(MS+p.getName()+" §f님이 탈락되셨습니다.");
		if(plist.size() == 1)
		{
			Win(plist.get(0));
		}
	}
	
	public static void Win(final Player p)
	{
		game_end = true;
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		Sendmessage(MS+"당신이 최후로 남았습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = econ.depositPlayer(p.getName(), 300);
				if (r.transactionSuccess()) {
					p.sendMessage(ChatColor.GOLD + "승리 보상으로 300원을 받으셨습니다.");
				}
				try{
					me.Bokum.EGM.Main.Spawn(p);
					} catch(Exception e){
					p.teleport(Main.Lobby);
					}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§a§l컬러매치§f가 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public static int Getrandom(int min, int max){
		  return (int)(Math.random() * (max-min+1)+min);
		}

	  public static void GameHelper(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c컬러매치");
			  p.sendMessage("§f일정시간마다 인벤토리에 양털이 지급됩니다.");
			  p.sendMessage("§f그리고 이 양털색에 맞는 위치에 시간내로 가야합니다.");
			  p.sendMessage("§f시간이 지나면 해당 양털의 땅 이외의 다른 땅은 사라집니다.");
			  p.sendMessage("§f마지막까지 살아남으면 승리 하게됩니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f양털은 버려지지 않습니다.");
			  p.closeInventory();
			  return;
			  
		  default: return;
		  }  
	  }
	  
		public static int Getcursch()
		{
			for(int i = 0 ; i < tasknum.length; i++)
			{
				if(tasknum[i] == -5)
				{
					return i;
				}
			}
			return 0;
		}
		
		
		public static void Canceltask(int cur)
		{
			Bukkit.getScheduler().cancelTask(tasknum[cur]);
			tasknum[cur] = -5;
			tasktime[cur] = -5;
		}
		
		public static void Startgame()
		{
			check_lobbystart = true;
			final int cur = Getcursch();
			tasktime[cur] = 5;
			Bukkit.broadcastMessage(MS+"§a§l컬러매치§f가 곧 시작됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendmessage(MS+"게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.");
						tasktime[cur]--;
					}
					else
					{
						Canceltask(cur);
						check_start = true;
						for(Player p : plist){
							p.getInventory().clear();
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
							p.sendMessage(MS+"게임이 시작 됐습니다.");
						}
						Timer();
					}
				}
			}, 200L, 200L);
		}
		
		public static void SetWool(){
			final int rn = Getrandom(0, 11);
			for(Player p : plist){
				for(int i = 0; i < 9; i++){
					p.getInventory().setItem(i, wool[rn]);	
					p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 0.8f);
				}
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
				public void run(){
					if(timertime > 18){
						timertime -= 2;
					}
					for(Location bl : blockloc){
						if(bl.getBlock().getData() != woolbyte.get(rn))
							bl.getBlock().setType(Material.AIR);
					}
					RestoreBlock();
				}		
			}, timertime);
		}
		
		public static void Timer(){
			timertime = 50;
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					if(check_start && !game_end){
						SetWool();
					} else {
						Bukkit.getScheduler().cancelTask(timernum);
					}
				}
			}, 200l, 200l);
		}
		
		@EventHandler
		public void onOpenChestBlock(PlayerInteractEvent e)
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
			{
				final Player p = e.getPlayer();
				if(!plist.contains(p) || e.getItem() == null) return;
				if(e.getItem().getType() == Material.COMPASS)
						  p.openInventory(gamehelper);
			}
		}
		
		@EventHandler
		public void onClickInventory(InventoryClickEvent e)
		{
			if(e.getWhoClicked() instanceof Player)
			{
				Player p = (Player) e.getWhoClicked();
				if(!plist.contains(p))
				{
					return;
				}
				 if(e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
					 GameHelper(p, e.getSlot());
					 e.setCancelled(true);
				 }
			}
		}
		
		@EventHandler
		public void onPlayerDamage(EntityDamageEvent e){
			if(!(e.getEntity() instanceof Player)) return;
			if(plist.contains((Player) e.getEntity()) && !check_start) e.setCancelled(true);
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e){
			if(!plist.contains(e.getPlayer())) return;
			if(e.getItemDrop().getItemStack().getType() == Material.COMPASS || e.getItemDrop().getItemStack().getType() == Material.WOOL) e.setCancelled(true);
		}
		
		@EventHandler
		public void onPlayercommand(PlayerCommandPreprocessEvent e)
		{
			Player p = e.getPlayer();
			if(plist.contains(p) && (e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
					|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ")))
			{
				GameQuit(p);
			}
		}
		
		@EventHandler
		public void onBlockbreak(BlockBreakEvent e){
			  if(plist.contains(e.getPlayer())){
				  e.setCancelled(true);
			  }
		}
		  
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent e){
			  if(plist.contains(e.getPlayer()))
			e.setCancelled(true);
		}
		
		  @EventHandler
		  public void onQuitPlayer(PlayerQuitEvent e) {
		    if (plist.contains(e.getPlayer()))
		    {
		      GameQuit(e.getPlayer());
		    }
		  }
		  
			@EventHandler
			public void onPlayerdeath(PlayerDeathEvent e)
			{
				Player p = e.getEntity();
				if(plist.contains(p))
				{
					GameQuit(p);
				}
			}
			
			@EventHandler
			public void onPvp(EntityDamageByEntityEvent e){
				if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
					Player p = (Player) e.getEntity();
					Player d = (Player) e.getDamager();
					if(plist.contains(p) && plist.contains(d)){
						e.setCancelled(true);
					}
				}
			}
}

