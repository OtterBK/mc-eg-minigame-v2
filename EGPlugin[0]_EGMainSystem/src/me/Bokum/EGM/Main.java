package me.Bokum.EGM;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener
{
	public static String MS = "§f[ §aEG §f] ";
	public static HashMap<String, String> gamelist = new HashMap<String, String>(250);
	public static Location Loc_Lobby;
	public static Inventory Kit_Lobby;
	public static List<Player> aclist = new ArrayList<Player>();
	public static Inventory Minigame;
	public static Inventory Serverhelp;
	public static Inventory prefixshop;
	public static Inventory mobarenainven;
	public static Inventory parkourinven;
	public static Inventory serverrule;
	public static Inventory baseQuest;
	public static Inventory patchNote;
	public static Economy econ = null;
	public static int daycnt = 0;
	public static int broadcastcnt = 0;
	public static Main instance;
	public static HashMap<String, Integer> cooldown = new HashMap<String, Integer>(300);
	public static HashMap<String, Integer> muteList = new HashMap<String, Integer>();
	public static List<String> buildlist = new ArrayList<String>(80);
	public static List<String> pvplist = new ArrayList<String>(80);
	public static List<String> noAllVoiceList = new ArrayList<String>(300);
	public static List<String> solochat = new ArrayList<String>(300);
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		LoadInventory();
		LoadConfig();
		UpdateScheduler();
		instance = this;
	    getServer().setWhitelist(true);
		getLogger().info("EG서버 메인 시스템이 로드 되었습니다.");
		
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
	
	public void LoadConfig(){
		Loc_Lobby = new Location(getServer().getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"),
				getConfig().getInt("Lobby_y"),getConfig().getInt("Lobby_z"));	
		Loc_Lobby.setPitch((float) getConfig().getDouble("Lobby_pitch"));
		Loc_Lobby.setYaw((float) getConfig().getDouble("Lobby_yaw"));
		solochat.addAll(getConfig().getStringList("solochatlist"));
	}
	
	private void SetInventory(){
		Kit_Lobby = Bukkit.createInventory(null, 36, "스폰킷");
		
		ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b미니게임 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7미니게임을 플레이 하시려면");
		lorelist.add("§f- §7우클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Kit_Lobby.setItem(0, item);
		
		item = new ItemStack(340, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §b서버 정보 §f]");
		lorelist.add("§f- §7서버에 대한 정보, 규칙 등등을");
		lorelist.add("§f- §7보시려면 우클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Kit_Lobby.setItem(1, item);
		
		item = new ItemStack(339, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §b패치노트 §f]");
		lorelist.add("§f- §7서버 패치내역을 봅니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Kit_Lobby.setItem(2, item);
		
		Minigame = Bukkit.createInventory(null, 54, "§c§l미니게임");
		
		item = new ItemStack(102, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§6장식");
		item.setItemMeta(meta);
		Minigame.setItem(0, item);
		Minigame.setItem(9, item);
		Minigame.setItem(18, item);
		Minigame.setItem(27, item);
		Minigame.setItem(36, item);
		Minigame.setItem(35, item);
		Minigame.setItem(44, item);
		Minigame.setItem(53, item);
		Minigame.setItem(45, item);
		Minigame.setItem(8, item);
		Minigame.setItem(17, item);
		Minigame.setItem(26, item);
		
		item = new ItemStack(351, 1, (byte)1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §c살인마를 찾아라4 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.FindTheMurder.Main.list.size()+"명 / 13명");
		lorelist.add("§f- §e최소인원 : 5명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(10, item);
		
		item = new ItemStack(Material.EMERALD, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §a팀 랜덤 배틀§f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.TRB.Main.plist.size()+"명 / 12명");
		lorelist.add("§f- §e최소인원 : 6명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §6신들의 전쟁§f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.FindTheMurder.Main.list.size()+"명 / 12명");
		lorelist.add("§f- §e최소인원 : 6명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(14, item);
		
		item = new ItemStack(Material.WOOD, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §2CTF §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 14명");
		lorelist.add("§f- §e최소인원 : 6명");
		lorelist.add("§f- §4해당 게임은 중도참여가 가능합니다.");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(16, item);
		
		item = new ItemStack(Material.STICK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §d선빵게임 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 9명");
		lorelist.add("§f- §e최소인원 : 3명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(19, item);
		
		item = new ItemStack(Material.DIAMOND, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §b히어로즈워 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 30명");
		lorelist.add("§f- §e최소인원 : 20명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(25, item);
		
		item = new ItemStack(146, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §c헝거게임 §f]");
		lorelist.add("§f- §4해당 게임은 현황 지원을 하지 않습니다.");
		lorelist.add("§f- §b채널을 선택하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(23, item);

		item = new ItemStack(Material.WOOL, 1 , (byte)14);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §4타겟 암살 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 9명");
		lorelist.add("§f- §e최소인원 : 4명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(21, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §b스플리프 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 7명");
		lorelist.add("§f- §e최소인원 : 3명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(30, item);
		
		item = new ItemStack(Material.FENCE, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §2파쿠르 게임 §f]");
		lorelist.add("§f- §e진정한 파쿠르를 느껴보자!");
		lorelist.add("§f- §b게임을 선택하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(32, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte) 5);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §a컬러매치 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 6명");
		lorelist.add("§f- §e최소인원 : 2명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(34, item);
		
		item = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e캐치마인드 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 6명");
		lorelist.add("§f- §e최소인원 : 4명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(28, item);
		
		item = new ItemStack(Material.WOOL, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e태극기 술래잡기 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 8명");
		lorelist.add("§f- §e최소인원 : 4명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(37, item);
		
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §a블럭 숨바꼭질 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 12명");
		lorelist.add("§f- §e최소인원 : 5명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(41, item);
		
		item = new ItemStack(Material.ANVIL, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e모루 피하기 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 7명");
		lorelist.add("§f- §e최소인원 : 2명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(43, item);
		
		item = new ItemStack(Material.FIREBALL, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §c데스런 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 8명");
		lorelist.add("§f- §e최소인원 : 2명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(46, item);
		
		item = new ItemStack(Material.MINECART, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §b자리뺏기 §f]");
		lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 8명");
		lorelist.add("§f- §e최소인원 : 4명");
		lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		Minigame.setItem(48, item);
		
		serverrule = Bukkit.createInventory(null, 27, "§c§l서버규칙");
		
		ItemStack deco = new ItemStack(102, 1);
		ItemMeta decometa = deco.getItemMeta();
		decometa.setDisplayName("§6장식");
		deco.setItemMeta(decometa);
		for(int i = 0; i <= 9; i++){
			serverrule.setItem(i, deco);
		}
		for(int i = 17; i < 27; i++){
			serverrule.setItem(i, deco);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e서버규칙 §f]");
		lorelist.add("§f- §6EG서버는 §e미니게임§f서버입니다.");
		lorelist.add("§f- §6아래의 규칙들만 준수하시면 즐겁게 게임을 하실 수 있습니다.");
		lorelist.add("§f- §e1. §2채팅도배§7를 하지마세요.");
		lorelist.add("§f- §e2. §7타서버 발언은 가능하되 지나친 타서버 홍보는 하지마세요.");
		lorelist.add("§f- §e3. §7특정 BJ의 언급을 하지말아주세요.");
		lorelist.add("§f- §e4. §7버그 발견, 오류발생시에는 §c'/문의'§7명령어를 사용해주세요.");
		lorelist.add("§f- §e5. §7신고는 §c카페§7에 부탁드립니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		serverrule.setItem(10, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e서버규칙 §f]");
		lorelist.add("§f- §e6. §b스마트무빙, 노더스§7등은 절대 사용하지마세요.");
		lorelist.add("§f- §e7. §c욕설 및 성드립§7등의 발언을 하지마세요.");
		lorelist.add("§f- §e8. §d닉네임스킨§7의 사용은 금지입니다.");
		lorelist.add("§f- §e9. §c§l게임마다 주어지는 게임도우미를 꼭 읽고");
		lorelist.add("§f- §e9-1. §c§l각 게임의 규칙을 준수하세요.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		serverrule.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e카페주소 §f]");
		lorelist.add("§f- §7클릭시 링크를 받습니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		serverrule.setItem(12, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §eOP목록 §f]");
		lorelist.add("§f- §eLimes_ §7: 총관리자 ");
		lorelist.add("§f- §eTomaRovini_ §7: 건축가 ");
		lorelist.add("§f- §eBokum §7: 플러그인 개발");
		lorelist.add("§f- §eSeol_Yee §7: 서버 관리자 ");
		lorelist.add("§f- §eingwan §7: 서버 관리자 ");
		lorelist.add("§f- §eYoun_w §7: 서버 관리자");
		lorelist.add("§f- §eU_Uv §7: 서버 관리자");
		lorelist.add("§f- §eniceyonom02 §7: 유저 관리자 ");
		lorelist.add("§f- §easzs423 §7: 유저 관리자 ");		
		lorelist.add("§f- §eSunny_p §7: 유저 관리자 ");
		lorelist.add("§f- §eG_Hatem §7: 유저 관리자 ");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		serverrule.setItem(13, item);
		
		
		Serverhelp = Bukkit.createInventory(null, 27, "§c§l서버 도움말");
		
		for(int i = 0; i <= 9; i++){
			Serverhelp.setItem(i, deco);
		}
		for(int i = 17; i < 27; i++){
			Serverhelp.setItem(i, deco);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e서버 규칙 및 관리진");
		item.setItemMeta(meta);
		Serverhelp.setItem(11, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e상점");
		item.setItemMeta(meta);
		Serverhelp.setItem(15, item);
		
		prefixshop = Bukkit.createInventory(null, 54, "§c§l칭호 상점");
		
		for(int i = 0; i < 54; i++){
			switch(i+9){
			case 10: 	meta.setDisplayName("§f[§2할인§f]"); break;
			case 11: 	meta.setDisplayName("§f[§3네로§f]"); break;
			case 12: 	meta.setDisplayName("§f[§5인질§f]"); break;
			case 14: 	meta.setDisplayName("§f[§7휴지§f]"); break;
			case 15: 	meta.setDisplayName("§f[§8통수§f]"); break;
			case 16: 	meta.setDisplayName("§f[§9에임§f]"); break;
			case 13: 	meta.setDisplayName("§f[§d돌풍§f]"); break;
			case 19: 	meta.setDisplayName("§f[§3네임§f]"); break;
			case 20: 	meta.setDisplayName("§f[§9시계§f]"); break;
			case 21:	meta.setDisplayName("§f[§d속도§f]"); break;
			case 22: 	meta.setDisplayName("§f[§7욕구§f]"); break;
			case 23: 	meta.setDisplayName("§f[§6파워§f]"); break;
			case 24: 	meta.setDisplayName("§f[§2개발§f]"); break;
			case 25: 	meta.setDisplayName("§f[§3연구§f]"); break;
			case 26: 	meta.setDisplayName("§f[§3스펠§f]"); break;
			case 28: 	meta.setDisplayName("§f[§5전구§f]"); break;
			case 29: 	meta.setDisplayName("§f[§e루미§f]"); break;
			case 30: 	meta.setDisplayName("§f[§a어둠§f]"); break;
			case 31: 	meta.setDisplayName("§f[§3로얄§f]"); break;
			case 32: 	meta.setDisplayName("§f[§2큰꽃§f]"); break;
			case 33: 	meta.setDisplayName("§f[§9질풍§f]"); break;
			case 34: 	meta.setDisplayName("§f[§8스컬§f]"); break;
			case 35: 	meta.setDisplayName("§f[§e오픈§f]"); break;
			case 37: 	meta.setDisplayName("§f[§7전력§f]"); break;
			case 38: 	meta.setDisplayName("§f[§6이득§f]"); break;
			case 39: 	meta.setDisplayName("§f[§5인정§f]"); break;
			case 40: 	meta.setDisplayName("§f[§e마음§f]"); break;
			case 41: 	meta.setDisplayName("§f[§d이슬§f]"); break;
			case 42: 	meta.setDisplayName("§f[§c슬기§f]"); break;
			case 43: 	meta.setDisplayName("§f[§b나무§f]"); break;
			case 46: 	meta.setDisplayName("§f[§a주목§f]"); break;
			case 47: 	meta.setDisplayName("§f[§2초롱§f]"); break;
			case 48:	meta.setDisplayName("§f[§5한빛§f]"); break;
			case 49: 	meta.setDisplayName("§f[§a누리§f]"); break;
			case 50: 	meta.setDisplayName("§f[§b미르§f]"); break;
			case 51: 	meta.setDisplayName("§f[§8고기§f]"); break;
			case 52: 	meta.setDisplayName("§f[§e스폰§f]"); break;
			case 53: 	meta.setDisplayName("§f[§3노력§f]"); break;
			case 55: 	meta.setDisplayName("§f[§3레어§f]"); break;
			case 56: 	meta.setDisplayName("§f[§d에픽§f]"); break;
			case 57: 	meta.setDisplayName("§f[§a전설§f]"); break;
			case 58: 	meta.setDisplayName("§f[§3인간§f]"); break;
			case 59: 	meta.setDisplayName("§f[§5덕후§f]"); break;
			case 60: 	meta.setDisplayName("§f[§6미겜§f]"); break;
			case 61: 	meta.setDisplayName("§f[§7솔로§f]"); break;
			case 62: 	meta.setDisplayName("§f[§e끈기§f]"); break;
			
			default: break;
			}
			item.setItemMeta(meta);
			prefixshop.setItem(i, item);				
		}
		
		baseQuest = Bukkit.createInventory(null, 54, "§c§l도전과제");
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e도전과제");
		item.setItemMeta(meta);
		
		for(int i1 = 0; i1 < 40; i1++){
			switch(i1){
			case 0: meta.setDisplayName("§f[§6 스타터 §f]"); break;
			case 1: meta.setDisplayName("§f[§6 섬세함이 넘쳐나는 군요! §f]"); break;
			case 2: meta.setDisplayName("§f[§6 기부자 §f]"); break;
			case 3: meta.setDisplayName("§f[§6 숨겨진 상자 §f]"); break;
			case 4: meta.setDisplayName("§f[§6 정점에 선 자 §f]"); break;
			case 5: meta.setDisplayName("§f[§6 러너 §f]"); break;
			case 6: meta.setDisplayName("§f[§6 살인의 흔적 §f]"); break;
			case 7: meta.setDisplayName("§f[§6 명사수 §f]"); break;
			case 8: meta.setDisplayName("§f[§6 운수 좋은날 §f]"); break;
			case 9: meta.setDisplayName("§f[§6 두명은 없다 §f]"); break;
			case 10: meta.setDisplayName("§f[§6 신변 보장 §f]"); break;
			case 11: meta.setDisplayName("§f[§6 죽은자의 소생 §f]"); break;
			case 12: meta.setDisplayName("§f[§6 활약은 없다 §f]"); break;
			case 13: meta.setDisplayName("§f[§6 갑옷은 좋은데... §f]"); break;
			case 14: meta.setDisplayName("§f[§6 퍼펙트 게임 §f]"); break;
			case 15: meta.setDisplayName("§f[§6 진정한 광부 §f]"); break;
			case 16: meta.setDisplayName("§f[§6 하늘의 전사 §f]"); break;
			case 17: meta.setDisplayName("§f[§6 팀 기여도 100% §f]"); break;
			case 18: meta.setDisplayName("§f[§6 힐러는 절대 약하지 않다 §f]"); break;
			case 19: meta.setDisplayName("§f[§6 손이 보이지 않아! §f]"); break;
			case 20: meta.setDisplayName("§f[§6 양보합시다 §f]"); break;
			case 21: meta.setDisplayName("§f[§6 타겟은 상관없어 §f]"); break;
			case 22: meta.setDisplayName("§f[§6 모두다 내꺼 §f]"); break;
			case 23: meta.setDisplayName("§f[§6 화가 §f]"); break;
			case 24: meta.setDisplayName("§f[§6 눈이와도 안심! §f]"); break;
			case 25: meta.setDisplayName("§f[§6 색맹 §f]"); break;
			case 26: meta.setDisplayName("§f[§6 근성인 §f]"); break;
			case 27: meta.setDisplayName("§f[§6 여긴어디? §f]"); break;
			case 28: meta.setDisplayName("§f[§6 왓더헬 §f]"); break;
			case 29: meta.setDisplayName("§f[§6 킬러조 §f]"); break;
			case 30: meta.setDisplayName("§f[§6 범인은 바로 당신이야! §f]"); break;
			case 31: meta.setDisplayName("§f[§6 제니퍼 로렌스 §f]"); break;
			case 32: meta.setDisplayName("§f[§6 독심술사 §f]"); break;
			case 33: meta.setDisplayName("§f[§6 순발력 향상 §f]"); break;
			case 34: meta.setDisplayName("§f[§6 나는야 축구왕 §f]"); break;
			case 35: meta.setDisplayName("§f[§6 테러범의 집 §f]"); break;
			case 36: meta.setDisplayName("§f[§6 물약제조 §f]"); break;
			case 37: meta.setDisplayName("§f[§6 마녀의 비밀장소 §f]"); break;
			case 38: meta.setDisplayName("§f[§6 포격준비 §f]"); break;
			case 39: meta.setDisplayName("§f[§6 귀차니즘 §f]"); break;
			default: break;
			}
			item.setItemMeta(meta);
			baseQuest.setItem(i1, item);	
		}
		
		for(int i = 0; i <= 53; i += 9){
			prefixshop.setItem(i, deco);
		}
		for(int i = 8; i <= 53; i += 9){
			prefixshop.setItem(i, deco);
		}
		
		parkourinven = Bukkit.createInventory(null, 27, "§c§l파쿠르");
		
		for(int i = 0; i <= 9; i++){
			parkourinven.setItem(i, deco);
		}
		for(int i = 17; i < 27; i++){
			parkourinven.setItem(i, deco);
		}
		
		item = new ItemStack(Material.PISTON_STICKY_BASE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e훈련장");
		item.setItemMeta(meta);
		parkourinven.setItem(10, item);
		
		item = new ItemStack(Material.PISTON_BASE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e실전 훈련장");
		item.setItemMeta(meta);
		parkourinven.setItem(12, item);
		
		item = new ItemStack(Material.BEACON, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e파쿠르 레이싱");
		item.setItemMeta(meta);
		parkourinven.setItem(14, item);
		
		item = new ItemStack(Material.JACK_O_LANTERN, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§e파쿠르 호박전쟁");
		item.setItemMeta(meta);
		parkourinven.setItem(16, item);
		
		mobarenainven = Bukkit.createInventory(null, 27, "§c§l몹 아레나");
		
		for(int i = 0; i <= 9; i++){
			mobarenainven.setItem(i, deco);
		}
		for(int i = 17; i < 27; i++){
			mobarenainven.setItem(i, deco);
		}
		
		patchNote = Bukkit.createInventory(null, 9, "§c§l패치노트");
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e패치노트 17/2/21 (1)§f ]");
		lorelist.add("§f- §6살인마를 찾아라4");
		lorelist.add("§7※ 암살자 직업 삭제");
		lorelist.add("§7※ 배신자 직업 추가");
		lorelist.add("§7※ 맵 변경");
		lorelist.add("§f");
		lorelist.add("§f- §6히어로즈워");
		lorelist.add("§7※ 모든 직업의 궁극기 쿨타임 조정 (감소)");
		lorelist.add("§7※ 궁극기 비용 24개 -> 16개 로 변경");
		lorelist.add("§7※ 패시브 비용 12개 -> 8개 로 변경");
		lorelist.add("§7※ 티켓 100개씩 추가");
		lorelist.add("§7※ 임라프 최소 공격력 1감소");
		lorelist.add("§7※ 종료 문구 수정");
		lorelist.add("§7※ 지형 수정");
		lorelist.add("§7※ 사망시 스폰으로 갔을때 부활이 적용되던 버그 수정");
		lorelist.add("§7※ 각 팀 기지 근처에서는 데미지를 입지않게 했습니다.");
		lorelist.add("§7※ 각 팀 포탑 근처에서는 데미지가 절반이 되게 했습니다.");
		lorelist.add("§7※ 영역생성자, 포탈 등등이 무슨팀의 것인지 양털로 구분이 가능합니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		patchNote.setItem(0, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e패치노트 17/11/25§f ]");
		lorelist.add("§f- §6스케치 퀴즈");
		lorelist.add("§7※ 답을 여러개 적었을 시 그 중 한개라도 답이 있으면");
		lorelist.add("§7   정답으로 인식되는 버그 수정");
		lorelist.add("§f");
		lorelist.add("§f- §6히어로즈워");
		lorelist.add("§7※ 포탈의 3번째 스킬로인해 NPC의 위치가 변경되는 버그 수정");
		lorelist.add("§f");
		lorelist.add("§f- §6팀 랜덤 배틀");
		lorelist.add("§7※ 게임 종료후 생존시 레벨이 사라지지 않는 버그 수정");
		lorelist.add("§f");
		lorelist.add("§f- §6파쿠르");
		lorelist.add("§7※ 매달리기 후 오를때 점프력 향상");
		lorelist.add("§f");
		lorelist.add("§f- §6헝거게임");
		lorelist.add("§7※ 게임 종료후 떨어진 아이템이 사라지지 않는 버그 수정");
		lorelist.add("§f");
		lorelist.add("§f- §6블럭 숨바꼭질");
		lorelist.add("§7※ 게임 종료후 모두가 승리처리되던 버그 수정");
		lorelist.add("§f");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		patchNote.setItem(2, item);
		
		/*item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		lorelist.clear();
		meta.setDisplayName("§f[ §e패치노트 9/22 (3)§f ]");
		lorelist.add("§f- §6모루 피하기");
		lorelist.add("§7※ 모루가 떨어지는 시스템 변경");
		lorelist.add("§7-> §c기존 모루 떨어지는 방식 (0.05초당 1개씩)");
		lorelist.add("§7-> §c초반에 모루가 0.1초마다 1개씩 떨어집니다.");
		lorelist.add("§7-> §c10초마다 아래로부터 3칸위까지에 존재하는 모루는 사라집니다.");
		lorelist.add("§7-> §c20초마다 0.1초당 떨어지는 모루의 개수가 1개씩 증가합니다.");
		lorelist.add("§f");
		lorelist.add("§f- §6자리뺏기");
		lorelist.add("§7※ 이제 마인카트를 연타해도 탄 사람이 내려지지 않도록 변경했습니다.");
		lorelist.add("§f");
		lorelist.add("§f- §6히어로즈워");
		lorelist.add("§7※ 포탈의 궁극기가 포탈의 3번째 스킬로 변경되었습니다.");
		lorelist.add("§7※ 포탈 위치변경 스킬 유효범위 40칸 -> 25칸으로 수정");
		lorelist.add("§7※ 포탈 위치변경 스킬 쿨타임 39초 -> 19초로 수정");
		lorelist.add("§7※ 포탈의 새로운 궁극기 차원 주머니 스킬이 추가됐습니다.");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		patchNote.setItem(7, item);*/
		
		saveInventoryToFile(patchNote, this.getDataFolder(), patchNote.getTitle());
		saveInventoryToFile(Serverhelp, this.getDataFolder(), Serverhelp.getTitle());
		saveInventoryToFile(Kit_Lobby, this.getDataFolder(), Kit_Lobby.getTitle());
		saveInventoryToFile(Minigame, this.getDataFolder(), Minigame.getTitle());
		saveInventoryToFile(prefixshop, this.getDataFolder(), prefixshop.getTitle());
		saveInventoryToFile(mobarenainven, this.getDataFolder(), mobarenainven.getTitle());
		saveInventoryToFile(serverrule, this.getDataFolder(), serverrule.getTitle());
		saveInventoryToFile(parkourinven, this.getDataFolder(), parkourinven.getTitle());
		saveInventoryToFile(baseQuest, this.getDataFolder(), baseQuest.getTitle());
		
		LoadInventory();
		}
	
	private void LoadInventory(){
		Kit_Lobby = getInventoryFromFile(new File(this.getDataFolder(), "스폰킷.invsave"));
		Minigame = getInventoryFromFile(new File(this.getDataFolder(), "§c§l미니게임.invsave"));
		patchNote = getInventoryFromFile(new File(this.getDataFolder(), "§c§l패치노트.invsave"));
		Serverhelp = getInventoryFromFile(new File(this.getDataFolder(), "§c§l서버 도움말.invsave"));
		prefixshop = getInventoryFromFile(new File(this.getDataFolder(), "§c§l칭호 상점.invsave"));
		mobarenainven = getInventoryFromFile(new File(this.getDataFolder(), "§c§l몹 아레나.invsave"));
		serverrule = getInventoryFromFile(new File(this.getDataFolder(), "§c§l서버규칙.invsave"));
		parkourinven = getInventoryFromFile(new File(this.getDataFolder(), "§c§l파쿠르.invsave"));
		baseQuest = getInventoryFromFile(new File(this.getDataFolder(), "§c§l도전과제.invsave"));
	}
	
	public void onDisable(){
		getLogger().info("EG서버 메인 시스템이 언로드 되었습니다.");
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String[] args){
		if(talker instanceof Player){
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("egs")){
				if(args.length <= 0){
					Helpmessages(p);
					return true;
				}else{
					EGScommand(p, args);
					return true;
				}
			} else if(str.equalsIgnoreCase("확성기")){
				AllVoice(p, args);
				return true;
			} else if(str.equalsIgnoreCase("문의")){
				Response(p, args);
				return true;
			} else if(str.equalsIgnoreCase("포인트")){
				Pointcommand(p, args);
				return true;
			} else if(str.equalsIgnoreCase("꺼내줘")){
				if(Checkcooldown(p, "꺼내줘")){
					Setcooldown(p, "꺼내줘", 20);
					Location loc = p.getLocation();
					loc.setY(loc.getY()+1);
					p.teleport(loc);
				}
				return true;
			} else if(str.equalsIgnoreCase("채팅")){
				chatRoomCommand(p, args);
			} else if(str.equalsIgnoreCase("확성기끄기")){
				noAllVoice(p);
			} else if(str.equalsIgnoreCase("짓걸이렴")){
				SoloVoice(p, args);
			}
		}
		return false;
	}
	
	private void SoloVoice(Player p, String[] args){
		if(args.length > 0){
			String t_name = args[0];
			if(solochat.contains(t_name)){
				solochat.remove(t_name);
				p.sendMessage(MS+t_name+" 님을 해제했습니다.");
			} else {
				solochat.add(t_name);
				p.sendMessage(MS+t_name+" 님은 이제 혼자서 채팅을 치게 됩니다.");	
			}
			getConfig().set("solochatlist", solochat);
			saveConfig();
		} else {
			p.sendMessage(MS+"/짓걸이렴 <닉네임> - <닉네임> 플레이어의 채팅은 자신을 제외한 누구도 보지 못하게 됩니다.");
		}
	}
	
	private void Helpmessages(Player p){
		p.sendMessage(MS+"/egs ac - 오피채팅");
		p.sendMessage(MS+"/egs say - 공지알림");
		p.sendMessage(MS+"/egs setspawn - 스폰설정");
		p.sendMessage(MS+"/egs setlobby - 로비설정");
		p.sendMessage(MS+"/egs mute <닉네임> - 확성기 뮤트");
		p.sendMessage(MS+"/egs unmute - 확성기 뮤트해제");
	}
	
	private void Pointcommand(Player p, String[] args){
		if(args.length <= 0){
			p.sendMessage(MS+"§b/포인트 확인 §7- 자신의 포인트를 확인합니다.");
			p.sendMessage(MS+"§b/포인트 보내기 <닉네임> <수량> <메세지> §7- 타인에게 포인트를 보냅니다.");
			return;
		} else if(args[0].equalsIgnoreCase("보내기")){
			if(args.length <= 1){
				p.sendMessage(MS+"§c보낼 사람의 닉네임을 적어주세요.");
				return;
			} 
			String target = args[1];
			if(getServer().getPlayer(target) == null){
				p.sendMessage(MS+"§c"+target+" 님은 온라인이 아닙니다.");
				return;
			}
			if(args.length <= 2){
				p.sendMessage(MS+"§c보낼 수량을 적어주세요.");
				return;
			} 
			int amt = 0;
			try{
				amt = Integer.valueOf(args[2]);
			} catch(Exception e){
				p.sendMessage(MS+"§c0이상의 숫자만 입력해주세요.");
			}
			if(econ.getBalance(p.getName()) < amt){
				p.sendMessage(MS+(amt-econ.getBalance(p.getName())+" 만큼의 포인트가 부족합니다."));
				return;
			}
			if(args.length <= 3){
				p.sendMessage(MS+"§c메세지를 적어주세요.");
				return;
			}
			String msg = "";
			for(int i = 3; i < args.length; i++){
				msg += " "+args[i];
			}
			econ.withdrawPlayer(p.getName(), amt);
			econ.depositPlayer(target, amt);
			p.sendMessage(MS+target+" 님께 "+amt+"포인트를 보냈습니다.");
			Player t = getServer().getPlayer(target);
			t.sendMessage(MS+p.getName()+" 님께서 "+amt+"포인트를 보내셨습니다.");
			t.sendMessage(MS+"첨부된 메세지 : §6"+msg);
			t.playSound(t.getLocation(), Sound.ITEM_PICKUP, 2.0f, 1.0f);
			return;
		} else if(args[0].equalsIgnoreCase("확인")){
			p.sendMessage(MS+"소유한 포인트 : §c"+econ.getBalance(p.getName())+" §P");
			return;
		}
	}
	
	private void EGScommand(Player p, String[] args){
		if(args[0].equalsIgnoreCase("setspawn") && p.isOp()){
			Location loc = p.getLocation();
			p.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ());
			p.sendMessage(MS+"스폰 설정완료");
		} else if(args[0].equalsIgnoreCase("setinvdata") && p.isOp()){
			SetInventory();
			LoadInventory();
			p.sendMessage(MS+"설정완료");
		} else if(args[0].equalsIgnoreCase("tppos") && p.isOp()){
			p.teleport(new Location(p.getWorld(), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3])));
		} else if(args[0].equalsIgnoreCase("setlobby")){
			getConfig().set("Lobby_world", p.getWorld().getName());
			getConfig().set("Lobby_x", p.getLocation().getBlockX());
			getConfig().set("Lobby_y", p.getLocation().getBlockY()+1);
			getConfig().set("Lobby_z", p.getLocation().getBlockZ());
			getConfig().set("Lobby_pitch", p.getLocation().getPitch());
			getConfig().set("Lobby_yaw", p.getLocation().getYaw());
			saveConfig();
			LoadConfig();
			p.sendMessage(MS+"로비 설정완료");
			
		} else if(args[0].equalsIgnoreCase("ac") && p.hasPermission("opchat")){
			Player target = null;
			for(Player t : aclist){
				if(p.getName().equalsIgnoreCase(t.getName())){
					target = t; break;
				}
			}
			if(target != null){
				aclist.remove(target);
				p.sendMessage(MS+"오피 전용 채팅을 껐습니다.");
			} else {
				aclist.add(p);
				p.sendMessage(MS+"오피 전용 채팅을 켰습니다.");
			}
		} else if(args[0].equalsIgnoreCase("say")&& p.isOp()){
			if(args.length <= 1){
				p.sendMessage(MS+"/egs say <할말>"); return;
			}
			String str = "";
			for(int i = 1; i < args.length; i++){
				str += " "+args[i];
			}
			Bukkit.broadcastMessage("§f[ §c§l알림 §f]§b§l"+str);
		} else if(args[0].equalsIgnoreCase("mute") && p.isOp()){
			if(args.length <= 2){
				if(args.length <= 1) p.sendMessage(MS+"닉네임을 입력해주세요.");
				else p.sendMessage(MS+"시간을 입력해주세요.");
				p.sendMessage(MS+"/egs mute <닉네임> <시간>");
			} else {
				long time = 0;
				try{
					time = Long.valueOf(args[2]);
				}catch(Exception e){
					p.sendMessage(MS+"시간은 숫자로 입력해주세요.");
					return;
				}
				Player t = Bukkit.getPlayer(args[1]);
				if(muteList.containsKey(t.getName())){
					p.sendMessage(MS+"이미 확성기 뮤트 상태인 플레이어입니다.");
					return;
				}
				if(!t.isOnline()){
					p.sendMessage(MS+args[1]+"님은 서버에 접속중이지 않습니다.");
					return;
				}
				muteAllVoice(p, t, time);
			}
		} else if(args[0].equalsIgnoreCase("unmute") && p.isOp()){
			if(args.length <= 1){
				p.sendMessage(MS+"닉네임을 입력해주세요.");
				p.sendMessage(MS+"/egs unmute <닉네임>");
				return;
			} else {
				if(muteList.containsKey(args[1])){
					Bukkit.getScheduler().cancelTask(muteList.get(args[1]));
					muteList.remove(args[1]);
					p.sendMessage(MS+args[1]+"님은 이제 확성기를 사용하실 수 있습니다.");
				} else {
					p.sendMessage(MS+args[1]+"님은 확성기 금지상태가 아닙니다.");
				}
			}
		}
	}
	
	public void muteAllVoice(final Player p, final Player t, long time){
		t.sendMessage(MS+p.getName()+"님이 당신을 "+time+"초 동안 확성기 금지상태로 설정하셨습니다.");
		p.sendMessage(MS+t.getName()+"님을 "+time+"초간 확성기 금지상태로 설정하였습니다.");
		muteList.put(t.getName(), Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
			public void run(){
				if(muteList.containsKey(t.getName())){
					muteList.remove(t.getName());
					t.sendMessage(MS+"이제 확성기를 사용하실 수 있습니다.");
					p.sendMessage(MS+t.getName()+"님의 확성기 금지상태가 해제되었습니다.");
				}
			}
		}, time*20));
	}
	
	public void AllVoice(Player p, String[] args){
		if(args.length <= 0){
			p.sendMessage(MS+"/확성기 <할말>"); return;
		}
		if(econ.getBalance(p.getName()) < 300){
			p.sendMessage(MS+"확성기를 사용하시려면 300포인트가 필요합니다.");return;
		}
		if(Checkcooldown(p, "확성기")){
			if(muteList.containsKey(p.getName())){
				p.sendMessage("당신은 확성기 금지 상태입니다.");
			} else {
				Setcooldown(p, "확성기", 20);
				EconomyResponse r = econ.withdrawPlayer(p.getName(), 300);
				String str = "";
				for(int i = 0; i < args.length; i++){
					str += " "+args[i];
				}
				str.replace("&", "");
				str.replace("§", "");
				p.sendMessage(MS+"확성기 비용으로 300포인트를 사용하셨습니다.");
				Player[] onlinePlayers = Bukkit.getOnlinePlayers();
				for(int i = 0; i < onlinePlayers.length; i++){
					Player recivePlayer = onlinePlayers[i];
					if(!noAllVoiceList.contains(recivePlayer.getName()))
					recivePlayer.sendMessage("§f[ §a§l확성기 §f] §6"+p.getName()+" §f: §a"+str);
				}
		
			}
		}
	}
	
	public void noAllVoice(Player p){
		String pName = p.getName();
		if(noAllVoiceList.contains(pName)){
			noAllVoiceList.remove(pName);
			p.sendMessage(MS+"확성기 차단을 해제했습니다.");
		} else {
			noAllVoiceList.add(pName);
			p.sendMessage(MS+"모든 확성기 메세지를 차단합니다.");
		}
	}
	
	public void SendOPchat(String str){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission("opchat")){
				p.sendMessage(str);
			}
		}
	}
	
	public void Response(Player p, String[] args){
		if(args.length <= 0){
			p.sendMessage(MS+"/문의 <의견>"); return;
		}
		String str = "";
		for(int i = 0; i < args.length; i++){
			str += " "+args[i];
		}
		String receiveList = "";
		for(OfflinePlayer op : getServer().getOperators()){
			if(op.isOnline()){
				Player t = (Player) op;
				t.sendMessage("§f[ §4§l문의 §f]"+p.getName()+" : "+str);
				receiveList += t.getName()+" | ";
			}
		}
		if(receiveList.equalsIgnoreCase("")){
			p.sendMessage(MS+"현재 문의를 받을수 있는 OP가 서버에 접속중이지 않습니다.");
		}else p.sendMessage(MS+"문의를 보냈습니다. 잠시만 기다려주세요.\n문의를 받은 OP목록 : "+receiveList);
	}
	
	public void chatRoomCommand(Player p, String args[]){
		
	}
	
	public static String getHandItemName(Player p){
		ItemStack item = p.getItemInHand();
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return null;
		return item.getItemMeta().getDisplayName();
	}
	
	public static boolean saveInventoryToFile(Inventory inventory, File path, String fileName) {
		if (inventory == null || path == null || fileName == null) return false;
		try {
			File invFile = new File(path, fileName + ".invsave");
			if (invFile.exists()) invFile.delete();
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);

			invConfig.set("Title", inventory.getTitle());
			invConfig.set("Size", inventory.getSize());
			invConfig.set("Max stack size", inventory.getMaxStackSize());

			ItemStack[] invContents = inventory.getContents();
			for (int i = 0; i < invContents.length; i++) {
				ItemStack itemInInv = invContents[i];
				if (itemInInv != null) if (itemInInv.getType() != Material.AIR) invConfig.set("Slot " + i, itemInInv);
			}

			invConfig.save(invFile);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean questDataToFile(Inventory inventory, String fileName) {
		if (inventory == null || fileName == null) return false;
		try {
			File invFile = new File(instance.getDataFolder().getPath()+"/quest", fileName + ".quest");
			if (invFile.exists()) invFile.delete();
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);

			invConfig.set("Title", inventory.getTitle());
			invConfig.set("Size", inventory.getSize());
			invConfig.set("Max stack size", inventory.getMaxStackSize());

			ItemStack[] invContents = inventory.getContents();
			for (int i = 0; i < invContents.length; i++) {
				ItemStack itemInInv = invContents[i];
				if (itemInInv != null) if (itemInInv.getType() != Material.AIR) invConfig.set("Slot " + i, itemInInv);
			}

			invConfig.save(invFile);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public void UpdateScheduler(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run(){
				UpdateMinigame();
				if(--daycnt < 0){
					daycnt = 60;
					getServer().getWorld("world").setTime(8000);
					broadcastcnt++;
					switch(broadcastcnt){
					case 1:
						Bukkit.broadcastMessage("[§cTip§f] §6게임 도중 게임에서 퇴장하시려면 §b'/spawn' §6또는 §b'/스폰'§6 명령어를 입력해주세요."); break;
					case 2:
						Bukkit.broadcastMessage("[§cTip§f] §6모든 플레이어에게 할 말씀이 있으시다면 §b'/확성기' §6명령어를 사용하세요."); break;
					case 3:
						Bukkit.broadcastMessage("[§cTip§f] §6포인트 관련 기능은 §b'/포인트' §6명령어를 사용하세요."); break;
					case 4:
						Bukkit.broadcastMessage("[§cTip§f] §6실력보다 중요한 것은 §b매너§6입니다."); break;
					case 5:
						Bukkit.broadcastMessage("[§cTip§f] §6게임 건의는 §b카페§6에 해주세요."); break;
					case 6:
						Bukkit.broadcastMessage("[§cTip§f] §6카페 주소 : §ehttp://cafe.naver.com/boli2"); break;
					case 7:
						Bukkit.broadcastMessage("[§cTip§f] §6블럭에 끼셨을 때는 §c'/꺼내줘' §6명령어를 입력해주세요."); break;
					case 8:
						Bukkit.broadcastMessage("[§cTip§f] §6버그 발견, 플레이어의 신고, 질문 등등은 §b'/문의' §6명령어를 사용하세요."); broadcastcnt = 0; break;
					default: broadcastcnt = 0; break;
					}
				}
			}
		}, 0L, 20L);
	}
	
	public void UpdateMinigame(){
		List<String> lorelist = new ArrayList<String>();
		
		lorelist.clear();
		if(me.Bokum.FindTheMurder.GameSystem.checkstart){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§c남은 인원 : \n§7살인마 팀 : §f"+me.Bokum.FindTheMurder.GameSystem.mlist.size()+"명\n §7시민팀 : §f"+me.Bokum.FindTheMurder.GameSystem.Getsurviver()+"명");
			if(me.Bokum.FindTheMurder.Main.jlist.size() >= 1 && (me.Bokum.FindTheMurder.GameSystem.mlist.size() <= 0 || me.Bokum.FindTheMurder.GameSystem.Getsurviver() <= 0)){
				if(me.Bokum.FindTheMurder.Main.Forcestoptimer >= 15){
					me.Bokum.FindTheMurder.GameSystem.ForceEnd();
					me.Bokum.FindTheMurder.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.FindTheMurder.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.FindTheMurder.GameSystem.lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.FindTheMurder.Main.list.size()+"명 / 13명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.FindTheMurder.Main.list.size()+"명 / 13명");
			lorelist.add("§f- §e최소인원 : 5명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		ItemMeta meta = Minigame.getItem(10).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(10).setItemMeta(meta);
		Minigame.getItem(10).setAmount(me.Bokum.FindTheMurder.Main.list.size() <= 0 ? 1 : me.Bokum.FindTheMurder.Main.list.size());
		
		lorelist.clear();
		if(me.Bokum.TRB.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§c현재 라운드 §f: §7"+me.Bokum.TRB.Main.round+" 라운드");
			lorelist.add("§e레드팀 승리수 §f: §7"+me.Bokum.TRB.Main.redscore+" 승");
			lorelist.add("§e블루팀 승리수 §f: §7"+me.Bokum.TRB.Main.bluescore+" 승");
		} else if(me.Bokum.TRB.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.TRB.Main.plist.size()+"명 / 12명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.TRB.Main.plist.size()+"명 / 12명");
			lorelist.add("§f- §e최소인원 : 6명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(12).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(12).setItemMeta(meta);
		Minigame.getItem(12).setAmount(me.Bokum.TRB.Main.plist.size() <= 0 ? 1 : me.Bokum.TRB.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.EWG.Main.check_start){
			lorelist.add("§f- §c게임 시작됨 ( 재참가 가능 )");
			lorelist.add("§e남은 제한 시간 : §7"+me.Bokum.EWG.Main.timertime+"분");
			lorelist.add("§f- §c관전하시려면 클릭해주세요.");
		} else if(me.Bokum.EWG.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.EWG.Main.plist.size()+"명 / 12명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.EWG.Main.plist.size()+"명 / 12명");
			lorelist.add("§f- §e최소인원 : 6명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(14).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(14).setItemMeta(meta);
		Minigame.getItem(14).setAmount(me.Bokum.EWG.Main.plist.size() <= 0 ? 1 : me.Bokum.EWG.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.DTC.Main.check_start){
			lorelist.add("§f- §c게임 시작됨 ( 중도참여 가능 )");
			lorelist.add("§e레드팀이 획득한 깃발 : §7"+me.Bokum.DTC.Main.Redcore_cnt+"개");
			lorelist.add("§e블루팀이 획득한 깃발 : §7"+me.Bokum.DTC.Main.Bluecore_cnt+"개");
		} else if(me.Bokum.DTC.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 20명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.DTC.Main.plist.size()+"명 / 20명");
			lorelist.add("§f- §e최소인원 : 8명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(16).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(16).setItemMeta(meta);
		Minigame.getItem(16).setAmount(me.Bokum.DTC.Main.plist.size() <= 0 ? 1 : me.Bokum.DTC.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.FirstHit.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e선두 플레이어의 킬수 : §6"+me.Bokum.FirstHit.Main.topkill);
			if(me.Bokum.FirstHit.Main.plist.size() <= 0){
				if(me.Bokum.FirstHit.Main.Forcestoptimer >= 15){
					me.Bokum.FirstHit.Main.Forcestop();
					me.Bokum.FirstHit.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.FirstHit.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.FirstHit.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.FirstHit.Main.plist.size()+"명 / 7명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.FirstHit.Main.plist.size()+"명 / 7명");
			lorelist.add("§f- §e최소인원 : 3명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(19).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(19).setItemMeta(meta);
		Minigame.getItem(19).setAmount(me.Bokum.FirstHit.Main.plist.size() <= 0 ? 1 : me.Bokum.FirstHit.Main.plist.size());
		
		
		lorelist.clear();
		if(me.Bokum.KTT.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.KTT.Main.plist.size()+"명");
			if(me.Bokum.KTT.Main.plist.size() <= 0){
				if(me.Bokum.KTT.Main.Forcestoptimer >= 15){
					me.Bokum.KTT.Main.Forcestop();
					me.Bokum.KTT.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.KTT.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.KTT.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.KTT.Main.plist.size()+"명 / 9명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.KTT.Main.plist.size()+"명 / 9명");
			lorelist.add("§f- §e최소인원 : 4명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(21).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(21).setItemMeta(meta);
		Minigame.getItem(21).setAmount(me.Bokum.KTT.Main.plist.size() <= 0 ? 1 : me.Bokum.KTT.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.Hungergame.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.Hungergame.Main.plist.size()+"명");
			if(me.Bokum.Hungergame.Main.plist.size() <= 0){
				if(me.Bokum.Hungergame.Main.Forcestoptimer >= 15){
					me.Bokum.Hungergame.Main.Forcestop();
					me.Bokum.Hungergame.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.Hungergame.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.Hungergame.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Hungergame.Main.plist.size()+"명 / 8명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Hungergame.Main.plist.size()+"명 / 8명");
			lorelist.add("§f- §e최소인원 : 3명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(23).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(23).setItemMeta(meta);
		Minigame.getItem(23).setAmount(me.Bokum.Hungergame.Main.plist.size() <= 0 ? 1 : me.Bokum.Hungergame.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.ESP.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.ESP.Main.plist.size()+"명");
			if(me.Bokum.ESP.Main.plist.size() <= 0){
				if(me.Bokum.ESP.Main.Forcestoptimer >= 15){
					me.Bokum.ESP.Main.Forcestop();
					me.Bokum.ESP.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.ESP.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.ESP.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.ESP.Main.plist.size()+"명 / 7명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.ESP.Main.plist.size()+"명 / 7명");
			lorelist.add("§f- §e최소인원 : 3명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(30).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(30).setItemMeta(meta);
		Minigame.getItem(30).setAmount(me.Bokum.ESP.Main.plist.size() <= 0 ? 1 : me.Bokum.ESP.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.ECM.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.ECM.Main.plist.size()+"명");
			if(me.Bokum.ECM.Main.plist.size() <= 0){
				if(me.Bokum.ECM.Main.Forcestoptimer >= 15){
					me.Bokum.ECM.Main.Forcestop();
					me.Bokum.ECM.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.ECM.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.ECM.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.ECM.Main.plist.size()+"명 / 6명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.ECM.Main.plist.size()+"명 / 6명");
			lorelist.add("§f- §e최소인원 : 2명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(34).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(34).setItemMeta(meta);
		Minigame.getItem(34).setAmount(me.Bokum.ECM.Main.plist.size() <= 0 ? 1 : me.Bokum.ECM.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.Catcher.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.Catcher.Main.plist.size()+"명");
			if(me.Bokum.Catcher.Main.plist.size() <= 0){
				if(me.Bokum.Catcher.Main.Forcestoptimer >= 15){
					me.Bokum.Catcher.Main.Forcestop();
					me.Bokum.Catcher.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.Catcher.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.Catcher.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Catcher.Main.plist.size()+"명 / 8명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Catcher.Main.plist.size()+"명 / 8명");
			lorelist.add("§f- §e최소인원 : 4명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(37).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(37).setItemMeta(meta);
		Minigame.getItem(37).setAmount(me.Bokum.Catcher.Main.plist.size() <= 0 ? 1 : me.Bokum.Catcher.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.CTM.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e현재 라운드 : §6"+me.Bokum.CTM.Main.round+" / 10");
			if(me.Bokum.CTM.Main.plist.size() <= 0){
				if(me.Bokum.CTM.Main.Forcestoptimer >= 15){
					me.Bokum.CTM.Main.Forcestop();
					me.Bokum.CTM.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.CTM.Main.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.CTM.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.CTM.Main.plist.size()+"명 / 6명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.CTM.Main.plist.size()+"명 / 6명");
			lorelist.add("§f- §e최소인원 : 4명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(28).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(28).setItemMeta(meta);
		Minigame.getItem(28).setAmount(me.Bokum.CTM.Main.plist.size() <= 0 ? 1 : me.Bokum.CTM.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.Parkour.Main.check_start_race){
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Parkour.Main.tlist.size()+"명 / 10명");
			lorelist.add("§f- §c게임 시작됨");
			if(me.Bokum.Parkour.Main.tlist.size() <= 0){
				if(me.Bokum.Parkour.Main.Forcestoptimer_race >= 15){
					me.Bokum.Parkour.Main.Forcestop_race();
					me.Bokum.Parkour.Main.Forcestoptimer_race = 0;
				}else{
					me.Bokum.Parkour.Main.Forcestoptimer_race++;
				}
			}
		} else if(me.Bokum.Parkour.Main.check_lobbystart_race) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Parkour.Main.tlist.size()+"명 / 10명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Parkour.Main.tlist.size()+"명 / 10명");
			lorelist.add("§f- §e최소인원 : 1명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = parkourinven.getItem(14).getItemMeta();
		meta.setLore(lorelist);
		parkourinven.getItem(14).setItemMeta(meta);
		parkourinven.getItem(14).setAmount(me.Bokum.Parkour.Main.tlist.size() <= 0 ? 1 : me.Bokum.Parkour.Main.tlist.size());
		
		lorelist.clear();
		if(me.Bokum.Parkour.Main.check_start_bed){
			lorelist.add("§f- §e남은인원 : §6"+me.Bokum.Parkour.Main.blist.size()+"명");
			lorelist.add("§f- §c게임 시작됨");
			if(me.Bokum.Parkour.Main.blist.size() <= 0){
				if(me.Bokum.Parkour.Main.Forcestoptimer_bed >= 15){
					me.Bokum.Parkour.Main.Forcestop_bed();
					me.Bokum.Parkour.Main.Forcestoptimer_bed = 0;
				}else{
					me.Bokum.Parkour.Main.Forcestoptimer_bed++;
				}
			}
		} else if(me.Bokum.Parkour.Main.check_lobbystart_bed) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Parkour.Main.blist.size()+"명 / 15명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.Parkour.Main.blist.size()+"명 / 15명");
			lorelist.add("§f- §e최소인원 : 7명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = parkourinven.getItem(16).getItemMeta();
		meta.setLore(lorelist);
		parkourinven.getItem(16).setItemMeta(meta);
		parkourinven.getItem(16).setAmount(me.Bokum.Parkour.Main.blist.size() <= 0 ? 1 : me.Bokum.Parkour.Main.blist.size());
		
		lorelist.clear();
		if(me.Bokum.MOB.Game.MobSystem.check_start){
			lorelist.add("§f- §c게임 시작됨 ( 재참가 가능 )");
			lorelist.add("§e블루팀 티켓수 : §b"+me.Bokum.MOB.Game.MobSystem.blue_ticket+"§6개");
			lorelist.add("§e레드팀 티켓수 : §b"+me.Bokum.MOB.Game.MobSystem.red_ticket+"§6개");
		} else if(me.Bokum.MOB.Game.MobSystem.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.MOB.Game.MobSystem.plist.size()+"명 / 34명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.MOB.Game.MobSystem.plist.size()+"명 / 34명");
			lorelist.add("§f- §e최소인원 : 20명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(25).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(25).setItemMeta(meta);
		Minigame.getItem(25).setAmount(me.Bokum.MOB.Game.MobSystem.plist.size() <= 0 ? 1 : me.Bokum.MOB.Game.MobSystem.plist.size());
		
		lorelist.clear();
		if(me.Bokum.BlockHunter.Main.check_start){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 도망자 : §6"+me.Bokum.BlockHunter.Main.blist.size()+"명");
			lorelist.add("§e남은 술래 : §6"+me.Bokum.BlockHunter.Main.clist.size()+"명");
			/*if(me.Bokum.BlockHunter.Main.blist.size() <= 0){
				if(me.Bokum.BlockHunter.Main.Forcestoptimer >= 15){
					me.Bokum.BlockHunter.Main.Forcestop();
					me.Bokum.BlockHunter.Main.Forcestoptimer = 0;
				}else{
					me.Bokum.BlockHunter.Main.Forcestoptimer++;
				}
			}*/
		} else if(me.Bokum.BlockHunter.Main.check_lobbystart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 10명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.BlockHunter.Main.plist.size()+"명 / 10명");
			lorelist.add("§f- §e최소인원 : 5명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(41).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(41).setItemMeta(meta);
		Minigame.getItem(41).setAmount(me.Bokum.BlockHunter.Main.plist.size() <= 0 ? 1 : me.Bokum.BlockHunter.Main.plist.size());
		
		lorelist.clear();
		if(me.Bokum.SMG.Main.avoidanvil.isStart){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.SMG.Main.avoidanvil.playerList.size()+"명");
			if(me.Bokum.SMG.Main.avoidanvil.playerList.size() <= 0){
				if(me.Bokum.SMG.Main.avoidanvil.Forcestoptimer >= 15){
					me.Bokum.SMG.Main.avoidanvil.forceStop();
					me.Bokum.SMG.Main.avoidanvil.Forcestoptimer = 0;
				}else{
					me.Bokum.SMG.Main.avoidanvil.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.SMG.Main.avoidanvil.isLobbyStart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.avoidanvil.playerList.size()+"명 / "+me.Bokum.SMG.Main.avoidanvil.maxPlayer+"명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.avoidanvil.playerList.size()+"명 / "+me.Bokum.SMG.Main.avoidanvil.maxPlayer+"명");
			lorelist.add("§f- §e최소인원 : "+me.Bokum.SMG.Main.avoidanvil.minPlayer+"명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(43).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(43).setItemMeta(meta);
		Minigame.getItem(43).setAmount(me.Bokum.SMG.Main.playingList.size() <= 0 ? 1 : me.Bokum.SMG.Main.avoidanvil.playerList.size());
		
		lorelist.clear();
		if(me.Bokum.SMG.Main.deathrun.isStart){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.SMG.Main.deathrun.playerList.size()+"명");
			if(me.Bokum.SMG.Main.deathrun.playerList.size() <= 0){
				if(me.Bokum.SMG.Main.deathrun.Forcestoptimer >= 15){
					me.Bokum.SMG.Main.deathrun.forceStop();
					me.Bokum.SMG.Main.deathrun.Forcestoptimer = 0;
				}else{
					me.Bokum.SMG.Main.deathrun.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.SMG.Main.deathrun.isLobbyStart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.deathrun.playerList.size()+"명 / "+me.Bokum.SMG.Main.deathrun.maxPlayer+"명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.deathrun.playerList.size()+"명 / "+me.Bokum.SMG.Main.deathrun.maxPlayer+"명");
			lorelist.add("§f- §e최소인원 : "+me.Bokum.SMG.Main.deathrun.minPlayer+"명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(46).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(46).setItemMeta(meta);
		Minigame.getItem(46).setAmount(me.Bokum.SMG.Main.playingList.size() <= 0 ? 1 : me.Bokum.SMG.Main.deathrun.playerList.size());
		
		lorelist.clear();
		if(me.Bokum.SMG.Main.takeseat.isStart){
			lorelist.add("§f- §c게임 시작됨");
			lorelist.add("§e남은 인원 : §6"+me.Bokum.SMG.Main.takeseat.playerList.size()+"명");
			if(me.Bokum.SMG.Main.takeseat.playerList.size() <= 0){
				if(me.Bokum.SMG.Main.takeseat.Forcestoptimer >= 15){
					me.Bokum.SMG.Main.takeseat.forceStop();
					me.Bokum.SMG.Main.takeseat.Forcestoptimer = 0;
				}else{
					me.Bokum.SMG.Main.takeseat.Forcestoptimer++;
				}
			}
		} else if(me.Bokum.SMG.Main.takeseat.isLobbyStart) {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.takeseat.playerList.size()+"명 / "+me.Bokum.SMG.Main.takeseat.maxPlayer+"명");
			lorelist.add("§f- §c시작 대기중 곧 게임이 시작됩니다.");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		} else {
			lorelist.add("§f- §e인원 : §6"+me.Bokum.SMG.Main.takeseat.playerList.size()+"명 / "+me.Bokum.SMG.Main.takeseat.maxPlayer+"명");
			lorelist.add("§f- §e최소인원 : "+me.Bokum.SMG.Main.takeseat.minPlayer+"명");
			lorelist.add("§f- §b참여 하시려면 클릭해주세요.");
		}
		meta = Minigame.getItem(48).getItemMeta();
		meta.setLore(lorelist);
		Minigame.getItem(48).setItemMeta(meta);
		Minigame.getItem(48).setAmount(me.Bokum.SMG.Main.playingList.size() <= 0 ? 1 : me.Bokum.SMG.Main.takeseat.playerList.size());
	}
	
	public static Inventory getInventoryFromFile(File file) {
		if (file == null) return null;
		if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".invsave")) return null;
		try {
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
			Inventory inventory = null;
			String invTitle = invConfig.getString("Title", "Inventory");
			int invSize = invConfig.getInt("Size", 27);
			int invMaxStackSize = invConfig.getInt("Max stack size", 64);
			InventoryHolder invHolder = null;
			if (invConfig.contains("Holder")) invHolder = Bukkit.getPlayer(invConfig.getString("Holder"));
			inventory = Bukkit.getServer().createInventory(invHolder, invSize, ChatColor.translateAlternateColorCodes('§', invTitle));
			inventory.setMaxStackSize(invMaxStackSize);
			try {
				ItemStack[] invContents = new ItemStack[invSize];
				for (int i = 0; i < invSize; i++) {
					if (invConfig.contains("Slot " + i)) invContents[i] = invConfig.getItemStack("Slot " + i);
					else invContents[i] = new ItemStack(Material.AIR);
				}
				inventory.setContents(invContents);
			} catch (Exception ex) {
			}
			return inventory;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Inventory getQuestData(String name) {
		File file = new File(instance.getDataFolder().getPath()+"/quest", name+".quest");
		if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".quest")) return null;
		try {
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
			Inventory inventory = null;
			String invTitle = invConfig.getString("Title", "Inventory");
			int invSize = invConfig.getInt("Size", 27);
			int invMaxStackSize = invConfig.getInt("Max stack size", 64);
			InventoryHolder invHolder = null;
			if (invConfig.contains("Holder")) invHolder = Bukkit.getPlayer(invConfig.getString("Holder"));
			inventory = Bukkit.getServer().createInventory(invHolder, invSize, ChatColor.translateAlternateColorCodes('§', invTitle));
			inventory.setMaxStackSize(invMaxStackSize);
			try {
				ItemStack[] invContents = new ItemStack[invSize];
				for (int i = 0; i < invSize; i++) {
					if (invConfig.contains("Slot " + i)) invContents[i] = invConfig.getItemStack("Slot " + i);
					else invContents[i] = new ItemStack(Material.AIR);
				}
				inventory.setContents(invContents);
			} catch (Exception ex) {
			}
			return inventory;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static FileConfiguration getPlayerData(String name) {
		if(name == null) return null;
		File file = new File(instance.getDataFolder().getPath()+"/data", name+".data");
		if (!file.exists() || file.isDirectory()) return null;
		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			return config;
		} catch(Exception e){
			return null;
		}
	}
	
	public static FileConfiguration getRank(String name) {
		if(name == null) return null;
		File file = new File(instance.getDataFolder().getPath()+"/rank", name+".rank");
		if (!file.exists() || file.isDirectory()) return null;
		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			return config;
		} catch(Exception e){
			return null;
		}
	}
	
	private static List<Player> getPlayingGame(Player p){
		return null;
	}
	
	private static double getDistance(Player p, Player t){
		return p.getLocation().distance(t.getLocation());
	}
	
	public void MinigameInvenClick(Player p, int slot){
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
		switch(slot){
		case 10: p.closeInventory(); me.Bokum.FindTheMurder.GameSystem.Joingame(p); return;
		case 12: p.closeInventory(); me.Bokum.TRB.Main.GameJoin(p); return;
		case 14: p.closeInventory(); me.Bokum.EWG.Main.GameJoin(p); return;
		case 16: p.closeInventory(); me.Bokum.DTC.Main.GameJoin(p); return;
		case 19: p.closeInventory(); me.Bokum.FirstHit.Main.GameJoin(p); return;
		case 21: p.closeInventory(); me.Bokum.KTT.Main.GameJoin(p); return;
		case 23: p.closeInventory(); me.Bokum.Hungergame.Main.GameJoin(p); return;
		case 39: p.closeInventory(); p.openInventory(mobarenainven); return;
		case 30: p.closeInventory(); me.Bokum.ESP.Main.GameJoin(p); return;
		case 32: p.closeInventory(); p.openInventory(parkourinven); return;
		case 34: p.closeInventory(); me.Bokum.ECM.Main.GameJoin(p); return;
		case 37: p.closeInventory(); me.Bokum.Catcher.Main.GameJoin(p); return;
		case 28: p.closeInventory(); me.Bokum.CTM.Main.GameJoin(p); return;
		case 25: p.closeInventory(); me.Bokum.MOB.Game.MobSystem.GameJoin(p); return;
		case 41: p.closeInventory(); me.Bokum.BlockHunter.Main.GameJoin(p); return;
		case 43: p.closeInventory(); me.Bokum.SMG.Main.avoidanvil.gameJoin(p); return;
		case 46: p.closeInventory(); me.Bokum.SMG.Main.deathrun.gameJoin(p); return;
		case 48: p.closeInventory(); me.Bokum.SMG.Main.takeseat.gameJoin(p); return;
		case 50: p.closeInventory(); me.Bokum.SMG.Main.chamber.gameJoin(p); return;
		default: return;
		}
	}
	
	public void ParkourInvenClick(Player p, int slot){
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
		switch(slot){
		case 10: p.closeInventory(); me.Bokum.Parkour.Main.TrainingJoin(p); return;
		case 12: p.closeInventory(); me.Bokum.Parkour.Main.PracticeJoin(p); return;
		case 14: p.closeInventory(); me.Bokum.Parkour.Main.GameJoinrace(p); return;
		case 16: p.closeInventory(); me.Bokum.Parkour.Main.GameJoinbed(p); return;
				
		default: return;
		}
	}
	
	public void ServerInfoInvenClick(Player p, int slot){
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
		switch(slot){
		case 11: p.closeInventory(); p.openInventory(serverrule); return;
		case 15: p.closeInventory(); p.openInventory(prefixshop); return;
		default: return;
		}
	}
	
	public void PrefixShopInvenClick(Player p, ItemStack item){
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2.0f, 0.8f);
		if(item != null && item.getTypeId() != 102){
			p.closeInventory();
			if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
			if(econ.getBalance(p.getName()) >= 6000){
				EconomyResponse r = econ.withdrawPlayer(p.getName(), 6000);
				String prefix = item.getItemMeta().getDisplayName();
				if(prefix.contains("[ ") || prefix.contains(" ]")) return;
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "칭호 추가 "+p.getName()+" "+prefix);
			}else{
				p.sendMessage(MS+"칭호를 구입하시려면 §c6000포인트 §f이상이 필요합니다.");
			}
		}
	}
	
	public void checkFirstJoin(File file, Player p) {
		if (file == null) return;
		if (file.isDirectory() || !file.getAbsolutePath().endsWith(".data")) return;
		if(!file.exists()){
		try {
				FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(file);
				dataConfig.save(file);
				questDataToFile(baseQuest, p.getName());
				Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "칭호 추가 "+p.getName()+" §f[§b유저§f]");
				Bukkit.dispatchCommand(p, "칭호 대표 1");
			} catch(Exception e){
				return;
			}
		}
	}

	public static boolean Checkcooldown(Player p, String str)
	{
		if(!Main.cooldown.containsKey(p.getName()+str) || Main.cooldown.get(p.getName()+str) <= (int)(java.lang.System.currentTimeMillis()/1000))
		{
			return true;
		}
		p.sendMessage(MS+ChatColor.AQUA+(Main.cooldown.get(p.getName()+str)-(int)(java.lang.System.currentTimeMillis()/1000))
				+ChatColor.RESET+"초 후 사용하실 수 있습니다.");
		return false;
	}
	
	public void Setcooldown(Player p, String str, int cooldown)
	{
		Main.cooldown.put(p.getName()+str, (int)(java.lang.System.currentTimeMillis()/1000)+cooldown);
	}
	
	public static void Spawn(Player p)
	{
		for(PotionEffect effect : p.getActivePotionEffects())
		{
			p.removePotionEffect(effect.getType());
		}
		p.setNoDamageTicks(0);
		p.setMaxHealth(20);
		p.setLevel(0);
		p.setExp(0);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSneaking(false);
		p.setAllowFlight(false);
		p.setFlying(false);
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().setContents(Kit_Lobby.getContents());
		p.teleport(Loc_Lobby);
		p.updateInventory();
		Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams leave "+p.getName());
		gamelist.put(p.getName(), "스폰");
		if(me.Bokum.SMG.Main.playingList.containsKey(p.getName())) me.Bokum.SMG.Main.playingList.remove(p.getName());
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e){
		if(aclist.contains(e.getPlayer())){
			e.setCancelled(true); SendOPchat("§f[ §9§l오피채팅 §f] "+e.getPlayer().getName()+" : "+e.getMessage());
		} else if(solochat.contains(e.getPlayer().getName())){
			for(Player onp : Bukkit.getOnlinePlayers()){
				if(!onp.getName().equalsIgnoreCase(e.getPlayer().getName()))
					e.getRecipients().remove(onp);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		e.setDeathMessage(null);
	}
	
	@EventHandler
	public void onPlayerKicked(PlayerKickEvent e){
		e.setLeaveMessage(null);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		e.setQuitMessage(null);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		Spawn(p);
		e.setJoinMessage(null);
		checkFirstJoin(new File(this.getDataFolder().getPath()+"/data", p.getName()+".data"), p);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLoginEvent(PlayerLoginEvent e){
		Player p = e.getPlayer();
		if(!p.isWhitelisted()){
			if(p.getName().length() < 16){
				p.setWhitelisted(true);
				e.allow();
			} else {
				e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "서버와의 접속이 원활하지 않습니다. 카페에 문의해주세요.");
			}
		}
	}
	
	@EventHandler
	public void onPlayercommand(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		if(e.getMessage().equalsIgnoreCase("/스폰") || e.getMessage().equalsIgnoreCase("/spawn")
				|| e.getMessage().equalsIgnoreCase("/넴주") || e.getMessage().equalsIgnoreCase("/ㅅㅍ"))
		{
			Spawn(p);
			e.setCancelled(true);
		}
		if(e.getMessage().contains("/ma") && !p.isOp()) e.setCancelled(true);
	    if ((!e.isCancelled())) {
	        String command = e.getMessage().split(" ")[0];
	        HelpTopic htopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
	        if (htopic == null) {
	          p.sendMessage(MS+"§c존재하지 않는 명령어입니다.");
	          e.setCancelled(true);
	        }
	    } 
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getItem() == null) return;
			Player p = e.getPlayer();
			String str = getHandItemName(p);
			if(str == null) return;
			switch(str){
			case "§f[ §b미니게임 §f]":
				p.openInventory(Minigame); return;
			case "§f[ §b서버 정보 §f]":
				p.openInventory(Serverhelp); return;
			case "§f[ §b패치노트 §f]":
				p.openInventory(patchNote); return;
			default: return;
			}
		}
	}
	
	@EventHandler
	public void onRightClickEntity(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof Player){
			Player t = (Player) e.getRightClicked();
			if(t.getName().equalsIgnoreCase("§f[§c스폰으로돌아가기§f]")) Spawn(p);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(gamelist.containsKey(p.getName()) && !gamelist.get(p.getName()).equalsIgnoreCase("신들의 전쟁") && !gamelist.get(p.getName()).equalsIgnoreCase("헝거게임")){
				e.setFoodLevel(20);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(!gamelist.containsKey(p.getName()) 
				|| !gamelist.get(p.getName()).equalsIgnoreCase("신들의 전쟁")){
			ItemStack item = p.getInventory().getHelmet();
			if(item != null)item.setDurability((short) (item.getType().getMaxDurability()-item.getType().getMaxDurability()));
			item = p.getInventory().getChestplate();
			if(item != null)item.setDurability((short) (item.getType().getMaxDurability()-item.getType().getMaxDurability()));
			item = p.getInventory().getLeggings();
			if(item != null)item.setDurability((short) (item.getType().getMaxDurability()-item.getType().getMaxDurability()));
			item = p.getInventory().getBoots();
			if(item != null)item.setDurability((short) (item.getType().getMaxDurability()-item.getType().getMaxDurability()));
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(!(e.getDamager() instanceof Player)) return;
		Player d = (Player) e.getDamager();
		if(!gamelist.get(d.getName()).equalsIgnoreCase("신들의 전쟁")){
			if(d.getItemInHand() != null && (
					d.getItemInHand().getType().toString().contains("AXE") || 
					d.getItemInHand().getType().toString().contains("HOE") ||
					d.getItemInHand().getType().toString().contains("SWORD") ||
					d.getItemInHand().getType().toString().contains("SPADE") ||
					d.getItemInHand().getType().toString().contains("BOW"))){
				d.getItemInHand().setDurability((short) (d.getItemInHand().getType().getMaxDurability() - d.getItemInHand().getType().getMaxDurability()));
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player p = (Player) e.getPlayer();
		if(gamelist.containsKey(p.getName()) && gamelist.get(p.getName()).equalsIgnoreCase("스폰") && !p.isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockPlaceEvent e){
		Player p = (Player) e.getPlayer();
		if(gamelist.containsKey(p.getName()) && gamelist.get(p.getName()).equalsIgnoreCase("스폰") && !p.isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		switch(e.getInventory().getTitle()){
		case "§c§l미니게임": e.setCancelled(true); p.updateInventory(); MinigameInvenClick(p, e.getSlot()); return;
		case "§c§l서버 도움말": e.setCancelled(true); p.updateInventory(); ServerInfoInvenClick(p, e.getSlot()); return;
		case "§c§l칭호 상점": e.setCancelled(true); p.updateInventory(); PrefixShopInvenClick(p, e.getCurrentItem()); return;
		case "§c§l파쿠르": e.setCancelled(true); p.updateInventory(); ParkourInvenClick(p, e.getSlot()); return;
		case "§c§l패치노트": e.setCancelled(true); p.updateInventory(); return;
		case "§c§l서버규칙": e.setCancelled(true); if(e.getSlot() == 12) p.sendMessage("§a서버주소 : §6http://cafe.naver.com/boli2"); return;
		default: return;
		}
	}
}
