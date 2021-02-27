package me.Bokum.DTC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static List<Player> plist = new ArrayList<Player>(); 
	public static String MS = "§f[ §aEG §f] ";
	public static Location Join_loc;
	public static Location Main_loc;
	public static Location Bluespawn_loc;
	public static Location Redspawn_loc;
	public static Location Redcore_loc;
	public static Location Bluecore_loc;
	public static Location RedArrive_loc;
	public static Location BlueArrive_loc;
	public static int tasknum[] = new int[50];
	public static int tasktime[] = new int[50];
	public static int timernum = 0;
	public static int timertime = 15;
	public static List<Player> bluelist = new ArrayList<Player>();
	public static List<Player> redlist = new ArrayList<Player>();
	public static Inventory jobs;
	public static boolean game_end = false;
	public static HashMap<String, Integer> cooldown = new HashMap<String, Integer>();
	public static HashMap<String, String> jlist = new HashMap<String, String>();
	public static List<String> archer1 = new ArrayList<String>();
	public static List<String> archer2 = new ArrayList<String>();
	public static List<String> healer1 = new ArrayList<String>();
	public static List<String> assasin1 = new ArrayList<String>();
	public static List<String> teamchat = new ArrayList<String>();
	public static int Bluecore_cnt = 0;
	public static int Redcore_cnt = 0;
	public static ItemStack helpitem;
	public static ItemStack redFlag;
	public static ItemStack blueFlag;
	public static Inventory gamehelper;
	public static ItemStack bluearmor1 = null;
	public static ItemStack bluearmor2 = null;
	public static ItemStack bluearmor3 = null;
	public static ItemStack bluearmor4 = null;
	public static ItemStack redarmor1 = null;
	public static ItemStack redarmor2 = null;
	public static ItemStack redarmor3 = null;
	public static ItemStack redarmor4 = null;
	public static Main instance;
	public static Economy econ = null;
	
	public void LoadConfig()
	{
		ConfigurationSection sec = getConfig().getConfigurationSection("Location_Join");
		Join_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_Main");
		Main_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_Blue");
		Bluespawn_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_Red");
		Redspawn_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_BlueCore");
		Bluecore_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_RedCore");
		Redcore_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_RedArrive");
		RedArrive_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		sec = getConfig().getConfigurationSection("Location_BlueArrive");
		BlueArrive_loc = new Location(getServer().getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
	}
	
	public void Createjobsinv()
	{
		jobs = Bukkit.createInventory(null, 9, "직업선택");
		ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§e탱커");
		item.setItemMeta(meta);
		jobs.setItem(0, item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e아쳐");
		item.setItemMeta(meta);
		jobs.setItem(2, item);
		
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e워리어");
		item.setItemMeta(meta);
		jobs.setItem(4, item);
		
		item = new ItemStack(Material.DIAMOND_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e어쌔신");
		item.setItemMeta(meta);
		jobs.setItem(6, item);
		
		item = new ItemStack(Material.WOOD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e힐러");
		item.setItemMeta(meta);
		jobs.setItem(8, item);
	}
	
	public static int Getcursch()
	{
		for(int i = 0 ; i < 200; i++)
		{
			if(tasknum[i] == -5)
			{
				return i;
			}
		}
		return 0;
	}
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("CTF 플러그인이 로드 되었습니다.");
		LoadConfig();
		Createjobsinv();
		for(int i = 0 ; i < tasknum.length; i++)
		{
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		instance = this;
		bluearmor4 = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta meta4 = (LeatherArmorMeta) bluearmor4.getItemMeta();
		meta4.setColor(Color.BLUE);
		bluearmor4.setItemMeta(meta4);

		bluearmor3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta meta3 = (LeatherArmorMeta) bluearmor3.getItemMeta();
		meta3.setColor(Color.BLUE);
		bluearmor3.setItemMeta(meta3);

		bluearmor2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta meta2 = (LeatherArmorMeta) bluearmor2.getItemMeta();
		meta2.setColor(Color.BLUE);
		bluearmor2.setItemMeta(meta2);

		bluearmor1 = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta meta1 = (LeatherArmorMeta) bluearmor1.getItemMeta();
		meta1.setColor(Color.BLUE);
		bluearmor1.setItemMeta(meta1);

		redarmor1 = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) redarmor1.getItemMeta();
		meta.setColor(Color.RED);
		redarmor1.setItemMeta(meta);

		redarmor2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		meta = (LeatherArmorMeta) redarmor2.getItemMeta();
		meta.setColor(Color.RED);
		redarmor2.setItemMeta(meta);

		redarmor3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		meta = (LeatherArmorMeta) redarmor3.getItemMeta();
		meta.setColor(Color.RED);
		redarmor3.setItemMeta(meta);

		redarmor4 = new ItemStack(Material.LEATHER_BOOTS, 1);
		meta = (LeatherArmorMeta) redarmor4.getItemMeta();
		meta.setColor(Color.RED);
		redarmor4.setItemMeta(meta);

		gamehelper = Bukkit.createInventory(null, 27, "§c§l도우미");
		
		ItemStack item = new ItemStack(34, 1);
		ItemMeta metatmp = item.getItemMeta();
		metatmp.setDisplayName("§6장식");
		item.setItemMeta(metatmp);
		for(int i = 0; i <= 9; i++){
			gamehelper.setItem(i, item);
		}
		for(int i = 17; i < 27; i++){
			gamehelper.setItem(i, item);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		metatmp = item.getItemMeta();
		metatmp.setDisplayName("§e플레이 방법");
		item.setItemMeta(metatmp);
		gamehelper.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		metatmp = item.getItemMeta();
		metatmp.setDisplayName("§e게임 규칙");
		item.setItemMeta(metatmp);
		gamehelper.setItem(13, item);
		
		item = new ItemStack(Material.BOOK, 1);
		metatmp = item.getItemMeta();
		metatmp.setDisplayName("§e팀채팅 on/off");
		List<String> lorelist1 = new ArrayList<String>();
		lorelist1.add("§f- §c게임이 시작된 후 사용할 수 있습니다.");
		item.setItemMeta(metatmp);
		gamehelper.setItem(15, item);
		
		helpitem = new ItemStack(345, 1);
		metatmp = helpitem.getItemMeta();
		metatmp.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist1.clear();
		lorelist1.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist1);
		helpitem.setItemMeta(metatmp);
		
		redFlag = new ItemStack(35, 1, (byte) 14);
		metatmp = redFlag.getItemMeta();
		metatmp.setDisplayName("§f[ §4레드팀 깃발 §f]");
		redFlag.setItemMeta(metatmp);
		
		blueFlag = new ItemStack(35, 1, (byte) 11);
		metatmp = blueFlag.getItemMeta();
		metatmp.setDisplayName("§f[ §b블루팀 깃발 §f]");
		blueFlag.setItemMeta(metatmp);
		
        if (!setupEconomy() ) {
            getLogger().info("[ 버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
            getServer().getPluginManager().disablePlugin(this);
            return;
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
		getLogger().info("CTF 플러그인이 언로드 되었습니다.");
		saveConfig();
	}
	
	public boolean onCommand(CommandSender talker, Command command, String string, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(string.equalsIgnoreCase("dtc") && p.hasPermission("dtc"))
			{
				if(args.length >= 1)
				{
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
					if(args[0].equalsIgnoreCase("job"))
					{
						p.openInventory(jobs);
						return true;
					}
					if(args[0].equalsIgnoreCase("set"))
					{
						Setlocation(p, args);
						return true;
					}
					if(args[0].equalsIgnoreCase("stop"))
					{
						Forcestop();
						return true;
					}
				}
				else
				{
					Helpmessages(p);
				}
			}
		}
		return false;
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/dtc join");
		p.sendMessage(MS+"/dtc quit");
		p.sendMessage(MS+"/dtc jobs");
		p.sendMessage(MS+"/dtc set");
		p.sendMessage(MS+"/dtc stop");
		p.sendMessage(MS+"버젼 0.61");
	}
	
	public void Setlocation(Player p, String[] args)
	{
		if(args.length >= 2)
		{
			if(args[1].equalsIgnoreCase("join"))
			{
				if(!getConfig().isConfigurationSection("Location_Join"))
				{
					getConfig().createSection("Location_Join");
				}
				getConfig().getConfigurationSection("Location_Join").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_Join").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_Join").set("y", p.getLocation().getBlockY()+1);
				getConfig().getConfigurationSection("Location_Join").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"입장 지점 설정됨");
			}
			if(args[1].equalsIgnoreCase("main"))
			{
				if(!getConfig().isConfigurationSection("Location_Main"))
				{
					getConfig().createSection("Location_Main");
				}
				getConfig().getConfigurationSection("Location_Main").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_Main").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_Main").set("y", p.getLocation().getBlockY()+1);
				getConfig().getConfigurationSection("Location_Main").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"로비 설정됨");
			}
			if(args[1].equalsIgnoreCase("blue"))
			{
				if(!getConfig().isConfigurationSection("Location_Blue"))
				{
					getConfig().createSection("Location_Blue");
				}
				getConfig().getConfigurationSection("Location_Blue").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_Blue").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_Blue").set("y", p.getLocation().getBlockY()+1);
				getConfig().getConfigurationSection("Location_Blue").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"블루 스폰 설정됨");
			}
			if(args[1].equalsIgnoreCase("red"))
			{
				if(!getConfig().isConfigurationSection("Location_Red"))
				{
					getConfig().createSection("Location_Red");
				}
				getConfig().getConfigurationSection("Location_Red").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_Red").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_Red").set("y", p.getLocation().getBlockY()+1);
				getConfig().getConfigurationSection("Location_Red").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"레드 스폰 설정됨");
			}
			if(args[1].equalsIgnoreCase("redcore"))
			{
				if(!getConfig().isConfigurationSection("Location_RedCore"))
				{
					getConfig().createSection("Location_RedCore");
				}
				getConfig().getConfigurationSection("Location_RedCore").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_RedCore").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_RedCore").set("y", p.getLocation().getBlockY()-1);
				getConfig().getConfigurationSection("Location_RedCore").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"레드 코어 설정됨");
			}
			if(args[1].equalsIgnoreCase("bluecore"))
			{
				if(!getConfig().isConfigurationSection("Location_BlueCore"))
				{
					getConfig().createSection("Location_BlueCore");
				}
				getConfig().getConfigurationSection("Location_BlueCore").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_BlueCore").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_BlueCore").set("y", p.getLocation().getBlockY()-1);
				getConfig().getConfigurationSection("Location_BlueCore").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"블루 코어 설정됨");
			}
			if(args[1].equalsIgnoreCase("redarrive"))
			{
				if(!getConfig().isConfigurationSection("Location_Arrive"))
				{
					getConfig().createSection("Location_RedArrive");
				}
				getConfig().getConfigurationSection("Location_RedArrive").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_RedArrive").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_RedArrive").set("y", p.getLocation().getBlockY()-1);
				getConfig().getConfigurationSection("Location_RedArrive").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"레드 전달지 설정됨");
			}
			if(args[1].equalsIgnoreCase("bluearrive"))
			{
				if(!getConfig().isConfigurationSection("Location_BlueArrive"))
				{
					getConfig().createSection("Location_BlueArrive");
				}
				getConfig().getConfigurationSection("Location_BlueArrive").set("world", p.getWorld().getName());
				getConfig().getConfigurationSection("Location_BlueArrive").set("x", p.getLocation().getBlockX());
				getConfig().getConfigurationSection("Location_BlueArrive").set("y", p.getLocation().getBlockY()-1);
				getConfig().getConfigurationSection("Location_BlueArrive").set("z", p.getLocation().getBlockZ());
				p.sendMessage(MS+"블루 전달지 설정됨");
			}
			saveConfig();
			LoadConfig();
		}
		else
		{
			p.sendMessage(MS+"/dtc set join");
			p.sendMessage(MS+"/dtc set main");
			p.sendMessage(MS+"/dtc set blue");
			p.sendMessage(MS+"/dtc set red");
			p.sendMessage(MS+"/dtc set bluecore");
			p.sendMessage(MS+"/dtc set redcore");
			p.sendMessage(MS+"/dtc set bluearrive");
			p.sendMessage(MS+"/dtc set redarrive");
		}
	}
	
	public static void Sendmessage(String str)
	{
		for(Player p : plist)
		{
			p.sendMessage(str);
		}
	}
	
	public void Bluemessage(String str)
	{
		for(Player p : bluelist)
		{
			p.sendMessage(str);
		}
	}
	
	public void Redmessage(String str)
	{
		for(Player p : redlist)
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
		if(plist.size() >= 20)
		{
			p.sendMessage(MS+"이미 최대인원(20)입니다. 해당 게임은 도중에 참여가 가능하기때문에 나중에 다시 시도해주세요.");
			return;
		}
		if(check_start)
		{
			plist.add(p);
			if(bluelist.size() <= redlist.size())
			{
				bluelist.add(p);
				p.getInventory().clear();
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join blue "+p.getName());
				p.teleport(Bluespawn_loc);
			}
			else
			{
				redlist.add(p);
				p.getInventory().clear();
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join red "+p.getName());
				p.teleport(Redspawn_loc);
			}
			p.sendMessage(MS+"CTF 게임에 중도 참여하셨습니다.");
		}
		else
		{
			plist.add(p);
			p.teleport(Join_loc);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "CTF");
			Sendmessage(MS+p.getName()+" 님이 CTF 게임에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c8 §f)");
			if(!(check_start || check_lobbystart) && plist.size() >= 8)
			{
				Startgame();
				Bluecore_loc.getBlock().setTypeIdAndData(35, (byte) 11, true);
				Redcore_loc.getBlock().setTypeIdAndData(35, (byte) 14, true);
			}
			for(Player t : plist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
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
		Bukkit.broadcastMessage(MS+"§e§lCTF §f게임이 곧 시작됩니다.");
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
					for(Player p : plist)
					{
						if(bluelist.size() <= redlist.size())
						{
							bluelist.add(p);
							p.teleport(Bluespawn_loc);
							p.sendMessage(MS+"당신은 블루팀입니다. 직업군을 선택해주세요.");
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join blue "+p.getName());
							p.setDisplayName("§b"+p.getName());
						}
						else
						{
							redlist.add(p);
							p.teleport(Redspawn_loc);
							p.sendMessage(MS+"당신은 레드팀입니다. 직업군을 선택해주세요.");
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join red "+p.getName());
							p.setDisplayName("§c"+p.getName());
						}
					}
					check_start = true;
				}
			}
		}, 200L, 200L);
	}
	
	  public static void GameHelper(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §cCTF");
			  p.sendMessage("§f게임이 시작되면 §c레드팀§f과 §b블루팀§f으로 나눠집니다.");
			  p.sendMessage("§f각 팀에는 §c깃발(각 팀색의 양털)§f이라는 것이 존재하며");
			  p.sendMessage("§f상대의 깃발(양털)을 캐면 깃발을 획득하게됩니다. ");
			  p.sendMessage("§f획득한 깃발을 각 팀에 존재하는 §b전달지(에메랄드 블럭)§f으로 전달하면");
			  p.sendMessage("§f깃발 갯수가 올라가게됩니다. 총 5개의 깃발을 전달하면 승리합니다.");
			  p.sendMessage("§b경험치바 : §7상대의 깃발은 20초마다 재생성됩니다. 경험치바로 그 시간을 알 수 있습니다.");
			  p.sendMessage("§b레벨 : §7자신의 팀이 회수한 상대의 깃발을 나타냅니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f상대방 기지에서 스폰킬을 하는 것은 불가능합니다.");
			  p.sendMessage("§c※ 팀킬은 자연적으로 막아주니 걱정마세요.");
			  p.closeInventory();
			  return;
			  
		  case 15:
			  if(!check_start) {
				  p.sendMessage("게임이 시작된 후 사용하실 수 있습니다.");
				  p.closeInventory();
				  return;
			  }
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
		if(bluelist.contains(p))
		{
			bluelist.remove(p);
		}
		if(redlist.contains(p))
		{
			redlist.remove(p);
		}
		p.teleport(Main_loc);
	}
	
	public static boolean Checkcooldown(Player p, String str)
	{
		if(!Main.cooldown.containsKey(p.getName()+str) || Main.cooldown.get(p.getName()+str) <= (int)(java.lang.System.currentTimeMillis()/1000))
		{
			return true;
		}
		p.sendMessage(MS+ChatColor.AQUA+(Main.cooldown.get(p.getName()+str)-(int)(java.lang.System.currentTimeMillis()/1000))
				+ChatColor.RESET+"초 후 스킬을 사용하실 수 있습니다.");
		return false;
	}
	
	public void Setcooldown(String str, int cooldown)
	{
		Main.cooldown.put(str, (int)(java.lang.System.currentTimeMillis()/1000)+cooldown);
	}
	
	public void Skill(final Player p, int id)
	{
		if(id == 347)
		{
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
			return;
		}
		
		if(!jlist.containsKey(p.getName()))
		{
			return;
		}
		
		switch(jlist.get(p.getName()))
		{
		case "탱커":
			switch(id)
			{
			
			case 264:
				if(Checkcooldown(p, "1"))
				{
					Setcooldown(p.getName()+"1", 8);
					p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 2), true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
							{
								public void run()
								{
									p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 0));
								}
							}, 60L);
					return;
				}
				
				break;
				
			case 388:
				if(Checkcooldown(p, "2"))
				{
					Setcooldown(p.getName()+"2", 17);
					p.getWorld().playSound(p.getLocation(), Sound.ANVIL_BREAK, 2.0f, 0.5f);
					for(Player t : (bluelist.contains(p) ? redlist : bluelist))
					{
						if(t.getLocation().distance(p.getLocation()) <= 6)
						{
							t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 31));
							t.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 240));
						}
					}
					return;
				}
				
				break;
				
			case 265:
				if(Checkcooldown(p, "3"))
				{
					Setcooldown(p.getName()+"3", 18);
					p.getWorld().playSound(p.getLocation(), Sound.ANVIL_USE, 2.0f, 0.5f);
					for(Player t : (!bluelist.contains(p) ? redlist : bluelist))
					{
						if(t.getLocation().distance(p.getLocation()) <= 6 && !jlist.get(t.getName()).equalsIgnoreCase("탱커"))
						{
							t.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1));
							t.sendMessage(MS+"탱커 "+p.getName()+" 님의 능력으로 인해 방어력이 올라갑니다.");
						}
					}
					return;
				}
				
				break;
				
			default: return;
			
			}  break;
			
		case "아쳐":
			switch(id)
			{
			
			case 264:
				if(Checkcooldown(p, "1"))
				{
					Setcooldown(p.getName()+"1", 12);
					archer1.add(p.getName());
					p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 2.0f, 0.5f);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
							{
								public void run()
								{
									if(archer1.contains(p.getName()))
									{
										archer1.remove(p.getName());
										p.sendMessage(MS+"확인사살의 지속시간이 끝났습니다.");
									}
								}
							}, 80L);
					return;
				}
				
				break;
				
			case 388:
				if(Checkcooldown(p, "2"))
				{
					Setcooldown(p.getName()+"2", 14);
					archer2.add(p.getName());
					p.getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 0.5f);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
							{
								public void run()
								{
									if(archer2.contains(p.getName()))
									{
										archer2.remove(p.getName());
										p.sendMessage(MS+"섬광화살의 지속시간이 끝났습니다.");
									}
								}
							}, 80L);
					return;
				}
				
				break;
				
			default: return;
			
			}  break;
			
		case "어쌔신":
			switch(id)
			{
			
			case 264:
				if(Checkcooldown(p, "1"))
				{
					Setcooldown(p.getName()+"1", 12);
					p.getWorld().playSound(p.getLocation(), Sound.CREEPER_HISS, 2.0f, 0.5f);
					PotionEffect potion = new PotionEffect(PotionEffectType.INVISIBILITY, 120, 1);
					p.addPotionEffect(potion);
					final ItemStack[] armor = p.getInventory().getArmorContents();
					p.getInventory().setArmorContents(null);
					p.updateInventory();
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
					{
						public void run()
						{
							p.getInventory().setArmorContents(armor);
							p.updateInventory();
							if(assasin1.contains(p.getName()))
							{
								assasin1.remove(p.getName());
								p.sendMessage(MS+"은신이 풀려 암살스킬이 해제 됐습니다.");
							}
						}
					}, 120);
				}
				
				break;
				
			case 388:
				if(Checkcooldown(p, "2"))
				{
					if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY))
					{
						p.sendMessage(MS+"은신 상태일때만 사용 가능합니다.");
						return;
					}
					Setcooldown(p.getName()+"2", 10);
					assasin1.add(p.getName());
					p.getWorld().playSound(p.getLocation(), Sound.CREEPER_DEATH, 2.0f, 0.5f);
					return;
				}
				
				break;
				
			default: return;
			
			}  break;
			
		case "워리어":
			switch(id)
			{
			
			case 264:
				if(Checkcooldown(p, "1"))
				{
					Setcooldown(p.getName()+"1", 13);
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_DEATH, 2.0f, 2.0f);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0), true);
					return;
				}
				
				break;
				
			case 388:
				if(Checkcooldown(p, "2"))
				{
					Setcooldown(p.getName()+"2", 11);
					p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 1.5f);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 0), true);
					return;
				}
				
				break;
				
			case 265:
				if(Checkcooldown(p, "3"))
				{
					Setcooldown(p.getName()+"3", 20);
					p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 2.0f, 1.2f);
					for(Player t : (bluelist.contains(p) ? redlist : bluelist))
					{
						if(t.getLocation().distance(p.getLocation()) <= 4)
						{
							t.setVelocity(new Vector(0,1.3f,0));
						}
					}
					return;
				}
				
				break;
				
			default: return;
			
			}  break;
			
			
			
		case "힐러":
			switch(id)
			{
			
			case 264:
				if(Checkcooldown(p, "1"))
				{
					Setcooldown(p.getName()+"1", 10);
					healer1.add(p.getName());
					p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 2.0f, 1.5f);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
					{
						public void run()
						{
							if(healer1.contains(p.getName()))
							{
								healer1.remove(p.getName());
								p.sendMessage(MS+"치유의 화살의 지속시간이 끝났습니다.");
							}
						}
					}, 80L);
					return;
				}
				
				break;
				
			case 388:
				if(Checkcooldown(p, "2"))
				{
					Setcooldown(p.getName()+"2", 23);
					p.getWorld().playSound(p.getLocation(), Sound.ORB_PICKUP, 2.0f, 0.8f);
					for(Player t : (!bluelist.contains(p) ? redlist : bluelist))
					{
						if(t.getLocation().distance(p.getLocation()) <= 5)
						{
							int hp = t.getHealth();
							hp += 8;
							if(hp > 20)
							{
								hp = 20;
							}
							t.setHealth(hp);
							t.sendMessage(MS+"힐러가 당신을 회복 시킵니다.");
						}
					}
					return;
				}
				
				break;
				
			default: return;
			
			}  break;
			
		default: return;
		
		}
	}
	
	public void Setjob(Player p, String str)
	{
		p.closeInventory();
		for(PotionEffect effect : p.getActivePotionEffects())
		{
			p.removePotionEffect(effect.getType());
		}
		
		if(bluelist.contains(p))
		{
			p.getInventory().setHelmet(bluearmor1);
			p.getInventory().setChestplate(bluearmor2);
			p.getInventory().setLeggings(bluearmor3);
			p.getInventory().setBoots(bluearmor4);
		}
		else
		{
			p.getInventory().setHelmet(redarmor1);
			p.getInventory().setChestplate(redarmor2);
			p.getInventory().setLeggings(redarmor3);
			p.getInventory().setBoots(redarmor4);
		}
		
		for(int i = 0; i < 36; i++)
		{
			p.getInventory().setItem(i, null);
		}
		
		ItemStack item;
		ItemMeta meta;
		List<String> lorelist = new ArrayList<String>();

		item = new ItemStack(Material.WATCH, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e팀채팅");
		lorelist.clear();
		lorelist.add("- §7우클릭시 팀챗을 켜거나 끕니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		p.getInventory().setItem(8, item);
		
		switch(str)
		{
		case "탱커":
			jlist.put(p.getName(), "탱커");
			
			item = new ItemStack(Material.STONE_SWORD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b탱커");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			meta.addEnchant(Enchantment.DURABILITY, 10, true);
			item.setItemMeta(meta);
			p.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.DIAMOND, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b철벽");
			lorelist.clear();
			lorelist.add("- §73초간 저항3");
			lorelist.add("- §c쿨타임 8초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.EMERALD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b제압");
			lorelist.clear();
			lorelist.add("- §7반경 6칸이내 적군 2초간 이동불가");
			lorelist.add("- §c쿨타임 17초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(2, item);
			
			item = new ItemStack(Material.IRON_INGOT, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b함성");
			lorelist.clear();
			lorelist.add("- §7반경 6칸이내 팀원 저항1");
			lorelist.add("- §c쿨타임 18초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(3, item);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 0));
			
			break;
			
		case "아쳐":
			jlist.put(p.getName(), "아쳐");
			
			item = new ItemStack(Material.BOW, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b아쳐");
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			meta.addEnchant(Enchantment.DURABILITY, 10, true);
			item.setItemMeta(meta);
			p.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.DIAMOND, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b확인사살");
			lorelist.clear();
			lorelist.add("- §7사용후 화살로 적을 맞출시");
			lorelist.add("- §7데미지 1.5배, 단 4초이내에 맞춰야한다.");
			lorelist.add("- §c쿨타임 12초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.EMERALD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b섬광화살");
			lorelist.clear();
			lorelist.add("- §7사용후 화살로 적을 맞출시");
			lorelist.add("- §7실명1 8초, 단 4초이내에 맞춰야한다.");
			lorelist.add("- §c쿨타임 14초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(2, item);
			
			item = new ItemStack(Material.ARROW, 64);
			p.getInventory().setItem(3, item);
			
			break;
			
		case "어쌔신":
			jlist.put(p.getName(), "어쌔신");
			
			item = new ItemStack(Material.DIAMOND_SWORD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b어쌔신");
			lorelist.clear();
			lorelist.add("- §7자신이 받는 모든 데미지는");
			lorelist.add("- §71.3배가 됩니다.");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.DIAMOND, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b은신");
			lorelist.clear();
			lorelist.add("- §76초간 모습을 감춥니다.");
			lorelist.add("- §c쿨타임 12초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.EMERALD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b암살");
			lorelist.clear();
			lorelist.add("- §7은신 상태에서만 사용가능");
			lorelist.add("- §7사용 후 첫 타격 데미지 1.5배");
			lorelist.add("- §7은신이 해제될 시 스킬 자동해제");
			lorelist.add("- §c쿨타임 10초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(2, item);
			
			break;
			
		case "워리어":
			jlist.put(p.getName(), "워리어");
			
			item = new ItemStack(Material.IRON_SWORD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b워리어");
			meta.addEnchant(Enchantment.DURABILITY, 10, true);
			item.setItemMeta(meta);
			p.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.DIAMOND, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b가격");
			lorelist.clear();
			lorelist.add("- §73초간 공격력 상승");
			lorelist.add("- §c쿨타임 13초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.EMERALD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b돌진");
			lorelist.clear();
			lorelist.add("- §74초간 이속1 증가");
			lorelist.add("- §c쿨타임 11초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(2, item);
			
			item = new ItemStack(Material.IRON_INGOT, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b분쇄");
			lorelist.clear();
			lorelist.add("- §7반경 3칸이내의 적을 위로 띄움");
			lorelist.add("- §c쿨타임 20초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(3, item);
			
			break;
			
		case "힐러":
			jlist.put(p.getName(), "힐러");
			
			item = new ItemStack(Material.BOW, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b힐러");
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			lorelist.clear();
			lorelist.add("- §7활로 팀원을 맞출시 데미지만큼 체력을 회복시켜줍니다.");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(0, item);
			
			item = new ItemStack(Material.WOOD_SWORD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b힐러");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			meta.addEnchant(Enchantment.DURABILITY, 10, true);
			item.setItemMeta(meta);
			p.getInventory().setItem(1, item);
			
			item = new ItemStack(Material.DIAMOND, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b치유의 화살");
			lorelist.clear();
			lorelist.add("- §75초간 화살 치유량이 2배가 됩니다.");
			lorelist.add("- §75초간 화살에 타격당한 적은 독에 중독됩니다.");
			lorelist.add("- §c쿨타임 10초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(2, item);
			
			item = new ItemStack(Material.EMERALD, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§b고무");
			lorelist.clear();
			lorelist.add("- §7반경 4칸 아군 체력 +8");
			lorelist.add("- §c쿨타임 16초");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().setItem(3, item);
			
			item = new ItemStack(Material.ARROW, 64);
			p.getInventory().setItem(4, item);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 72000, 0));
			
			break;
			
			default: return;
		}
		
		p.updateInventory();
	}
	
	public static void Cleardata()
	{
		Bukkit.getScheduler().cancelTasks(instance);
		timernum = 0;
		timertime = 15;
		check_start = false;
		check_lobbystart = false;
		plist.clear();
		bluelist.clear();
		redlist.clear();
		game_end = false;
		cooldown.clear();
		jlist.clear();
		archer1.clear();
		archer2.clear();
		healer1.clear();
		teamchat.clear();
		Redcore_cnt = 0;
		Bluecore_cnt = 0;
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"CTF 게임이 강제종료 되었습니다.");
		for(Player p : plist)
		{
	          try{
	        	  me.Bokum.EGM.Main.Spawn(p);
	          } catch(Exception e){
	        	  p.teleport(Main.Main_loc);
	          }
		}
		Cleardata();
	}
	
	public void RedWin()
	{
		game_end = true;
		Sendmessage(MS+"레드팀이 먼저 5개의 깃발을 모았습니다!");
		Sendmessage(MS+"레드팀의 승리로 게임이 종료됐습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
	public void run()
	{
		for(Player p : plist)
		{
			if(!redlist.contains(p))
			{
			      EconomyResponse r = econ.depositPlayer(p.getName(), 600);
			      if (r.transactionSuccess()) {
			            p.sendMessage(ChatColor.GOLD + "패배 보상으로 600원을 받으셨습니다.");
			      }
			}
			else
			{
			      EconomyResponse r = econ.depositPlayer(p.getName(), 1200);
			      if (r.transactionSuccess()) {
			            p.sendMessage(ChatColor.GOLD + "승리 보상으로 1200원을 받으셨습니다.");
			      }
			}
	          try{
	        	  me.Bokum.EGM.Main.Spawn(p);
	          } catch(Exception e){
	        	  p.teleport(Main.Main_loc);
	          }
		}
		Cleardata();
		Bukkit.broadcastMessage(MS+"§e§lCTF가 레드팀의 승리로 종료 됐습니다.");
	}
		}, 140L);
		}catch(Exception e){
			Forcestop();
		}
	}
	
	public static void BlueWin()
	{
		game_end = true;
		Sendmessage(MS+"블루팀이 먼저 5개의 깃발을 모았습니다.");
		Sendmessage(MS+"블루팀의 승리로 게임이 종료됐습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
				{
			public void run()
			{
				for(Player p : plist)
				{
					if(bluelist.contains(p))
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 1200);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "승리 보상으로 1200원을 받으셨습니다.");
					      }
					}
					else
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 600);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "패배 보상으로 600원을 받으셨습니다.");
					      }
					}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Main_loc);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§e§lCTF가 블루팀의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop();
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
	public void onClickInventory(InventoryClickEvent e)
	{
		if(e.getWhoClicked() instanceof Player)
		{
			Player p = (Player) e.getWhoClicked();
			if(!plist.contains(p))
			{
				return;
			}
			if(e.getSlot() == 36 || e.getSlot() == 37 || e.getSlot() == 38 || e.getSlot() == 39)
			{
				e.setCancelled(true);
			}
			if(e.getInventory().getName().equalsIgnoreCase("직업선택"))
			{
				e.setCancelled(true);
				switch(e.getSlot())
				{
				case 0:
					Setjob(p, "탱커"); break;
				case 2:
					Setjob(p,"아쳐"); break;
				case 4:
					Setjob(p, "워리어"); break;
				case 6:
					Setjob(p, "어쌔신");; break;
				case 8:
					Setjob(p, "힐러"); break;
				default: return;
				}
			}
			 if(e.getInventory().getTitle().equalsIgnoreCase("§c§l도우미")){
				 GameHelper(p, e.getSlot());
				 e.setCancelled(true);
			 }
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e)
	{
		if(plist.contains(e.getPlayer()) && check_start)
		{
			Player p = e.getPlayer();
			e.setRespawnLocation(bluelist.contains(p) ? Bluespawn_loc : Redspawn_loc);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		if(plist.contains(e.getPlayer()))
		{
			GameQuit(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onRightClickItem(PlayerInteractEvent e)
	{
		if(!plist.contains(e.getPlayer())) return;
		Player p = e.getPlayer();
	 	if(e.getItem() != null && e.getItem().getType() == Material.COMPASS){
	 		p.openInventory(gamehelper); return;
	 	}
		if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
			Skill(p, p.getItemInHand().getTypeId()); return;
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
			if(e.getClickedBlock().getTypeId() == 19) p.openInventory(jobs);
			Location bl = e.getClickedBlock().getLocation();
			Location l = new Location(bl.getWorld(), bl.getBlockX(), bl.getBlockY(), bl.getBlockZ());
			if(l.equals(RedArrive_loc) && redlist.contains(p) && !game_end){
				if(p.getInventory().contains(35)){
					p.getInventory().remove(35);
					Redcore_cnt++;
					Sendmessage(MS+"§c§l레드팀 §a§l"+p.getName()+"§6§l 님이 깃발을 전달했습니다! §e(§c레드팀§f: "+Redcore_cnt+"개 §b블루팀:§f "+Bluecore_cnt+"개");
					for(Player t : redlist){
						t.setLevel(Redcore_cnt);
					}
					for(Player t : plist){
						t.playSound(t.getLocation(), Sound.ANVIL_USE, 0.5f, 3.0f);
					}
					if(Redcore_cnt >= 5){
						RedWin();
					}
					removeFlagEffect(p);
				}else{
					p.sendMessage(MS+"상대의 깃발을 가지고 우클릭하세요!");
				}
			}
			if(l.equals(BlueArrive_loc) && bluelist.contains(p) && !game_end){
				if(p.getInventory().contains(35)){
					p.getInventory().remove(35);
					Sendmessage(MS+"§b§l블루팀 §a§l"+p.getName()+"§6§l 님이 깃발을 전달했습니다!");
					Bluecore_cnt++;
					for(Player t : bluelist){
						t.setLevel(Bluecore_cnt);
					}
					for(Player t : plist){
						t.playSound(t.getLocation(), Sound.ANVIL_USE, 0.5f, 3.0f);
					}
					if(Bluecore_cnt >= 5){
						BlueWin();
					}
					removeFlagEffect(p);
				}else{
					p.sendMessage(MS+"상대의 깃발을 가지고 우클릭하세요!");
				}
			}
		}
	}
	
	@EventHandler
	public void onLobbyDamage(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!plist.contains(p)) return;
		if(bluelist.contains(p) && Bluespawn_loc.distance(p.getLocation()) <= 30){
			p.sendMessage(MS+"기지에서 30칸내는 무적입니다.");
			e.setCancelled(true);
		}
		if(redlist.contains(p) && Redspawn_loc.distance(p.getLocation()) <= 30){
			p.sendMessage(MS+"기지에서 30칸내는 무적입니다.");
			e.setCancelled(true);
		}
		if(!check_start){
			e.setCancelled(true); return;
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			if(!(plist.contains(p) && jlist.containsKey(p.getName())))
			{
				return;
			}
			if(!check_start)
			{
				e.setCancelled(true);
				return;
			}
			Player d = null;
			if(e.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow) e.getDamager();
				if(arrow.getShooter() instanceof Player)
				{
					d = (Player) arrow.getShooter();
				}
			}
			else if(e.getDamager() instanceof Player)
			{
				d = (Player) e.getDamager();
			}
			if(d == null || !(plist.contains(d) && jlist.containsKey(d.getName())))
			{
				return;
			}
			if(plist.contains(p) && plist.contains(d))
			{
				if((redlist.contains(p) && redlist.contains(d)) || (bluelist.contains(p) && bluelist.contains(d)))
				{
					if(jlist.get(d.getName()).equalsIgnoreCase("힐러"))
					{
						if(e.getDamager() instanceof Arrow)
						{
							int hp = p.getHealth();
							hp += healer1.contains(d.getName()) ? e.getDamage()*2 : e.getDamage();
							if(hp > 20)
							{
								hp = 20;
							}
							p.setHealth(hp);
							p.sendMessage(MS+"힐러가 당신을 회복 시킵니다.");
							p.getWorld().playSound(d.getLocation(), Sound.BREATH, 1.0f, 0.6f);
						}
					}
					e.setCancelled(true);
					return;
				}
				if(jlist.get(p.getName()).equalsIgnoreCase("어쌔신"))
				{
					e.setDamage((int)(e.getDamage()*1.3));
				}
				if(e.getDamager() instanceof Arrow)
				{
					if(archer1.contains(d.getName()))
					{
						archer1.remove(d.getName());
						d.sendMessage(MS+p.getName()+" 님의 머리를 맞췄습니다. (확인사살 스킬)");
						e.setDamage((int) (e.getDamage()*1.5));
					}
					if(healer1.contains(d.getName()))
					{
						healer1.remove(d.getName());
						d.sendMessage(MS+p.getName()+" 님은 독에 중독됩니다!");
						PotionEffect potion = new PotionEffect(PotionEffectType.POISON, 40, 1);
						p.addPotionEffect(potion);
					}
					if(archer2.contains(d.getName()))
					{
						d.sendMessage(MS+p.getName()+" 님의 시야를 방해합니다. (섬광화살 스킬)");
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 1));
					}
				}
				else
				{
					if(assasin1.contains(d.getName()))
					{
						assasin1.remove(d.getName());
						d.sendMessage(MS+p.getName()+" 님의 급소를 공격했습니다. (암살 스킬)");
						e.setDamage((int)(e.getDamage()*1.5));
					}
				}
			}
		}
	}
	
	/*@EventHandler
	public void onArrowHit(ProjectileHitEvent e)
	{
		if(e.getEntity() instanceof Arrow)
		{
			Arrow arrow = (Arrow) e.getEntity();
			if(arrow.getShooter() instanceof Player && arrow.get)
		}
	}*/
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e)
	{
		if(plist.contains(e.getPlayer()))
		{
			e.setCancelled(true);
		}	
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		if(plist.contains(e.getEntity()))
		{
			e.getEntity().getInventory().clear();
			e.getDrops().clear();
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		  if(plist.contains(e.getPlayer()))
		e.setCancelled(true);
	}
	
	public void removeFlagEffect(Player p){
		PlayerInventory pinv = p.getInventory();
		if(pinv.getHelmet() != null){
			ItemMeta meta = pinv.getHelmet().getItemMeta();
			meta.removeEnchant(Enchantment.ARROW_INFINITE);
			pinv.getHelmet().setItemMeta(meta);
		}
		if(pinv.getChestplate() != null){
			ItemMeta meta = pinv.getChestplate().getItemMeta();
			meta.removeEnchant(Enchantment.ARROW_INFINITE);
			pinv.getChestplate().setItemMeta(meta);
		}
		if(pinv.getLeggings() != null){
			ItemMeta meta = pinv.getLeggings().getItemMeta();
			meta.removeEnchant(Enchantment.ARROW_INFINITE);
			pinv.getLeggings().setItemMeta(meta);
		}
		if(pinv.getBoots() != null){
			ItemMeta meta = pinv.getBoots().getItemMeta();
			meta.removeEnchant(Enchantment.ARROW_INFINITE);
			pinv.getBoots().setItemMeta(meta);
		}
		p.removePotionEffect(PotionEffectType.SLOW);
	}
	
	public void getFlagEffect(Player p){
		PlayerInventory pinv = p.getInventory();
		if(pinv.getHelmet() != null){
			ItemMeta meta = pinv.getHelmet().getItemMeta();
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			pinv.getHelmet().setItemMeta(meta);
		}
		if(pinv.getChestplate() != null){
			ItemMeta meta = pinv.getChestplate().getItemMeta();
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			pinv.getChestplate().setItemMeta(meta);
		}
		if(pinv.getLeggings() != null){
			ItemMeta meta = pinv.getLeggings().getItemMeta();
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			pinv.getLeggings().setItemMeta(meta);
		}
		if(pinv.getBoots() != null){
			ItemMeta meta = pinv.getBoots().getItemMeta();
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			pinv.getBoots().setItemMeta(meta);
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 72000, 0));
	}
	
	@EventHandler
	public void onBlockbreak(final BlockBreakEvent e)
	{
		if(plist.contains(e.getPlayer()))
		{
			Player p = e.getPlayer();
			Location tmpbl = e.getBlock().getLocation();
			Location bl = new Location(p.getWorld(), tmpbl.getBlockX(), tmpbl.getBlockY(), tmpbl.getBlockZ());
			e.setCancelled(true);
			if(bl.equals(Bluecore_loc)){
				if(bluelist.contains(p)){
					p.sendMessage(MS+"팀의 깃발을 부수지마세요!");
				}else if(p.getInventory().contains(35)){
					p.sendMessage(MS+"이미 깃발을 가지고 있습니다. 기지로 전달하세요!");
				}else{
					e.getBlock().setType(Material.AIR);
					p.sendMessage(Main.MS+"블루팀의 깃발을 얻었습니다! 기지에 있는 전달지로 전달하세요!");
					Sendmessage(MS+"§c§l레드팀 §a§l"+p.getName()+"§6§l 님이 §b§l블루팀§6§l의 깃발을 획득했습니다!\n"+MS+"깃발을 가진 플레이어는 갑옷이 빛납니다.");
					p.getInventory().addItem(blueFlag);
					getFlagEffect(p);
					final int cur = Getcursch();
					tasktime[cur] = 20;
					tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
						public void run(){
							if(--tasktime[cur] > 0){
								for(Player t : redlist){
									t.setExp(1/tasktime[cur]);
								}
							}else{
								Canceltask(cur);
								Redmessage(MS+"블루팀의 깃발이 재생성 되었습니다.");
								e.getBlock().setTypeIdAndData(35, (byte) 11, true);
							}
						}
					}, 0l, 20l);
					return;
				}
			}
			if(bl.equals(Redcore_loc)){
				if(redlist.contains(p)){
					p.sendMessage(MS+"팀의 깃발을 부수지마세요!");
				}else if(p.getInventory().contains(35)){
					p.sendMessage(MS+"이미 깃발을 가지고 있습니다. 기지로 전달하세요!");
				}else{
					e.getBlock().setType(Material.AIR);
					p.sendMessage(Main.MS+"레드팀의 깃발을 얻었습니다! 기지에 있는 전달지로 전달하세요!");
					Sendmessage(MS+"§b§l블루팀 §a§l"+p.getName()+"§6§l 님이 §c§l레드팀§6§l의 깃발을 획득했습니다!\n"+MS+"깃발을 가진 플레이어는 갑옷이 빛납니다.");
					p.getInventory().addItem(redFlag);
					getFlagEffect(p);
					final int cur = Getcursch();
					tasktime[cur] = 20;
					tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
						public void run(){
							if(--tasktime[cur] > 0){
								for(Player t : bluelist){
									t.setExp(1/tasktime[cur]);
								}
							}else{
								Canceltask(cur);
								Redmessage(MS+"레드팀의 깃발이 재생성 되었습니다.");
								e.getBlock().setTypeIdAndData(35, (byte) 14, true);
							}
						}
					}, 0l, 20l);
					return;
				}
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
