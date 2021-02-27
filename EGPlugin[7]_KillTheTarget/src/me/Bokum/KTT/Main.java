package me.Bokum.KTT;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static String MS = "§f[ §aEG §f] ";
	public static Location Lobby;
	public static Inventory playerlist;
	public static Location joinpos;
	public static Main instance;
	public static int enabled_box = 0;
	public static String enabled_name = "없음";
	public static ItemStack[] chestitem = new ItemStack[33];
	public static List<Location> chestlist = new ArrayList<Location>();
	public static List<Player> plist = new ArrayList<Player>();
	public static Location startpos[] = new Location[15];
	public static Location boxpos[] = new Location[10];
	public static int tasknum[] = new int[10];
	public static int tasktime[] = new int[10];
	public static ItemStack helpitem;
	public static int timernum = 0;
	public static int timertime = 0;
	public static Inventory gamehelper;
	public static Economy econ = null;
	public static List<String> woollist = new ArrayList<String>();
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static ItemStack wool[] = new ItemStack[9];
	public static int Forcestoptimer = 0;
	
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("타겟암살 플러그인이 로드 되었습니다.");
		
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
		wool[0] = (new ItemStack(Material.WOOL, 1, (short) 14));
		wool[1] = (new ItemStack(Material.WOOL, 1, (short) 1));
		wool[2] = (new ItemStack(Material.WOOL, 1, (short) 4));
		wool[3] = (new ItemStack(Material.WOOL, 1, (short) 5));
		wool[4] = (new ItemStack(Material.WOOL, 1, (short) 11));
		wool[5] = (new ItemStack(Material.WOOL, 1, (short) 9));
		wool[6] = (new ItemStack(Material.WOOL, 1, (short) 10));
		wool[7] = (new ItemStack(Material.WOOL, 1));
		wool[8] = (new ItemStack(Material.WOOL, 1, (short) 15));
		
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
		
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e남은 유저");
		item.setItemMeta(meta2);
		gamehelper.setItem(15, item);
		
		List<String> lorelist = new ArrayList<String>();
		
		chestitem[0] = Makeitem(352, "쇠파이프", 3, 5);
		chestitem[1] = Makeitem(287, "채찍", 4, 4);
		chestitem[2] = Makeitem(406, "접시조각", 2, 3);
		chestitem[3] = Makeitem(267, "식칼", 6, 7);
		chestitem[4] = Makeitem(359, "가위", 5, 5);
		chestitem[5] = Makeitem(369, "금속배트", 4, 5);
		chestitem[6] = new ItemStack(261, 1);
		chestitem[7] = Makeitem(273, "숟가락", 2, 2);
		chestitem[8] = new ItemStack(262, 2);
		chestitem[9] = MakeGun(292, "M9", 7, 7);
		chestitem[10] = Makeitem(362, "9mm탄환", 1, 1);
		chestitem[11] = new ItemStack(373, 1, (short) 16389);
		chestitem[12] = new ItemStack(373, 1, (short) 16421);
		chestitem[13] = new ItemStack(373, 1, (short) 16386);
		chestitem[14] = new ItemStack(373, 1, (short) 16388);
		chestitem[15] = new ItemStack(373, 1, (short) 16385);
		chestitem[16] = Makeitem(377, "투명 망토", 1, 1);
		chestitem[17] = Makeitem(286, "망치", 5, 6);
		chestitem[18] = Makeitem(256, "삽", 5, 6);
		chestitem[19] = Makeitem(362, "9mm탄환", 1, 1);
		chestitem[20] = new ItemStack(300, 1);
		chestitem[21] = new ItemStack(301, 1);
		chestitem[22] = new ItemStack(373, 1, (short) 8194);
		chestitem[23] = Makeitem(340, "사전", 1, 5);
		chestitem[24] = Makeitem(336, "벽돌", 1, 5);
		chestitem[25] = new ItemStack(368, 1);
		chestitem[26] = MakeGun(292, "M9", 7, 7);
		chestitem[27] = Makeitem(362, "9mm탄환", 1, 1);
		chestitem[28] = Makeitem(287, "채찍", 4, 4);
		chestitem[29] = Makeitem(352, "쇠파이프", 3, 5);
		chestitem[30] = Makeitem(369, "금속배트", 4, 5);
		chestitem[31] = Makeitem(267, "장미칼", 7, 8);
		chestitem[32] = Makeitem(264, "저항의 돌", 1, 1);
		
		
		helpitem = new ItemStack(345, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미를 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		playerlist = Bukkit.createInventory(null, 18, "§c§l전화박스");
		
		Loadconfig();
		
        if (!setupEconomy() ) {
            getLogger().info("[버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
        }
	}
	
	  public static void Setplayerlist(){
		  for(int i = 0; i < 18; i++){
			 playerlist.setItem(i, null);
		  }
		  for(int i = 0; i < plist.size(); i++){
			  ItemStack item = new ItemStack(397, 1, (byte) 3);
			  ItemMeta meta = item.getItemMeta();
			  meta.setDisplayName(plist.get(i).getName());
			  List<String> lorelist = new ArrayList<String>();
			  lorelist.add("§c클릭시 이 플레이어의 양털색을 알 수 있습니다.");
			  meta.setLore(lorelist);
			  item.setItemMeta(meta);
			  playerlist.setItem(i, item);
		  }
	  }
	
	  public static ItemStack MakeGun(int id, String name, int min, int max){
		  ItemStack tmpitem = new ItemStack(id, 1);
		  ItemMeta meta = tmpitem.getItemMeta();
		  meta.setDisplayName("§c"+name);
		  List<String> lorelist = new ArrayList<String>();
		  String damage = "§7화력: ";
		  if(max == min){
			  damage += String.valueOf(min);
		  } else {
			  damage += min+"~";
			  damage += max;
		  }
		  lorelist.add(damage);
		  meta.setLore(lorelist);
		  tmpitem.setItemMeta(meta);
		  return tmpitem;
	  }
	
	  public static ItemStack Makeitem(int id, String name, int min, int max){
		  ItemStack tmpitem = new ItemStack(id, 1);
		  ItemMeta meta = tmpitem.getItemMeta();
		  meta.setDisplayName("§c"+name);
		  List<String> lorelist = new ArrayList<String>();
		  String damage = "§7공격력: ";
		  if(max == min){
			  damage += String.valueOf(min);
		  } else {
			  damage += min+"~";
			  damage += max;
		  }
		  lorelist.add(damage);
		  meta.setLore(lorelist);
		  tmpitem.setItemMeta(meta);
		  return tmpitem;
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
		getLogger().info("타겟암살 플러그인이 언로드 되었습니다.");
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
		try
		{
			for(int j = 1; j < 16; j++){
				  startpos[j-1]= new Location(Bukkit.getWorld(getConfig().getString("start_world"+j)), getConfig().getInt("start_x"+j), getConfig().getInt("start_y"+j), getConfig().getInt("start_z"+j));
			}
		}
		catch (Exception e)
		{
			 getLogger().info("시작 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
		try
		{
			for(int j = 1; j < 10; j++){
				  boxpos[j-1]= new Location(Bukkit.getWorld(getConfig().getString("box_world"+j)), getConfig().getInt("box_x"+j), getConfig().getInt("box_y"+j), getConfig().getInt("box_z"+j));
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
			if(str.equalsIgnoreCase("ktt") && p.isOp())
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
					if(args[0].equalsIgnoreCase("test")){
						p.teleport(startpos[Integer.valueOf(args[1])]);
						return true;
					}
					if(args[0].equalsIgnoreCase("wool")){
							Setwool();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/ktt join");
		p.sendMessage(MS+"/ktt quit");
		p.sendMessage(MS+"/ktt set");
		p.sendMessage(MS+"/ktt reload");
		p.sendMessage(MS+"/ktt inv");
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"타겟암살이 강제 종료 되었습니다.");
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
		enabled_box = 0;
		enabled_name = "없음";
		chestlist.clear();
		plist.clear();
		woollist.clear();
		check_start = false;
		check_lobbystart = false;
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/ktt set lobby");
			p.sendMessage(MS+"/ktt set join");
			p.sendMessage(MS+"/kit set start 1~15");
			p.sendMessage(MS+"/kit set box 1~10");
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
				p.sendMessage(MS+"/ktt set start 1~15");
				p.sendMessage(MS+"/kit set box 1~10");
			}
			return;
		}
		else if(args.length >= 3)
		{
			if(args[1].equalsIgnoreCase("start"))
			{
					int i = Integer.valueOf(args[2]);
					if(!(1 <= i && i <= 15))
					{
						p.sendMessage("1~15의 숫자만 입력 가능합니다.");
						return;
					}
						getConfig().set("start_world"+i, p.getWorld().getName());
						getConfig().set("start_x"+i, p.getLocation().getBlockX());
						getConfig().set("start_y"+i, p.getLocation().getBlockY());
						getConfig().set("start_z"+i, p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+i+"번째 시작 지점 설정 완료");
			}
			if(args[1].equalsIgnoreCase("box"))
			{
					int i = Integer.valueOf(args[2]);
					if(!(1 <= i && i <= 15))
					{
						p.sendMessage("1~10의 숫자만 입력 가능합니다.");
						return;
					}
						getConfig().set("box_world"+i, p.getWorld().getName());
						getConfig().set("box_x"+i, p.getLocation().getBlockX());
						getConfig().set("box_y"+i, p.getLocation().getBlockY()-1);
						getConfig().set("box_z"+i, p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+i+"번째 박스 지점 설정 완료");
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
	
	public static void GameJoin(Player p)
	{
		if(plist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(plist.size() >= 9)
		{
			p.sendMessage(MS+"이미 최대인원(9)입니다.");
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
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "타겟 암살");
			Sendmessage(MS+p.getName()+" 님이 타겟 암살에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c4 §f)");
			if(!check_lobbystart && plist.size() >= 4)
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
		Setplayerlist();
		if(!check_start)
		{
			return;
		}
		Sendmessage(MS+p.getName()+" §f님이 탈락되셨습니다.");
		woollist.remove(p.getName());
		if(plist.size() <= 1)
		{
			try{
				Win(plist.get(0));
			}catch(Exception e){
				Forcestop();
			}
		}
	}
	
	public static void Win(final Player p)
	{
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		Sendmessage(MS+"당신이 최후로 남았습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = econ.depositPlayer(p.getName(), 400);
				if (r.transactionSuccess()) {
					p.sendMessage(ChatColor.GOLD + "승리 보상으로 800원을 받으셨습니다.");
				}
				try{
					me.Bokum.EGM.Main.Spawn(p);
					} catch(Exception e){
					p.teleport(Main.Lobby);
					}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§e§l타겟 암살§f이 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
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
			  p.sendMessage("§7게임 이름 §f: §c타겟 암살");
			  p.sendMessage("§f게임이 시작되면 모든 플레이어에게 \n각각 §b다른색§f의 양털을 지급합니다.");
			  p.sendMessage("§f양털색에 따라 §c죽일수 있는 §f양털색이 정해져있습니다.");
			  p.sendMessage("§f양털색에 맞춰 플레이어를 죽여 마지막까지 살아남으면");
			  p.sendMessage("§f승리 하게됩니다. 또한 맵 곳곳에는 §e전화박스§f가\n존재합니다. 이 전화박스는 2분마다");
			  p.sendMessage("§f랜덤으로 1개가 활성화 되며 활성화 된 §e전화박스§f를 우클릭할 시\n플레이어 1명의 양털색을 알 수 있습니다.");
			  p.sendMessage("§f만약 2분이 지나 다른 §e전화박스§f가 활성화 되면 기존에 있던\n§e전화박스§f는 비활성화됩니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f양털은 버려지지 않습니다.");
			  p.sendMessage("§f누군가 죽더라도 양털은 떨어지지 않습니다.");
			  p.sendMessage("§f맵 곳곳에는 상자가 있으며 \n우클릭시 아이템을 얻으실 수 있습니다.\n양털색에 따라 죽이는 순서:");
			  p.sendMessage("§c빨강 §f-> §6주황 §f-> §e노랑 §f-> §a초록 §f-> §b파랑 \n§f-> §1청록 §f-> §9보라 §f-> 하양 -> §8검정  -> §c빨강");
			  p.closeInventory();
			  return;
			  
		  case 15:
			  p.sendMessage("§f- 남은 플레이어들");
			  for(Player t : plist){
				  p.sendMessage("§c- §e"+t.getName());
			  }
			  
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
			Bukkit.broadcastMessage(MS+"§4§l타겟암살§f이 곧 시작됩니다.");
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
							p.sendMessage(MS+"게임이 시작 됐습니다.");
							p.teleport(startpos[Getrandom(0, 14)]);
						}
						Setwool();
						Timer();
					}
				}
			}, 200L, 200L);
		}
		
		 public static boolean Hasitem(Player p, int id, int amt){
			  int tamt = amt;
				for(int i = 0; i < p.getInventory().getSize(); i++){
					if(tamt > 0){
						ItemStack pitem = p.getInventory().getItem(i);
						if(pitem != null && pitem.getTypeId() == id){
							tamt -= pitem.getAmount();
							if(tamt <= 0){
								return true;
							}
						}
					}
				}
				
				return false;
		  }
		  
		  public static boolean Takeitem(Player p, int id, int amt){
			  int tamt = amt;
				for(int i = 0; i < p.getInventory().getSize(); i++){
					if(tamt > 0){
						ItemStack pitem = p.getInventory().getItem(i);
						if(pitem != null && pitem.getTypeId() == id){
							tamt -= pitem.getAmount();
							if(tamt <= 0){
								Removeitem(p, id, amt);
								return true;
							}
						}
					}
				}
				
				return false;
		  }
		  
		  public static void Timer(){
			  timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				  public void run(){
					  if(check_start){
						  int rn = Getrandom(0, 8);
						  enabled_box = rn;
						  enabled_name = null;
						  Setplayerlist();
						  Sendmessage(MS+(rn+1)+"번 전화박스가 활성화 되었습니다. \n위치 : §eX: "+boxpos[rn].getBlockX()+" Y: "+boxpos[rn].getBlockY()+" Z: "+boxpos[rn].getBlockZ());
					  }else{
						  Bukkit.getScheduler().cancelTask(timernum);
					  }
				  }
			  }, 1200L, 2400L);
		  }
		  
		  public static void Removeitem(Player p, int id, int amt){
				for(int i = 0; i < p.getInventory().getSize(); i++){
					if(amt > 0){
						ItemStack pitem = p.getInventory().getItem(i);
						if(pitem != null && pitem.getTypeId() == id){
							if(pitem.getAmount() >= amt){
								int itemamt = pitem.getAmount()-amt;
								pitem.setAmount(itemamt);
								p.getInventory().setItem(i, amt > 0 ? pitem : null);
							    p.updateInventory();
								return;
							}
							else{
								amt -= pitem.getAmount();
								p.getInventory().setItem(i, null);
							    p.updateInventory();
							} 
						}
					}
					else{
						return;
					}
				}
		  }
		
		public static void Setwool(){
			List<String> lorelist = new ArrayList<String>();
			String str = "";
			if(plist.size() >= 1){
				str += "§c빨강";
			}
			if(plist.size() >= 2){
				str += "§f -> §6주황";
				if(plist.size() == 2){
					str += "§f -> §c빨강 ";
				}
			}
			if(plist.size() >= 3){
				str += "§f -> §e노랑";
				if(plist.size() == 3){
					str += "§f -> §c빨강 ";
				}
				lorelist.add(str);
				str = "";
			}
			if(plist.size() >= 4){
				str += "§f -> §a초록";
				if(plist.size() == 4){
					str += "§f -> §c빨강 ";
					lorelist.add(str);
				}
			}
			if(plist.size() >= 5){
				str += "§f -> §b파랑";
				if(plist.size() == 5){
					str += "§f -> §c빨강 ";
					lorelist.add(str);
				}
			}
			if(plist.size() >= 6){
				str += "§f -> §1청록";
				if(plist.size() == 6){
					str += "§f -> §c빨강 ";
				}
				lorelist.add(str);
				str = "";
			}
			if(plist.size() >= 7){
				str += "§f -> §9보라";
				if(plist.size() == 7){
					str += "§f -> §c빨강 ";
					lorelist.add(str);
				}
			}
			if(plist.size() >= 8){
				str += "§f -> §f하양";
				if(plist.size() == 8){
					str += "§f -> §c빨강 ";
					lorelist.add(str);
				}
			}
			if(plist.size() >= 9){
				str += "§f -> §8검정";
				if(plist.size() == 9){
					str += "§f -> §c빨강 ";
				}
				lorelist.add(str);
			}
			for(int i = 0; i < wool.length; i++){
				ItemMeta meta = wool[i].getItemMeta();
				meta.setLore(lorelist);
				wool[i].setItemMeta(meta);
			}
			List<Player> tmplist = new ArrayList<Player>();
			for(Player t : plist){
				tmplist.add(t);
			}
			for(int i = 0; i < plist.size(); i++){
				int rn = Getrandom(0, (tmplist.size()-1));
				Player p = tmplist.get(rn);
				woollist.add(p.getName());
				p.getInventory().setItem(8, wool[i]);
				tmplist.remove(p);
			}
		}
		
		public void UseBox(Player p, String name){
			Player t = getServer().getPlayer(name);
			Inventory inven = t.getInventory();
			String str ="";
			if(inven.contains(wool[0])) str = "빨강";
			if(inven.contains(wool[1])) str = "주황";
			if(inven.contains(wool[2])) str = "노랑";
			if(inven.contains(wool[3])) str = "초록";
			if(inven.contains(wool[4])) str = "파랑";
			if(inven.contains(wool[5])) str = "청록";
			if(inven.contains(wool[6])) str = "보라";
			if(inven.contains(wool[7])) str = "하양";
			if(inven.contains(wool[8])) str = "검정";
			p.sendMessage(MS+t.getName()+" 님의 양털색은 §7"+str+" 입니다.");
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
		public void onPickUpItem(PlayerPickupItemEvent e){
			if(e.getItem().getItemStack().getTypeId() == 35){
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e){
			if(!plist.contains(e.getPlayer())) return;
			if(e.getItemDrop().getItemStack().getType() == Material.WOOL){
				e.getPlayer().sendMessage(MS+"양털은 버리실 수 없습니다.");
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onDamaged(EntityDamageEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(plist.contains(p)){
					if(!check_start){
						e.setCancelled(true);
					} else if ((e.getCause() == EntityDamageEvent.DamageCause.FALL))
				      {
				        e.setCancelled(true);
				        p.damage(e.getDamage());
				      }
				}
			}
		}
		
		  public static void Givechestitem(Player p){
			  int num = Getrandom(0,35);
			  p.getWorld().playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
			  if(num == 35){
				  p.sendMessage("§7아무런 아이템도 찾을 수 없다.");
				  return;
			  }
			  if(num == 34){
				  p.sendMessage("§7잡동사니밖에 보이지 않는다.");
				  return;
			  }
			  if(num == 33){
				  p.sendMessage("§7특별한게 있어보이진 않는다.");
				  return;
			  }
			  if(num == 8 || num == 19 || num == 10 || num == 27){
				  for(int i = 0; i <= Getrandom(1,5); i++)
					  p.getInventory().addItem(chestitem[num]);
				  p.updateInventory();
				  return;
			  }
			  p.getInventory().addItem(chestitem[num]);
			  p.updateInventory();
		  }
		  
		  public static int getItemDamage(Player p){
			  ItemStack item = p.getItemInHand();
			  if(item == null || !item.hasItemMeta()) return 1;
			  if(!item.getItemMeta().hasLore()) return 1;
			  List<String> lorelist = item.getItemMeta().getLore();
			  if(lorelist.size() <= 0) return 1;
			  String damagestr = lorelist.get(0);
			  if(!damagestr.contains("공격력")) return 1;
			  if(!damagestr.contains("~"))return Integer.valueOf(damagestr.substring(7, 8));
			  int min = Integer.valueOf(damagestr.substring(7, 8));
			  int max = Integer.valueOf(damagestr.substring(9, 10));
			  return (int)(Math.random()*(max-min+1)+min);
		  }
		
		@EventHandler
		public void onPvp(EntityDamageByEntityEvent e){
			Player p = null;
			Player d = null;
			if(!(e.getEntity() instanceof Player)) return;
			p = (Player) e.getEntity();
			if(!plist.contains(p)) return;
			if(e.getDamager() instanceof Snowball){
				Snowball snowball = (Snowball) e.getDamager();
				if(snowball.getShooter() instanceof Player){
					d = (Player) snowball.getShooter();
					e.setDamage(7);
				}
			}
			if(e.getDamager() instanceof Arrow){
				Arrow arrow = (Arrow) e.getDamager();
				if(arrow.getShooter() instanceof Player){
					d = (Player) arrow.getShooter();
				}
			}
			if(e.getDamager() instanceof Player){
				d = (Player) e.getDamager();
			}
		      if(d != null && d.getItemInHand() != null && d.getItemInHand().getType() != Material.AIR && getItemDamage(d) != 0){
		    	  e.setDamage(getItemDamage(d));
		      }

			if(plist.contains(p) && plist.contains(d)){
				if((p.getHealth()-e.getDamage()) <= 0){
					int killerwool = woollist.indexOf(d.getName());
					int playerwool = woollist.indexOf(p.getName());
					if((killerwool+1) == woollist.size()){
						killerwool = -1;
					}
					if((killerwool+1) != playerwool){
						d.sendMessage(MS+"잘못된 플레이어를 죽여 탈락했습니다!");
						GameQuit(d);
						d.damage(100);
						p.setHealth(20);
						p.sendMessage(MS+"§6"+d.getName()+" §f님의 타겟은 당신이 아니었습니다!");
					}
				}
			}
		}		
		
		  @EventHandler
		  public void onRightClick(PlayerInteractEvent e){
			  if(!plist.contains(e.getPlayer()) || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
			  Player p = e.getPlayer();
			  if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BOOKSHELF 
					  && check_start){
				  Location bl = e.getClickedBlock().getLocation();
				  Location bx = boxpos[enabled_box];
				  if(!(bl.getBlockX() == bx.getBlockX() && bl.getBlockY() == bx.getBlockY() && bl.getBlockZ() == bx.getBlockZ())) return;
				  if(enabled_name != null){
					  p.sendMessage(MS+"이미 "+enabled_name+" 님께서 전화박스를 사용했습니다.");
					  return;
				  }
				  enabled_name = p.getName();
				  Sendmessage(MS+p.getName()+" 님이 전화박스를 사용했습니다!");
				  p.openInventory(playerlist);
			  }
			  if(e.getClickedBlock() != null && e.getClickedBlock().getTypeId() == 54 && check_start){
				  e.setCancelled(true);
				  final Location bl = e.getClickedBlock().getLocation();
				  if(!chestlist.contains(bl)){
					  chestlist.add(bl);
					  Givechestitem(p);
					  Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
						  public void run(){
							  chestlist.remove(bl);
						  }
					  }, 1200L);
				  } else {
					  p.sendMessage("§7이미 누군가 털어간 듯 하다.");
				  }
			  }
			  if(e.getItem() == null) return;
				if(e.getItem().getType() == Material.COMPASS)
					  p.openInventory(gamehelper);
				else if(e.getItem().getType() == Material.IRON_HOE){
				  if(!Takeitem(p, 362, 1)){
					  p.sendMessage("§c9mm탄환이 부족합니다."); return;
				  }
				  Snowball snowball = p.launchProjectile(Snowball.class); //here we launch the snowball
				  Block target = p.getTargetBlock(null, 50);
				  Vector velocity = (target.getLocation().toVector().subtract(snowball.getLocation().toVector()).normalize()).multiply(7);
				  snowball.setVelocity(velocity);
				  snowball.setShooter(p);
				  p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 4.0f, 2.0f);
				  p.getWorld().playEffect(e.getPlayer().getLocation(), Effect.SMOKE, 20); //We will play a smoke effect at the shooter's location
			  } else if(e.getItem().getTypeId() == 377){
				  p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
				  Removeitem(p, 377, 1);
				  p.sendMessage(MS+"투명 망토를 사용 했습니다. 15초간 모습을 감춥니다.");
			  } else if(e.getItem().getTypeId() == 264){
				  p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
				  Removeitem(p, 264, 1);
				  p.sendMessage(MS+"저항의 돌을 사용 했습니다.  5초간 저항2를 받게됩니다.");
			  }
		  }
		  
		  @EventHandler
		  public void onClickInventory(InventoryClickEvent e){
			  if(!(e.getWhoClicked() instanceof Player)) return; 
			  Player p = (Player) e.getWhoClicked();
			  if(!plist.contains(p)) return;
			  if(e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
				  GameHelper(p, e.getSlot());
				  e.setCancelled(true);
			  }
			  if(e.getInventory().getTitle().equalsIgnoreCase("§c§l전화박스")){
				  e.setCancelled(true);
				  if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)return;
				  p.closeInventory();
				  UseBox(p, e.getCurrentItem().getItemMeta().getDisplayName());
			  }
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
			public void onPlayerdeath(PlayerDeathEvent e)
			{
				Player p = e.getEntity();
				if(plist.contains(p))
				{
					for(int i = 0; i < 9; i++){
						e.getDrops().remove(wool[i]);
					}
					GameQuit(p);
				}
			}
}
