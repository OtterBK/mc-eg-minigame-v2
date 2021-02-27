package me.Bokum.EWG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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
	public static List<Player> plist = new ArrayList<Player>();
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static int tasknum[] = new int[30];
	public static int tasktime[] = new int[30];
	public static Location Lobby;
	public static List<String> teamchat = new ArrayList<String>();
	public static Location Red_spawn;
	public static Location Blue_spawn;
	public static Location Red_core;
	public static Location Blue_core;
	public static Location specLoc;
	public static List<Player> redlist = new ArrayList<Player>();
	public static List<Player> bluelist = new ArrayList<Player>();
	public static Location joinpos;
	public static String MS = "§f[ §aEG §f] ";
	public static Main instance;
	public static ItemStack[] hades_inventory;
	public static ItemStack[] hades_armor;
	public static Player atena = null;
	public static HashMap<String, String> jlist = new HashMap<String, String>();
	public static Economy econ = null;
	public static ItemStack helpitem;
	public static boolean game_end = false;
	public static Inventory gamehelper;
	public static Location tntlocation = null;
	public static int timertime = 0;
	public static int timernum = 0;
	public static HashMap<String, Integer> cooldown = new HashMap<String, Integer>(80);
	public static HashMap<String, String> abilitytarget = new HashMap<String, String>();
	public static int Forcestoptimer = 0;
	public static HashMap<String, String> backuptlist = new HashMap<String, String>();
	
	public void onEnable(){
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.BLAZE_ROD)).shape(new String[]{"|","|","|"}).setIngredient('|', Material.STICK);
		getServer().addRecipe(recipe);
		getServer().getPluginManager().registerEvents(this, this);
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
		instance = this;
		
		getLogger().info("EG 신들의 전쟁 플러그인이 로드 되었습니다.");
		
        if (!setupEconomy() ) {
            getLogger().info("[버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
        }
        
        ItemStack blazestick = new ItemStack(Material.BLAZE_ROD, 1);        
        
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
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e팀채팅 on/off");
		List<String> lorelist1 = new ArrayList<String>();
		lorelist1.add("§f- §c게임이 시작된 후 사용할 수 있습니다.");
		item.setItemMeta(meta2);
		gamehelper.setItem(15, item);
		
		helpitem = new ItemStack(345, 1);
		meta2 = helpitem.getItemMeta();
		meta2.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist1.clear();
		lorelist1.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta2.setLore(lorelist1);
		helpitem.setItemMeta(meta2);
		
		Loadconfig();
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
	
	public void Loadconfig()
	{
	  try
	  {
	    joinpos = new Location(Bukkit.getWorld(getConfig().getString("Join_world")), getConfig().getInt("Join_x"), getConfig().getInt("Join_y"), getConfig().getInt("Join_z"));
	    Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
	    specLoc = new Location(Bukkit.getWorld(getConfig().getString("Spec_world")), getConfig().getInt("Spec_x"), getConfig().getInt("Spec_y"), getConfig().getInt("Spec_z"));
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("참여 지점 또는 로비가 설정되어 있지 않습니다.");
	  }
	  try
	  {
	    Red_spawn = new Location(Bukkit.getWorld(getConfig().getString("Red_world")), getConfig().getInt("Red_x"), getConfig().getInt("Red_y"), getConfig().getInt("Red_z"));
	    Blue_spawn = new Location(Bukkit.getWorld(getConfig().getString("Blue_world")), getConfig().getInt("Blue_x"), getConfig().getInt("Blue_y"), getConfig().getInt("Blue_z"));
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("레드팀 스폰 또는 블루팀 스폰이 설정되어 있지 않습니다.");
	  }
	  try
	  {
	    Red_core = new Location(Bukkit.getWorld(getConfig().getString("Redcore_world")), getConfig().getInt("Redcore_x"), getConfig().getInt("Redcore_y"), getConfig().getInt("Redcore_z"));
	    Blue_core = new Location(Bukkit.getWorld(getConfig().getString("Bluecore_world")), getConfig().getInt("Bluecore_x"), getConfig().getInt("Bluecore_y"), getConfig().getInt("Bluecore_z"));
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("레드팀 코어 또는 블루팀 코어가 설정되어 있지 않습니다.");
	  }
	}
	
	public void onDisbale(){
		getLogger().info("EG 신들의 전쟁 플러그인이 언로드 되었습니다..");
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String[] args){
		if(talker instanceof Player){
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("ewg") && p.isOp()){
				if(args.length <= 0){
					Helpmessages(p);
					return true;
				}
				else{
					if(args[0].equalsIgnoreCase("set")){
						Setloc(p, args);
						return true;
					}
					if(args[0].equalsIgnoreCase("join")){
						GameJoin(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("quit")){
						GameQuit(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("stop")){
						Forcestop();
						return true;
					}
				}
			}
			if(str.equalsIgnoreCase("wg") && plist.contains(p) && check_start){
				if(args.length <= 0){
					p.sendMessage(MS+"/wg <x/tc/item/help>"); return true;
				}
				if(args[0].equalsIgnoreCase("x") && plist.contains(p)){
					Targeting(p, args);
					return true;
				}
				if(args[0].equalsIgnoreCase("tc") && plist.contains(p)){
					if(teamchat.contains(p.getName()))
					{
						teamchat.remove(p.getName());
						p.sendMessage(MS+"팀채팅을 껐습니다.");
					}
					else
					{
						teamchat.add(p.getName());
						p.sendMessage(MS+"팀채팅을 켰습니다.");
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("item") && plist.contains(p)){
					if(Checkcooldown(p, "기본템")){
						Addcooldown(p, "기본템", 300);
						giveBaseItem(p);
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("help") && plist.contains(p)){
					AbilityHelp(p);
					return true;
				}
			}
		}
		return false;
	}
	
	public static void Forcestop(){
		Bukkit.broadcastMessage(MS+"신들의 전쟁이 강제 종료 되었습니다.");
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
	
	public static void Targeting(Player player, String[] args){
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		if(args.length <= 1){
			player.sendMessage(MS+"타겟 지정 방법 : '/wg x 플레이어닉네임'");
			return;
		}
		String target = args[1];
		if(!jlist.containsKey(target)){
			player.sendMessage(MS+"해당 플레이어는 게임에 참여 중이지 않습니다."); return;
		}
		switch(jlist.get(player.getName())){
		case "텔레포터":
			Player targetp = null;
			for(Player t : bluelist.contains(player) ? bluelist : redlist){
				if(t.getName().equalsIgnoreCase(target))
					targetp = t;
			}
			if(targetp != null)
			{
				if (!target.equalsIgnoreCase(player.getName()))
				{
					abilitytarget.put(player.getName(), target);
					player.sendMessage("타겟을 등록했습니다.   "+ChatColor.GREEN+target);
				}
				else
					player.sendMessage("자기 자신을 타겟으로 등록 할 수 없습니다.");
			}
			else
				player.sendMessage("자신의 팀원만 지정이 가능합니다.");	
		}
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/ewg join");
		p.sendMessage(MS+"/ewg quit");
		p.sendMessage(MS+"/ewg set");
		p.sendMessage(MS+"/ewg stop");
	}
	
	public static void Sendmessage(String str){
		for(Player p : plist){
			p.sendMessage(str);
		}
	}
	
	public void Setloc(Player p, String[] args){
		if(args.length <= 1){
			p.sendMessage(MS+"/ewg set lobby");
			p.sendMessage(MS+"/ewg set join");
			p.sendMessage(MS+"/ewg set red");
			p.sendMessage(MS+"/ewg set blue");
			p.sendMessage(MS+"/ewg set redcore");
			p.sendMessage(MS+"/ewg set bluecore");
			return;
		}
		if(args.length <= 2){
			if(args[1].equalsIgnoreCase("lobby")){
			    getConfig().set("Lobby_world", p.getLocation().getWorld().getName());
			    getConfig().set("Lobby_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Lobby_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Lobby_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"로비 설정 완료");
			}
			else if(args[1].equalsIgnoreCase("join")){
			    getConfig().set("Join_world", p.getLocation().getWorld().getName());
			    getConfig().set("Join_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Join_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Join_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"대기실 설정 완료");
			} else if(args[1].equalsIgnoreCase("red")){
			    getConfig().set("Red_world", p.getLocation().getWorld().getName());
			    getConfig().set("Red_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Red_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Red_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"레드팀 스폰 설정 완료");
			} else if(args[1].equalsIgnoreCase("blue")){
			    getConfig().set("Blue_world", p.getLocation().getWorld().getName());
			    getConfig().set("Blue_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Blue_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Blue_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"블루팀 스폰 설정 완료");
			} else if(args[1].equalsIgnoreCase("redcore")){
			    getConfig().set("Redcore_world", p.getLocation().getWorld().getName());
			    getConfig().set("Redcore_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Redcore_y", Integer.valueOf(p.getLocation().getBlockY() - 1));
			    getConfig().set("Redcore_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"레드팀 코어 설정 완료");
			} else if(args[1].equalsIgnoreCase("bluecore")){
			    getConfig().set("Bluecore_world", p.getLocation().getWorld().getName());
			    getConfig().set("Bluecore_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Bluecore_y", Integer.valueOf(p.getLocation().getBlockY() - 1));
			    getConfig().set("Bluecore_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"블루팀 코어 설정 완료");
			} else if(args[1].equalsIgnoreCase("spec")){
			    getConfig().set("Spec_world", p.getLocation().getWorld().getName());
			    getConfig().set("Spec_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Spec_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Spec_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"관전 설정 완료");
			}
			return;
		}
	}
	
	public static void GameJoin(Player p){
		if(plist.contains(p)){
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(backuptlist.containsKey(p.getName())){
			plist.add(p);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "신들의 전쟁");
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			Sendmessage(MS+p.getName()+" 님이 신들의 전쟁 게임에 재참가하셨습니다.");
			if(backuptlist.get(p.getName()).equalsIgnoreCase("블루")){
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join blue "+p.getName());
				bluelist.add(p);
				p.teleport(Blue_spawn);
			}else{
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join red "+p.getName());
				redlist.add(p);
				p.teleport(Red_spawn);
			}
			return;
		}
		if(check_start){
			p.sendMessage(MS+"이미 게임이 진행 중이기 때문에 관전을 시작합니다.");
			p.teleport(specLoc);
		} else if(plist.size() >= 12){
			p.sendMessage(MS+"이미 최대인원(12)입니다.");
			return;
		}else{
			plist.add(p);
			p.teleport(joinpos);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "신들의 전쟁");
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			Sendmessage(MS+p.getName()+" 님이 신들의 전쟁 게임에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c6 §f)");
			if(!check_lobbystart && plist.size() >= 6){
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
		if(teamchat.contains(p.getName()))
		{
			teamchat.remove(p.getName());
		}
		plist.remove(p);
		if(!check_start)
		{
			return;
		}
		if(bluelist.contains(p))
		{
			Sendmessage(MS+"§b블루팀 §6"+p.getName()+" §f님이 퇴장 하셨습니다.");
			bluelist.remove(p);
		}
		if(redlist.contains(p))
		{
			Sendmessage(MS+"§c레드팀 §6"+p.getName()+" §f님이 퇴장 하셨습니다.");
			redlist.remove(p);
		}
	}
	
	public static int Getcursch(){
		for(int i = 0 ; i < tasknum.length; i++){
			if(tasknum[i] == -5){
				return i;
			}
		}
		return 0;
	}
	
	
	public static boolean Checkcooldown(Player p, String str)
	{
		if(!Main.cooldown.containsKey(p.getName()+str) || Main.cooldown.get(p.getName()+str) <= (int)(java.lang.System.currentTimeMillis()/1000))
		{
			return true;
		}
		p.sendMessage(Main.MS+ChatColor.AQUA+(Main.cooldown.get(p.getName()+str)-(int)(java.lang.System.currentTimeMillis()/1000))
				+ChatColor.RESET+"초 후 스킬을 사용하실 수 있습니다.");
		return false;
	}
	
	public static void Addcooldown(Player p, String str, int cooldown)
	{
		Main.cooldown.put(p.getName()+str, (int)(java.lang.System.currentTimeMillis()/1000)+cooldown);
		Countdown(p, cooldown, str);
	}

	
	public static void Canceltask(int cur)
	{
		Bukkit.getScheduler().cancelTask(tasknum[cur]);
		tasknum[cur] = -5;
		tasktime[cur] = -5;
	}
	
	public static void Countdown(final Player p, long time, final String skill)
	{
		time *= 20;
		if(time < 60){
			return;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(MS+ChatColor.AQUA+"3초후 "+ChatColor.RESET+skill+"능력의 쿨타임이 끝납니다.");
			}
		}, time-60);
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(MS+ChatColor.AQUA+"2초후 "+ChatColor.RESET+skill+"능력의 쿨타임이 끝납니다.");
			}
		}, time-40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(MS+ChatColor.AQUA+"1초후 "+ChatColor.RESET+skill+"능력의 쿨타임이 끝납니다.");
			}
		}, time-20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(MS+ChatColor.RESET+skill+"능력의 쿨타임이 끝났습니다.");
			}
		}, time);
	}
	
	  public static void GameHelper(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c신들의 전쟁");
			  p.sendMessage("§f게임이 시작되면 §c레드팀§f과 §b블루팀§f으로 나눠집니다.");
			  p.sendMessage("§f각팀의 기지에는 §b다이아블럭(코어)§f가 있습니다.");
			  p.sendMessage("§f적팀의 §b다이아블럭§f을 먼저 §d부수는§f 팀이 승리합니다.");
			  p.sendMessage("§f시작후 §c기본템과 §e능력§f이 정해지며");
			  p.sendMessage("§e능력§f은 §6블레이즈 막대§f를 §a좌 또는 우클릭§f하여 사용합니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f적팀의 §e창고§f를 털 수 있습니다.");
			  p.sendMessage("§f적팀의 §b기지 또는 코블스톤 계단§f에 §c용암§f을 두지 마세요!\n §f이 행위는 §4용암테러§f로 간주되어 처벌을 받습니다. ");
			  p.sendMessage("§f실력 보다 중요한 것은 §d매너§f입니다. 서로간의 욕설은 하지마세요.");
			  p.sendMessage("§f다이아블럭을 흑요석으로 감싸는 것은 안됩니다.");
			  p.sendMessage("§f다이아블럭은 돌곡괭이로도 캘 수 있습니다.");
			  p.sendMessage("§f적팀의 기지에 물을 설치하는 것은 가능합니다. \n §f고의적인 §c용암 팀킬§f은 처벌을 받으실 수 있습니다.");
			  p.sendMessage("§c※ 팀킬은 자연적으로 막아주니 걱정마세요.");
			  p.closeInventory();
			  return;
			  
		  case 15:
			  p.sendMessage("§f/wg x - §e능력 사용 대상을 설정합니다. (관련 능력만)");
			  p.sendMessage("§f/wg tc - §e팀채팅을 켜거나 끕니다.");
			  p.sendMessage("§f/wg item - §e지원 아이템을 받습니다. (쿨타임 300초)");
			  p.sendMessage("§f/wg help - §e능력을 확인합니다.");
			  p.sendMessage("§c※ 블레이즈 막대는 §e일반 막대기§f를 §b가운데에 한 줄§f로 \n놓으시면 조합 가능합니다.");
			  p.closeInventory();
			  return;
			  
		  default: return;
		  }  
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
			p.sendMessage(MS+"§7돌이 §c"+tamt+"§7개 부족합니다.");
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
	
		public static void SkillCountdown(final Player p, long time, final String skill)
		{
			time *= 20;
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
			{
				public void run()
				{
					p.sendMessage(MS+ChatColor.AQUA+"3초후 "+ChatColor.RESET+skill+"능력이 해제됩니다.");
				}
			}, time-60);
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
			{
				public void run()
				{
					p.sendMessage(MS+ChatColor.AQUA+"2초후 "+ChatColor.RESET+skill+"능력이 해제됩니다.");
				}
			}, time-40);
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
			{
				public void run()
				{
					p.sendMessage(MS+ChatColor.AQUA+"1초후 "+ChatColor.RESET+skill+"능력이 해제됩니다.");
				}
			}, time-20);
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
			{
				public void run()
				{
					p.sendMessage(MS+ChatColor.AQUA+skill+"능력이 해제됐습니다.");
				}
			}, time);
		}
	  
	public static void Setbase(Player p){
		p.teleport(bluelist.contains(p) ? Blue_spawn : Red_spawn);
		giveBaseItem(p);
		p.sendMessage(MS+"능력을 확인하시려면 §e/wg help §f를 입력하세요 \n §e/wg tc §f명령어로 팀채팅을 켜거나 끕니다. \n §e/wg item §f명령어로 300초마다 템을 지급받습니다.");
	}
	
	public static boolean AirToFar(Player player, Block block)
	{
		if (block.getTypeId() != 0)
			return true;
		else
		{
			player.sendMessage(ChatColor.RED+"대상과의 거리가 너무 멉니다.");
			return false;
		}
	}
	
	public static void AbilityHelp(Player player){
		if(!plist.contains(player)) return;
		if(!jlist.containsKey(player.getName())){
			player.sendMessage(MS+"능력이 없습니다.");
		}
		switch(jlist.get(player.getName())){
		case "하데스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 하데스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ S ]");
			player.sendMessage("죽음의 신입니다.\n"+
							   "패시브 능력으로 사망시 80% 확률로 아이템을 잃지 않습니다.\n" +
							   "액티브 능력으로 상대를 나락으로 떨어뜨릴 수 있으며 블레이즈 로드를 들었을때 발동 시킬 수 있습니다.\n" +
							   "일반능력은 주변 2칸에 있는 자신을 포함한 모든 플레이어,몬스터와 함께 나락으로 떨어지며\n" +
							   "고급능력은 주변 4칸에 있는 자신을 제외한 모든 플레이어,몬스터를 나락으로 떨어뜨립니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 100초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 10개 소모, 쿨타임 200초\n");
			break;
		case "데메테르":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 데메테르 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("수확의 신입니다.\n"+
							   "코블스톤을 이용해서 빵을 얻을 수 있습니다.\n" +
							   "기본적으로 배고픔이 닳지 않으며 체력 회복속도가 4배로 상승합니다.\n" +
							   ChatColor.GREEN+"(우클릭) "+ChatColor.WHITE+" 코블스톤 10개 소모, 쿨타임 30초\n");
			break;
		case "아테나":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 아테나 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ S ]");
			player.sendMessage("지혜의 신입니다.\n"+
							   "플레이어들이 사망할 때 마다 경험치를 얻습니다.\n" +
							   "자신이 사망하면 경험치는 초기화됩니다.\n" +
							   "좌클릭으로 책을 얻을 수 있으며 우클릭으로 인챈트 테이블을 얻을수 있습니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 10초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 64개 소모, 쿨타임 3초\n");
			break;
		case "아르테미스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 아르테미스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("사냥과 달의 신입니다.\n"+
							   "화살과 활을 코블스톤과 교환할수 있습니다.(좌클릭 : 화살 1개, 우클릭 : 활 1개)\n" +
							   "화살로 공격당한 플레이어는 20퍼센트의 확률로 즉사합니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 7개 소모, 쿨타임 20초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 15개 소모, 쿨타임 180초\n");
			break;
			
		case "아레스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 아레스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Passive  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("전쟁의 신입니다.\n"+
							   "모든 공격 데미지가 1.5배 상승합니다.\n" +
							   "추가 패시브 능력으로 10퍼센트 확률로 공격을 회피합니다");
			break;
			
		case "헤파이토스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 헤파이토스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("불의 신입니다.\n"+
							   "화염 및 용암 데미지를 받지 않습니다.");
			break;
		case "아스클리피어스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 아스클리피어스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("의술의 신입니다.\n"+
							   "자신의 체력회복 혹은 주변 팀원의 체력을 회복합니다.\n" +
							   "일반능력으로 자신을 모두 회복 시킬 수 있으며\n" +
							   "고급능력으로 주변 5칸에 있는 자신을 제외한 팀원의 체력을 모두 회복 시킬 수 있습니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 1개 소모, 쿨타임 50초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 100초\n");
			break;
		case "헤르메스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 헤르메스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Active,Passive,Buff  "+ChatColor.GREEN+"Rank[ S ]");
			player.sendMessage("여행자의 신입니다.\n"+
							   "기본적으로 이동속도가 빠르며 블레이즈 로드를 사용한 능력을 통해 비행 할 수 있습니다.(점프하면서 쓰시면 바로 날 수 있습니다.)\n" +
							   "비행 중에는 낙사 데미지를 받지 않습니다.\n" +
							   ChatColor.GREEN+"좌클릭 : 5초간 비행 할 수 있습니다.\n" +
							   ChatColor.AQUA+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 2개 소모, 쿨타임 60초\n");
			break;
			
		case "디오니소스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 디오니소스 ]  "+ChatColor.RED+"[ 신 ]  "+ChatColor.BLUE+"Passive,DeBuff  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("술의 신입니다.\n"+
							   "25% 확률로 자신을 공격한 10초간 상대의 시야를 어지럽히며\n" +
							   "동시에 상대의 이동속도, 공격력을 낮춥니다.\n");
			break;
		case "광부":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 광부 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("돌을 효율적으로 캐는 능력입니다.\n"+
							   "코블스톤을 캘 때 일정 3% 확률로 한번에 20개를 얻을 수 있습니다.\n");
			break;
		case "텔레포터":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 텔레포터 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("순간이동을 돕는 마법사입니다.\n"+
							   "블레이즈 로드를 이용해 자신이 원하는 위치(15칸)에 텔레포트 할 수 있으며 같은 팀원 위치와 스위칭도 가능합니다.\n" +
							   "좌클릭으로 자신이 가리키고 있는곳으로 텔레포트 하며.\n" +
							   "우클릭으로 타겟에 등록해 둔 자신의 팀원과 위치를 치환합니다.(타겟 등록법 : /wg x <Player>)\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 4개 소모, 쿨타임 25초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 4개 소모, 쿨타임 40초\n");
			break;
			
		case "붐버":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 봄버 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("폭발을 다루는 능력입니다.\n"+
							   "지정된 위치에 2.0의 폭발을 일으킵니다.\n" +
							   "좌클릭으로 해당 위치에 보이지 않는 tnt를 설치하며\n" +
							   "우클릭으로 어디서든 폭발시킬 수 있습니다.\n" +
							   ChatColor.GREEN+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 27초"); 
			break;
			
		case "크리퍼":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 크리퍼 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ C ]");
			player.sendMessage("몬스터형 능력입니다.\n"+
							   "블레이즈 로드를 통해 능력을 사용하면\n" +
							   "크리퍼와 같은 폭발력의 폭발을 일으킵니다.\n" +
							   ChatColor.GREEN+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 6개 소모, 쿨타임 33초\n");
			break;
			
		case "암살자": 
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 암살자 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("민첩한 몸놀림을 가지고있는 능력입니다.\n"+
							   "점프한 후 능력을 사용하면 현재 보는 방향으로 점프를 한번 더 할 수 있습니다.\n" +
							   "좌클릭으로 해당방향으로 점프를 합니다.\n" +
							   "우클릭으로 반경 10칸내에 있는 적의 등으로 순간이동 합니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 0개 소모, 쿨타임 2초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 2개 소모, 쿨타임 20초\n");
			break;
			
		case "반사":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 반사 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("공격 받은 데미지를 공격자에게 반사하는 능력입니다..\n"+
							   "33% 확률로 자신이 받은 데미지의 반을 상대방에게 반사합니다.(방어무시)\n" +
							   "화살등 간접적으로 받는데미지는 반사하지 못합니다.");
			break;
			
		case "블라인더":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 블라인더 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("상대방의 시야를 가리는 능력입니다.\n"+
							   "자신을 공격한 상대는 20%확률로 시야가 가려집니다.(4초 지속)\n" +
							   "블레이즈 로드를 이용한 능력으로 자신의 팀원을 제외한 모든 유저를 블라인드 할 수 있습니다.(8초 지속)\n" +
							   ChatColor.GREEN+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 73초\n");
			break;
			
		case "클로킹":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 클로킹 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ A ]");
			player.sendMessage("일정시간 자신의 몸을 숨길 수 있는 능력입니다.\n"+
								"타격 받을시 10%확률로 신속버프를 받습니다.(4초 지속)\n" +
							   "일반능력을 이용해 자신의 모습을 15초간 투명 버프를 받습니다.\n" +
							   ChatColor.GREEN+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 4개 소모, 쿨타임30초\n");
			break;
			
		case "대장장이":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 대장장이 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active  "+ChatColor.GREEN+"Rank[ S ]");
			player.sendMessage("철,다이아를 만들어 낼 수 있는 능력입니다.\n"+
							   "일반능력으로 코블스톤을 소비하여 철괴 8개를 획득 할 수 있습니다.\n" +
							   "고급능력으로 철괴를 소비하여 다이아 4개를 얻을 수 있습니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 70개 소모, 쿨타임 140초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 70개 소모, 쿨타임 250초\n");
			break;
			
		case "복서":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 복서 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Passive  "+ChatColor.GREEN+"Rank[ S ]");
			player.sendMessage("빠른 주먹을 사용하는 능력입니다.\n"+
							   "주먹을 이용해서 공격하면 아주 빠른 속도로 공격할 수 있습니다.\n" +
							   "상대가 블로킹 중이라면 효과를 받지 않습니다.\n" +
							   "당신의 광클실력을 보여주세요.(오토마우스는 쓰지마요ㅠㅠ)");
			break;
			
		case "사제":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 사제 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active, Buff  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("버프를 사용 할 수 있는 능력입니다.\n"+
							   "블레이즈 로드를 이용해서 능력을 사용할 수 있습니다.\n" +
							   "일반능력은 자신에게 랜덤으로 버프를 적용합니다.\n" +
							   "고급능력은 자신을 포함한 자신의 모든 팀원에게 랜덤으로 버프를 적용합니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 1개 소모, 쿨타임 35초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤2개 소모, 쿨타임 90초\n");
			break;
			
		case "마녀":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 마녀 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("디버프를 사용하는 능력입니다.\n"+
							   "블레이즈 로드를 이용한 능력 사용시 주변 10칸 안에 있는 자신의 팀원을 제외한 모두에게 각종 10초간 디버프를 적용합니다.\n" +
							   "자신을 공격한 상대는 7% 확률로 5초간 디버프에 걸리게 됩니다. \n" +
							   ChatColor.GREEN+"(좌클릭) "+ChatColor.WHITE+" 코블스톤 3개 소모, 쿨타임 66초\n");
			break;
			
		case "아쳐":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 아처 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Active,Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("궁수 입니다.\n"+
							   "활공격 데미지가 1.2배로 상승합니다.\n 당신의 화살은  매우 빨라중력의 거의 영향을 받지 않습니다.\n" +
							   "좌클릭으로 화살을 얻을수있으며 우클릭으로 활을 얻을수 있습니다.\n" +
							   ChatColor.AQUA+"일반(좌클릭) "+ChatColor.WHITE+" 코블스톤 5개 소모, 쿨타임 20초\n" +
							   ChatColor.RED+"고급(우클릭) "+ChatColor.WHITE+" 코블스톤 15개 소모, 쿨타임 60초\n");
			break;
			
		case "스탠스":
			player.sendMessage(ChatColor.DARK_GREEN+"=================== "+ChatColor.YELLOW+"능력 정보"+ChatColor.DARK_GREEN+" ===================");
			player.sendMessage(ChatColor.YELLOW+"[ 스탠스 ]  "+ChatColor.RED+"[ 인간 ]  "+ChatColor.BLUE+"Passive  "+ChatColor.GREEN+"Rank[ B ]");
			player.sendMessage("모든 공격에 대하여 밀쳐지지 않습니다..\n");
			break;
			
		default: return;
		}
	}
	
	public static void LeftClick(final Player player){
		if(!jlist.containsKey(player.getName())){
			player.sendMessage(MS+"능력이 없습니다.");
		}
		switch(jlist.get(player.getName())){
		case "하데스":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 5))
			{
				Addcooldown(player, "좌클릭", 100);
				Entity entity=player;
				Location location = player.getLocation();
				location.setY(-2.0d);
				List<Entity> entitylist = entity.getNearbyEntities(2, 2, 2);
				for (Entity e : entitylist)
				{
					if (e instanceof LivingEntity)
					{
						e.teleport(location);
						if (e.getType() == EntityType.PLAYER)
							((Player)e).sendMessage("죽음의 신의 능력에 의해 나락으로 떨어집니다.");
						player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_STARE, 2.0f, 2.0f);
					}
				}
				player.teleport(location);
			} break;
			
		case "아테나":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 5))
			{
				Addcooldown(player, "좌클릭", 10);
				player.getInventory().addItem(new ItemStack(Material.BOOK.getId(),3));
				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0f, 2.0f);
			}
			break;
			
		case "아르테미스":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 7))
			{
				Addcooldown(player, "좌클릭", 20);
				player.getInventory().addItem(new ItemStack(Material.ARROW.getId(), 1));
				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0f, 2.0f);
			}
			break;
			
		case "아스클리피어스":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 1))
			{
				Addcooldown(player, "좌클릭", 50);
				player.setHealth(player.getMaxHealth());
				player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 1.5f);
			} break;
			
		case "헤르메스":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 2))
			{
				Addcooldown(player, "좌클릭", 60);
				player.setAllowFlight(true);
				player.setFlying(true);
				SkillCountdown(player, 5, "비행");
				player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2.0f, 1.7f);
				Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
				{
					public void run()
					{
						player.setAllowFlight(false);
						player.setFlying(false);
					}
				}, 100L);
			} break;
		case "텔레포터":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 4))
			{
				Block block = null;
				try{
					block = player.getTargetBlock(null, 15);
				}catch(Exception exception){
					return;
				}
				if(block == null){
					return;
				}
				if (AirToFar(player, block))
				{
					Location location0 = block.getLocation();
					Location location1 = block.getLocation();
					location0.setY(location0.getY()+1);
					location1.setY(location1.getY()+2);
					Block block0 = location0.getBlock();
					Block block1 = location1.getBlock();
					if ((block0.getTypeId()==0 || block1.getTypeId() == 78)&&block1.getTypeId()==0)
					{
						Addcooldown(player, "좌클릭", 25);
						Location plocation = player.getLocation();
						Location tlocation = block.getLocation();
						tlocation.setPitch(plocation.getPitch());
						tlocation.setYaw(plocation.getYaw());
						tlocation.setY(tlocation.getY()+1);
						tlocation.setX(tlocation.getX()+0.5);
						tlocation.setZ(tlocation.getZ()+0.5);
						player.teleport(tlocation);
						player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 1.2f);
					}
					else
						player.sendMessage("텔레포트 할 수 있는 공간이 없어 텔레포트에 실패 했습니다.");
				}
			}
			break;
			
		case "붐버":
			Block block = null;
			try{
				block = player.getTargetBlock(null, 5);
			}catch(Exception exception){
				return;
			}
			if(block == null){
				return;
			}
			if (block.getTypeId() != 0)
			{
				Location location = block.getLocation();
				location.setY(location.getY()+1);
				tntlocation = location;
				player.sendMessage("해당 블럭에 폭탄이 설치되었습니다.");	
				player.getWorld().playSound(player.getLocation(), Sound.DOOR_OPEN, 2.0f, 2.0f);
			} break;
			
		case "크리퍼":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 6))
			{
				Addcooldown(player, "좌클릭", 33);
				World world = player.getWorld();
				Location location = player.getLocation();
				float power = 3.0f;
				player.damage(30);
				world.createExplosion(location, power);
			} break;
			
		case "암살자":
			Location temp = player.getLocation();
			Block b = temp.add(0,-1,0).getBlock();
			if ((b.getTypeId()==0) || (b.getTypeId()==78) || (b.getTypeId()==44))
			{	
				if (Checkcooldown(player, "좌클릭"))
				{
					Addcooldown(player, "좌클릭", 2);
					World world = player.getWorld();
					Location location = player.getLocation();
					Vector v = player.getEyeLocation().getDirection();
					v.setY(0.5);			
					player.setVelocity(v);
					world.playEffect(location, Effect.ENDER_SIGNAL, 1);
					world.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 1.5f);
				}
			} break;
			
		case "블라인더":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 5))
			{
				Addcooldown(player, "좌클릭", 73);
				player.sendMessage("자신의 팀원을 제외한 모든 플레이어를 블라인드 합니다.");
				for (Player e : bluelist.contains(player) ? redlist : bluelist)
				{
					e.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0), true);
					e.sendMessage("블라인더에 의해 시야가 어두워집니다.");
				}
			} break;
			
		case "클로킹":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 4))
			{
				Addcooldown(player, "좌클릭", 30);
				player.sendMessage(MS+"15초간 모습을 투명 버프를 받습니다.");
				player.getWorld().playSound(player.getLocation(), Sound.MAGMACUBE_JUMP, 2.0f, 1.2f);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
			} break;
			
		case "대장장이":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 60))
			{
				Addcooldown(player, "좌클릭", 140);
				World world = player.getWorld();
				world.dropItem(player.getLocation().add(0,2,0), new ItemStack(Material.IRON_INGOT.getId(), 10));
			}
			
			break;
			
		case "사제":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 1))
			{
				Addcooldown(player, "좌클릭", 35);
				if (Getrandom(1, 2) == 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600,0),true);
					player.sendMessage(ChatColor.LIGHT_PURPLE+"데미지 저항 효과가 적용되었습니다.");
				}
				if (Getrandom(1, 2) == 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600,0),true);
					player.sendMessage(ChatColor.RED+"데미지 증가 효과가 적용되었습니다.");
				}
				if (Getrandom(1, 2) == 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600,0),true);
					player.sendMessage(ChatColor.GOLD+"체력회복속도 증가 효과가 적용되었습니다.");
				}
				if (Getrandom(1, 2) == 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600,0),true);
					player.sendMessage(ChatColor.AQUA+"이동속도 증가 효과가 적용되었습니다.");
				}
				if (Getrandom(1, 2) == 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 600,0),true);
					player.sendMessage(ChatColor.GREEN+"빠른 채광 효과가 적용되었습니다.");
				}
			} break;
			
		case "마녀":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 3))
			{
					Addcooldown(player, "좌클릭", 66);
					for (Player e : bluelist.contains(player) ? redlist : bluelist)
					{
						
						e.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200,0),true);
						e.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200,0),true);
						e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200,0),true);
						e.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200,0),true);
						e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200,0),true);
						e.sendMessage("마녀에 의해 저주에 걸렸습니다.");
					}
			}
			
		case "아쳐":
			if (Checkcooldown(player, "좌클릭") && Takeitem(player, 4, 5))
			{
				Addcooldown(player, "좌클릭", 20);
				World world = player.getWorld();
				Location location = player.getLocation();
				world.dropItem(location, new ItemStack(Material.ARROW.getId(), 1));
			} break;
			
		default: return;
		}
	}
	
	public static void RightClick(Player player){
		if(!jlist.containsKey(player.getName())){
			player.sendMessage(MS+"능력이 없습니다.");
		}
		switch(jlist.get(player.getName())){
		case "하데스":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 10))
			{
				Addcooldown(player, "우클릭", 200);
				Entity entity=player;
				Location location = player.getLocation();
				location.setY(-2.0d);
				List<Entity> entitylist = entity.getNearbyEntities(4, 4, 4);
				for (Entity e : entitylist)
				{
					if (e instanceof LivingEntity)
					{
						e.teleport(location);
						if (e.getType() == EntityType.PLAYER)
							((Player)e).sendMessage(ChatColor.RED+"죽음의 신의 능력에 의해 나락으로 떨어집니다.");
						player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_STARE, 2.0f, 2.0f);
					}
				}
			} break;
			
		case "데메테르":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 10))
			{
				Addcooldown(player, "우클릭", 30);
				Inventory inventory = player.getInventory();
				inventory.addItem(new ItemStack(Material.BREAD, 10));
				player.getWorld().playSound(player.getLocation(), Sound.ITEM_PICKUP, 2.0f, 1.5f);
			} break;
			
		case "아테나":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 64))
			{
				Addcooldown(player, "우클릭", 3);
				player.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE.getId(),1));
				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0f, 1.5f);
			} break;
			
		case "아르테미스":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 15))
			{
				Addcooldown(player, "우클릭", 180);
				player.getInventory().addItem(new ItemStack(Material.BOW.getId(), 1));
				player.getWorld().playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0f, 2.0f);
			} break;
			
		case "아스클리피어스":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 5))
			{
				Addcooldown(player, "우클릭", 100);
				player.sendMessage("자신을 포함한 모든 팀원의 체력을 회복합니다.");
				for (Player e : bluelist.contains(player) ? bluelist : redlist)
				{
					e.setHealth(e.getMaxHealth());
					e.sendMessage(ChatColor.YELLOW+"의술의 신의 능력으로 모든 체력을 회복합니다.");
					player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 1.5f);
				}
			} break;
		case "텔레포터":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 5))
			{
				if (abilitytarget.containsKey(player.getName()))
				{
					String targetname = abilitytarget.get(player.getName());
					Player target = null;
					for(Player t : plist){
						if(t.getName().equalsIgnoreCase(targetname))
							target = t;
					}
					if (target != null)
					{
						Location location = player.getLocation();
						location.setY(location.getY()-1);
						Addcooldown(player, "우클릭", 45);
						Location tloc = target.getLocation();
						Location ploc = player.getLocation();
						target.teleport(ploc);
						player.teleport(tloc);
						target.sendMessage("텔레포터의 능력에 의해 위치가 텔레포터의 위치로 변경되었습니다.");
					}
					else
						player.sendMessage("플레이어가 게임에 참여 중이 아닙니다.");
				}
				else
					player.sendMessage("타겟이 지정되지 않았습니다. (타겟 등록법 : /x <Player>)");
			} break;
			
		case "붐버":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 5))
			{
				if (tntlocation != null)
				{
					Addcooldown(player, "우클릭", 27);
					World world = player.getWorld();
					world.createExplosion(tntlocation, 2.0f, true);
					tntlocation = null;
					player.sendMessage("TNT가 폭발했습니다!");	
					player.getWorld().playSound(player.getLocation(), Sound.FUSE, 2.0f, 0.7f);
				}
				else
					player.sendMessage("TNT가 설치되지 않았습니다.");
			} break;
			
		case "암살자":
			if (Checkcooldown(player, "우클릭"))
			{
				Player target = null;
				double mindistance = 10;
				for(Player t : bluelist.contains(player) ? redlist : bluelist){
					double dis = t.getLocation().distance(player.getLocation());
					if(dis <= mindistance){
						target = t;
						mindistance = dis;
					}
				}
				if(target == null){
					player.sendMessage(MS+"주위 10칸내에 적이 없습니다.");
				} else {
					if(!Takeitem(player, 4, 2)) return;
					Addcooldown(player, "우클릭", 20);
					player.teleport(target.getLocation().subtract(target.getEyeLocation().getDirection()));
					player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 1.5f);
				}
			} break;
			
		case "대장장이":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 70))
			{
				Addcooldown(player, "우클릭", 250);
				World world = player.getWorld();
				world.dropItem(player.getLocation().add(0,2,0), new ItemStack(Material.DIAMOND.getId(), 5));
			} break;
			
		case "사제":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 2))
			{
				Addcooldown(player, "우클릭", 90);
					for (Player e : bluelist.contains(player) ? bluelist : redlist)
					{
						if (Getrandom(1, 2) == 1)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600,0),true);
							e.sendMessage(ChatColor.LIGHT_PURPLE+"사제의 능력에 의해 데미지 저항 효과가 적용되었습니다.");
						}
						if (Getrandom(1, 2) == 1)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600,0),true);
							e.sendMessage(ChatColor.RED+"사제의 능력에 의해 데미지 증가 효과가 적용되었습니다.");
						}
						if (Getrandom(1, 2) == 1)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600,0),true);
							e.sendMessage(ChatColor.GOLD+"사제의 능력에 의해 체력회복속도 증가 효과가 적용되었습니다.");
						}
						if (Getrandom(1, 2) == 1)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600,0),true);
							e.sendMessage(ChatColor.AQUA+"사제의 능력에 의해 이동속도 증가 효과가 적용되었습니다.");
						}
						if (Getrandom(1, 2) == 1)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 600,0),true);
							e.sendMessage(ChatColor.GREEN+"사제의 능력에 의해 빠른 채광 효과가 적용되었습니다.");
						}
					}
			} break;
			
		case "아쳐":
			if (Checkcooldown(player, "우클릭") && Takeitem(player, 4, 15))
			{
				Addcooldown(player, "우클릭", 60);
				World world = player.getWorld();
				Location location = player.getLocation();
				world.dropItem(location, new ItemStack(Material.BOW.getId(), 1));
			} break;
		default: return;
		}
	}
	
	public static void SetAbility(){
		jlist.clear();
		List<String> ablist = new ArrayList<String>();
		ablist.clear();
        ablist.add("하데스");
        ablist.add("데메테르");
        ablist.add("아테나");
        ablist.add("아르테미스");
        ablist.add("아레스");
        ablist.add("헤파이토스");
        ablist.add("아스클리피어스");
        ablist.add("헤르메스");
        ablist.add("디오니소스");
        ablist.add("광부");
        ablist.add("스탠스");
        ablist.add("텔레포터");
        ablist.add("붐버");
        ablist.add("크리퍼");
        ablist.add("암살자");
        ablist.add("반사");
        ablist.add("블라인더");
        ablist.add("클로킹");
        ablist.add("대장장이");
        ablist.add("복서");
        ablist.add("사제");
        ablist.add("마녀");
        ablist.add("아쳐");
        
        for(int i = 0; i < plist.size(); i++){
        	int random = Getrandom(0, (ablist.size()-1));
        	jlist.put(plist.get(i).getName(), ablist.get(random));
        	if(jlist.get(plist.get(i).getName()).equalsIgnoreCase("아테나")) atena = plist.get(i);
        	if(jlist.get(plist.get(i).getName()).equalsIgnoreCase("헤르메스")) plist.get(i).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
        	ablist.remove(random);
        }
	}
	
	public static int Getrandom(int min, int max){
		  return (int)(Math.random() * (max-min+1)+min);
		}
	
	public static void giveBaseItem(Player p){
		p.getInventory().addItem(new ItemStack(327 ,1));
		p.getInventory().addItem(new ItemStack(327 ,1));
		p.getInventory().addItem(new ItemStack(326 ,1));
		p.getInventory().addItem(new ItemStack(326 ,1));
		p.getInventory().addItem(new ItemStack(3 ,15));
		p.getInventory().addItem(new ItemStack(3 ,15));
		p.getInventory().addItem(new ItemStack(54 ,1));
		p.getInventory().addItem(new ItemStack(6 ,3));
		p.getInventory().addItem(new ItemStack(351 ,10, (byte) 15));
	}
	
	public static void Startgame(){
		check_lobbystart = true;
		final int cur = Getcursch();
		tasktime[cur] = 5;
		Bukkit.broadcastMessage(MS+"§b§l신들의 전쟁 게임§f이 곧 시작됩니다.");
		tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
		{
			public void run()
			{
				if(tasktime[cur] > 0)
				{
					Sendmessage(MS+"게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.");
					tasktime[cur]--;
					if(tasktime[cur] == 3){
						Bukkit.broadcastMessage(MS+"§a10초 후 §6신들의 전쟁§f의 맵을 초기화합니다. §c렉에 주의해주세요!");
					}
					if(tasktime[cur] == 2){
						Bukkit.broadcastMessage(MS+"§6신들의 전쟁§f의 맵을 초기화합니다. §c렉주의!!!");
						Player tmp = plist.get(0);
						tmp.setOp(true);
						Bukkit.dispatchCommand(tmp, "/schematic load EWGmap");
						Bukkit.dispatchCommand(tmp, "/paste -o");
						tmp.setOp(false);
						Sendmessage(MS+"해당 플러그인은 http://cafe.naver.com/craftproducer/1033 에서 개발하신");
						Sendmessage(MS+"신들의 전쟁 아이디어를 이용합니다.");
					}
				}
				else
				{
					Canceltask(cur);
					check_start = true;
					SetAbility();
					for(Player p : plist)
					{
						p.setFoodLevel(20);
						if(bluelist.size() <= redlist.size())
						{
							bluelist.add(p);
							Setbase(p);
							p.sendMessage(MS+"당신은 블루팀입니다.");
							p.setDisplayName("§b"+p.getName());
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join blue "+p.getName());
							backuptlist.put(p.getName(), "블루");
						}
						else
						{
							redlist.add(p);
							Setbase(p);
							p.sendMessage(MS+"당신은 레드팀입니다.");
							p.setDisplayName("§c"+p.getName());
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join red "+p.getName());
							backuptlist.put(p.getName(), "레드");
						}
					}
					Timer();
				}
			}
		}, 200L, 200L);
	}
	
	
	public static void Timer(){
		timertime = 25;
		timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
			public void run(){
				if(!check_start){
					Bukkit.getScheduler().cancelTask(timernum);
				}
				if(timertime > 0){
					Sendmessage(MS+"무승부까지 §c§l"+timertime+"§f분 남았습니다.");
					timertime--;
				} else {
					Bukkit.getScheduler().cancelTask(timernum);
					Draw();
				}
			}
		}, 200L, 1200L);
	}

	public static void Draw()
	{
		if(game_end) return;
		game_end = true;
	    for(Player p1 : plist){
	    	p1.getInventory().clear();
	    	p1.playSound(p1.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
	    }
		Sendmessage(MS+"§6경기 시간이 끝났습니다! 무승부 판정입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{	
				for(Player p : plist)
				{
						EconomyResponse r = econ.depositPlayer(p.getName(), 300);
						if (r.transactionSuccess()) 
						{
							p.sendMessage(ChatColor.GOLD + "무승부 보상으로 300원을 받으셨습니다.");
						}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Lobby);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§b§l신들의 전쟁§f이 §6§l무승부§f로 종료 됐습니다.");
			}
		}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public static void RedWin(Player p)
	{
		if(game_end) return;
		game_end = true;
	    for(Player p1 : plist){
	    	p.getInventory().clear();
	    	p1.playSound(p1.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
	    }
		Sendmessage(MS+"§6§l"+p.getName()+" §f님께서 블루팀의 다이아 블럭을 부셨습니다!");
		Sendmessage(MS+p.getName()+" §c레드팀§f의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{	
				for(Player p : plist)
				{
					if(bluelist.contains(p))
					{
						EconomyResponse r = econ.depositPlayer(p.getName(), 500);
						if (r.transactionSuccess()) 
						{
							p.sendMessage(ChatColor.GOLD + "패배 보상으로 500원을 받으셨습니다.");
						}
					}
					else
					{
						EconomyResponse r = econ.depositPlayer(p.getName(), 1000);
						if (r.transactionSuccess()) 
						{
							p.sendMessage(ChatColor.GOLD + "승리 보상으로 1000원을 받으셨습니다.");
						}
					}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Lobby);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§b§l신들의 전쟁§f이 §c§l레드팀§f의 승리로 종료 됐습니다.");
			}
		}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public static void Cleardata(){
		Bukkit.getScheduler().cancelTasks(instance);
		Forcestoptimer = 0;
		plist.clear();
		check_start = false;
		check_lobbystart = false;
		redlist.clear();
		bluelist.clear();
		hades_inventory = null;
		hades_armor = null;
		atena = null;
		jlist.clear();
		teamchat.clear();
		tntlocation = null;
		cooldown.clear();
		game_end = false;
		abilitytarget.clear();
		backuptlist.clear();
	}
	
	public static void BlueWin(Player p)
	{
		if(game_end) return;
		game_end = true;
	    for(Player p1 : plist){
	    	p.getInventory().clear();
	    	p1.playSound(p1.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
	    }
		Sendmessage(MS+"§6§l"+p.getName()+" §f님께서 레드팀의 다이아 블럭을 부셨습니다!");
		Sendmessage(MS+p.getName()+" §c블루팀§f의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
				{
			public void run()
			{
				for(Player p : plist)
				{
					if(bluelist.contains(p))
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 1000);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "승리 보상으로 1000원을 받으셨습니다.");
					      }
					}
					else
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 500);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "패배 보상으로 500원을 받으셨습니다.");
					      }
					}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Lobby);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§b§l신들의 전쟁§f이 §9§l블루팀§f의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public void BreakDiamond(Player player){
		if(bluelist.contains(player)){
			BlueWin(player);
		}
		else{
			RedWin(player);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(e.getItem() == null) return;
		if(plist.contains(player)){
			if(e.getItem().getType() == Material.COMPASS) player.openInventory(gamehelper);
			if(!jlist.containsKey(player.getName()) || e.getItem().getType() != Material.BLAZE_ROD) return;
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) LeftClick(player);
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) RightClick(player);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(plist.contains(event.getPlayer())){
			Player player = event.getPlayer();
			if(!check_start){
				event.setCancelled(true);
				return;
			}
			if(event.getBlock().getType() == Material.DIAMOND_BLOCK) {
				Material item = player.getItemInHand().getType();
				if(item == Material.IRON_PICKAXE || item == Material.DIAMOND_PICKAXE || item == Material.GOLD_PICKAXE){
					event.setCancelled(true);
					player.sendMessage(MS+"철곡, 다이아곡, 금곡괭이로는 캐실 수 없습니다!");
					return;
				}
				Location bl = event.getBlock().getLocation();
				if((bluelist.contains(player) && (bl.getBlockX() == Blue_core.getBlockX() && bl.getBlockY() == Blue_core.getBlockY() && bl.getBlockZ() == Blue_core.getBlockZ()))
						|| (redlist.contains(player) && (bl.getBlockX() == Red_core.getBlockX() && bl.getBlockY() == Red_core.getBlockY() && bl.getBlockZ() == Red_core.getBlockZ()))){
					player.sendMessage(MS+"팀의 코어를 캐지마세요!!!");
					event.setCancelled(true);
					return;
				}
					BreakDiamond(player);
			}
			if(event.getBlock().getTypeId() == 24 || event.getBlock().getTypeId() == 20 || event.getBlock().getTypeId() == 35) event.setCancelled(true);
			if(!jlist.containsKey(player.getName())) return;
			switch(jlist.get(player.getName())){
			case "광부":
				Block block = event.getBlock();
				if (block.getTypeId() == 4)
				{
					Location location = block.getLocation();
					World world = event.getPlayer().getWorld();
					if (Getrandom(1, 33) == 1)
					{
						player.sendMessage("잭팟!");
						world.dropItemNaturally(location, new ItemStack(4, 19));
					}
				}
				break;
			default: return;
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		if(!plist.contains(player)) return;
		if(!check_start){
			event.setCancelled(true); return;
		}
		switch(jlist.get(player.getName())){
		case "스탠스":
			if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE)
			{
				int damage = event.getDamage();
				player.damage(damage);
				event.setCancelled(true);
			} break;
		case "헤파이토스":
			DamageCause dc = event.getCause();
			if (dc.equals(DamageCause.LAVA) ||
				dc.equals(DamageCause.FIRE) ||
				dc.equals(DamageCause.FIRE_TICK))
			{
				event.setCancelled(true);
				player.setFireTicks(0);
			}
			else if (dc.equals(DamageCause.DROWNING))
				event.setDamage(event.getDamage()<<1);
			break;
			
		default: return;
		}
	}
	
	@EventHandler
	public void onPlayerdeath(PlayerDeathEvent event){
		Player player = (Player)event.getEntity();
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		if(atena != null) atena.setLevel(atena.getLevel()+1);
		switch(jlist.get(player.getName())){
		case "하데스":
			int rn = (int) (Math.random()*5);
			if (rn != 0)
			{
				hades_inventory=event.getEntity().getInventory().getContents();
				hades_armor = event.getEntity().getInventory().getArmorContents();
				event.getDrops().clear();
			}
			else
				player.sendMessage("아이템을 모두 잃었습니다.");
			break;
			
		default: return;
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		if(check_start){
			event.setRespawnLocation(bluelist.contains(player) ? Blue_spawn : Red_spawn);
		}
		switch(jlist.get(player.getName())){
		case "하데스":
			if (hades_inventory !=null)
				player.getInventory().setContents(hades_inventory);
			if (hades_armor !=null)
			player.getInventory().setArmorContents(hades_armor);
			hades_inventory = null;
			hades_armor = null;
			break;
			
		case "헤르메스":
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
				public void run(){
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
				}
			}, 20l);
			break;
			
		default: return;
		}
	}
	
	@EventHandler
	public void FoodLevelChange(FoodLevelChangeEvent event){
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		switch(jlist.get(player.getName())){
		case "데메테르":
			player.setFoodLevel(20);
			event.setCancelled(true);
			break;
			
		default: return;
		}
	}
	
	@EventHandler
	public void EntityDamageByEntity(EntityDamageByEntityEvent event){
		
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		
		Player damager = null;
		
		if(event.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) event.getDamager();
			damager = (Player) arrow.getShooter();
			
			if((bluelist.contains(player) && bluelist.contains(damager)) || (redlist.contains(player) && redlist.contains(damager)))
			{
				event.setCancelled(true);
				return;
			}
			
			if(jlist.get(damager.getName()).equalsIgnoreCase("아르테미스"))
			{
				if (Getrandom(1, 5) == 1)//1/5확률
				{
					event.setDamage(100);
					damager.sendMessage("화살이 상대방의 심장을 꿰뚫었습니다!");
					player.sendMessage("아르테미스의 화살에 즉사하였습니다.");
				}
			} else if(jlist.get(damager.getName()).equalsIgnoreCase("아쳐")) event.setDamage((int)(event.getDamage()*1.2));
			
		} 
		if(!(event.getDamager() instanceof Player)) return;
		
		damager = (Player) event.getDamager();
		
		if((bluelist.contains(player) && bluelist.contains(damager)) || (redlist.contains(player) && redlist.contains(damager)))
		{
			event.setCancelled(true);
			return;
		}
		
		if(jlist.containsKey(damager.getName()) && jlist.get(damager.getName()).equalsIgnoreCase("아레스")){
			event.setDamage((int) (event.getDamage()*1.5));
		}
		if(jlist.get(player.getName()).equalsIgnoreCase("아레스")){
			if (Getrandom(1, 10) == 1)//1/5확률
			{
				event.setCancelled(true);
				player.sendMessage("§c공격을 회피했습니다!");
			}
		}
		
		if (jlist.get(player.getName()).equalsIgnoreCase("디오니소스"))
		{
			if (Getrandom(1, 4) == 1)//1/5확률
			{
			damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,0), true);
			damager.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,200,0), true);
			damager.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,240,0), true);
			damager.sendMessage("술에 취해서 정신이 없습니다!");
			player.sendMessage("상대방에게 술을 먹였습니다.");
			}
		}
		
		if(jlist.get(player.getName()).equalsIgnoreCase("반사")){
			if (Getrandom(1, 3) == 1)
			{
				damager.damage(event.getDamage()>>1);
				event.setDamage(event.getDamage()>>1);
			}
		}
		
		if(jlist.get(player.getName()).equalsIgnoreCase("블라인더")){
			if (Getrandom(1, 5) == 1)
			{
				damager.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0), true);
				damager.sendMessage("블라인더에 의해 시야가 어두워집니다.");
			}
		}	
		
		if(jlist.get(damager.getName()).equalsIgnoreCase("복서")){
			if (player.getItemInHand().getTypeId()==0)
			{
				if (!player.isBlocking())
					player.setNoDamageTicks(0);
			}
		}
		
		if(jlist.get(player.getName()).equalsIgnoreCase("마녀")){
			if (Getrandom(1, 14) == 1)
			{
				damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100,0),true);//*20	
				damager.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100,0),true);
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,0),true);
				damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100,0),true);
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100,0),true);
				damager.sendMessage("마녀에 의해 저주가 걸렸습니다.");
			}
		}
		
		if(jlist.get(player.getName()).equalsIgnoreCase("클로킹")){
			if (Getrandom(1, 10) == 1)
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80,0),true);//*20	
				player.sendMessage("신속 버프를 받습니다.");
			}
		}
	}
	
	public void Sendteamchat(List<Player> list, String str)
	{
		for(Player p : list)
		{
			p.sendMessage(str);
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e){
		if(e.getEntity() instanceof Arrow){
			Arrow arrow = (Arrow) e.getEntity();
			if(arrow.getShooter() instanceof Player) return;
			Player player = (Player) arrow.getShooter();
			if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
			if(jlist.get(player.getName()).equalsIgnoreCase("아쳐")){
				arrow.setVelocity(player.getEyeLocation().getDirection().multiply(5));
			}
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
	public void RegainHealth(EntityRegainHealthEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		if(!plist.contains(player) || !jlist.containsKey(player.getName())) return;
		switch(jlist.get(player.getName())){
		case "데메테르":
			event.setAmount(event.getAmount()<<2);
			break;
			
		default: return;
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
	  public void onUseBucket(PlayerBucketEmptyEvent e){
		  if(!plist.contains(e.getPlayer())) return;
		  Material bucket = e.getBucket();
		  Player p = e.getPlayer();
		  if (bucket.toString().contains("WATER") || bucket.toString().contains("LAVA")) {
			  if(bluelist.contains(p) && Red_spawn.distance(p.getLocation()) <= 25){
				  e.setCancelled(true);
				  p.sendMessage(MS+"레드팀 기지 25칸내에는 용암 및 물 양동이를 사용하실 수 없습니다.");
			  }
			  if(redlist.contains(p) && Blue_spawn.distance(p.getLocation()) <= 25){
				  e.setCancelled(true);
				  p.sendMessage(MS+"블루팀 기지 25칸내에는 용암 및 물 양동이를 사용하실 수 없습니다.");
			  }
		  }
	  }
	  
	@EventHandler
	public void onChat(PlayerChatEvent e)
	{
		if(plist.contains(e.getPlayer()))
		{
			Player p = e.getPlayer();
			if(teamchat.contains(p.getName()))
			{
				e.setCancelled(true);
				Sendteamchat(bluelist.contains(p) ? bluelist : redlist, "§f[ §9팀채팅 §f] "+p.getName()+" : "+e.getMessage());
			}
		}
	}
}
