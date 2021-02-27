package me.Bokum.TRB;

import java.util.ArrayList;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static String MS = "§f[ §aEG §f] ";
	public static Location Lobby;
	public static Location[][] startpos = new Location[6][3];
	public static Location joinpos;
	public static int tasknum[] = new int[10];
	public static int tasktime[] = new int[10];
	public static List<Player> plist = new ArrayList<Player>();
	public static List<Player> redlist = new ArrayList<Player>();
	public static List<Player> bluelist = new ArrayList<Player>();
	public static List<String> deadlist = new ArrayList<String>();
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static int round = 0;
	public static ItemStack[] bluearmor = new ItemStack[4];
	public static ItemStack[] redarmor = new ItemStack[4];
	public static ItemStack clock = new ItemStack(Material.WATCH, 1);
	public static ItemStack emerald = new ItemStack(Material.EMERALD, 1);
	public static int red_death_cnt = 0;
	public static int blue_death_cnt = 0;
	public static int redscore = 0;
	public static int bluescore = 0;
	public static ItemStack helpitem;
	public static Inventory gamehelper;
	public static boolean check_round_start = false;
	public static List<String> teamchat = new ArrayList<String>();
	public static Economy econ = null;
	public static Main instance;
	public static int Forcestoptimer = 0;
	public static List<String> pickList = new ArrayList<String>();
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("팀 랜덤 배틀 플러그인이 로드 되었습니다.");
		
		instance = this;
		
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
		bluearmor[0] = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) bluearmor[0].getItemMeta();
		meta.setColor(Color.BLUE);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		bluearmor[0].setItemMeta(meta);
		
		bluearmor[1] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		meta = (LeatherArmorMeta) bluearmor[1].getItemMeta();
		meta.setColor(Color.BLUE);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		bluearmor[1].setItemMeta(meta);
		
		bluearmor[2] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		meta = (LeatherArmorMeta) bluearmor[2].getItemMeta();
		meta.setColor(Color.BLUE);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		bluearmor[2].setItemMeta(meta);

		bluearmor[3] = new ItemStack(Material.LEATHER_BOOTS, 1);
		meta = (LeatherArmorMeta) bluearmor[3].getItemMeta();
		meta.setColor(Color.BLUE);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		bluearmor[3].setItemMeta(meta);

		redarmor[0] = new ItemStack(Material.LEATHER_HELMET, 1);
		meta = (LeatherArmorMeta) redarmor[0].getItemMeta();
		meta.setColor(Color.RED);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		redarmor[0].setItemMeta(meta);

		redarmor[1] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		meta = (LeatherArmorMeta) redarmor[1].getItemMeta();
		meta.setColor(Color.RED);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		redarmor[1].setItemMeta(meta);

		redarmor[2] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		meta = (LeatherArmorMeta) redarmor[2].getItemMeta();
		meta.setColor(Color.RED);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		redarmor[2].setItemMeta(meta);

		redarmor[3] = new ItemStack(Material.LEATHER_BOOTS, 1);
		meta = (LeatherArmorMeta) redarmor[3].getItemMeta();
		meta.setColor(Color.RED);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		redarmor[3].setItemMeta(meta);
		
		ItemMeta meta1 = clock.getItemMeta();
		meta1.setDisplayName("§e팀채팅");
		List<String> lorelist = new ArrayList<String>();
		lorelist.clear();
		lorelist.add("- §7우클릭시 팀챗을 켜거나 끕니다.");
		meta.setLore(lorelist);
		clock.setItemMeta(meta1);
		
		meta1 = emerald.getItemMeta();
		meta1.setDisplayName("§c무기뽑기");
		lorelist.clear();
		lorelist.clear();
		lorelist.add("- §7우클릭시 무기를 뽑습니다.");
		meta.setLore(lorelist);
		emerald.setItemMeta(meta1);
		
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
		meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		Loadconfig();
		
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
		getLogger().info("팀 랜덤 배틀 플러그인이 언로드 되었습니다.");
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
	  for (int i = 1; i <= 6; i++)
	  {
			ConfigurationSection sec = getConfig().getConfigurationSection("Pos"+i);
			try
			{
			  startpos[(i-1)][0] = new Location(Bukkit.getWorld(sec.getString("Spec_world")), sec.getInt("Spec_x"), sec.getInt("Spec_y"), sec.getInt("Spec_z"));
			  startpos[(i-1)][1] = new Location(Bukkit.getWorld(sec.getString("Redstart_world")), sec.getInt("Redstart_x"), sec.getInt("Redstart_y"), sec.getInt("Redstart_z"));
			  startpos[(i-1)][2] = new Location(Bukkit.getWorld(sec.getString("Bluestart_world")), sec.getInt("Bluestart_x"), sec.getInt("Bluestart_y"), sec.getInt("Bluestart_z"));
			}
			catch (Exception e)
			{
			  getLogger().info(i + "번째  지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
			}
	  }
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("trb") && p.isOp())
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
				}
			}
		}
		return false;
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/trb join");
		p.sendMessage(MS+"/trb quit");
		p.sendMessage(MS+"/trb set");
		p.sendMessage(MS+"/trb reload");
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"팀 랜덤 배틀이 강제 종료 되었습니다.");
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
		plist.clear();
		redlist.clear();
		bluelist.clear();
		teamchat.clear();
		check_start = false;
		check_lobbystart = false;
		round = 0;
		red_death_cnt = 0;
		blue_death_cnt = 0;
		redscore = 0;
		bluescore = 0;
		check_round_start = false;
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/trb set lobby");
			p.sendMessage(MS+"/trb set join");
			p.sendMessage(MS+"/trb set pos 1~6 <death/red/blue>");
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
		else if(args.length >= 4)
		{
			if(args[1].equalsIgnoreCase("pos"))
			{
					int i = Integer.valueOf(args[2]);
					if(!(1 <= i && i <= 6))
					{
						p.sendMessage("1~6의 숫자만 입력 가능합니다.");
						return;
					}
					if(!(args[3].equalsIgnoreCase("spec") || args[3].equalsIgnoreCase("red") || args[3].equalsIgnoreCase("blue")))
					{
						p.sendMessage(MS+"/trb set pos 1~6 <spec/red/blue>");
						return;
					}
					if(!getConfig().isConfigurationSection("Pos"+i))
					{
						getConfig().createSection("Pos"+i);
					}
					if(args[3].equalsIgnoreCase("spec"))
					{
						getConfig().getConfigurationSection("Pos"+i).set("Spec_world", p.getWorld().getName());
						getConfig().getConfigurationSection("Pos"+i).set("Spec_x", p.getLocation().getBlockX());
						getConfig().getConfigurationSection("Pos"+i).set("Spec_y", p.getLocation().getBlockY()+1);
						getConfig().getConfigurationSection("Pos"+i).set("Spec_z", p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+"pos "+i+"의 spec 설정 완료");
					}
					if(args[3].equalsIgnoreCase("red"))
					{
						getConfig().getConfigurationSection("Pos"+i).set("Redstart_world", p.getWorld().getName());
						getConfig().getConfigurationSection("Pos"+i).set("Redstart_x", p.getLocation().getBlockX());
						getConfig().getConfigurationSection("Pos"+i).set("Redstart_y", p.getLocation().getBlockY()+1);
						getConfig().getConfigurationSection("Pos"+i).set("Redstart_z", p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+"pos "+i+"의 red 설정 완료");
					}
					if(args[3].equalsIgnoreCase("blue"))
					{
						getConfig().getConfigurationSection("Pos"+i).set("Bluestart_world", p.getWorld().getName());
						getConfig().getConfigurationSection("Pos"+i).set("Bluestart_x", p.getLocation().getBlockX());
						getConfig().getConfigurationSection("Pos"+i).set("Bluestart_y", p.getLocation().getBlockY()+1);
						getConfig().getConfigurationSection("Pos"+i).set("Bluestart_z", p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+"pos "+i+"의 blue 설정 완료");
					}
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
		if(plist.size() >= 12)
		{
			p.sendMessage(MS+"이미 최대인원(12)입니다.");
			return;
		}
		if(check_start)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다. 현재 "+round+" 라운드");
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
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "팀 랜덤 배틀");
			Sendmessage(MS+p.getName()+" 님이 팀 랜덤 배틀 게임에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c6 §f)");
			if(!check_lobbystart && plist.size() >= 6)
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
		if(teamchat.contains(p))
		{
			teamchat.remove(p);
		}
		if(check_round_start && !deadlist.contains(p.getName())){
			deadlist.remove(p.getName());
			if(redlist.contains(p))
			{
				red_death_cnt--;
				if(red_death_cnt <= 0)
				{
					BlueRoundWin();
				}
			}
			else
			{
				blue_death_cnt--;
				if(blue_death_cnt <= 0)
				{
					RedRoundWin();
				}
			}
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
		if(redlist.size() <= 0)
		{
			Sendmessage(MS+"모든 레드팀이 나갔습니다! 블루팀의 승리입니다!");
			BlueWin();
		}else if(bluelist.size() <= 0)
		{
			Sendmessage(MS+"모든 블루팀이 나갔습니다! 레드팀의 승리입니다!");
			RedWin();
		}
	}
	
	  public static void GameHelper(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c팀 랜덤 배틀");
			  p.sendMessage("§f게임이 시작되면 §c레드팀§f과 §b블루팀§f으로 나눠집니다.");
			  p.sendMessage("§f먼저 §d3라운드§f를 승리한 팀이 승리합니다.");
			  p.sendMessage("§f매 라운드마다 §a에메랄드§f를 받으며 §a에메랄드§f를 우클릭시");
			  p.sendMessage("§c장비 및 레벨§f이 §e랜덤§f으로 주어집니다.");
			  p.sendMessage("§f먼저 적팀을 전부 사살한 팀이 라운드에서 승리합니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f사망 후에는 해당 라운드를 관전하게 됩니다.");
			  p.sendMessage("§f서로 장비를 §c교환§f하거나 대신 §b인첸트§f해주실 수 있습니다. ");
			  p.sendMessage("§f라운드가 시작된 후에는 아이템을 뽑으실 수 없습니다.");
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
		Bukkit.broadcastMessage(MS+"§6§l팀 랜덤 배틀 게임§f이 곧 시작됩니다.");
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
					for(Player p : plist)
					{
						if(bluelist.size() <= redlist.size())
						{
							bluelist.add(p);
							p.sendMessage(MS+"당신은 블루팀입니다.");
							p.setDisplayName("§b"+p.getName());
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join blue "+p.getName());
						}
						else
						{
							redlist.add(p);
							p.sendMessage(MS+"당신은 레드팀입니다.");
							p.setDisplayName("§c"+p.getName());
							Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join red "+p.getName());
						}
					}
					StartRound();
				}
			}
		}, 200L, 200L);
	}
	
	public static void RedWin()
	{
	    for(Player p : plist){
	    	p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
	    }
		Sendmessage(MS+"레드팀이 먼저 3승을 하였습니다! 레드팀의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{	
				for(Player p : plist)
				{
					if(bluelist.contains(p))
					{
						EconomyResponse r = econ.depositPlayer(p.getName(), 400);
						if (r.transactionSuccess()) 
						{
							p.sendMessage(ChatColor.GOLD + "패배 보상으로 400원을 받으셨습니다.");
						}
					}
					else
					{
						EconomyResponse r = econ.depositPlayer(p.getName(), 800);
						if (r.transactionSuccess()) 
						{
							p.sendMessage(ChatColor.GOLD + "승리 보상으로 800원을 받으셨습니다.");
						}
					}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Lobby);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§e§l팀 랜덤 배틀§f이 §c레드팀§f의 승리로 종료 됐습니다.");
			}
		}, 140L);
		}catch(Exception e){
			Forcestop();
		}
	}
	
	public static void BlueWin()
	{
	    for(Player p : plist){
	    	p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 0.5f);
	    }
		Sendmessage(MS+"블루팀이 먼저 3승을 하였습니다! 블루팀의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
				{
			public void run()
			{
				for(Player p : plist)
				{
					if(bluelist.contains(p))
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 800);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "승리 보상으로 800원을 받으셨습니다.");
					      }
					}
					else
					{
					      EconomyResponse r = econ.depositPlayer(p.getName(), 400);
					      if (r.transactionSuccess()) {
					            p.sendMessage(ChatColor.GOLD + "패배 보상으로 400원을 받으셨습니다.");
					      }
					}
			          try{
			        	  me.Bokum.EGM.Main.Spawn(p);
			          } catch(Exception e){
			        	  p.teleport(Main.Lobby);
			          }
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§e§l팀 랜덤 배틀§f이 §9블루팀§f의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public static void StartRound()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				check_round_start = false;
				deadlist.clear();
				for(Player p : bluelist)
				{
					p.getInventory().clear();
					p.setLevel(0);
					p.getInventory().setHelmet(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setLeggings(null);
					p.getInventory().setBoots(null);
					p.setHealth(p.getMaxHealth());
					p.teleport(startpos[0][2]);
					p.getInventory().setItem(8, helpitem);
					p.getInventory().setItem(0, emerald);
				}
				for(Player p : redlist)
				{
					p.getInventory().clear();
					p.setLevel(0);
					p.getInventory().setHelmet(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setLeggings(null);
					p.getInventory().setBoots(null);
					p.setHealth(p.getMaxHealth());
					p.teleport(startpos[0][1]);
					p.getInventory().setItem(8, helpitem);
					p.getInventory().setItem(0, emerald);
				}
				if(redscore >= 3)
				{
					RedWin();
					return;
				}
				else if(bluescore >= 3)
				{
					BlueWin();
					return;
				}
				round++;
				pickList.clear();
				Sendmessage(MS+"§6"+round+" §b라운드가 70초 후 시작됩니다. 에메랄드를 우클릭하여 무기를 뽑아주세요.");
				Startround2();
			}
		}, 100L);
	}
	
	public static void Startround2()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				check_round_start = true;
				red_death_cnt = redlist.size();
				blue_death_cnt = bluelist.size();
				for(Player p : bluelist)
				{
					p.teleport(startpos[round][1]);
				}
				for(Player p : redlist)
				{
					p.teleport(startpos[round][2]);
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
					public void run(){
						for(Player p : bluelist)
						{
							p.teleport(startpos[0][1]);
						}
						for(Player p : redlist)
						{
							p.teleport(startpos[0][2]);
						}
						Sendmessage(MS+"§6"+round+" §b라운드가 시작 됐습니다! 적들을 섬멸하세요!");
					}
				}, 15l);
				Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
					public void run(){
						for(Player p : bluelist)
						{
							p.sendMessage(MS+"tp 렉을 방지하기 위하여 한번 더 tp 됩니다.");
							p.teleport(startpos[round][1]);
						}
						for(Player p : redlist)
						{
							p.sendMessage(MS+"tp 렉을 방지하기 위하여 한번더 tp 됩니다.");
							p.teleport(startpos[round][2]);
						}
						Sendmessage(MS+"§6"+round+" §b라운드가 시작 됐습니다! 적들을 섬멸하세요!");
					}
				}, 30l);
			}
		}, 1400L);
	}
	
	public void BlueRoundWin()
	{
		bluescore++;
		Sendmessage(MS+"§6"+round+"라운드 "+"§7블루팀 승리!");
		Sendmessage(MS+"현재 스코어 §c레드 : "+redscore+" 점 §b블루 : "+bluescore+" 점");
		StartRound();
	}
	
	public void RedRoundWin()
	{
		redscore++;
		Sendmessage(MS+"§6"+round+"라운드 "+"§7레드팀 승리!");
		Sendmessage(MS+"현재 스코어 §c레드 : "+redscore+" 점 §b블루 : "+bluescore+" 점");
		StartRound();
	}
	
	public void Sendteamchat(List<Player> list, String str)
	{
		for(Player p : list)
		{
			p.sendMessage(str);
		}
	}
	
	public void Giveweapon(Player p){
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		pickList.add(p.getName());
		Inventory pinv = p.getInventory();
		switch(Getrandom(1,5)){
		case 1:
			pinv.addItem(new ItemStack(298, 1)); break;
		case 2:
			pinv.addItem(new ItemStack(302, 1)); break;
		case 3:
			pinv.addItem(new ItemStack(314, 1)); break;
		case 4:
			pinv.addItem(new ItemStack(306, 1)); break;
		case 5:
			pinv.addItem(new ItemStack(310, 1)); break;
		default: break;
		}
		switch(Getrandom(1,5)){
		case 1:
			pinv.addItem(new ItemStack(299, 1)); break;
		case 2:
			pinv.addItem(new ItemStack(303, 1)); break;
		case 3:
			pinv.addItem(new ItemStack(315, 1)); break;
		case 4:
			pinv.addItem(new ItemStack(307, 1)); break;
		case 5:
			pinv.addItem(new ItemStack(311, 1)); break;
		default: break;
		}
		switch(Getrandom(1,5)){
		case 1:
			pinv.addItem(new ItemStack(300, 1)); break;
		case 2:
			pinv.addItem(new ItemStack(304, 1)); break;
		case 3:
			pinv.addItem(new ItemStack(316, 1)); break;
		case 4:
			pinv.addItem(new ItemStack(308, 1)); break;
		case 5:
			pinv.addItem(new ItemStack(312, 1)); break;
		default: break;
		}
		switch(Getrandom(1,5)){
		case 1:
			pinv.addItem(new ItemStack(301, 1)); break;
		case 2:
			pinv.addItem(new ItemStack(305, 1)); break;
		case 3:
			pinv.addItem(new ItemStack(317, 1)); break;
		case 4:
			pinv.addItem(new ItemStack(309, 1)); break;
		case 5:
			pinv.addItem(new ItemStack(313, 1)); break;
		default: break;
		}
		switch(Getrandom(1,6)){
		case 1:
			pinv.addItem(new ItemStack(268, 1)); break;
		case 2:
			pinv.addItem(new ItemStack(272, 1)); break;
		case 3:
			pinv.addItem(new ItemStack(283, 1)); break;
		case 4:
			pinv.addItem(new ItemStack(267, 1)); break;
		case 5:
			pinv.addItem(new ItemStack(276, 1)); break;
		case 6:
			pinv.addItem(new ItemStack(261, 1)); pinv.addItem(new ItemStack(262, Getrandom(1, 64))); break;
		default: break;
		}
		p.setLevel(Getrandom(1, 25)*10);
		p.updateInventory();
	}
	
	public static int Getrandom(int min, int max){
	  return (int)(Math.random() * (max-min+1)+min);
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
	public void onRightClick(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(!plist.contains(p)) return;
		if(e.getItem() == null) return;
		if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
		{
			if(p.getItemInHand().getTypeId() == 388)
			{
				if(!check_round_start)
				{
					Giveweapon(p);
					p.getInventory().remove(388);
				}
				else
				{
					p.sendMessage(MS+"게임이 시작된 후에는 뽑으실 수 없습니다.");
				}
			}
			 if(e.getItem().getType() == Material.COMPASS){
				  p.openInventory(gamehelper);
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
	  public void onQuitPlayer(PlayerQuitEvent e) {
	    if (plist.contains(e.getPlayer()))
	    {
	      GameQuit(e.getPlayer());
	    }
	  }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		if(!plist.contains(p))
		{
			return;
		}
		if(check_round_start)
		{
			e.setRespawnLocation(startpos[round][0]);
		}
		else
		{
			e.setRespawnLocation(startpos[0][redlist.contains(p) ? 1 : 2]);
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHit(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			Player d = null;
			if(e.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow) e.getDamager();
				d = (Player) arrow.getShooter();
			} else if(e.getDamager() instanceof Player)
			{
				d = (Player) e.getDamager();
			}
			if(d == null) return;
			if(plist.contains(p) && plist.contains(d))
			{
				if((bluelist.contains(p) && bluelist.contains(d)) || (redlist.contains(p) && redlist.contains(d)))
				{
					e.setCancelled(true);
				}
				else if(deadlist.contains(d.getName()) || deadlist.contains(p.getName()))
				{
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerdeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		if(plist.contains(p) && check_round_start)
		{
	    	e.getDrops().clear();
	    	e.setDroppedExp(0);
			deadlist.add(p.getName());
			p.getInventory().clear();
			if(redlist.contains(p))
			{
				red_death_cnt--;
				Sendmessage(MS+"§c레드팀 §6"+p.getName()+" §f님이 사망 하셨습니다.");
				if(red_death_cnt <= 0)
				{
					BlueRoundWin();
				}
			}
			else
			{
				blue_death_cnt--;
				Sendmessage(MS+"§b블루팀 §6"+p.getName()+" §f님이 사망 하셨습니다.");
				if(blue_death_cnt <= 0)
				{
					RedRoundWin();
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(!plist.contains(e.getPlayer())) return;
		if(!pickList.contains(p.getName())){
			p.sendMessage(MS+"아이템을 뽑기 전(에메랄드 우클릭)까지는 아이템을 버리실 수 없습니다.");
			e.setCancelled(true); 
			return;
		}
		if(e.getItemDrop().getItemStack().getType() == Material.COMPASS) e.setCancelled(true);
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
