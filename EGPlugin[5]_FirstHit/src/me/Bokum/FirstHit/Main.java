package me.Bokum.FirstHit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static List<Player> plist = new ArrayList<Player>();
	public static String MS = "§f[§aEG§f]§e ";
	public static Economy econ = null;
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static Location Lobby;
	public static Location[] startpos = new Location[30];
	public static Location joinpos;
	public static ItemStack helpitem;
	public static int schtime = 5;
	public static Main instance;
	public static boolean game_end = false;
	public static ItemStack stick;
	public static int schnum = 0;
	public static int topkill = 0;
	public static Inventory gamehelper;
	public static int Forcestoptimer = 0;
	public static HashMap<String, Integer> killlist = new HashMap<String, Integer>();
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("선빵게임 플러그인이 로드 되었습니다.");
		
		instance = this;
		
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
		
		helpitem = new ItemStack(345, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		List<String> lorelist1 = new ArrayList<String>();
		lorelist1.clear();
		lorelist1.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist1);
		helpitem.setItemMeta(meta1);
		
		stick = new ItemStack(280, 1);
		ItemMeta meta = stick.getItemMeta();
		meta.setDisplayName(MS+"선빵게임");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§b선빵을 날리세요!");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		
		Loadconfig();
		
        if (!setupEconomy() ) {
            getLogger().info("[버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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
	  for (int i = 1; i <= startpos.length; i++)
	  {
			ConfigurationSection sec = getConfig().getConfigurationSection("Pos"+i);
			try
			{
			  startpos[i-1] = new Location(Bukkit.getWorld(sec.getString("Start_world")), sec.getInt("Start_x"), sec.getInt("Start_y"), sec.getInt("Start_z"));
			}
			catch (Exception e)
			{
			  getLogger().info(i + "번째  지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
			}
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
		getLogger().info("선빵게임 플러그인이 언로드 되었습니다.");
	}
	
	public boolean onCommand(CommandSender talker, Command command, String string, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(string.equalsIgnoreCase("fh") && p.hasPermission("fh") && args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("join"))
				{
					GameJoin(p);
					return true;
				}
				if(args[0].equalsIgnoreCase("set"))
				{
					Setloc(p, args);
					return true;
				}
				else if(args[0].equalsIgnoreCase("quit"))
				{
					GameQuit(p);
					return true;
				}
				else if(args[0].equalsIgnoreCase("stop"))
				{
					Forcestop();
					return true;
				}
				else if(args[0].equalsIgnoreCase("test"))
				{
					p.teleport(startpos[Integer.valueOf(args[1])]);
					return true;
				}
			}
		}
		return false;
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/fh set lobby");
			p.sendMessage(MS+"/fh set join");
			p.sendMessage(MS+"/fh set pos 1~30");
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
			else
			{
				p.sendMessage(MS+"/trb set pos 1~6 <spec/red/blue>");
			}
			return;
		}
		else if(args.length >= 3)
		{
			if(args[1].equalsIgnoreCase("pos"))
			{
					int i = Integer.valueOf(args[2]);
					if(!(1 <= i && i <= 30))
					{
						p.sendMessage("1~30의 숫자만 입력 가능합니다.");
						return;
					}
					if(!getConfig().isConfigurationSection("Pos"+i))
					{
						getConfig().createSection("Pos"+i);
					}
						getConfig().getConfigurationSection("Pos"+i).set("Start_world", p.getWorld().getName());
						getConfig().getConfigurationSection("Pos"+i).set("Start_x", p.getLocation().getBlockX());
						getConfig().getConfigurationSection("Pos"+i).set("Start_y", p.getLocation().getBlockY()+1);
						getConfig().getConfigurationSection("Pos"+i).set("Start_z", p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+"pos "+i+"의 spec 설정 완료");
			}
		}
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"선빵게임이 강제 종료 되었습니다.");
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
	
	public static void Cleardata(){
		Bukkit.getScheduler().cancelTasks(instance);
		Forcestoptimer = 0;
		plist.clear();
		check_start = false;
		check_lobbystart = false;
		schtime = 5;
		topkill = 0;
		killlist.clear();
		game_end = false;
	}
	
	public static void GameJoin(Player p)
	{
		if(plist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(plist.size() >= 7)
		{
			p.sendMessage(MS+"이미 최대인원(7)입니다.");
			return;
		}
		if(check_start)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다.");
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
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "선빵게임");
			Sendmessage(MS+p.getName()+" 님이 선빵게임에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c3 §f)");
			if(!check_lobbystart && plist.size() >= 3)
			{
				Startgame();
			}
			for(Player t : plist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
	}
	
	public static void Sendmessage(String str)
	{
		for(Player p : plist)
		{
			p.sendMessage(str);
		}
	}
	
	public static void Startgame()
	{
		check_lobbystart = true;
		Bukkit.broadcastMessage(MS+"§d§l선빵게임§f이 곧 시작됩니다.");
		schtime = 5;
		schnum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
		{
			public void run()
			{
				if(schtime > 0)
				{
					Sendmessage(MS+"게임이 "+schtime*10+" 초 후 시작됩니다.");
					schtime--;
				}
				else
				{
					Bukkit.getScheduler().cancelTask(schnum);
					check_start = true;
					for(Player p : plist){
						Setitem(p);
						p.teleport(startpos[Getrandom(0, 6)]);
					}
					if(plist.size() <= 0){
						Forcestop();
					}
				}
			}
		}, 200L, 200L);
	}
	
	public static int Getrandom(int min, int max){
		  return (int)(Math.random() * (max-min+1)+min);
		}
	
	public static void Setitem(final Player p){
			p.getInventory().addItem(stick);
			p.updateInventory();
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
				public void run(){
					p.setSneaking(true);
				}
			}, 10L);
	}
	
	public void Win(final Player p){
		game_end = true;
		Sendmessage(MS+p.getName()+" 님께서 승리하셨습니다!");
		for(Player t : plist){
			t.getInventory().clear();
		}
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			public void run()
			{	
				for(Player t : plist)
				{
						EconomyResponse r = econ.depositPlayer(p.getName(), 100);
						if (r.transactionSuccess()) 
						{
							t.sendMessage(ChatColor.GOLD + "참여 보상으로 100원을 받으셨습니다.");
					          try{
					        	  me.Bokum.EGM.Main.Spawn(t);
					          } catch(Exception e){
					        	  t.teleport(Main.Lobby);
					          }
						}
				}
				Cleardata();
				EconomyResponse r = econ.depositPlayer(p.getName(), 300);
				p.sendMessage(ChatColor.GOLD + "승리 보상으로 300원을 받으셨습니다.");
				Bukkit.broadcastMessage(MS+"§e§l선빵게임§f이 §c"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
		}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	  public static void GameHelper(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c선빵게임");
			  p.sendMessage("§f게임이 시작되면 §6막대기§f를 하나 받습니다.");
			  p.sendMessage("§f이 막대기로 상대를 타격시 상대는 즉사 하게됩니다.");
			  p.sendMessage("§f총 15킬을 한 플레이어가 승리합니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f사망하셔도 부활합니다. \n§f리스폰 위치는 랜덤입니다.");
			  p.closeInventory();
			  return;
			  
		  default: return;
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
			Sendmessage(MS+"§6"+p.getName()+" §f님이 퇴장 하셨습니다.");
			if(plist.size() == 1)
			{
				try{
					Win(plist.get(0));
				}catch(Exception e){
					Forcestop();
				}
			}
	}
	
	@EventHandler
	public void onPlayerdeath(PlayerDeathEvent e){
		if(plist.contains(e.getEntity())) e.getDrops().clear();
	}
	
	@EventHandler
	public void onBlockbreak(BlockBreakEvent e){
		  if(plist.contains(e.getPlayer()))
		e.setCancelled(true);
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
	public void onSneak(PlayerToggleSneakEvent e)
	{
		if(plist.contains(e.getPlayer()) && check_start)
		{
			e.getPlayer().sendMessage(MS+"해당 게임은 sneak을 사용할 필요가 없습니다.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(e.getItem() == null) return;
		if(!plist.contains(p)) return;
		if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
		{
			 if(e.getItem().getType() == Material.COMPASS){
				  p.openInventory(gamehelper);
			 }
		}
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent e)
	{
		if(e.getWhoClicked() instanceof Player)
		{
			Player p = (Player) e.getWhoClicked();
			if(plist.contains(p) && e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
					 GameHelper(p, e.getSlot());
					 e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		if(plist.contains(e.getPlayer())){
			e.setRespawnLocation(startpos[Getrandom(0, 29)]);
			Setitem(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		if(plist.contains(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamge(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(plist.contains(p) && !check_start){
			e.setCancelled(true); return;
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if(plist.contains(p) && plist.contains(d) && d.getItemInHand().getTypeId() == 280 && !game_end)
			{
				e.setCancelled(true);
				p.damage(30);
				p.sendMessage(MS+d.getName()+" 님이 당신에게 선빵을 날렸습니다!");
				d.sendMessage(MS+p.getName()+" 님에게 선빵을 날렸습니다!");
			      EconomyResponse r = econ.depositPlayer(d.getName(), 25);
			      if (r.transactionSuccess()) {
			            d.sendMessage(ChatColor.GOLD + "사살 보상으로 25원을 받으셨습니다.");
			      }
			      if(!killlist.containsKey(d.getName())){
			    	  killlist.put(d.getName(), 1);
			    	  return;
			      }
			      int killcount = killlist.get(d.getName());
			      if(++killcount > topkill){
			    	  Sendmessage(MS+"현재 §6"+d.getName()+" §f님이 선두입니다. (§c"+killcount+"킬§f)");
			    	  topkill = killcount;
			      }else d.sendMessage("§f현재 킬 수 : §e"+killcount+"킬");
			      killlist.put(d.getName(), killcount);
			      if(killcount >= 15) Win(d);
			}
		}
	}
}
