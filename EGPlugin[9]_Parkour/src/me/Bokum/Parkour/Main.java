package me.Bokum.Parkour;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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
	public static Location joinpos_race;
	public static Location joinpos_bed;
	public static Location joinpos_pat;
	public static Main instance;
	public static List<Player> plist = new ArrayList<Player>();
	public static List<Player> tlist = new ArrayList<Player>();
	public static List<Player> blist = new ArrayList<Player>();
	public static List<Player> patlist = new ArrayList<Player>();
	public static List<Player> pat_police = new ArrayList<Player>();
	public static List<Player> pat_theif = new ArrayList<Player>();
	public static Location startpos_race;
	public static Location startpos_pat;
	public static Location training_pos;
	public static Location practice_pos;
	public static Location[] startpos_bed = new Location[20];
	public static Location checkpoint[] = new Location[10];
	public static Location pat_chest_pos[] = new Location[40];
	public static int tasknum[] = new int[15];
	public static int tasktime[] = new int[15];
	public static int endtimernum = 0;
	public static int endtimertime = 0;
	public static ItemStack helpitem;
	public static ItemStack pat_stunstick;
	public static ItemStack pat_stungun;
	public static ItemStack pat_invisible;
	public static ItemStack pumpkin;
	public static ItemStack clock;
	public static ItemStack posinfo;
	public static ItemStack seerank;
	public static Inventory gamehelper;
	public static Inventory gamehelper2;
	public static Inventory passwordinven;
	public static Inventory infoinven;
	public static Inventory shopinven;
	public static String Firstname = null;
	public static String Secondname = null;
	public static String Thirdname = null;
	public static double Firsttime = 0;
	public static double Secondtime = 0;
	public static double Thirdtime = 0;
	public static List<Location> pumpkinclearlist = new ArrayList<Location>();
	public static HashMap<String, String> insertingpassword = new HashMap<String, String>();
	public static HashMap<String, ItemStack[]> deathinven = new HashMap<String, ItemStack[]>();
	public static HashMap<String, Location> pumpkin_loc = new HashMap<String, Location>();
	public static HashMap<String, String> password = new HashMap<String, String>();
	public static HashMap<String, Player> solvingtarget = new HashMap<String, Player>();
	public static HashMap<String, Integer> cplist = new HashMap<String, Integer>();
	public static HashMap<String, Integer> deathcount = new HashMap<String, Integer>();
	public static HashMap<String, Double> time = new HashMap<String, Double>();
	public static List<Location> blockloc = new ArrayList<Location>();
	public static List<String> jumpinglist = new ArrayList<String>();
	public static HashMap<String, Integer> climbinglist = new HashMap<String, Integer>();
	public static Economy econ = null;
	public static ItemStack chestitem[] = new ItemStack[36];
	public static boolean check_start_race = false;
	public static boolean pvp = false;
	public static boolean check_lobbystart_race = false;
	public static boolean check_start_pat = false;
	public static boolean check_lobbystart_pat = false;
	public static boolean check_start_bed = false;
	public static boolean check_lobbystart_bed = false;
	public static boolean check_raceend = false;
	public static List<String> rank_name = new ArrayList<String>();
	public static List<Double> rank_time = new ArrayList<Double>();
	public static List<Location> chestlist = new ArrayList<Location>();
	public static int Forcestoptimer_race = 0;
	public static int Forcestoptimer_bed = 0;
	
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("파쿠르 플러그인이 로드 되었습니다.");
		
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
		
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e랭킹보기");
		item.setItemMeta(meta2);
		gamehelper.setItem(15, item);
		
		gamehelper2 = Bukkit.createInventory(null, 27, "§c§l도우미");
		
		item = new ItemStack(34, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§6장식");
		item.setItemMeta(meta2);
		for(int i = 0; i <= 9; i++){
			gamehelper2.setItem(i, item);
		}
		for(int i = 17; i < 27; i++){
			gamehelper2.setItem(i, item);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e플레이 방법");
		item.setItemMeta(meta2);
		gamehelper2.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e게임 규칙");
		item.setItemMeta(meta2);
		gamehelper2.setItem(13, item);
		
		List<String> lorelist = new ArrayList<String>();
		
		helpitem = new ItemStack(345, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		clock = new ItemStack(347, 1);
		meta1 = clock.getItemMeta();
		meta1.setDisplayName("§f[ §6정보 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 가진 정보를 엽니다.");
		meta1.setLore(lorelist);
		clock.setItemMeta(meta1);
		
		seerank = new ItemStack(371, 1);
		meta1 = seerank.getItemMeta();
		meta1.setDisplayName("§f[ §6랭킹보기 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시레이싱 랭킹을 봅니다.");
		meta1.setLore(lorelist);
		seerank.setItemMeta(meta1);
		
		posinfo = new ItemStack(402, 1);
		meta1 = clock.getItemMeta();
		meta1.setDisplayName("§f[ §6자신의 호박 좌표 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 자신의 호박좌표를 봅니다.");
		meta1.setLore(lorelist);
		posinfo.setItemMeta(meta1);
		
		pumpkin = new ItemStack(91, 1);
		meta1 = pumpkin.getItemMeta();
		meta1.setDisplayName(MS+"설치하세요.");
		lorelist.clear();
		lorelist.add("§f- §7설치한 장소를 자신의 기지로 설정합니다.");
		lorelist.add("§f- §7설치후에는 호박을 클릭하여 상점을 이용하실 수 있습니다.");
		lorelist.add("§f- §7설치후에는 비밀번호를 설정하게됩니다.");
		meta1.setLore(lorelist);
		pumpkin.setItemMeta(meta1);
		
		passwordinven = Bukkit.createInventory(null, 45, "§c§l비밀번호");
		
		item = new ItemStack(34, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§6장식");
		item.setItemMeta(meta2);
		for(int i = 0; i <= 9; i++){
			passwordinven.setItem(i, item);
		}
		for(int i = 36; i <= 44; i++){
			passwordinven.setItem(i, item);
		}
		passwordinven.setItem(18, item);
		passwordinven.setItem(27, item);
		passwordinven.setItem(17, item);
		passwordinven.setItem(26, item);
		passwordinven.setItem(35, item);
		passwordinven.setItem(13, item);
		passwordinven.setItem(22, item);
		passwordinven.setItem(31, item);
		
		int amt = 1;
		
		for(int i = 10; i < 36; i += 9){
			for(int j = 0; j < 3; j++){
				item = new ItemStack(131, 1);
				meta2 = item.getItemMeta();
				meta2.setDisplayName("§b§l"+amt);
				item.setItemMeta(meta2);
				amt++;
				
				passwordinven.setItem(i+j, item);
			}
		}
		
		item = new ItemStack(131, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§b§l0");
		item.setItemMeta(meta2);
		
		passwordinven.setItem(38, item);

		item = new ItemStack(Material.WOOL, 1, (byte) 14);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§b§l다시 입력");
		item.setItemMeta(meta2);
		
		passwordinven.setItem(15, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte) 5);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§b§l확인");
		item.setItemMeta(meta2);
		
		passwordinven.setItem(33, item);
		
		infoinven = Bukkit.createInventory(null, 18, "§c§l정보");
		
		shopinven = Bukkit.createInventory(null, 36, "§c§l상점");
		
		chestitem[0] = Makeitem(69, "쇠파이프", 3, 3, 0, 14);
		chestitem[1] = Makeitem(359, "가위", 3, 4, 0, 18);
		chestitem[2] = Makeitem(369, "금속배트", 4, 4, 0, 22);
		chestitem[3] = Makeitem(75, "못박힌 배트", 4, 5, 0, 26);
		chestitem[4] = Makeitem(268, "낡은 검", 5, 5, 0, 30);
		chestitem[5] = Makeitem(272, "식칼", 5, 6, 0, 35);
		chestitem[6] = Makeitem(287, "채찍", 6, 6, 0, 40);
		chestitem[7] = Makeitem(283, "총검", 6, 7, 0, 45);
		chestitem[8] = Makeitem(292, "빠루", 7, 7, 0, 50);
		chestitem[9] = Makeitem(377, "너클", 7, 8, 0, 56);
		chestitem[10] = Makeitem(267, "카타나", 8, 8, 0, 62);
		chestitem[11] = Makeitem(276, "마체테", 8, 9, 0, 68);
		chestitem[12] = Makeitem(279, "해머", 9, 9, 0, 74);
		chestitem[13] = Makeitem(261, "활", 0, 0, 0, 75);
		chestitem[14] = Makeitem(262, "화살", 0, 0, 0, 1);
		chestitem[15] = Makeitem(261, "활", 0, 0, 0, 75);
		chestitem[16] = Makeitem(298, "가죽 모자", 0, 0, 0, 10);
		chestitem[17] = Makeitem(299, "가죽 튜닉", 0, 0, 0, 14);
		chestitem[18] = Makeitem(373, "회복 포션", 0, 0, 8197, 3);
		chestitem[19] = Makeitem(373, "회복 포션 2단계", 0, 0, 8229, 5);
		chestitem[20] = Makeitem(373, "회복 포션 투척용", 0, 0, 16389, 5);
		chestitem[21] = Makeitem(373, "회복 포션 투척용 2단계", 0, 0, 16421, 8);
		chestitem[22] = Makeitem(373, "독 포션", 0, 0, 16388, 13);
		chestitem[23] = Makeitem(373, "고통의 포션", 0, 0, 16396, 11);
		chestitem[24] = Makeitem(370, "아드레날린", 0, 0, 0, 4);
		item = new ItemStack(266, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§a도박");
		lorelist.clear();
		lorelist.add("§f- §7코인 10개를 소모해 1~19개중 랜덤으로 얻습니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		chestitem[25] = item;
		item = new ItemStack(339, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§a정보 갱신");
		lorelist.clear();
		lorelist.add("§f- §7에메랄드 100개를 소비하여 모든 플레이어의 ");
		lorelist.add("§f- §7정보를 추가로 얻습니다. ( 모든 플레이어에게 적용 )");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		chestitem[26] = item;
		chestitem[27] = Makeitem(300, "가죽 바지", 0, 0, 0, 13);
		chestitem[28] = Makeitem(301, "가죽 장화", 0, 0, 0, 10);
		chestitem[29] = Makeitem(302, "사슬 모자", 0, 0, 0, 20);
		chestitem[30] = Makeitem(303, "사슬 갑옷", 0, 0, 0, 27);
		chestitem[31] = Makeitem(304, "사슬 바지", 0, 0, 0, 24);
		chestitem[32] = Makeitem(305, "사슬 부츠", 0, 0, 0, 20);
		
		for(int i = 0; i < 33; i++){
			shopinven.setItem(i, chestitem[i]);
		}
		
		for(int i = 0; i < 10; i++){
			rank_name.add(me.Bokum.EGM.Main.getRank("Parkour").getString(("rank_name_"+i)));
		}
		
		for(int i = 0; i < 10; i++){
			rank_time.add(me.Bokum.EGM.Main.getRank("Parkour").getDouble(("rank_time_"+i)));
		}
		
		Timer();
		
		Loadconfig();
		
        if (!setupEconomy() ) {
            getLogger().info("[ 버그 발생 우려 ] Vault플러그인이 인식되지 않았습니다!");
        }
	}
	
	public static ItemStack coin(int amt){
		ItemStack item = new ItemStack(388, amt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a코인 §f]");
		item.setItemMeta(meta);
		return item;
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
    
	public void onDisable()
	{
		getLogger().info("파쿠르 플러그인이 언로드 되었습니다.");
	}
	
	  public static ItemStack Makeitem(int id, String name, int min, int max, int shortnum, int price){
		  ItemStack tmpitem = null;
		  if(shortnum == 0){
			 tmpitem = new ItemStack(id, 1);
		  } else {
				 tmpitem = new ItemStack(id, 1, (short) shortnum);
		  }
		  ItemMeta meta = tmpitem.getItemMeta();
		  meta.setDisplayName("§c"+name);
		  List<String> lorelist = new ArrayList<String>();
		  if(min != 0 && max != 0){
			  String damage = "§7공격력: ";
			  if(max == min){
				  damage += String.valueOf(min);
			  } else {
				  damage += min+"~";
				  damage += max;
			  }
			  lorelist.add(damage);
		  } else {
			  lorelist.add("§7무기의 기본 데미지");
		  }

		  lorelist.add("§c가격 §f:§6 "+price);
		  meta.setLore(lorelist);
		  tmpitem.setItemMeta(meta);
		  return tmpitem;
	  }

	public void Loadconfig()
	{
	  try
	  {
		  training_pos = new Location(Bukkit.getWorld(getConfig().getString("training_world")), getConfig().getInt("training_x"), getConfig().getInt("training_y"), getConfig().getInt("training_z"));
		  practice_pos = new Location(Bukkit.getWorld(getConfig().getString("practice_world")), getConfig().getInt("practice_x"), getConfig().getInt("practice_y"), getConfig().getInt("practice_z"));
		joinpos_race = new Location(Bukkit.getWorld(getConfig().getString("Join_world_race")), getConfig().getInt("Join_x_race"), getConfig().getInt("Join_y_race"), getConfig().getInt("Join_z_race"));
		joinpos_bed = new Location(Bukkit.getWorld(getConfig().getString("Join_world_bed")), getConfig().getInt("Join_x_bed"), getConfig().getInt("Join_y_bed"), getConfig().getInt("Join_z_bed"));
	    Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
	    startpos_race = new Location(Bukkit.getWorld(getConfig().getString("start_worldrace")), getConfig().getInt("start_xrace"), getConfig().getInt("start_yrace"), getConfig().getInt("start_zrace"));
	    for(int i = 1; i <= 20; i++)
	    startpos_bed[i-1] = new Location(Bukkit.getWorld(getConfig().getString("start_worldbed"+i)), getConfig().getInt("start_xbed"+i), getConfig().getInt("start_ybed"+i), getConfig().getInt("start_zbed"+i));
	  }
	  catch (IllegalArgumentException e)
	  {
	    getLogger().info("참여 지점 또는 로비, 시작지점이 설정되어 있지 않습니다");
	  }
		ConfigurationSection sec = getConfig();
		try
		{
			for(int j = 1; j <= 10; j++){
				  checkpoint[j-1]= new Location(Bukkit.getWorld(sec.getString("checkpoint_world"+j)), sec.getInt("checkpoint_x"+j), sec.getInt("checkpoint_y"+j), sec.getInt("checkpoint_z"+j));
			}
		}
		catch (Exception e)
		{
			 getLogger().info("시작 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
		try
		{
			for(int j = 1; j <= getConfig().getInt("blockamt"); j++){
				  blockloc.add(new Location(getServer().getWorld("world"), getConfig().getInt("block_loc_"+j+"_x"), getConfig().getInt("block_loc_"+j+"_y"), getConfig().getInt("block_loc_"+j+"_z")));
			}
		}
		catch (Exception e)
		{
			 getLogger().info("블럭 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
		try
		{
			for(int j = 1; j <= 40; j++){
				  pat_chest_pos[j-1]= new Location(Bukkit.getWorld(sec.getString("chestpos_world"+j)), sec.getInt("chestpos_x"+j), sec.getInt("chestpos_y"+j), sec.getInt("chestpos_z"+j));
			}
		}
		catch (Exception e)
		{
			 getLogger().info("pat상자 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
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
					if(block_loc.getBlock().getType() == Material.IRON_FENCE){
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
		for(Location loc : blockloc){
			loc.getBlock().setType(Material.IRON_FENCE);
		}
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("pkr"))
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
						GameJoinrace(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("bedjoin"))
					{
						GameJoinbed(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("quit"))
					{
						GameQuitrace(p);
						return true;
					}
					if(args[0].equalsIgnoreCase("stop"))
					{
						if(args.length <= 1){
							p.sendMessage(MS+"/pkr stop bed/race");
							return true;
						}
						if(args[1].equalsIgnoreCase("race")){
							Forcestop_race();
						} else if(args[1].equalsIgnoreCase("bed")){
							Forcestop_bed();
						}
						return true;
					}
					if(args[0].equalsIgnoreCase("block"))
					{
						SaveBlock(p, args[1], args[2], args[3], args[4], args[5], args[6]);
						return true;
					}
					if(args[0].equalsIgnoreCase("test"))
					{
						p.teleport(training_pos);
					}
				}
			}
		}
		return false;
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/pkr join");
		p.sendMessage(MS+"/pkr quit");
		p.sendMessage(MS+"/pkr set");
		p.sendMessage(MS+"/pkr reload");
		p.sendMessage(MS+"/pkr inv");
	}
	
	public static void Forcestop_race()
	{
		Bukkit.broadcastMessage(MS+"파쿠르 레이싱이 강제 종료 되었습니다.");
		for(Player p : tlist)
		{
	          try{
	        	  me.Bokum.EGM.Main.Spawn(p);
	          } catch(Exception e){
	        	  p.teleport(Main.Lobby);
	          }
		}
		Cleardata_race();
	}
	
	public static void Forcestop_bed()
	{
		Bukkit.broadcastMessage(MS+"파쿠르 호박전쟁이 강제 종료 되었습니다.");
		for(Player p : blist)
		{
	          try{
	        	  me.Bokum.EGM.Main.Spawn(p);
	          } catch(Exception e){
	        	  p.teleport(Main.Lobby);
	          }
		}
		Cleardata_bed();
	}
	
	public static void Cleardata_race()
	{
		Bukkit.getScheduler().cancelTask(endtimernum);
		Forcestoptimer_race = 0;
		for(Player t : tlist){
			plist.remove(t);
		}
		check_raceend = false;
		tlist.clear();
		cplist.clear();
		Firstname = null;
		Firsttime = 0;
		Secondname = null;
		Secondtime = 0;
		Thirdname = null;
		Thirdtime = 0;
		check_start_race = false;
		check_lobbystart_race = false;
	}
	
	public static void Cleardata_bed()
	{
		for(Player t : blist){
			plist.remove(t);
		}
		Forcestoptimer_bed = 0;
		check_start_bed = false;
		check_lobbystart_bed = false;
		blist.clear();
		pvp = false;
		chestlist.clear();
		insertingpassword.clear();
		for(Location l : pumpkinclearlist){
			l.getBlock().setType(Material.AIR);
		}
		pumpkinclearlist.clear();
		pumpkin_loc.clear();
		chestlist.clear();
		password.clear();
		solvingtarget.clear();
		deathcount.clear();
		check_start_bed = false;
		check_lobbystart_bed = false;
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/pkr set race/bed lobby");
			p.sendMessage(MS+"/pkr set race/bed join");
			p.sendMessage(MS+"/pkr set race/bed start 1~15");
			p.sendMessage(MS+"/pkr set cp 1~10");
			return;
		}
		if(args.length <= 2)
		{
			p.sendMessage(MS+"/pkr set race/bed lobby");
			p.sendMessage(MS+"/pkr set race/bed join");
			p.sendMessage(MS+"/pkr set race/bed start 1~15");
			if(args[1].equalsIgnoreCase("training"))
			{
				p.sendMessage(MS+"트레이닝룸 설정완료");
				getConfig().set("training_world", p.getWorld().getName());
				getConfig().set("training_x", p.getLocation().getBlockX());
				getConfig().set("training_y", p.getLocation().getBlockY()+1);
				getConfig().set("training_z", p.getLocation().getBlockZ());
				saveConfig();
				Loadconfig();
			}
			if(args[1].equalsIgnoreCase("practice"))
			{
				p.sendMessage(MS+"실전연습장소 설정완료");
				getConfig().set("practice_world", p.getWorld().getName());
				getConfig().set("practice_x", p.getLocation().getBlockX());
				getConfig().set("practice_y", p.getLocation().getBlockY()+1);
				getConfig().set("practice_z", p.getLocation().getBlockZ());
				saveConfig();
				Loadconfig();
			}
			return;
		}
		if(args.length >= 3)
		{
			if(args[1].equalsIgnoreCase("cp"))
			{
					int i = Integer.valueOf(args[2]);
					if(!(1 <= i && i <= 10))
					{
						p.sendMessage("1~10의 숫자만 입력 가능합니다.");
						return;
					}
						getConfig().set("checkpoint_world"+i, p.getWorld().getName());
						getConfig().set("checkpoint_x"+i, p.getLocation().getBlockX());
						getConfig().set("checkpoint_y"+i, p.getLocation().getBlockY()-1);
						getConfig().set("checkpoint_z"+i, p.getLocation().getBlockZ());
					    saveConfig();
					    Loadconfig();
						p.sendMessage(MS+i+"번째 체크포인트 지점 설정 완료");
						return;
			}
			if(!args[1].equalsIgnoreCase("bed") && !args[1].equalsIgnoreCase("race")) return;
			String gamename = args[1];
			if(args[2].equalsIgnoreCase("lobby"))
			{
			    getConfig().set("Lobby_world", p.getLocation().getWorld().getName());
			    getConfig().set("Lobby_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Lobby_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Lobby_z", Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"로비 설정 완료");
			}
			else if(args[2].equalsIgnoreCase("join"))
			{
			    getConfig().set("Join_world_"+gamename, p.getLocation().getWorld().getName());
			    getConfig().set("Join_x_"+gamename, Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Join_y_"+gamename, Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Join_z_"+gamename, Integer.valueOf(p.getLocation().getBlockZ()));
			    saveConfig();
			    Loadconfig();
				p.sendMessage(MS+"대기실 설정 완료");
			}
			else if(args[2].equalsIgnoreCase("start"))
			{
				if(gamename.equalsIgnoreCase("race")){
			    getConfig().set("start_world"+gamename, p.getLocation().getWorld().getName());
			    getConfig().set("start_x"+gamename, Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("start_y"+gamename, Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("start_z"+gamename, Integer.valueOf(p.getLocation().getBlockZ()));
				p.sendMessage(MS+"시작지점 설정 완료");
				} else if(gamename.equals("bed")){
					if(args.length <= 3) return;
				    getConfig().set("start_world"+gamename+args[3], p.getLocation().getWorld().getName());
				    getConfig().set("start_x"+gamename+args[3], Integer.valueOf(p.getLocation().getBlockX()));
				    getConfig().set("start_y"+gamename+args[3], Integer.valueOf(p.getLocation().getBlockY() + 1));
				    getConfig().set("start_z"+gamename+args[3], Integer.valueOf(p.getLocation().getBlockZ()));
					p.sendMessage(MS+args[3]+"번째 시작 지점 설정 완료(최대 20)");
				}
			    saveConfig();
			    Loadconfig();
			}
			else
			{
				p.sendMessage(MS+"/pkr set race/bed lobby");
				p.sendMessage(MS+"/pkr set race/bed join");
				p.sendMessage(MS+"/pkr set race/bed start 1~15");
				p.sendMessage(MS+"/pkr set cp 1~10");
			}
			return;
		}
	}
	
	public static void Sendpatmessage(String str)
	{
		for(Player p : patlist)
		{
			p.sendMessage(str);
		}
	}
	
	public static void Sendracemessage(String str)
	{
		for(Player p : tlist)
		{
			p.sendMessage(str);
		}
	}
	
	public static void Sendbedmessage(String str)
	{
		for(Player p : blist)
		{
			p.sendMessage(str);
		}
	}
	
	public static void GameJoinrace(Player p)
	{
		if(tlist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(tlist.size() >= 10)
		{
			p.sendMessage(MS+"이미 최대인원(10)입니다.");
			return;
		}
		if(check_start_race)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다.");
			return;
		}
		else
		{
			tlist.add(p);
			plist.add(p);
			p.teleport(joinpos_race);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "파쿠르 레이싱");
			Sendracemessage(MS+p.getName()+" 님이 파쿠르 레이싱에 참여하셨습니다. 인원 (§e "+tlist.size()+"§7 / §c1 §f)");
			p.sendMessage(MS+"§c§l나침반 우클릭 -> 랭킹보기 §7로 랭킹을 보실 수 있습니다.");
			if(!check_lobbystart_race && tlist.size() >= 1)
			{
				Startgamerace();
				RestoreBlock();
			}
			for(Player t : tlist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
	}
	
	public static void GameJoinbed(Player p)
	{
		if(blist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(blist.size() >= 12)
		{
			p.sendMessage(MS+"이미 최대인원(12)입니다.");
			return;
		}
		if(check_start_bed)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다.");
			return;
		}
		else
		{
			blist.add(p);
			plist.add(p);
			p.teleport(joinpos_bed);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "파쿠르 호박전쟁");
			Sendbedmessage(MS+p.getName()+" 님이 파쿠르 호박전쟁에 참여하셨습니다. 인원 (§e "+blist.size()+"§7 / §c7 §f)");
			if(!check_lobbystart_bed && blist.size() >= 7)
			{
				Startgamebed();
			}
			for(Player t : blist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
	}
	
	public static void GameJoinPAT(Player p)
	{
		if(patlist.contains(p))
		{
			p.sendMessage(MS+"이미 게임에 참여중이십니다.");
			return;
		}
		if(patlist.size() >= 12)
		{
			p.sendMessage(MS+"이미 최대인원(12)입니다.");
			return;
		}
		if(check_start_pat)
		{
			p.sendMessage(MS+"이미 게임이 진행중입니다.");
			return;
		}
		else
		{
			patlist.add(p);
			plist.add(p);
			p.teleport(joinpos_pat);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			p.getInventory().setItem(8, helpitem);
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "경찰과 도둑");
			Sendpatmessage(MS+p.getName()+" 님이 파쿠르 경찰과 도둑에 참여하셨습니다. 인원 (§e "+tlist.size()+"§7 / §c8 §f)");
			p.sendMessage(MS+"§c§l나침반 우클릭 -> 랭킹보기 §7로 랭킹을 보실 수 있습니다.");
			if(!check_lobbystart_race && tlist.size() >= 8)
			{
				Startgamepat();
			}
			for(Player t : patlist){
				t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
			}
		}
	}
	
	public void GameQuitrace(Player p)
	{
		if(!tlist.contains(p) && !blist.contains(p))
		{
			return;
		}
		tlist.remove(p);
		plist.remove(p);
		if(!check_start_race)
		{
			return;
		}
		Sendracemessage(MS+p.getName()+" §f님이 퇴장하셨습니다.");
		if(tlist.size() <= 0)
		{
			Bukkit.broadcastMessage(MS+"파쿠르 레이싱이 종료 되었습니다.");
			Cleardata_race();
		}
	}
	
	public void GameQuitpat(Player p)
	{
		if(!patlist.contains(p))
		{
			return;
		}
		patlist.remove(p);
		plist.remove(p);
		if(!check_start_pat)
		{
			return;
		}
		Sendpatmessage(MS+p.getName()+" §f님이 퇴장하셨습니다.");
		if(patlist.size() <= 0)
		{
			Bukkit.broadcastMessage(MS+"파쿠르 경찰과 도둑이 종료 되었습니다.");
			//Cleardata_pat();
		}
	}
	
	public static void TrainingJoin(Player p){
		p.getInventory().clear();
		p.getInventory().addItem(seerank);
		p.sendMessage(MS+"트레이닝룸으로 이동합니다.");
		p.teleport(training_pos);
		plist.add(p);
	}
	
	public static void PracticeJoin(Player p){
		p.getInventory().clear();
		p.getInventory().addItem(seerank);
		p.sendMessage(MS+"실전 연습맵으로 이동합니다.");
		p.teleport(practice_pos);
		plist.add(p);
	}
	
	public static void GameQuitbed(Player p)
	{
		if(!tlist.contains(p) && !blist.contains(p))
		{
			return;
		}
		blist.remove(p);
		plist.remove(p);
		if(!check_start_bed)
		{
			return;
		}
		if(pumpkin_loc.containsKey(p.getName())){
			try{
			pumpkin_loc.get(p.getName()).getBlock().setType(Material.AIR);
			}catch(Exception e){
				
			}
		}
		insertingpassword.remove(p.getName());
		pumpkin_loc.remove(p.getName());
		password.remove(p.getName());
		solvingtarget.remove(p.getName());
		deathcount.remove(p.getName());
		Sendbedmessage(MS+p.getName()+" §f님이 탈락하셨습니다.");
		UpdateInfo();
		if(blist.size() == 1)
		{
			try{
			Winbed(blist.get(0));
			}catch(Exception e){
				Forcestop_bed();
			}
		}
	}
	
	public static void Winrace(final Player p)
	{
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		Sendracemessage(MS+p.getName()+"님이 승리했습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = econ.depositPlayer(p.getName(), 1500);
				if (r.transactionSuccess()) {
					p.sendMessage(ChatColor.GOLD + "승리 보상으로 1500원을 받으셨습니다.");
				}
				for(Player t : tlist){
					EconomyResponse r1 = econ.depositPlayer(t.getName(), 800);
					if (r1.transactionSuccess()) {
						t.sendMessage(ChatColor.GOLD + "플레이 보상으로 800원을 받으셨습니다.");
					}
					try{
						me.Bokum.EGM.Main.Spawn(t);
						} catch(Exception e){
						t.teleport(Main.Lobby);
						}
				}
				Cleardata_race();
				Bukkit.broadcastMessage(MS+"§b§l파쿠르 레이싱§f이 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop_race();
	    }
	}
	
	public static void Winbed(final Player p)
	{
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		if(pumpkin_loc.containsKey(p.getName())){
			try{
			pumpkin_loc.get(p.getName()).getBlock().setType(Material.AIR);
			}catch(Exception e){
				
			}
		}
		Sendbedmessage(MS+p.getName()+"님이 승리했습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = econ.depositPlayer(p.getName(), 1500);
				p.sendMessage(ChatColor.GOLD + "승리 보상으로 1500원을 받으셨습니다.");
				me.Bokum.EGM.Main.Spawn(p);
				Cleardata_bed();
				Bukkit.broadcastMessage(MS+"§6§l파쿠르 호박전쟁§f이 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
			}
				}, 140L);
		}catch(Exception e){
	    	Forcestop_race();
	    }
	}
	
	public static int Getrandom(int min, int max){
		  return (int)(Math.random() * (max-min+1)+min);
		}

	  public void GameHelperrace(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c파쿠르 레이싱");
			  p.sendMessage("§f도시를 자유롭게 거닐며 골까지 가야합니다.");
			  p.sendMessage("§f다양한 행동들 ( 벽차기, 난간타기, 멀리뛰기 등등)을");
			  p.sendMessage("§f이용하여 골을 노려보세요!");
			  p.sendMessage("§c대기실에서 튜토리얼을 해보세요!");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f꼭 트레이닝룸에서 기본동작을 배우세요!");
			  p.sendMessage("§f보상은 1등, 2등, 3등에게만 주어집니다.");
			  p.sendMessage("§f높은 기록을 세울시 랭킹에 오릅니다.");
			  p.closeInventory();
			  return;
			  
		  case 15:
			  showRank(p);
			  p.closeInventory();
			  return;
			  
		  default: return;
		  }  
	  }
	  
	  public static void GameHelperbed(Player p, int slot){
		  switch(slot){
		  case 11:
			  p.sendMessage("§7게임 이름 §f: §c파쿠르 호박전쟁");
			  p.sendMessage("§f게임이 시작되면 §e호박§f과 시계를 받습니다.");
			  p.sendMessage("§f호박을 설치하면 §a비밀번호§f를 입력하는 창이 뜨며");
			  p.sendMessage("§f원하는 비밀번호를 설정해야합니다.");
			  p.sendMessage("§f비밀번호를 설정하신 후 호박을 다시 클릭하시면");
			  p.sendMessage("§f상점을 여실 수 있습니다. 또한 다른 플레이어의");
			  p.sendMessage("§f호박을 클릭하면 그 플레이어가 설정한 비밀번호를 입력해야하며 ");
			  p.sendMessage("§f옳바른 비밀번호를 입력하면 그 플레이어를 탈락시킬 수 있습니다.");
			  p.sendMessage("§f이렇게 최후의 1인이 되면 승리합니다.");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f동맹은 2명까지 가능합니다.");
			  p.sendMessage("§f플레이어가 죽을때마다 그 플레이어의 비밀번호 1자리를");
			  p.sendMessage("§f알 수 있습니다. 또한 맵에 있는 상자를 클릭하면");
			  p.sendMessage("§f1~4개의 코인 중 랜덤으로 얻으실 수 있으며");
			  p.sendMessage("§f플레이어를 사살시 10개, 플레이어를 탈락시킬시");
			  p.sendMessage("§f50개의 코인을 얻으실 수 있습니다.");
			  p.sendMessage("§f상자 재파밍의 쿨타임은 1분입니다.");
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
		
		public static void Startgamerace()
		{
			check_lobbystart_race = true;
			final int cur = Getcursch();
			tasktime[cur] = 6;
			Bukkit.broadcastMessage(MS+"§2§l파쿠르 레이싱§f이 곧 시작됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendracemessage(MS+"게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.");
						tasktime[cur]--;
					}
					else
					{
						Canceltask(cur);
						check_start_race = true;
						for(Player p : tlist){
							p.teleport(startpos_race);
						}
						CountDown();
					}
				}
			}, 200L, 200L);
		}
		
		public static void Startgamebed()
		{
			check_lobbystart_bed = true;
			final int cur = Getcursch();
			tasktime[cur] = 6;
			Bukkit.broadcastMessage(MS+"§6§l파쿠르 호박전쟁§f이 곧 시작됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendbedmessage(MS+"게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.");
						tasktime[cur]--;
					}
					else
					{
						Canceltask(cur);
						check_start_bed = true;
						for(Player p : blist){
							climbinglist.remove(p.getName());
							p.teleport(startpos_bed[blist.indexOf(p)]);
							GiveBaseItem(p);
							deathcount.put(p.getName(), 0);
						}
						checkplacepumpkin();
					}
				}
			}, 200L, 200L);
		}

		public static void Startgamepat()
		{
			check_lobbystart_bed = true;
			final int cur = Getcursch();
			tasktime[cur] = 6;
			Bukkit.broadcastMessage(MS+"§e§l파쿠르 경찰과 도둑§f이 곧 시작됩니다.");
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						Sendpatmessage(MS+"게임이 "+tasktime[cur]*10+" 초 후 시작됩니다.");
						tasktime[cur]--;
					}
					else
					{
						Canceltask(cur);
						check_start_pat = true;
						for(Player p : patlist){
							climbinglist.remove(p.getName());
							p.teleport(startpos_pat);
						}
						PATTimer();
						for(int i = 0; i < 10; i++){
							PAT_Place_Chest();
						}
					}
				}
			}, 200L, 200L);
		}
		
		public static void PATTimer(){
			List<Player> tmplist = new ArrayList<Player>(patlist.size());
			for(Player p : patlist){
				p.sendMessage(MS+"게임이 시작되었습니다.");
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.0f);
				tmplist.add(p);
			}
			
			for(int i = 0; i < tmplist.size(); i++){
				Player p = tmplist.get(Getrandom(0, tmplist.size()-1));
				if(pat_theif.size()-1 <= pat_police.size()){
					pat_theif.add(p);
					p.sendMessage(MS+"당신은 도둑입니다.");
					PAT_TheifItem(p);
				}else{
					pat_police.add(p);
					p.sendMessage(MS+"당신은 경찰입니다.");
					PAT_PoliceItem(p);
				}
			}
			
			final int cur = Getcursch(); 
			
			tasktime[cur] = 721;
			for(Player p : patlist){
				p.sendMessage(MS+"남은 시간은 레벨로 확인하실 수 있습니다.");
				p.playSound(p.getLocation(), Sound.CLICK, 1.5f, 0.5f);
			}
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					for(Player p : patlist){
						p.setLevel(--tasktime[cur]);
					}
					if(tasktime[cur] <= 0){
						Bukkit.getScheduler().cancelTask(tasktime[cur]);
						//PAT_PoliceWin();
					}
				}
			}, 200l, 20l);
		}

		public static void PAT_Place_Chest(){
			for(int i = 0; i < 100; i++){
				int rn = Getrandom(0, 39);
				if(pat_chest_pos[rn].getBlock().getType() == Material.AIR){
					pat_chest_pos[rn].getBlock().setType(Material.CHEST);
					break;
				}
			}
		}
		
		public static void checkplacepumpkin(){
			final int cur = Getcursch();
			tasktime[cur] = 200;
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable()
			{
				public void run()
				{
					if(tasktime[cur] > 0)
					{
						for(Player p : blist){
							p.setLevel(tasktime[cur]);
						}
						if(tasktime[cur] <= 30){
							for(Player p : blist){
								p.playSound(p.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
								if(!password.containsKey(p.getName())){
									p.sendMessage(MS+"앞으로 "+tasktime[cur]+"초 안에 비밀번호를 설정하지 않으면 사망합니다. ");
								}
							}
						}
						tasktime[cur]--;
					}
					else
					{
						Canceltask(cur);
						for(int i = 0; i < blist.size(); i++){
							Player p = blist.get(i);
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
							if(!password.containsKey(p.getName())){
								if(pumpkin_loc.containsKey(p.getName())){
									Location l = pumpkin_loc.get(p.getName());
									l.getBlock().setType(Material.AIR);
								}
								p.sendMessage(MS+"비밀번호를 설정하지 않아 사망하셨습니다.");
								GameQuitbed(p);
								me.Bokum.EGM.Main.Spawn(p);
								i--;
							}
						}
						Sendbedmessage(MS+"모든 플레이어가 호박을 설치했습니다. 이제 본격적인 게임을 시작합니다.");
						for(Player t : blist){
							t.setLevel(0);
							pumpkinclearlist.add(pumpkin_loc.get(t.getName()));
						}
						pvp = true;
						UpdateInfo();
					}
				}
			}, 20L, 20L);
		}
		
		public static void GiveBaseItem(Player p){	
			p.getInventory().addItem(pumpkin);
			p.getInventory().addItem(clock);
		}
		
		public static void PAT_TheifItem(Player p){
			for(int i = 0; i < 2; i++){
				p.getInventory().addItem(pat_invisible);
				p.getInventory().addItem(chestitem[24]);
			}
		}
		
		public static void PAT_PoliceItem(Player p){
			for(int i = 0; i < 2; i++){
				p.getInventory().addItem(pat_stunstick);
				p.getInventory().addItem(pat_stungun);
			}
		}
		
		public static void CountDown(){
			final int cur = Getcursch();
			tasktime[cur] = 11;
			tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					if(--tasktime[cur] > 0){
						for(Player p : tlist){
							p.playSound(p.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
							p.sendMessage(MS+tasktime[cur]+"초후 경주를 시작합니다.");
						}
					}else{
						Canceltask(cur);
						for(Player p : tlist){
							p.playSound(p.getLocation(), Sound.NOTE_PIANO, 2.0f, 1.0f);
							p.sendMessage(MS+"경주가 시작됐습니다!");
							cplist.put(p.getName(), -1);
							time.put(p.getName(), (double)java.lang.System.currentTimeMillis()/1000);
						}
						Removefence();
						EndTimer();
					}
					
				}
			}, 0l, 20l);
		}
		
		public static void EndTimer(){
			Bukkit.getScheduler().cancelTask(endtimernum);
			endtimertime = 20;
			endtimernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					if(endtimertime > 0){
						for(Player p : tlist){
							p.playSound(p.getLocation(), Sound.ARROW_HIT, 2.0f, 0.5f);
						}
						Sendracemessage(MS+"§c"+endtimertime+" 분 §e남았습니다.");
						endtimertime--;
					} else {
						Bukkit.getScheduler().cancelTask(endtimernum);
						for(Player p : tlist){
							p.playSound(p.getLocation(), Sound.GHAST_CHARGE, 2.0f, 1.5f);
						}
						check_raceend = true;
						Sendracemessage(MS+"§c경기가 종료 되었습니다.");
						Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
							public void run(){
								for(Player t : tlist){
									econ.depositPlayer(t.getName(), 800);
									t.sendMessage(ChatColor.GOLD + "플레이 보상으로 800원을 받으셨습니다.");
									me.Bokum.EGM.Main.Spawn(t);
								}
								Cleardata_race();
							}
						}, 140l);
					}
				}
			}, 600L, 1200L);
		}

		public static void Removefence(){
			for(Location loc : blockloc){
				loc.getBlock().setType(Material.AIR);
			}
		}
		
		public void Timer(){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					for(Player p : plist){
						if(p.getExp() != 1){
							if(!climbinglist.containsKey(p.getName())){
								if(!p.isSprinting()){
									p.setExp(p.getExp()+0.02f > 1 ? 1 : p.getExp()+0.075f);
								} else {
									p.setExp(p.getExp()+0.02f > 1 ? 1 : p.getExp()+0.05f);
								}
							} else {
								p.setExp(p.getExp()+0.02f > 1 ? 1 : p.getExp()+0.05f);
							}
						}
					}
				}
			}, 20L, 30l);

		}
		
		public void PasswordClick(Player p, int slot){
			String num = "0";
			p.playSound(p.getLocation(),Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
			switch(slot){
			case 10: num = "1"; break;
			case 11: num = "2"; break;
			case 12: num = "3"; break;
			case 19: num = "4"; break;
			case 20: num = "5"; break;
			case 21: num = "6"; break;
			case 28: num = "7"; break;
			case 29: num = "8"; break;
			case 30: num = "9"; break;
			case 38: num = "0"; break;
			case 15: p.sendMessage(MS+"초기화 했습니다."); insertingpassword.put(p.getName(), ""); return;
			case 33: if(insertingpassword.containsKey(p.getName())){
				if(insertingpassword.get(p.getName()).length() != 4){
					p.sendMessage(MS+"4자리의 비밀번호를 입력해주세요.");
					return;
				} else {
					p.closeInventory();
					p.sendMessage(MS+"§6비밀번호의 설정이 완료되었습니다. 비밀번호 : §c"+insertingpassword.get(p.getName()));
					password.put(p.getName(), insertingpassword.get(p.getName()));
					insertingpassword.remove(p.getName());
					deathcount.put(p.getName(), 0);
					return;
				}
			}
			default: return;
			}
			
			if(insertingpassword.containsKey(p.getName()) && insertingpassword.get(p.getName()).length() >= 4){
				p.sendMessage(MS+"비밀번호는 4자리까지만 입력 가능합니다.");
			} else {
				insertingpassword.put(p.getName(), insertingpassword.containsKey(p.getName()) ? insertingpassword.get(p.getName())+num : num);
				p.sendMessage(MS+"입력한 비밀번호 : "+insertingpassword.get(p.getName()));
			}
		}
		
		public void SolvePassword(Player p, int slot){
			String num = "0";
			p.playSound(p.getLocation(),Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
			switch(slot){
			case 10: num = "1"; break;
			case 11: num = "2"; break;
			case 12: num = "3"; break;
			case 19: num = "4"; break;
			case 20: num = "5"; break;
			case 21: num = "6"; break;
			case 28: num = "7"; break;
			case 29: num = "8"; break;
			case 30: num = "9"; break;
			case 38: num = "0"; break;
			case 15: p.sendMessage(MS+"초기화 했습니다."); insertingpassword.put(p.getName(), ""); return;
			case 33: if(insertingpassword.containsKey(p.getName())){
				if(insertingpassword.get(p.getName()).length() != 4){
					p.sendMessage(MS+"4자리의 비밀번호를 입력해주세요.");
					return;
				} else {
					Player target = solvingtarget.get(p.getName());
					if(insertingpassword.get(p.getName()).equalsIgnoreCase(password.get(target.getName()))){
						p.closeInventory();
						insertingpassword.remove(p.getName());
						pumpkin_loc.get(target.getName()).getBlock().setType(Material.AIR);
						Sendbedmessage(MS+p.getName()+" 님이 "+target.getName()+" 님의 비밀번호를 풀었습니다!");
						GameQuitbed(target);
						target.damage(100);
						p.getWorld().playSound(p.getLocation(), Sound.CHEST_OPEN, 2.0f, 1.0f);
						p.sendMessage(MS+"암호 해제 보상으로 50코인을 받았습니다.");
						p.getInventory().addItem(coin(50));
						p.updateInventory();
					} else {
						insertingpassword.remove(p.getName());
						p.getWorld().playSound(p.getLocation(), Sound.ANVIL_USE, 2.0f, 1.0f);
						p.sendMessage(MS+"비밀번호가 틀렸습니다!");
					}
				}
			}
			default: return;
			}
			
			if(insertingpassword.containsKey(p.getName()) && insertingpassword.get(p.getName()).length() >= 4){
				p.sendMessage(MS+"비밀번호는 4자리까지만 입력 가능합니다.");
			} else {
				insertingpassword.put(p.getName(), insertingpassword.containsKey(p.getName()) ? insertingpassword.get(p.getName())+num : num);
				p.sendMessage(MS+"입력한 비밀번호 : "+insertingpassword.get(p.getName()));
			}
		}
		
		public boolean Usestamina(Player p, int amt){
			float minus = (float) amt / 100;
			if(p.getExp() - minus < 0){
				p.sendMessage(MS+"스테미나가 부족합니다.");
				return true;
			}
			p.setExp(p.getExp()-minus);
			return false;
		}
		
		public static void UpdateInfo(){
			infoinven.clear();
			for(int j = 0; j < blist.size(); j++){
				Player p = blist.get(j);
				String pass = "";
				for(int i = 0; i < 4; i++){
					if(deathcount.get(p.getName()) > i){
						pass += password.get(p.getName()).charAt(i);
					} else {
						pass += "?";
					}
				}
				String loc = "?";
				if(deathcount.get(p.getName()) >= 5){
					Location pl = pumpkin_loc.get(p.getName());
					loc = "x : "+pl.getBlockX()+", y : "+pl.getBlockY()+", z : "+pl.getBlockZ();
				}
				ItemStack item = new ItemStack(397, 1, (byte) 3);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§6"+p.getName());
				List<String> lorelist = new ArrayList<String>();
				lorelist.add("§c비밀번호 : "+pass);
				lorelist.add("§c기지 좌표 : "+loc);
				meta.setLore(lorelist);
				item.setItemMeta(meta);
				infoinven.setItem(j, item);
			}
			Sendbedmessage(MS+"§c정보 목록이 갱신 되었습니다. 시계를 우클릭해보세요.");
		}
		
		public void InputPassword(Player p, Location bl){
			Player target = null;
			for(Player t : blist){
				if(pumpkin_loc.containsKey(t.getName())){
				Location loc = pumpkin_loc.get(t.getName());
				if(loc.getBlockX() == bl.getBlockX() && loc.getBlockY() == bl.getBlockY() && loc.getBlockZ() == bl.getBlockZ()){
					target = t;
				}
				}
			}
			if(target == null){
				p.sendMessage(MS+"이 기지의 주인은 게임에 참가중이 아닙니다.");
				return;
			}
			if(!password.containsKey(target.getName())){
				p.sendMessage(MS+"이 기지의 주인은 아직 비밀번호를 설정하지 않았습니다.");
				return;
			}
			if(target.getName() == p.getName()){
				p.openInventory(shopinven);
				return;
			}
			if(!pvp){
				p.sendMessage(MS+"아직 킬타임이 아닙니다.");
				return;
			}
			target.sendMessage(MS+"누군가가 당신의 비밀번호를 풀고있습니다.");
			p.sendMessage(MS+"이 기지는 "+target.getName()+" 님의 기지입니다.");
			insertingpassword.remove(p.getName());
			solvingtarget.put(p.getName(), target);
			p.openInventory(passwordinven);
		}
		
		public void Usecheckpoint(final Player p, Location bcl){
			int cpnum = -1;
			for(int i = 0; i < 10; i++){
				if(bcl.getBlockX() == checkpoint[i].getBlockX() && bcl.getBlockY() == checkpoint[i].getBlockY() && bcl.getBlockZ() == checkpoint[i].getBlockZ()){
					cpnum = i;
					break;
				}
			}
			if(cpnum == -1) return;
			if(cplist.get(p.getName()) >= cpnum) return;
			if(cplist.get(p.getName())+1 == cpnum){
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
				cplist.put(p.getName(), cpnum);
				p.setLevel(cpnum+1);
				Sendracemessage(MS+p.getName()+" 님께서 "+(cplist.get(p.getName())+1)+" 번째 체크포인트에 도달했습니다.");
				for(Player t : tlist){
					t.playSound(t.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
				}
				if(cpnum >= 9){
					Sendracemessage(MS+p.getName()+" 님이 골인했습니다!");
					double racetime = ((double) java.lang.System.currentTimeMillis()/1000 - time.get(p.getName()));
					racetime = (int)(racetime*1000);
					racetime = (double) racetime / 1000;
					p.sendMessage(MS+"당신의 기록 : §c"+racetime+"§f 초");
					if(!check_raceend){
						Bukkit.getScheduler().cancelTask(endtimernum);
						check_raceend = true;
						try{
						final int cur = Getcursch();
						tasktime[cur] = 30;
						tasknum[cur] = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
						{
							public void run()
							{
								if(tasktime[cur] > 0)
								{
									for(Player t : tlist){
										t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
									}
									Sendracemessage(MS+"경주가 "+tasktime[cur]+" 초 후 종료됩니다.");
									tasktime[cur]--;
								}
								else
								{
									Canceltask(cur);
									Winrace(p);
								}
							}
						}, 0L, 20L);
						}catch(Exception e){
							Forcestop_race();
						}
					}
					me.Bokum.EGM.Main.getPlayerData(p.getName()).set("Parkour_racetime", racetime);
					try{
						me.Bokum.EGM.Main.getPlayerData(p.getName()).save(new File(me.Bokum.EGM.Main.instance.getDataFolder().getPath()+"/data", p.getName()+".data"));
						} catch(Exception e){
							
						}
					for(int i = 0; i < rank_time.size(); i++){
						if(rank_time.get(i) > racetime){
							if(rank_name.contains(p.getName())){
								if(rank_time.get(rank_name.indexOf(p.getName())) <= racetime) return;
								else{
									for(int j = rank_name.indexOf(p.getName()); j > i; j--){
										rank_name.set(j, rank_name.get(j-1));
										rank_time.set(j, rank_time.get(j-1));
									} 
									rank_name.set(i, p.getName());
									rank_time.set(i, racetime);
								}
							}else{
								for(int j = 9; j > i; j--){
									rank_name.set(j, rank_name.get(j-1));
									rank_time.set(j, rank_time.get(j-1));
								} 
								rank_name.set(i, p.getName());
								rank_time.set(i, racetime);
							}
							saveRank();
							Bukkit.broadcastMessage(MS+"§2파쿠르 레이싱§7에서 §e"+p.getName()+" §7님께서 §e"+racetime+"§7초 의 기록으로 §e"+(i+1)+"§7등을 하셨습니다.");
							return;
						}
					}
				}
			}
			if(cplist.get(p.getName())+1 < cpnum){
				p.sendMessage(MS+"먼저 "+(cplist.get(p.getName())+2)+" 번째 체크포인트를 가야합니다.");
			}
		}
		
		public void showRank(Player p){
			for(int i = 0; i < 10; i++){
				p.sendMessage(MS+(i+1)+"등 : §6"+rank_name.get(i)+" §7- §b"+rank_time.get(i)+" 초");
			}
		}
		
		public void saveRank(){
			FileConfiguration rankconfig = me.Bokum.EGM.Main.getRank("Parkour");
			for(int i = 0; i < 10; i++){
				rankconfig.set("rank_name_"+i, rank_name.get(i));
				rankconfig.set("rank_time_"+i, rank_time.get(i));
			}
			try{
			rankconfig.save(new File(me.Bokum.EGM.Main.instance.getDataFolder().getPath()+"/rank", "Parkour.rank"));
			} catch(Exception e){
				
			}
		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e){
			if(!plist.contains(e.getPlayer())) return;
				Player p = e.getPlayer();
				if(blist.contains(p) && pumpkin_loc.containsKey(p.getName()) && !password.containsKey(p.getName())){
					insertingpassword.remove(p.getName());
					p.openInventory(passwordinven);
					p.sendMessage(MS+"비밀번호를 입력해주세요.");
					return;
				}
				if(p.getGameMode() == GameMode.CREATIVE) return;
				if(p.getLocation().getY() < 0){
					p.damage(100);
					return;
				}
				if(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY()
						&& e.getFrom().getZ() == e.getTo().getZ()) return;
				Material reb = p.getLocation().getBlock().getRelative(0, -1, 0).getType();
				if(reb == Material.BEACON) Usecheckpoint(p,p.getLocation().getBlock().getRelative(0, -1, 0).getLocation());
				if(reb == Material.PISTON_BASE) p.teleport(new Location(p.getWorld(), -2000, 50, 400));
				if(reb == Material.PISTON_STICKY_BASE && p.getExp() < 1){
					p.setExp(1); p.sendMessage(MS+"스테미나가 회복 되었습니다.");
				}
				if(p.getLocation().getY() <= 16){
					if(!p.hasPotionEffect(PotionEffectType.SLOW)){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 72000, 0));
					p.removePotionEffect(PotionEffectType.SPEED);
					}
				} else {
					p.removePotionEffect(PotionEffectType.SLOW);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 1));
				}
				if(reb == Material.SPONGE){
				      p.setVelocity(p.getLocation().getDirection().multiply(0.8D).setY(0.8D));
				      p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 0.8f);
				}
				if(climbinglist.containsKey(p.getName())){
					e.setCancelled(true);
				}
				if(reb != Material.AIR){
					if(jumpinglist.contains(p.getName()))
					jumpinglist.remove(p.getName());
				}
				Block tb = null;
				try{
					tb = p.getTargetBlock(null, 1);
				}catch(Exception exception){
					return;
				}
				if(tb == null){
					return;
				}
				double dix_x = Math.abs(p.getLocation().getX() - tb.getLocation().getBlockX());
				double dix_z = Math.abs(p.getLocation().getZ() - tb.getLocation().getBlockZ());
				if((tb.getTypeId() == 85 || tb.getTypeId() == 113 || tb.getTypeId() == 139) &&
						(dix_x <= 0.5 || dix_z <= 0.5)){
					p.getWorld().playSound(p.getLocation(), Sound.DIG_SNOW, 3.0f, 0.7f);
					p.setAllowFlight(true);
					p.setFlying(true);
				} else {
					p.setAllowFlight(false);
					p.setFlying(false);
				}
			    if (e.getTo().getBlockY() > e.getFrom().getBlockY()) {
					if(p.isSneaking()){
						if((!jumpinglist.contains(p.getName())
								&& reb != Material.AIR) || p.getLocation().getBlock().getType() == Material.STEP){
							if(Usestamina(p, 10)) return;
							jumpinglist.add(p.getName());
							p.setVelocity(new Vector(0,0.77f,0));
							p.getWorld().playSound(p.getLocation(), Sound.MAGMACUBE_JUMP, 3.0f, 0.8f);
						}
					}
			    }
		}
		
		@EventHandler
		public void onSneak(PlayerToggleSneakEvent e){
			if(!plist.contains(e.getPlayer())) return;
			Player p = e.getPlayer();
			if(p.isSneaking()) return;
			if(p.getGameMode() == GameMode.CREATIVE) return;
			Block tb = null;
			try{
				tb = p.getTargetBlock(null, 1);
			}catch(Exception exception){
				return;
			}
			if(tb == null){
				return;
			}
			 if(climbinglist.containsKey(p.getName())){
					if(Usestamina(p, 25)) return;
					p.playSound(p.getLocation(), Sound.FALL_BIG, 3.0f, 1.6f);
					switch(climbinglist.get(p.getName())){
					case 1: p.setVelocity(new Vector(0,0.35f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.65D)); break;
					case 2: p.setVelocity(new Vector(0,0.50f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.76D));break;
					case 3: p.setVelocity(new Vector(0,0.65f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.87D));break;
					case 4: p.setVelocity(new Vector(0,0.77f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.96D));break;
					case 5: p.setVelocity(new Vector(0,0.77f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(1.05D));break;
					
					default: p.setVelocity(new Vector(0,0.77f,0)); p.setVelocity(p.getLocation().getDirection().multiply(0.2D).setY(1.05D));break;
					}
					climbinglist.remove(p.getName());	
					return;
			 }
			/*double dix_x = Math.abs(p.getLocation().getX() - tb.getLocation().getBlockX());
			double dix_z = Math.abs(p.getLocation().getZ() - tb.getLocation().getBlockZ());*/
			 if(p.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR
					 && p.getLocation().getBlock().getType() != Material.STEP
					&& tb.getType() != Material.AIR && !climbinglist.containsKey(p.getName())){
					if(Usestamina(p, 5)) return;
				    p.setVelocity(p.getLocation().getDirection().multiply(-0.65D).setY(0.65D));
					p.getWorld().playSound(p.getLocation(), Sound.DIG_STONE, 3.0f, 0.3f);
					return;
				}
		}
		
		@EventHandler
		public void onPvp(EntityDamageByEntityEvent e){
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
				Player p = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();
				if(blist.contains(p) && blist.contains(damager)){
				      if(!(getItemDamage(damager) == 0)){
				    	  e.setDamage(getItemDamage(damager));
				      }
				}
			}
		}
		
		@EventHandler
		public void onRightClick(PlayerInteractEvent e)
		{
			if(!plist.contains(e.getPlayer())) return;
			Player p = e.getPlayer();
			if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)){
				if(climbinglist.containsKey(e.getPlayer().getName())){
					climbinglist.remove(e.getPlayer().getName());
					e.getPlayer().sendMessage(MS+"손을 놨습니다.");
					e.getPlayer().setAllowFlight(false);
					e.getPlayer().setFlying(false);
					return;
				}else if(e.getClickedBlock() != null && p.isSprinting()){
					Block bl = e.getClickedBlock();
					if(bl.getLocation().getY() == (p.getLocation().getY()+1) && (bl.getRelative(0, 1, 0).getType() == Material.AIR 
							|| bl.getRelative(0, 1, 0).getType() == Material.RAILS)){
						if(Usestamina(p, 5)) return;
						p.setVelocity(p.getLocation().getDirection().multiply(0.55D).setY(0.70D));
						p.playSound(p.getLocation(), Sound.LAVA_POP, 2.0f, 0.5f);	
					}
				}
			}
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
			{
				if(e.getClickedBlock() != null && blist.contains(p)){
					if(e.getClickedBlock().getType() == Material.JACK_O_LANTERN){
						InputPassword(p, e.getClickedBlock().getLocation());
						return;
					} else if(e.getClickedBlock().getType() == Material.CHEST){
						e.setCancelled(true);
						  final Location bl = e.getClickedBlock().getLocation();
						  if(!chestlist.contains(bl)){
							  chestlist.add(bl);
								p.getInventory().addItem(coin(Getrandom(1, 3)));
								p.getWorld().playSound(p.getLocation(), Sound.CHEST_OPEN, 2.0f, 1.0f);
								p.updateInventory();
							  Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
								  public void run(){
									  chestlist.remove(bl);
								  }
							  }, 1200L);
						  } else {
							  p.sendMessage("§7빈 상자입니다.");
						  }
					}
				}
				if(e.getItem() != null && e.getItem().getType() == Material.COMPASS){
					if(tlist.contains(p)) p.openInventory(gamehelper);
					if(blist.contains(p)) p.openInventory(gamehelper2);
				}
				if(e.getItem() != null && e.getItem().getTypeId() == 370){
					Removeitem(p, 370, 1);
					p.setExp(1); p.sendMessage(MS+"스테미나가 회복 되었습니다.");
				}
				if(e.getItem() != null && e.getItem().getType() == Material.WATCH){
					if(blist.contains(p)) p.openInventory(infoinven);
				}
				if(e.getItem() != null && e.getItem().getTypeId() == 371){
					showRank(p);
				}
				if(e.getItem() != null && e.getItem().getTypeId() == 402){
					if(pumpkin_loc.containsKey(p)){
						Location pl = pumpkin_loc.get(p.getName());
						p.sendMessage(MS+"x : "+pl.getBlockX()+", y : "+pl.getBlockY()+", z : "+pl.getBlockZ());
					}
				}
				if(p.getGameMode() == GameMode.CREATIVE) return;
				if(e.getClickedBlock() != null && p.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR
						&& e.getClickedBlock().getLocation().getBlockY() > p.getLocation().getY() 
						&& (e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.AIR 
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.THIN_GLASS 
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.IRON_FENCE
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.FENCE
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.NETHER_FENCE
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.COBBLE_WALL
						|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.RAILS)){
					Block bl = e.getClickedBlock();
					double dis_y = Math.abs(bl.getLocation().getY() - p.getLocation().getY());
					if(dis_y <= 4
							&& (Math.abs(bl.getLocation().getBlockX()-p.getLocation().getX()) <= 3
							&& Math.abs(bl.getLocation().getBlockZ()-p.getLocation().getZ()) <= 3)){
						p.setAllowFlight(true);
						p.setFlying(true);
						p.teleport(p.getLocation());
						climbinglist.put(p.getName(), (int) Math.round(dis_y)+1);
						p.sendMessage(MS+"매달리셨습니다. - 좌클릭 : 해제 , 쉬프트 : 오르기");
					}
				}
			}
		}
		

		  public static int getItemDamage(Player p){
			  ItemStack item = p.getItemInHand();
			  if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return 1;
			  List<String> lorelist = item.getItemMeta().getLore();
			  if(lorelist.size() <= 0) return 1;
			  String damagestr = lorelist.get(0);
			  if(!damagestr.contains("공격력")) return 1;
			  if(!damagestr.contains("~"))return Integer.valueOf(damagestr.substring(7, 8));
			  int min = Integer.valueOf(damagestr.substring(7, 8));
			  int max = Integer.valueOf(damagestr.substring(9, 10));
			  return (int)(Math.random()*(max-min+1)+min);
		  }
		  
		  

		  public static int getPrice(ItemStack item){
			  if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;
			  List<String> lorelist = item.getItemMeta().getLore();
			  if(lorelist.size() <= 1) return 0;
			  String pricestr = lorelist.get(1);
			  if(!pricestr.contains("가격")) return 0;
			  //"§[0]c[1]가[2]격[3] [4]§[5]f[6]:[7]§[8]6[9] [10]"
			  int price = Integer.valueOf(pricestr.substring(11, 12));
			  if(pricestr.length() > 12) {
				  price *= 10;
				  price += Integer.valueOf(pricestr.substring(12, 13));
			  }
			  return price;
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
					 if(tlist.contains(p)){
						 GameHelperrace(p, e.getSlot());
					 } else {
						 GameHelperbed(p, e.getSlot());
					 }
					 e.setCancelled(true);
				 }
				 if(e.getInventory().getTitle().equalsIgnoreCase("§c§l비밀번호")){
					 e.setCancelled(true);
					 if(blist.contains(p)){
						 if(!password.containsKey(p.getName())){
							 PasswordClick(p, e.getSlot());
						 } else {
							 SolvePassword(p, e.getSlot());
						 }
					 }
					 return;
				 }
				 if(e.getInventory().getTitle().equalsIgnoreCase("§c§l상점")){
					 e.setCancelled(true);
					 if(blist.contains(p) && e.getCurrentItem() != null){
						 Shopping(p, e.getCurrentItem());
					 }
				 }
				 if(e.getInventory().getTitle().equalsIgnoreCase("§c§l정보")) e.setCancelled(true);
			}
		}
		
		public void Shopping(Player p, ItemStack item) {
			if(item.getTypeId() == 266){
				if(Takeitem(p, 388, 10)){
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 0.7f);
					p.getInventory().addItem(coin(Getrandom(1, 19)));
					p.updateInventory();
				}else {
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
					p.sendMessage(MS+"코인이 부족합니다.");
				}
				return;
			}
			if(item.getTypeId() == 339){
				if(Takeitem(p, 388, 100)){
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 0.7f);
					for(Player t : blist){
						if(!t.getName().equalsIgnoreCase(p.getName())){
							deathcount.put(t.getName(), deathcount.get(t.getName())+1);
						}
					}
					p.sendMessage(MS+"자신을 제외한 모두의 정보를 한단계 더 높입니다.");
					UpdateInfo();
				}else {
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
					p.sendMessage(MS+"코인이 부족합니다.");
				}
			}
			int price = getPrice(item);
			if(Takeitem(p, 388, price)){
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0f, 0.7f);
				p.getInventory().addItem(item);
			} else {
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
				p.sendMessage(MS+"코인이 부족합니다.");
			}
		}

		@EventHandler
		public void onPlayerDamage(EntityDamageEvent e){
			if(!(e.getEntity() instanceof Player)) return;
			final Player p = (Player) e.getEntity();
			if(!plist.contains(p)) return;
			if ((e.getCause() == EntityDamageEvent.DamageCause.FALL)){
				e.setCancelled(true);
				if(!p.isSneaking() || e.getDamage() >= 40){
					if(e.getDamage() <= 19 || p.getLocation().getBlock().getRelative(0, 1, 0).getType() == Material.SPONGE) return;
							p.damage(1);
							p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 2.0f);
							p.sendMessage(MS+"다리를 다쳤습니다!");
							p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 199));
				} else{
							p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 3.0f);
				}
				return;
				} else {
					if(blist.contains(p) && pvp){
						return;
					}
					e.setCancelled(true);
				}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e){
			if(!plist.contains(e.getPlayer())) return;
			if(e.getItemDrop().getItemStack().getType() == Material.COMPASS || e.getItemDrop().getItemStack().getType() == Material.WATCH) e.setCancelled(true);
		}
		
		@EventHandler
		public void onPlayercommand(PlayerCommandPreprocessEvent e)
		{
			Player p = e.getPlayer();
			if(plist.contains(p) && e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
					|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ"))
			{
				plist.remove(p);
			    if (tlist.contains(p))
			    {
			      GameQuitrace(p);
			    } else if(blist.contains(p)){
			    	GameQuitbed(p);
			    }
			}
			if(plist.contains(p) && e.getMessage().equalsIgnoreCase("/꺼내줘")){
				p.sendMessage(MS+"이 게임에서는 사용하실 수 없습니다.");
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onBlockbreak(BlockBreakEvent e){
			if(plist.contains(e.getPlayer())){
				Player p = e.getPlayer();
					e.setCancelled(true);
			}

		}
		  
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent e){
			  if(plist.contains(e.getPlayer())){
				  if(!blist.contains(e.getPlayer()) && !e.getPlayer().isOp()){
					  e.setCancelled(true);
					  return;
				  } else {
					  Player p = e.getPlayer();
					  if(e.getBlock().getType() == Material.JACK_O_LANTERN){
						  pumpkin_loc.put(p.getName(), e.getBlock().getLocation());
						  p.sendMessage(MS+"이곳을 기지로 설정했습니다.");
						  p.sendMessage(MS+"비밀번호를 입력해주세요.");
						  insertingpassword.remove(p.getName());
						  p.openInventory(passwordinven);
					  }
				  }
			  }
		}
		
		  @EventHandler
		  public void onQuitPlayer(PlayerQuitEvent e) {
			  if(!plist.contains(e.getPlayer())) return;
			  plist.remove(e.getPlayer());
		    if (tlist.contains(e.getPlayer()))
		    {
		      GameQuitrace(e.getPlayer());
		    } else if(blist.contains(e.getPlayer())){
		    	GameQuitbed(e.getPlayer());
		    }
		  }
		  
		  @EventHandler
		  public void onRespawn(PlayerRespawnEvent e){
			  if(!plist.contains(e.getPlayer())) return;
			  final Player p = e.getPlayer();
			  if(climbinglist.containsKey(p.getName())){
				  climbinglist.remove(p.getName());
				  p.setFlying(false);
				  p.setAllowFlight(false);
			  }
			  if(blist.contains(e.getPlayer())){
				  if(!pumpkin_loc.containsKey(p.getName())){
					  p.sendMessage(MS+"호박을 설치하지 않고 죽어 탈락하셨습니다.");
					  GameQuitbed(p);
				  }else{
					  e.setRespawnLocation(pumpkin_loc.get(p.getName()));
					  if(deathinven.containsKey(p.getName())){
						  Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							  public void run(){
								  p.getInventory().setContents(deathinven.get(p.getName()));
								  deathinven.remove(p.getName());
							  }
						  }, 20l);						 
					  }
				  }
			  }
		  }
		  
			@EventHandler
			public void onPlayerdeath(PlayerDeathEvent e)
			{
				Player p = e.getEntity();
				  if(!plist.contains(p)) return;
				if(tlist.contains(p))
				{
					plist.remove(p);
					GameQuitrace(p);
				} else if(blist.contains(p)){
					if(password.containsKey(p.getName())){
						deathinven.put(p.getName(), p.getInventory().getContents());
						e.getDrops().clear();
						p.setFlying(false);
						p.setAllowFlight(false);
						if(p.getKiller() != null){
							Player k = p.getKiller();
							k.getInventory().addItem(coin(10));
							p.updateInventory();
							k.sendMessage(MS+"킬 보상으로 에메랄드 10개를 받았습니다.");
						}
						if(deathcount.get(p.getName()) <= 5){
							deathcount.put(p.getName(), deathcount.get(p.getName())+1);
							UpdateInfo();
						} 
						if(climbinglist.containsKey(p.getName())) climbinglist.remove(p.getName());
					}
				}
			}
}
