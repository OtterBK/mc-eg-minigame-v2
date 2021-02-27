package me.Bokum.BlockHunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
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
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static String MS = "§f[ §aEG §f] ";
	public static Location Lobby;
	public static Location joinpos;
	public static Location startpos[] = new Location[3];
	public static Main instance;
	public static List<Player> backuplist = new ArrayList<Player>();
	public static List<Player> plist = new ArrayList<Player>();
	public static List<Player> blist = new ArrayList<Player>();
	public static List<Player> clist = new ArrayList<Player>();
	public static List<Location> changeloc = new ArrayList<Location>();
	public static List<String> cooldownlist = new ArrayList<String>();
	public static List<Material> blacklist = new ArrayList<Material>();
	public static HashMap<String, Byte> bytelist = new HashMap<String, Byte>();
	public static HashMap<String, Integer> blocklist = new HashMap<String, Integer>();
	public static int tasknum[] = new int[10];
	public static int tasktime[] = new int[10];
	public static int map = 1;
	public static ItemStack helpitem;
	public static ItemStack hint;
	public static Inventory gamehelper;
	public static Economy econ = null;
	public static boolean blocking = false;
	public static boolean game_end = false;
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static int Forcestoptimer = 0;
	
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("블럭 숨바꼭질 플러그인이 로드 되었습니다.");
		
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
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
		
		helpitem = new ItemStack(Material.WATCH, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미를 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		hint = new ItemStack(Material.BOOK, 1);
		meta2 = hint.getItemMeta();
		meta2.setDisplayName("§ef[ §e힌트 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 힌트를 사용합니다.");
		meta1.setLore(lorelist);
		hint.setItemMeta(meta2);
		
		
		blacklist.add(Material.AIR);
		blacklist.add(Material.SAPLING);
		blacklist.add(Material.LADDER);
		blacklist.add(Material.WATER);
		blacklist.add(Material.LAVA);
		blacklist.add(Material.WEB);
		blacklist.add(Material.DEAD_BUSH);
		blacklist.add(Material.LONG_GRASS);
		blacklist.add(Material.YELLOW_FLOWER);
		blacklist.add(Material.RED_ROSE);
		blacklist.add(Material.RED_MUSHROOM);
		blacklist.add(Material.BROWN_MUSHROOM);
		blacklist.add(Material.TORCH);
		blacklist.add(Material.FIRE);
		blacklist.add(Material.WATER);
		blacklist.add(Material.LEVER);
		blacklist.add(Material.REDSTONE_TORCH_OFF);
		blacklist.add(Material.REDSTONE_TORCH_ON);
		blacklist.add(Material.STONE_BUTTON);
		blacklist.add(Material.WOOD_BUTTON);
		blacklist.add(Material.SNOW);
		blacklist.add(Material.WATER);
		blacklist.add(Material.IRON_FENCE);
		blacklist.add(Material.THIN_GLASS);
		blacklist.add(Material.VINE);
		blacklist.add(Material.WATER_LILY);
		blacklist.add(Material.PORTAL);
		blacklist.add(Material.ENDER_PORTAL);
		blacklist.add(Material.ENDER_PORTAL_FRAME);
		blacklist.add(Material.TRIPWIRE_HOOK);
		blacklist.add(Material.HOPPER);
		blacklist.add(Material.BOWL);
		blacklist.add(Material.SEEDS);
		blacklist.add(Material.STRING);
		blacklist.add(Material.STATIONARY_WATER);
		blacklist.add(Material.STATIONARY_LAVA);
		blacklist.add(Material.PAINTING);
		blacklist.add(Material.SIGN);
		blacklist.add(Material.SIGN_POST);
		blacklist.add(Material.CAKE);
		blacklist.add(Material.BED);
		blacklist.add(Material.BED_BLOCK);
		blacklist.add(Material.CAULDRON);
		blacklist.add(Material.CAULDRON_ITEM);
		blacklist.add(Material.ITEM_FRAME);
		blacklist.add(Material.SKULL);
		blacklist.add(Material.WHEAT);
		blacklist.add(Material.SKULL_ITEM);
		blacklist.add(Material.LEAVES);
		blacklist.add(Material.STEP);
		blacklist.add(Material.WOOD_STEP);
		blacklist.add(Material.TRAP_DOOR);
		blacklist.add(Material.WHEAT);
		
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
		getLogger().info("블럭 숨바꼭질 플러그인이 언로드 되었습니다.");
	}
	
	public void Loadconfig()
	{
	  try
	  {
		joinpos = new Location(Bukkit.getWorld(getConfig().getString("Join_world")), getConfig().getInt("Join_x"), getConfig().getInt("Join_y"), getConfig().getInt("Join_z"));
	    Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
	    for(int i = 0; i < 3; i++){
	    	startpos[i] = new Location(Bukkit.getWorld(getConfig().getString("Start_world"+i)), getConfig().getInt("Start_x"+i), getConfig().getInt("Start_y"+i), getConfig().getInt("Start_z"+i));
	    }
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("참여 지점 또는 로비가 설정되어 있지 않습니다");
	  }
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("ebh") && p.isOp())
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
					if(args[0].equalsIgnoreCase("blocking"))
					{
						if(blocking){
							blocking = false;
						}else{
							blocking = true;
						}
						return true;
					}
					if(args[0].equalsIgnoreCase("test")){
						SpawnFireWork(p);
					}
				}
			}
		}
		return false;
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/ebh join");
		p.sendMessage(MS+"/ebh quit");
		p.sendMessage(MS+"/ebh set");
		p.sendMessage(MS+"/ebh reload");
		p.sendMessage(MS+"/ebh inv");
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"블럭 숨바꼭질이 강제 종료 되었습니다.");
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
		for(Player t : backuplist){
			t.showPlayer(t);
		}
		plist.clear();
		blist.clear();
		clist.clear();
		blocklist.clear();
		bytelist.clear();
		game_end = false;
		check_start = false;
		check_lobbystart = false;
	}
	
	public void SpawnFireWork(Player p){
		Firework f = p.getWorld().spawn(p.getLocation(), Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder()
				.flicker(false)
				.trail(true)
				.with(Type.STAR)
				.withColor(Color.GREEN)
				.withFade(Color.BLUE)
				.build());
		fm.setPower(0);
		f.setFireworkMeta(fm);
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/ebh set lobby");
			p.sendMessage(MS+"/ebh set join");
			p.sendMessage(MS+"/ebh set start");
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
			else if(args[1].equalsIgnoreCase("start1"))
			{
			    getConfig().set("Start_world"+0, p.getLocation().getWorld().getName());
			    getConfig().set("Start_x"+0, Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Start_y"+0, Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Start_z"+0, Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"1시작지점 설정 완료");
			}
			else if(args[1].equalsIgnoreCase("start2"))
			{
			    getConfig().set("Start_world"+1, p.getLocation().getWorld().getName());
			    getConfig().set("Start_x"+1, Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Start_y"+1, Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Start_z"+1, Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"2시작지점 설정 완료");
			}
			else if(args[1].equalsIgnoreCase("start3"))
			{
			    getConfig().set("Start_world"+2, p.getLocation().getWorld().getName());
			    getConfig().set("Start_x"+2, Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Start_y"+2, Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Start_z"+2, Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"3시작지점 설정 완료");
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
	
	public void useHint(Player p){
		Removeitem(p, 340, 1);
		Sendmessage(MS+p.getName()+" 님께서 힌트를 사용하십니다.");
		for(Player t : blist){
			SpawnFireWork(t);
		}
	}
	
	public static void GameJoin(Player p)
	{
		if(blocking){
			p.sendMessage(Main.MS+"점검중");
			return;
		}
		if(plist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(plist.size() >= 10)
		{
			p.sendMessage(MS+"이미 최대인원(10)입니다.");
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
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "블럭 숨바꼭질");
			Sendmessage(MS+p.getName()+" 님이 블럭 숨바꼭질에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c5 §f)");
			if(!check_lobbystart && plist.size() >= 5)
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
		if(blist.contains(p)){
			blist.remove(p);
			if(blist.size() <= 0)
			{
				CatcherWin();
			}
		}
		if(clist.contains(p)){
			clist.remove(p);
			if(clist.size() <= 0 && !game_end){
				Sendmessage(Main.MS+"모든 술래가 나가 타이머가 0초가 됩니다.");
				RunnerWin();
			}
		}
		Sendmessage(MS+p.getName()+" §f님이 탈락되셨습니다.");
	}
	
	public static void RunnerWin()
	{
		game_end = true;
		for(Player p : plist){
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		}
		Sendmessage(MS+"시간이 다 되었습니다! 도망자팀의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				for(Player p : blist){
					EconomyResponse r = econ.depositPlayer(p.getName(), 700);
					if (r.transactionSuccess()) {
						p.sendMessage(ChatColor.GOLD + "승리 보상으로 700원을 받으셨습니다.");
					}
					try{
						me.Bokum.EGM.Main.Spawn(p);
						} catch(Exception e){
						p.teleport(Main.Lobby);
						}
				}
				for(Player p : clist){
					EconomyResponse r = econ.depositPlayer(p.getName(), 300);
					if (r.transactionSuccess()) {
						p.sendMessage(ChatColor.GOLD + "패배 보상으로 300원을 받으셨습니다.");
					}
					try{
						me.Bokum.EGM.Main.Spawn(p);
						} catch(Exception e){
						p.teleport(Main.Lobby);
						}
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§a§l블럭 숨바꼭질§f이 §9도망자팀§f의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop();
	    }
	}
	
	public static void CatcherWin()
	{
		game_end = true;
		for(Player p : plist){
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		}
		Sendmessage(MS+"모든 도망자가 잡혔습니다! 술래팀의 승리입니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				for(Player p : clist){
					EconomyResponse r = econ.depositPlayer(p.getName(), 800);
					if (r.transactionSuccess()) {
						p.sendMessage(ChatColor.GOLD + "승리 보상으로 800원을 받으셨습니다.");
					}
					try{
						me.Bokum.EGM.Main.Spawn(p);
						} catch(Exception e){
						p.teleport(Main.Lobby);
						}
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§a§l블럭 숨바꼭질§f이 §9술래팀§f의 승리로 종료 됐습니다.");
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
			  p.sendMessage("§7게임 이름 §f: §c블럭 숨바꼭질");
			  p.sendMessage("§f게임이 무작위로 맵을 정해 이동합니다.");
			  p.sendMessage("§b50초 후§f에는 술래와 도망자가 정해집니다.");
			  p.sendMessage("§f정해진 시간까지 도망자가 살아남으면 승리하게 되고");
			  p.sendMessage("§f정해진 시간내에 §6술래§f가 §2도망자§f를 모두 잡으면 술래가 승리합니다.");
			  p.sendMessage("§c단, 도망자는 Shift를 눌러 자신의 발아래 블럭으로 변신할 수 있습니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f술래는 머리에 잭 오 랜턴을 끼고 있습니다.");
			  p.sendMessage("§f도망자는 모든 블럭으로 변신할 수 있는게 아닙니다.");
			  p.sendMessage("§f정해진 맵 밖으로 나가시면 안됩니다.");
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
			Bukkit.broadcastMessage(MS+"§b§l블럭 숨바꼭질§f이 곧 시작됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendmessage(MS+"게임이 "+tasktime[cur]--*10+" 초 후 시작됩니다.");
					}
					else
					{
						Canceltask(cur);
						check_start = true;
						map = Getrandom(0, 2);
						for(Player p : plist){
							blist.add(p);
							p.getInventory().clear();
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
							p.sendMessage(MS+"게임이 시작 됐습니다.");
							p.teleport(startpos[map]);
							backuplist.add(p);
						}
						CatcherTimer();
						Timer();
					}
				}
			}, 200L, 200L);
		}
		
		public static void CatcherTimer(){
			final int cur = Getcursch();
			tasktime[cur] = 5;
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendmessage(MS+"술래가 "+tasktime[cur]--*10+" 초 후 정해집니다.");
					}
					else
					{
						Canceltask(cur);
						check_start = true;
						if(plist.size() == 0){
							Forcestop(); return;
						}
						Player p = blist.get(Getrandom(0, (blist.size()-1)));
						p.teleport(startpos[map]);
						SetCatcher(p);
						p.getInventory().addItem(hint);
						if(blist.size() >= 6){
							p = blist.get(Getrandom(0, (blist.size()-1)));
							p.teleport(startpos[map]);
							SetCatcher(p);
						p.getInventory().addItem(hint);
						}
					}
				}
			}, 0L, 200L);
		}
		
		public static void SetCatcher(Player p){
			if(blist.contains(p)) blist.remove(p);
			clist.add(p);
			p.sendMessage(Main.MS+"술래가 되셨습니다.\n"+MS+"책 우클릭시 힌트를 사용합니다.");
			p.getInventory().clear();
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0, false));
			for(Player t : clist){
				t.showPlayer(p);
			}
			p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
			p.getInventory().addItem(hint);
			p.getInventory().setHelmet(new ItemStack(Material.JACK_O_LANTERN, 1));
			if(blist.size() <= 0)
			{
				CatcherWin();
			}
		}
		
		public static void Timer(){
			final int cur = Getcursch();
			tasktime[cur] = blist.size()*120+100;
			Sendmessage(Main.MS+"§c§n남은 시간은 레벨에 표시됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(game_end){
						Canceltask(cur);
					}
					if(--tasktime[cur] > 0)
					{
						for(Player p : plist){
							p.setLevel((int)(tasktime[cur]/2));
							for(Location l : changeloc){
								p.sendBlockChange(l, l.getBlock().getTypeId(), l.getBlock().getData());
							}
							for(Player t : blist){
								if(blocklist.containsKey(t.getName()) && !t.getName().equalsIgnoreCase(p.getName())){
									for(Player c : clist){
										c.hidePlayer(t);
									}
									p.sendBlockChange(t.getLocation(), blocklist.get(t.getName()), 
											bytelist.containsKey(t.getName()) ? bytelist.get(t.getName()) : (byte) 0);
								}
							}
						}
						changeloc.clear();
						for(Player t : blist){
							if(blocklist.containsKey(t.getName())){
								changeloc.add(new Location(t.getWorld(),t.getLocation().getBlockX(),
										t.getLocation().getBlockY(), t.getLocation().getBlockZ()));
							}
						}
					}
					else
					{
						Canceltask(cur);
						RunnerWin();
					}
				}
			}, 0L, 10L);
		}
		
		@EventHandler
		public void onRightClick(PlayerInteractEvent e)
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
			{
				final Player p = e.getPlayer();
				if(!plist.contains(p) || e.getItem() == null) return;
				if(e.getItem().getType() == Material.WATCH) p.openInventory(gamehelper);
				if(e.getItem().getType() == Material.BOOK) useHint(p);
			}
			if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(!plist.contains(e.getPlayer()) || e.getClickedBlock() == null) return;
				Block b = e.getClickedBlock();
				Location bl = new Location(b.getWorld(), b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ());
				try{
					for(String s : blocklist.keySet()){
						Player p = null;
						try{
							p = Bukkit.getPlayer(s);
						}catch(Exception excep){
							
						}
						if(p == null) continue;
						Location pl = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
						if(bl.equals(pl)){
							if(blocklist.containsKey(s)){
								blocklist.remove(p.getName());
								bytelist.remove(p.getName());
								p.getInventory().setHelmet(null);
								p.sendMessage(Main.MS+"변신이 풀렸습니다.");
								for(Player t : clist){
									t.showPlayer(p);
								}
								p.damage(1);
							}
						}
					}
				}catch(Exception exception){
						
				}
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
				 if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.JACK_O_LANTERN){
					 e.setCancelled(true);
					 p.updateInventory();
				 }
				 if(blocklist.containsKey(p.getName())){
					 e.setCancelled(true);
					 p.updateInventory();
				 }
			}
		}
		
		@EventHandler
		public void onPlayerDamage(EntityDamageEvent e){
			if(!(e.getEntity() instanceof Player)) return;
			if(plist.contains((Player) e.getEntity()) && (!check_start || game_end)) e.setCancelled(true);
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e){
			if(!plist.contains(e.getPlayer())) return;
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onPlayercommand(PlayerCommandPreprocessEvent e)
		{
			Player p = e.getPlayer();
			if(plist.contains(p) && e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
					|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ"))
			{
				GameQuit(p);
			}
		}
		
		@EventHandler
		public void onToggleSneak(PlayerToggleSneakEvent e){
			  if(plist.contains(e.getPlayer())){
				  final Player p = e.getPlayer();
				  /*if(blist.contains(p) && !p.isSneaking()){
					  Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
						  public void run(){
							  p.setExp(0.33f);
							  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
							  p.sendMessage(Main.MS+"3초 후 변신됩니다.");
						  }
					  }, 0l);
					  Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
						  public void run(){
							  p.setExp(0.33f);
							  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
							  p.sendMessage(Main.MS+"3초 후 변신됩니다.");
						  }
					  }, 20l);
					  Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
						  public void run(){
							  p.setExp(0.33f);
							  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
							  p.sendMessage(Main.MS+"3초 후 변신됩니다.");
						  }
					  }, 40l);*/
				  if(clist.contains(p) || !check_start) return;
				  if(!p.isSneaking()){
						  Location l = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY()-1
								  ,p.getLocation().getBlockZ());
						  if(!blacklist.contains(l.getBlock().getType())){
							  blocklist.put(p.getName(), l.getBlock().getTypeId());
							  bytelist.put(p.getName(), l.getBlock().getData());
							  p.getInventory().setHelmet(new ItemStack(l.getBlock().getType(), 1));
							  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
							  p.sendMessage(Main.MS+blocklist.get(p.getName())+":"+bytelist.get(p.getName())+" 코드 블럭으로 변신했습니다.");
							  p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
						  }else{
							  p.sendMessage(Main.MS+"이 블럭으로는 변신하실 수 없습니다.");
						  }
				  }else{
						if(blocklist.containsKey(p.getName())){
							blocklist.remove(p.getName());
							bytelist.remove(p.getName());
							p.getInventory().setHelmet(null);
							p.sendMessage(Main.MS+"변신이 풀렸습니다.");
							for(Player t : clist){
								t.showPlayer(p);
							}
						}
				 // }
				  }
			  }
		}
		
		@EventHandler
		public void onBlockbreak(BlockBreakEvent e){
			  if(plist.contains(e.getPlayer())){
				  e.setCancelled(true);
			  }
		}
		
		@EventHandler
		public void onRespawn(PlayerRespawnEvent e){
			if(plist.contains(e.getPlayer())){
				final Player p = e.getPlayer();
				e.setRespawnLocation(startpos[map]);
				if(blist.contains(p) && clist.size() >= 1){
					Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
						public void run(){
							SetCatcher(p);
						}
					}, 10l);
				}
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
				if(!plist.contains(p)) return;
				e.getDrops().clear();
				e.setDroppedExp(0);
				if(!blist.contains(p)){
					GameQuit(p);
				}
			}

			@EventHandler
			public void onPvp(EntityDamageByEntityEvent e){
				if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
					Player p = (Player) e.getEntity();
					Player d = (Player) e.getDamager();
					if(!plist.contains(p)) return;
					if((clist.contains(p) && clist.contains(d)) || (blist.contains(p) && blist.contains(d))){
						e.setCancelled(true);return;
					}
				}
			}
}

