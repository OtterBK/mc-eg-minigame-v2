package me.Bokum.FindTheMurder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
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
	public ItemStack t = new ItemStack(Material.)
	
  public static String MS = ChatColor.RESET + "[" + ChatColor.GREEN + "EG" + ChatColor.RESET + "] " + ChatColor.YELLOW;
  public static List<Player> list = new ArrayList<Player>();
  public static HashMap<String, Player> plist = new HashMap<String, Player>();
  public static HashMap<String, String> jlist = new HashMap<String, String>();
  public static HashMap<String, String> glist = new HashMap<String, String>();
  public static HashMap<String, Integer> vlist = new HashMap<String, Integer>();
  public static List<Location> chestlist = new ArrayList<Location>();
  public static List<Player> vplist = new ArrayList<Player>();
  public static Main instance;
  public static Location Lobby;
  public static Location[] Startpos = new Location[15];
  public static Location joinpos;
  public static Inventory gamehelper;
  public static Inventory votemenu;
  public static int schtmp2 = 0;
  public static ItemStack[] chestitem = new ItemStack[23];
  public static ItemStack rose_sword;
  public static ItemStack helpitem;
  public static Economy econ = null;
  public static int Forcestoptimer = 0;

  public void LoadConfig()
  {
    try
    {
      joinpos = new Location(Bukkit.getWorld(getConfig().getString("Join_world")), getConfig().getInt("Join_x"), getConfig().getInt("Join_y"), getConfig().getInt("Join_z"));
      Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
    }
    catch (IllegalArgumentException e)
    {
      getLogger().info("시작 지점 또는 로비가 설정되어 있지 않습니다");
    }
    for (int i = 1; i <= Startpos.length; i++)
    {
      try
      {
        Startpos[(i - 1)] = new Location(Bukkit.getWorld(getConfig().getString("Start_world" + i)), getConfig().getInt("Start_x" + i), getConfig().getInt("Start_y" + i), getConfig().getInt("Start_z" + i));
      }
      catch (IllegalArgumentException e)
      {
        getLogger().info(i + "번째 시작지점이 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
      }
    }
	gamehelper = Bukkit.createInventory(null, 27, "§c§l도우미");
	
	if (!setupEconomy() ) {
        getLogger().info("[버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
    }
	
	ItemStack item = new ItemStack(34, 1);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName("§6장식");
	item.setItemMeta(meta);
	for(int i = 0; i <= 9; i++){
		gamehelper.setItem(i, item);
	}
	for(int i = 17; i < 27; i++){
		gamehelper.setItem(i, item);
	}
	
	item = new ItemStack(Material.BOOK, 1);
	meta = item.getItemMeta();
	meta.setDisplayName("§e플레이 방법");
	item.setItemMeta(meta);
	gamehelper.setItem(11, item);
	
	item = new ItemStack(Material.BOOK, 1);
	meta = item.getItemMeta();
	meta.setDisplayName("§e게임 규칙");
	item.setItemMeta(meta);
	gamehelper.setItem(13, item);
	
	item = new ItemStack(Material.BOOK, 1);
	meta = item.getItemMeta();
	meta.setDisplayName("§e투표 기능");
	List<String> lorelist = new ArrayList<String>();
	lorelist.add("§f- §c게임이 시작된 후 사용할 수 있습니다.");
	item.setItemMeta(meta);
	gamehelper.setItem(15, item);
	
	votemenu = Bukkit.createInventory(null, 18, "§c§l투표");
	
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
	chestitem[20] = new ItemStack(298, 1);
	chestitem[21] = new ItemStack(299, 1);
	chestitem[22] = Makeitem(264, "저항의 돌", 1, 1);
	
	rose_sword = Makeitem(276, "장미칼", 7, 8);
	
	helpitem = new ItemStack(345, 1);
	meta = helpitem.getItemMeta();
	meta.setDisplayName("§f[ §6게임 도우미 §f]");
	lorelist.clear();
	lorelist.add("§f- §7우클릭시 게임 도움미 엽니다.");
	meta.setLore(lorelist);
	helpitem.setItemMeta(meta);
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

  public static void Cancelalltasks()
  {
    Bukkit.getScheduler().cancelTasks(instance);
  }

  public static void Startgame()
  {
    Bukkit.broadcastMessage(MS + ChatColor.DARK_RED + "살인마를 찾아라 " + ChatColor.GRAY + "게임이 곧 시작됩니다.");
    GameSystem.schtime = 5;
    schtmp2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
    {
      public void run()
      {
        if (GameSystem.schtime > 0)
        {
          Main.SendMessage(Main.MS + ChatColor.DARK_RED + "살인마를 찾아라 " + ChatColor.GRAY + "게임이 " + ChatColor.AQUA + GameSystem.schtime * 10 + ChatColor.GRAY + " 초 후 시작합니다.");
          GameSystem.schtime -= 1;
        }
        else
        {
          Bukkit.getScheduler().cancelTask(Main.schtmp2);
          Main.StartTP();
        }
      }
    }
    , 200L, 200L);
  }

  public static void StartTP()
  {
    Bukkit.broadcastMessage(MS + ChatColor.DARK_RED + "살인마를 찾아라 " + ChatColor.GRAY + "가 시작 되었습니다!");
    GameSystem.checkstart = true;
    for (int i = 0; i < list.size(); i++)
    {
      ((Player)list.get(i)).teleport(Startpos[GameSystem.Getrandom(Startpos.length)]);
    }
    GameSystem.schtime = 3;
    schtmp2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
    {
      public void run()
      {
        if (GameSystem.schtime > 0)
        {
          Main.SendMessage(Main.MS + "직업이 " + ChatColor.GOLD + GameSystem.schtime * 10 + ChatColor.GRAY + "초 후 정해집니다.");
          GameSystem.schtime -= 1;
        }
        else
        {
          Bukkit.getScheduler().cancelTask(Main.schtmp2);
          GameSystem.Setjob();
          GameSystem.GiveBasicitem();
          Main.DayCycle();
          Setvoteinven();
        }
      }
    }
    , 0L, 200L);
  }


  public static void DayCycle()
  {
    if (GameSystem.checkstart)
    {
      if (GameSystem.isday)
      {
    	  GameSystem.Setvotelist();
        SetNight();
      }
      else
      {
        SetDay();
      }
    }
  }

  public static void SetDay()
  {
    GameSystem.isday = true;
    GameSystem.dayinit();
    GameSystem.daycnt += 1;
    for(Player p : Main.list){
    	p.playSound(p.getLocation(), Sound.CHICKEN_IDLE, 1.5f, 1.0f);
    	p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1140, 0));
    }
    SendMessage(MS + ChatColor.AQUA + GameSystem.daycnt + ChatColor.YELLOW + "일째 날의 낮이 되었습니다.");
    SendMessage(MS + "직업을 밝히고 싶은 플레이어에게 투표하세요. \n§c게임 도우미 -> 투표기능");
    for(int i = 0; i < list.size(); i++){
    	if(GameSystem.Getjob(list.get(i).getName()).equalsIgnoreCase("배신자")){
    		list.get(i).sendMessage(MS+"당신은 시민팀을 배신하지 않기로 결심했습니다. (§4시민 살인 불가능.§f)");
    	}
    }
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
    	  Main.SendMessage(Main.MS+"곧 밤이 됩니다. \n투표를 하지 않은 플레이어는 빨리 투표해주세요!\n§c게임 도우미 -> 투표기능");
    	    for(Player p : Main.list){
    	    	p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.5f, 0.5f);
    	    }
      }
    }
    , 1000L);
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
        Main.DayCycle();
      }
    }
    , 1200L);
  }

  public static void SetNight()
  {
    GameSystem.isday = false;
    GameSystem.nightinit();
    for(Player p : Main.list){
    	p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 1.5f, 1.4f);
    }
    SendMessage(MS + ChatColor.AQUA + GameSystem.daycnt + ChatColor.YELLOW + "번째 날의 밤이 되었습니다.");
    PotionEffect pt = new PotionEffect(PotionEffectType.BLINDNESS, 940, 0);
    for (int i = 0; i < list.size(); i++)
    {
      ((Player)list.get(i)).addPotionEffect(pt);
    }
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
    	  Main.SendMessage(Main.MS+"곧 낮이 됩니다.");
    	    for(Player p : Main.list){
    	    	p.playSound(p.getLocation(), Sound.COW_IDLE, 1.5f, 1.4f);
    	    }
      }
    }
    , 800L);
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
        Main.DayCycle();
      }
    }
    , 1000L);
  }

  public static void Murderwin()
  {
    SendMessage(MS + ChatColor.RED + "모든 시민팀이 사망했습니다!!!");
    SendMessage(MS + ChatColor.RED + "살인팀이 승리했습니다!");
    SendMessage(MS + "플레이 보상으로 600원을 받으셨습니다.");
    try{
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
        for (Player player : list)
        {
        	econ.depositPlayer(player.getName(), 600);
          try{
        	  me.Bokum.EGM.Main.Spawn(player);
          } catch(Exception e){
        	  //player.teleport(Main.Lobby);
          }
        }
        GameSystem.End();
        Bukkit.broadcastMessage(Main.MS + ChatColor.GREEN + ChatColor.BOLD + "살인마팀의 승리로 살인마를 찾아라가 종료 되었습니다.");
      }
    }
    , 100L);
    }catch(Exception e){
    	GameSystem.ForceEnd();
    }
  }

  public static void Civilwin()
  {
    SendMessage(MS + ChatColor.RED + "모든 살인마팀이 사망했습니다!!!");
    SendMessage(MS + ChatColor.RED + "시민팀이 승리했습니다!");
    SendMessage(MS + "플레이 보상으로 400원을 받으셨습니다.");
    try{
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
    {
      public void run()
      {
        for (Player player : list)
        {
            econ.depositPlayer(player.getName(), 400);
          try{
        	  me.Bokum.EGM.Main.Spawn(player);
          } catch(Exception e){
        	  //player.teleport(Main.Lobby);
          }
        }
        GameSystem.End();
        Bukkit.broadcastMessage(Main.MS + ChatColor.GREEN + ChatColor.BOLD + "시민팀의 승리로 살인마를 찾아라가 종료 되었습니다.");
      }
    }
    , 100L);
  }catch(Exception e){
  	GameSystem.ForceEnd();
  }
  }

  public void SaveLobby(Player player)
  {
    getConfig().set("Lobby_world", player.getLocation().getWorld().getName());
    getConfig().set("Lobby_x", Integer.valueOf(player.getLocation().getBlockX()));
    getConfig().set("Lobby_y", Integer.valueOf(player.getLocation().getBlockY() + 1));
    getConfig().set("Lobby_z", Integer.valueOf(player.getLocation().getBlockZ()));
    saveConfig();
    LoadConfig();
    player.sendMessage(MS + "메인로비가 설정 되었습니다.");
  }

  public void SaveStartpos(Player player, int posn)
  {
    getConfig().set("Start_world" + posn, player.getLocation().getWorld().getName());
    getConfig().set("Start_x" + posn, Integer.valueOf(player.getLocation().getBlockX()));
    getConfig().set("Start_y" + posn, Integer.valueOf(player.getLocation().getBlockY() + 1));
    getConfig().set("Start_z" + posn, Integer.valueOf(player.getLocation().getBlockZ()));
    saveConfig();
    LoadConfig();
    player.sendMessage(MS + posn + "번째 시작지점이 설정 되었습니다.");
  }

  public void SaveJoinpos(Player player)
  {
    getConfig().set("Join_world", player.getLocation().getWorld().getName());
    getConfig().set("Join_x", Integer.valueOf(player.getLocation().getBlockX()));
    getConfig().set("Join_y", Integer.valueOf(player.getLocation().getBlockY() + 1));
    getConfig().set("Join_z", Integer.valueOf(player.getLocation().getBlockZ()));
    saveConfig();
    LoadConfig();
    player.sendMessage(MS + "로비가 설정 되었습니다.");
  }

  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(this, this);
    getLogger().info("살인마를 찾아라 플러그인 로드 완료! - 제작 : Bokum");
    instance = this;
    LoadConfig();
  }

  public void onDisable()
  {
    getLogger().info("살인마를 찾아라 플러그인 언로드 완료");
  }

  public boolean onCommand(CommandSender talker, Command command, String string, String[] args)
  {
    if ((talker instanceof Player))
    {
      Player player = (Player)talker;
      if (string.equalsIgnoreCase("ftm"))
      {
        if (args.length == 0)
        {
          HelpMessage(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("set"))
        {
          if (!player.hasPermission("ftm.set"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          if (args.length <= 1)
          {
            HelpMessage(player);
            return true;
          }
          if (args[1].equalsIgnoreCase("start"))
          {
            if (args.length <= 2)
            {
              player.sendMessage(MS + "/ftm set start <1~15>");
              return true;
            }
            SaveStartpos(player, Integer.valueOf(args[2]).intValue());
            return true;
          }
          if (args[1].equalsIgnoreCase("lobby"))
          {
            SaveLobby(player);
            return true;
          }
          if (args[1].equalsIgnoreCase("join"))
          {
            SaveJoinpos(player);
            return true;
          }

          HelpMessage(player);
        }

        if (args[0].equalsIgnoreCase("join"))
        {
          if (!player.hasPermission("ftm.join"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          GameSystem.Joingame(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("quit"))
        {
          if (!player.hasPermission("ftm.quit"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          GameSystem.Quitplayer(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("use"))
        {
          if (!plist.containsKey(player.getName()))
          {
            player.sendMessage(MS + "게임에 참여중이지 않습니다.");
            return true;
          }
          if (!jlist.containsKey(player.getName()))
          {
            player.sendMessage(MS + "직업이 설정되어 있지 않습니다. - 버그로 추정");
            return true;
          }
          String target = null;
          if (args.length > 1)
          {
            target = args[1];
          }
          GameSystem.Use(player, target);
          return true;
        }
        if (args[0].equalsIgnoreCase("help"))
        {
          Jobhelp(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("list"))
        {
          Glisting(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("jlist"))
        {
          if (!player.hasPermission("ftm.jlist"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          Jlisting(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("reload"))
        {
          if (!player.hasPermission("ftm.reload"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          LoadConfig();
          return true;
        }
        if (args[0].equalsIgnoreCase("test"))
        {
          if (!player.hasPermission("ftm.test"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          list.add(player);
		  Givechestitem(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("stop"))
        {
          if (!player.hasPermission("ftm.stop"))
          {
            player.sendMessage(MS + "권한이 없습니다.");
            return false;
          }
          GameSystem.ForceEnd();
          return true;
        }
        if (args[0].equalsIgnoreCase("vote"))
        {
          String str = null;
          if (args.length >= 2)
          {
            str = args[1];
          }
          Voteplayer(player, str);
          return true;
        }
      }
    }
    return true;
  }

  public void Voteplayer(Player player, String str)
  {
    if (!GameSystem.checkstart)
    {
      player.sendMessage(MS + "게임이 시작되지 않았습니다.");
    }
    else if (!list.contains(player))
    {
      player.sendMessage(MS + "당신은 게임에 참가중이지 않습니다.");
    }
    else if (!GameSystem.isday)
    {
      player.sendMessage(MS + "투표는 낮에만 가능합니다.");
    }
    else if (str == null)
    {
      player.sendMessage(MS + "이름을 입력해주세요.");
    }
    else if (!plist.containsKey(str))
    {
      player.sendMessage(MS + str + " 님은 게임에 참여중이 아닙니다.");
    }
    else if (vplist.contains(player))
    {
      player.sendMessage(MS + str + " 이미 투표 하셨습니다.");
    }
    else
    {
      vplist.add(player);
      if (!vlist.containsKey(str))
      {
        vlist.put(str, Integer.valueOf(1));
      }
      else
      {
        vlist.put(str, Integer.valueOf(((Integer)vlist.get(str)).intValue() + 1));
      }
      if (GameSystem.Getjob(player.getName()).equalsIgnoreCase("모범시민"))
      {
        vlist.put(str, Integer.valueOf(((Integer)vlist.get(str)).intValue() + 1));
        player.sendMessage(MS + "능력의 효과로 2표의 효과를 발휘합니다.");
      }
      player.sendMessage(MS + str + " 님을 투표했습니다.");
      SendMessage(MS + "누군가가 " + ChatColor.AQUA + str + ChatColor.YELLOW + " 님에게 투표했습니다.");
      for(Player p : Main.list){
      	p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.5f, 1.2f);
      }
    }
  }

  public void Jobhelp(Player player)
  {
    if (!jlist.containsKey(player.getName()))
    {
      player.sendMessage(MS + "직업이 없습니다.");
    }
	else
	{
		switch(GameSystem.Getjob(player.getName()))
		{
			case "살인마": Help_Murder(player);return;
			case "의사": Help_Doctor(player);return;
			case "경찰": Help_Police(player);return;
			case "군인": Help_Soldier(player);return;
			case "스파이": Help_Spy(player);return;
			case "보디가드": Help_Guardian(player);return;
			case "마술사": Help_Magician(player);return;
			case "모범시민": Help_Nice_Civilian(player);return;
			case "시민": Help_Civilian(player);return;
			case "탐정": Help_Detective(player);return;
			case "기자": Help_Reporter(player);return;
			case "배신자": Help_Assasin(player);return;
			case "탈옥수": Help_Prisoner(player);return;
			default: player.sendMessage("없는 직업입니다. - 버그 의심"); return;
		}
    }
  }

  public void Help_Murder(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 강력한 무기를 가지고 있습니다. 이 무기를 이용하여 모든 시민팀을 죽여야합니다.");
    player.sendMessage(ChatColor.GRAY + "스파이가 능력을 사용하여 당신과 접선시 스파이는 동료가 됩니다.");
    player.sendMessage(ChatColor.GRAY + "또한 배신자가 시민을 사살할시 당신의 동료가 됩니다.");
  }

  public void Help_Doctor(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 밤마다 1명을 지정할 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "이 지정한 플레이어는 그 밤과 다음날 낮에 사망시 한번만 부활하며 ");
    player.sendMessage(ChatColor.GRAY + "다음 밤이 찾아온다면 다시 플레이어를 지정해야 합니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Police(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 낮마다 1명을 조사할 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "조사한 플레이어가 살인마일 경우 ");
    player.sendMessage(ChatColor.GRAY + "그 플레이어가 살인마라는 메세지가 당신에게 전해집니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Soldier(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 단련된 신체를 가졌습니다.");
    player.sendMessage(ChatColor.GRAY + "사망시 한번만 부활하게 됩니다. ");
    player.sendMessage(ChatColor.GRAY + "부활후에는 직업이 시민으로 변경됩니다.");
  }

  public void Help_Spy(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "밤마다 1명의 플레이어와 접선이 가능합니다.");
    player.sendMessage(ChatColor.GRAY + "만약 접선한 플레이어가 살인마라면 당신은 살인마의 동료가 되며");
    player.sendMessage(ChatColor.GRAY + "시민팀을 죽이고 다닐 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "만약 접선한 플레이어가 살인마가 아니라면 당신은 그 플레이어의 직업을 알 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Guardian(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "밤마다 두명의 플레이어를 지정하실 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "이 지정한 플레이어는 다음날 낮에 투표로 인하여 직업이 밝혀지지 않습니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Magician(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "밤일 때 단 한번만 능력을 사용할 수 있으며");
    player.sendMessage(ChatColor.GRAY + "사용한 플레이어의 직업을 빼앗아 버립니다.");
    player.sendMessage(ChatColor.GRAY + "사용 대상의 직업이 살인마라면 그 플레이어는 사망하게 됩니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Nice_Civilian(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "투표시 당신의 투표는 2표로 취급되며 시작부터");
    player.sendMessage(ChatColor.GRAY + "자신의 직업이 직업리스트에 보여집니다.");
    player.sendMessage(ChatColor.GRAY + "당신은 누구에게도 의심받지 않을 것 입니다.");
  }

  public void Help_Civilian(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 특별한 능력을 지니지 않았습니다.");
  }

  public void Help_Detective(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "낮마다 한명의 플레이어를 지정할 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "밤이 되었을 때 /ftm use 를 입력하시면");
    player.sendMessage(ChatColor.GRAY + "그 플레이어가 지금 들고있는 무기, 좌표, 체력 정보를 수시로");
    player.sendMessage(ChatColor.GRAY + "확인하실 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Reporter(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "밤일 때 한번만 능력을 사용할 수 있으며");
    player.sendMessage(ChatColor.GRAY + "능력을 사용한 플레이어의 직업을 모두에게");
    player.sendMessage(ChatColor.GRAY + "알립니다. 또한 직업리스트에 그 플레이어의 직업이");
    player.sendMessage(ChatColor.GRAY + "공개됩니다.");
    player.sendMessage(ChatColor.GRAY + "능력 사용방법 /ftm use <닉네임>");
  }

  public void Help_Assasin(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 자신이 원하는 팀을 고르실 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "첫날밤에 시민팀을 사살할시 당신은 살인마팀이 됩니다.");
    player.sendMessage(ChatColor.GRAY + "반대로 첫날째에 시민팀을 사살하지 않으면 시민팀에 남게됩니다.");
    player.sendMessage(ChatColor.GRAY + "모든 결정은 첫날째안에 해야합니다.");
    player.sendMessage(ChatColor.GRAY + "당신의 타겟 : " + ChatColor.RED + GameSystem.astarget);
    player.sendMessage(ChatColor.GRAY + "이 플레이어가 누구에게든 살해당하면 됩니다.");
  }
  
  public void Help_Prisoner(Player player)
  {
    player.sendMessage(ChatColor.GOLD + "당신의 능력 : " + ChatColor.AQUA + GameSystem.Getjob(player.getName()));
    player.sendMessage(ChatColor.GRAY + "당신은 교도소를 탈출한 탈옥수입니다.");
    player.sendMessage(ChatColor.GRAY + "당신은 밤중에 살인마처럼 사람들을 죽일 수 있습니다.");
    player.sendMessage(ChatColor.GRAY + "단, 조심하세요. 낮에 사람을 죽이면 체포됩니다!");
  }

  public void HelpMessage(Player player)
  {
    player.sendMessage(MS + "/ftm help - 능력의 설명을 봅니다.");
    player.sendMessage(MS + "/ftm use <닉네임> - 해당 대상에게 능력을 사용합니다.");
    player.sendMessage(MS + "/ftm list - 직업 리스트를 봅니다.");
    player.sendMessage(MS + "/ftm join - 게임에 참여합니다.");
    player.sendMessage(MS + "/ftm quit - 게임에서 퇴장합니다..");
    player.sendMessage(MS + "/ftm set lobby - 서있는 지점을 메인로비로 설정합니다.");
    player.sendMessage(MS + "/ftm set join- 서있는 지점을 로비로 설정합니다.");
    player.sendMessage(MS + "/ftm set start 1~15 - 서있는 지점을 시작지점으로 설정합니다.");
    player.sendMessage(MS + "/ftm reload - config를 리로드 합니다.");
    player.sendMessage(MS + "/ftm stop - 게임을 강제 종료 합니다.");
  }

  public static void SendMessage(String str)
  {
    for (int i = 0; i < list.size(); i++)
    {
      ((Player)list.get(i)).sendMessage(str);
    }
  }

  public void Glisting(Player player)
  {
    player.sendMessage(MS + "직업 리스트 (인원 " + glist.size() + "명): ");
    Set keys = glist.keySet();
    Iterator it = keys.iterator();
    while (it.hasNext())
    {
      String name = (String)it.next();
      player.sendMessage(ChatColor.RED + name + ChatColor.RESET + " : " + ChatColor.GOLD + (String)glist.get(name));
    }
  }

  public void Jlisting(Player player)
  {
    player.sendMessage(MS + "직업 리스트 :");
    Set keys = jlist.keySet();
    Iterator it = keys.iterator();
    while (it.hasNext())
    {
      String name = (String)it.next();
      player.sendMessage(ChatColor.RED + name + ChatColor.RESET + " : " + ChatColor.GOLD + (String)jlist.get(name));
    }
  }

  public static int getItemDamage(Player p){
	  ItemStack item = p.getItemInHand();
	  if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return 1;
	  List<String> lorelist = item.getItemMeta().getLore();
	  if(lorelist.size() <= 0) return 1;
	  String damagestr = lorelist.get(0);
	  if(!damagestr.contains("공격력")) return 1;
	  //§7공격력: 7~8
	  if(!damagestr.contains("~"))return Integer.valueOf(damagestr.substring(7, 8));
	  int min = Integer.valueOf(damagestr.substring(7, 8));
	  int max = Integer.valueOf(damagestr.substring(9, 10));
	  return (int)(Math.random()*(max-min+1)+min);
  }
  
  public static void Givechestitem(Player p){
	  int num = GameSystem.Getrandom(26);
	  p.getWorld().playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
	  if(num == 25){
		  p.sendMessage("§7딱히 쓸만한 것이 보이지 않는다.");
		  return;
	  }
	  if(num == 24){
		  p.sendMessage("§7먼지밖에 없는 것 같다...");
		  return;
	  }
	  if(num == 23){
		  p.sendMessage("§7별다른게 보이지는 않는다.");
		  return;
	  }
	  if(num == 8 || num == 19 || num == 10){
		  for(int i = 0; i <= GameSystem.Getrandom(5); i++)
			  p.getInventory().addItem(chestitem[num]);
		  p.updateInventory();
		  return;
	  }
	  p.getInventory().addItem(chestitem[num]);
	  p.updateInventory();
  }
  
  public static void Setvoteinven(){
	  for(int i = 0; i < 18; i++){
		  votemenu.setItem(i, null);
	  }
	  for(int i = 0; i < list.size(); i++){
		  ItemStack item = new ItemStack(397, 1, (byte) 3);
		  ItemMeta meta = item.getItemMeta();
		  meta.setDisplayName(list.get(i).getName());
		  List<String> lorelist = new ArrayList<String>();
		  lorelist.add("§e직업 §f: §b"+glist.get(list.get(i).getName()));
		  lorelist.add("§c클릭시 이 플레이어에게 투표합니다.");
		  meta.setLore(lorelist);
		  item.setItemMeta(meta);
		  votemenu.setItem(i, item);
	  }
  }
  
  public static void GameHelper(Player p, int slot){
	  switch(slot){
	  case 11:
		  p.sendMessage("§7게임 이름 §f: §c살인마를 찾아라");
		  p.sendMessage("§f게임이 시작되고 30초 뒤 모든 플레이어의 직업이 정해집니다.");
		  p.sendMessage("§f직업 중 §4살인마, 스파이, 탈옥수§f는 살인마팀이며");
		  p.sendMessage("§f그 외의 직업들은 시민팀입니다.");
		  p.sendMessage("§4단, 배신자는 살인마팀이 될 수도 있습니다.");
		  p.sendMessage("§f시민팀은 모든 살인마팀을 죽여야하며");
		  p.sendMessage("§f살인마팀은 모든 시민팀을 죽여야합니다.");
		  p.sendMessage("§c※ 맵 곳곳에 있는 상자를 우클릭시 아이템을 얻으실 수 있습니다.");
		  p.closeInventory();
		  return;
		  
	  case 13:
		  p.sendMessage("§f게임 도중 사망하신 후에는 게임에 관여하실 수 없습니다.");
		  p.sendMessage("§f직업 공개 등등의 행위는 불가능합니다.");
		  p.sendMessage("§f살인마 팀에 경우 고의로 같은 팀원을 죽일시 경고를 받으실 수 있습니다.");
		  p.sendMessage("§c※ 살인마 팀은 서로를 때릴시 팀원임을 알 수 있습니다.");
		  p.closeInventory();
		  return;
		  
	  case 15:
		  if(!glist.containsKey(p.getName())) {
			  p.sendMessage("게임이 시작된 후 사용하실 수 있습니다.");
			  p.closeInventory();
			  return;
		  }
		  p.openInventory(votemenu);
		  return;
		  
	  default: return;
	  }  
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
  
  @EventHandler
  public static void onHitPlayer(EntityDamageByEntityEvent e)
  {
    if (((e.getEntity() instanceof Player)) && (GameSystem.checkstart))
    {
      Player player = (Player)e.getEntity();
      Player damager = null;
      if(!list.contains(player)) return;
		if(e.getDamager() instanceof Snowball){
			Snowball snowball = (Snowball) e.getDamager();
			if(snowball.getShooter() instanceof Player){
				damager = (Player) snowball.getShooter();
				e.setDamage(7);
			}
		}
		if(e.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) e.getDamager();
			if(arrow.getShooter() instanceof Player){
				damager = (Player) arrow.getShooter();
			}
		}
      if(e.getDamager() instanceof Player) damager = (Player) e.getDamager();
      if(damager == null) return;
      if (!list.contains(damager) || (!jlist.containsKey(damager.getName())))
      {
        if (list.contains(player))
        {
          e.setCancelled(true);
        }
        return;
      }
      if(!(getItemDamage(damager) == 0)){
    	  e.setDamage(getItemDamage(damager));
      }
      if(GameSystem.Getjob(damager.getName()).equalsIgnoreCase("탈옥수") && GameSystem.isday)
      {
    	  damager.sendMessage(MS+"지금은 낮입니다! 이 사람을 죽이면 체포될 것 입니다!");
      }
      if (GameSystem.Getjob(damager.getName()).equalsIgnoreCase("경찰"))
      {
        if (damager.getItemInHand().hasItemMeta())
        {
          if (damager.getItemInHand().getItemMeta().hasDisplayName())
          {
            if (damager.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "경찰봉"))
            {
              if (damager.getItemInHand().getAmount() != 1)
              {
                damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
              }
              else
              {
                damager.setItemInHand(null);
              }
              player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 10));
              player.sendMessage(ChatColor.RED + "경찰봉에 맞았습니다!!!");
              damager.sendMessage(ChatColor.RED + "경찰봉을 사용 하였습니다.");
            }
          }
        }
      }
      if ((GameSystem.mlist.contains(player)) && (GameSystem.mlist.contains(damager)))
      {
        player.sendMessage(MS + "같은 살인마팀인 " + damager.getName() + "님이 당신을 공격했습니다! ");
        damager.sendMessage(MS + player.getName() + " 님은 같은 살인마팀 입니다! 조심해주세요!");
        e.setDamage(1);
      }
      int damage = e.getDamage();
      if ((player.getHealth() - damage <= 0) && (!player.isDead()))
      {
        if ((!GameSystem.mlist.contains(player)) && (!GameSystem.Getjob(player.getName()).equalsIgnoreCase("스파이")))
        {
          if (!GameSystem.mlist.contains(damager))
          {
        	if(GameSystem.Getjob(damager.getName()).equalsIgnoreCase("배신자")){
        		damager.sendMessage(MS+"당신은 시민팀을 배신하기로 결심했습니다. (§4이제 시민팀을 제한없이 죽일 수 있습니다.§f)");
        		GameSystem.mlist.add(damager);
        	} else if (!GameSystem.astarget.equalsIgnoreCase(player.getName())) {
        		if(!(GameSystem.Getjob(player.getName()).equalsIgnoreCase("배신자") && GameSystem.daycnt == 1)){
        			e.setCancelled(true);
                    GameSystem.Removeplayerfl(damager);
                    damager.sendMessage(MS + "당신은 무고한 플레이어를 죽여버려 처형 되였습니다.");
                    damager.setHealth(0);
                    return;	
        		}
            }
          }
          if (GameSystem.CheckReverse(player))
          {
            e.setCancelled(true);
            GameSystem.Reverse(player);
            return;
          }

        }
        else if (GameSystem.CheckReverse(player))
        {
          e.setCancelled(true);
          GameSystem.Reverse(player);
          return;
        }
      }
    }
  }

  @EventHandler
  public static void onPlayerdeath(PlayerDeathEvent e)
  {
    if (((e.getEntity() instanceof Player)) && (GameSystem.checkstart))
    {
      Player player = e.getEntity();
      if(!list.contains(player)) return;
      if(GameSystem.isday)
      {
    	  if(player.getKiller() instanceof Player)
    	  {
    		  Player killer = (Player) player.getKiller();
    		  if(jlist.containsKey(killer.getName())  && GameSystem.Getjob(player.getKiller().getName()).equalsIgnoreCase("탈옥수"))
    		  {
    	    	  killer.sendMessage(MS+"낮에 살인을 저질러 체포 되었습니다.");
    	    	  killer.setHealth(1);
    	    	  killer.damage(30);
    		  }
    	  }
      }
    	e.getDrops().clear();
    	e.setDroppedExp(0);
        if (GameSystem.mlist.contains(player))
        {
          GameSystem.Murderdead(player);
        }
        else
        {
          GameSystem.Civildead(player);
        }
        Setvoteinven();
        for(Player p : Main.list){
        	p.playSound(p.getLocation(), Sound.GHAST_DEATH, 1.5f, 1.6f);
        }
    }
  }

  @EventHandler
  public static void onQuitPlayer(PlayerQuitEvent e) {
    if (list.contains(e.getPlayer()))
    {
      GameSystem.Quitplayer(e.getPlayer());
    }
  }
  
  @EventHandler
  public void onRightClick(PlayerInteractEvent e){
	  if(!list.contains(e.getPlayer()) || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
	  Player p = e.getPlayer();
	  if(e.getClickedBlock() != null && e.getClickedBlock().getTypeId() == 54 && GameSystem.checkstart){
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
			  p.sendMessage("§7이미 누구간 털어간 흔적이 있다...");
		  }
	  }
	  if(e.getItem() == null || e.getItem().getType() == Material.AIR) return;
	  if(GameSystem.getHandItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")){
		  p.openInventory(gamehelper);
	  } else if(e.getItem().getType() == Material.IRON_HOE){
		  if(!Takeitem(p, 362, 1)){
			  p.sendMessage("§c9mm탄환이 부족합니다."); return;
		  }
		  Snowball snowball = p.launchProjectile(Snowball.class); //here we launch the snowball
		  Block target = p.getTargetBlock(null, 50);
		  Vector velocity = (target.getLocation().toVector().subtract(snowball.getLocation().toVector()).normalize()).multiply(6);
		  snowball.setVelocity(velocity);
		  snowball.setShooter(p);
		  p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 4.0f, 2.0f);
		  p.getWorld().playEffect(e.getPlayer().getLocation(), Effect.SMOKE, 20); //We will play a smoke effect at the shooter's location
	  } else if(e.getItem().getType() == Material.IRON_SWORD){
		  if(Takeitem(p, 267, 3)){
			  p.sendMessage(Main.MS+"장미칼을 얻었다!");
			  p.getInventory().addItem(rose_sword);
		  }
	  } else if(e.getItem().getTypeId() == 377){
		  p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
		  Removeitem(p, 377, 1);
		  p.sendMessage(MS+"투명 망토를 사용 했습니다. 15초간 모습을 감춥니다.");
	  } else if(e.getItem().getTypeId() == 264){
		  p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
		  Removeitem(p, 264, 1);
		  p.sendMessage(MS+"저항의 돌을 사용 했습니다. 5초간 저항2를 받게됩니다.");
	  }
  }
 
  @EventHandler
  public void onClickInventory(InventoryClickEvent e){
	  if(!(e.getWhoClicked() instanceof Player)) return; 
	  Player p = (Player) e.getWhoClicked();
	  if(!list.contains(p)) return;
	  if(e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
		  GameHelper(p, e.getSlot());
		  e.setCancelled(true);
	  }
	  if(e.getInventory().getTitle().equalsIgnoreCase("§c§l투표")){
		  e.setCancelled(true);
		  if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)return;
		  p.closeInventory();
		  Voteplayer(p, e.getCurrentItem().getItemMeta().getDisplayName());
	  }
  }

	@EventHandler
	public void onPlayercommand(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		if(list.contains(p) && (e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
				|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ")))
		{
			GameSystem.Quitplayer(p);
		}
	}
  
  @EventHandler
  public void onBlockbreak(BlockBreakEvent e){
	  if(list.contains(e.getPlayer()))
	  e.setCancelled(true);
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e){
	  if(list.contains(e.getPlayer()))
	  e.setCancelled(true);
  }
  
  @EventHandler
  public void onFall(EntityDamageEvent e) {
    if (e.getEntityType() == EntityType.PLAYER)
    {
      Player player = (Player)e.getEntity();
      if(!list.contains(player)) return;
      if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) || !GameSystem.checkstart)
      {
        e.setCancelled(true);
      }
    }
  }
}