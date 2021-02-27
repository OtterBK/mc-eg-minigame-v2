package me.Bokum.CTM;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
	public static Location drawerpos;
	public static Main instance;
	public static List<Player> plist = new ArrayList<Player>();
	public static int tasknum[] = new int[10];
	public static int tasktime[] = new int[10];
	public static int timernum = 0;
	public static int timertime = 0;
	public static ItemStack helpitem;
	public static Inventory gamehelper;
	public static Inventory selectcolor;
	public static Inventory scorelist;
	public static int drawerint = 0;
	public static Player drawer = null;
	public static HashMap<String, Integer> score = new HashMap<String, Integer>();
	public static Economy econ = null;
	public static int round = 1;
	public static int timertime2 = 0;
	public static ItemStack brush;
	public static ItemStack eraser;
	public static ItemStack color;
	public static ItemStack paper;
	public static String topicstr;
	public static ItemStack topic;
	public static byte selcolor = 15;
	public static List<Player> answerlist = new ArrayList<Player>(); 
	public static List<Location> blockloc = new ArrayList<Location>(2550);
	public static List<String> topiclist = new ArrayList<String>(300);
	public static boolean check_start = false;
	public static boolean check_lobbystart = false;
	public static int Forcestoptimer = 0;
	
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("캐치마인드 플러그인이 로드 되었습니다.");
		
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		
		topiclist.add("태극기");
		topiclist.add("소방차");
		topiclist.add("의자");
		topiclist.add("가위");
		topiclist.add("칼");
		topiclist.add("나무");
		topiclist.add("컴퓨터");
		topiclist.add("마이크");
		topiclist.add("키보드");
		topiclist.add("시계");
		topiclist.add("물통");
		topiclist.add("안경");
		topiclist.add("계단");
		topiclist.add("엘레베이터");
		topiclist.add("에스컬레이터");
		topiclist.add("종이");
		topiclist.add("풀잎");
		topiclist.add("스피커");
		topiclist.add("자동차");
		topiclist.add("경찰차");
		topiclist.add("인간");
		topiclist.add("총");
		topiclist.add("모자");
		topiclist.add("군대");
		topiclist.add("수건");
		topiclist.add("약");
		topiclist.add("약국");
		topiclist.add("마인크래프트");
		topiclist.add("카카오톡");
		topiclist.add("신발");
		topiclist.add("신발끈");
		topiclist.add("요정");
		topiclist.add("물");
		topiclist.add("불");
		topiclist.add("바람");
		topiclist.add("다이아몬드");
		topiclist.add("에메랄드");
		topiclist.add("철");
		topiclist.add("석탄");
		topiclist.add("잔디");
		topiclist.add("돌");
		topiclist.add("점토");
		topiclist.add("울타리");
		topiclist.add("지옥");
		topiclist.add("천국");
		topiclist.add("흑요석");
		topiclist.add("기반암");
		topiclist.add("화산");
		topiclist.add("대한민국");
		topiclist.add("제주도");
		topiclist.add("독도");
		topiclist.add("바다");
		topiclist.add("계곡");
		topiclist.add("폭포");
		topiclist.add("장갑");
		topiclist.add("책상");
		topiclist.add("휴지");
		topiclist.add("컴퓨터");
		topiclist.add("단풍잎");
		topiclist.add("나뭇가지");
		topiclist.add("새싹");
		topiclist.add("화분");
		topiclist.add("사자");
		topiclist.add("호랑이");
		topiclist.add("토끼");
		topiclist.add("거북이");
		topiclist.add("개미");
		topiclist.add("거미");
		topiclist.add("거미줄");
		topiclist.add("뽀로로");
		topiclist.add("짱구");
		topiclist.add("도라에몽");
		topiclist.add("가방");
		topiclist.add("지갑");
		topiclist.add("연필");
		topiclist.add("지우개");
		topiclist.add("책");
		topiclist.add("학교");
		topiclist.add("물방울");
		topiclist.add("휴지통");
		topiclist.add("열쇠");
		topiclist.add("문");
		topiclist.add("얼음");
		topiclist.add("핸드폰");
		topiclist.add("피자");
		topiclist.add("치킨");
		topiclist.add("돈");
		topiclist.add("선풍기");
		topiclist.add("아파트");
		topiclist.add("주택");
		topiclist.add("소");
		topiclist.add("강아지");
		topiclist.add("전구");
		topiclist.add("유리");
		topiclist.add("창문");
		topiclist.add("사진");
		topiclist.add("지구");
		topiclist.add("바지");
		topiclist.add("허리띠");
		topiclist.add("날개");
		topiclist.add("심장");
		topiclist.add("아령");
		topiclist.add("축구공");
		topiclist.add("야구공");
		topiclist.add("카드");
		topiclist.add("양털");
		topiclist.add("곡괭이");
		topiclist.add("삽");
		topiclist.add("도끼");
		topiclist.add("활");
		topiclist.add("화살");
		topiclist.add("낚시대");
		topiclist.add("당근");
		topiclist.add("가지");
		topiclist.add("토마토");
		topiclist.add("라이터");
		topiclist.add("칫솔");
		topiclist.add("치약");
		topiclist.add("한글");
		topiclist.add("영어");
		topiclist.add("수영");
		topiclist.add("꽃");
		topiclist.add("뼈다귀");
		topiclist.add("새");
		topiclist.add("횃불");
		topiclist.add("레드스톤");
		topiclist.add("화로");
		topiclist.add("감자");
		topiclist.add("쿠키");
		topiclist.add("상자");
		topiclist.add("신호기");
		topiclist.add("사과");
		topiclist.add("깔때기");
		topiclist.add("가죽");
		topiclist.add("나침반");
		topiclist.add("우주선");
		topiclist.add("고기");
		topiclist.add("태양");
		topiclist.add("병원");
		topiclist.add("은행");
		topiclist.add("스펀지");
		topiclist.add("청금석");
		topiclist.add("설탕");
		topiclist.add("실");
		topiclist.add("괭이");
		topiclist.add("사탕수수");
		topiclist.add("닭");
		topiclist.add("오리");
		topiclist.add("기차");
		topiclist.add("비행기");
		topiclist.add("자전거");
		topiclist.add("스키");
		topiclist.add("신문");
		topiclist.add("소리");
		topiclist.add("사전");
		topiclist.add("놀이터");
		topiclist.add("목걸이");
		topiclist.add("반지");
		topiclist.add("팔찌");
		topiclist.add("팽귄");
		topiclist.add("곰");
		topiclist.add("지붕");
		topiclist.add("구멍");
		topiclist.add("철장");
		topiclist.add("태극기");
		topiclist.add("방송");
		topiclist.add("영화");
		topiclist.add("버섯");
		topiclist.add("가로등");
		topiclist.add("캠핑");
		topiclist.add("텐트");
		topiclist.add("냄비");
		topiclist.add("후라이팬");
		topiclist.add("라면");
		topiclist.add("케이크");
		topiclist.add("신호등");
		topiclist.add("도로");
		topiclist.add("인도");
		topiclist.add("무지개");
		topiclist.add("구름");
		topiclist.add("하늘");
		topiclist.add("산");
		topiclist.add("동물");
		topiclist.add("농장");
		topiclist.add("게임");
		topiclist.add("횡단보도");
		topiclist.add("벽돌");
		topiclist.add("발광석");
		topiclist.add("반블럭");
		topiclist.add("조약돌");
		topiclist.add("사탕");
		topiclist.add("초콜릿");
		topiclist.add("양초");
		topiclist.add("헬리콥터");
		topiclist.add("보따리");
		topiclist.add("대나무");
		topiclist.add("쇼파");
		topiclist.add("텔레비젼");
		topiclist.add("인터넷");
		topiclist.add("버튼");
		//200
		topiclist.add("미국");
		topiclist.add("전봇대");
		topiclist.add("이마트");
		topiclist.add("잠자리");
		topiclist.add("이불");
		topiclist.add("달");
		topiclist.add("밤");
		topiclist.add("도토리");
		topiclist.add("교실");
		topiclist.add("오토바이");
		//210
		topiclist.add("달력");
		topiclist.add("태풍");
		topiclist.add("돋보기");
		topiclist.add("클립");
		topiclist.add("호수");
		topiclist.add("해바라기");
		topiclist.add("카메라");
		topiclist.add("망치");
		topiclist.add("호박");
		topiclist.add("바코드");
		//220
		topiclist.add("김");
		topiclist.add("탱크");
		topiclist.add("삼각형");
		topiclist.add("사각형");
		topiclist.add("원");
		topiclist.add("소화기");
		topiclist.add("올림픽");
		topiclist.add("귤");
		topiclist.add("포도");
		topiclist.add("옥수수");
		//230
		topiclist.add("그네");
		topiclist.add("시소");
		topiclist.add("미끄럼틀");
		topiclist.add("철봉");
		topiclist.add("슬라임");
		topiclist.add("하트");
		topiclist.add("클로버");
		topiclist.add("우유");
		topiclist.add("파도");
		topiclist.add("폭탄");
		//240
		topiclist.add("비");
		topiclist.add("우산");
		topiclist.add("엔더맨");
		topiclist.add("좀비");
		topiclist.add("스켈레톤");
		topiclist.add("톱");
		topiclist.add("금");
		topiclist.add("은");
		topiclist.add("빵");
		topiclist.add("방파제");
		//250
		topiclist.add("양조기");
		topiclist.add("송전탑");
		topiclist.add("컵");
		topiclist.add("크레인");
		topiclist.add("넥타이");
		topiclist.add("별");
		topiclist.add("큐브");
		topiclist.add("주사위");
		topiclist.add("음표");
		topiclist.add("리본");
		//260
		topiclist.add("단무지");
		topiclist.add("언덕");
		topiclist.add("빗자루");
		topiclist.add("무인도");
		topiclist.add("기타");
		topiclist.add("막대기");
		topiclist.add("와이파이");
		topiclist.add("드릴");
		topiclist.add("못");
		topiclist.add("오목");
		//270		
		
		scorelist = Bukkit.createInventory(null, 9, "§c§l점수판");
		
		selectcolor = Bukkit.createInventory(null, 18, "§c§l색 선택");
		
		for(int i = 0; i <= 15; i++){
			selectcolor.setItem(i, new ItemStack(35, 1, (byte) i));
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
		
		helpitem = new ItemStack(345, 1);
		ItemMeta meta1 = helpitem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미 엽니다.");
		meta1.setLore(lorelist);
		helpitem.setItemMeta(meta1);
		
		brush = new ItemStack(75, 1);
		meta1 = brush.getItemMeta();
		meta1.setDisplayName("§f[ §a붓 §f]");
		lorelist.clear();
		lorelist.add("§f- §7캔버스에 우클릭으로 그림을 그리세요!");
		meta1.setLore(lorelist);
		brush.setItemMeta(meta1);
		
		eraser = new ItemStack(265, 1);
		meta1 = eraser.getItemMeta();
		meta1.setDisplayName("§f[ §f전체 지우기 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 캔버스를 지웁니다.");
		meta1.setLore(lorelist);
		eraser.setItemMeta(meta1);
		
		color = new ItemStack(340, 1);
		meta1 = color.getItemMeta();
		meta1.setDisplayName("§f[ §e색 선택 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 색을 변경합니다.");
		meta1.setLore(lorelist);
		color.setItemMeta(meta1);
		
		topic = new ItemStack(321, 1);
		meta1 = topic.getItemMeta();
		meta1.setDisplayName("주제");
		lorelist.clear();
		meta1.setLore(lorelist);
		topic.setItemMeta(meta1);
		
		paper = new ItemStack(Material.PAPER, 1);
		meta1 = paper.getItemMeta();
		meta1.setDisplayName("점수판");
		lorelist.clear();
		meta1.setLore(lorelist);
		paper.setItemMeta(meta1);
		
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
		getLogger().info("캐치마인드 플러그인이 언로드 되었습니다.");
	}
	
	public void Loadconfig()
	{
	  try
	  {
		joinpos = new Location(Bukkit.getWorld(getConfig().getString("Join_world")), getConfig().getInt("Join_x"), getConfig().getInt("Join_y"), getConfig().getInt("Join_z"));
	    Lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_world")), getConfig().getInt("Lobby_x"), getConfig().getInt("Lobby_y"), getConfig().getInt("Lobby_z"));
	    drawerpos = new Location(Bukkit.getWorld(getConfig().getString("Draw_world")), getConfig().getInt("Draw_x"), getConfig().getInt("Draw_y"), getConfig().getInt("Draw_z"));
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
			 getLogger().info("블럭 지점이 완벽히 설정 되어있지 않습니다. 버그 발생의 우려가 있습니다.");
		}
	}
	
	public boolean onCommand(CommandSender talker, Command command, String str, String args[])
	{
		if(talker instanceof Player)
		{
			Player p = (Player) talker;
			if(str.equalsIgnoreCase("ctm") && p.isOp())
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
		for(Location loc : blockloc){
			loc.getBlock().setTypeIdAndData(35, (byte) 0, true);
		}
	}
	
	public void Helpmessages(Player p)
	{
		p.sendMessage(MS+"/ctm join");
		p.sendMessage(MS+"/ctm quit");
		p.sendMessage(MS+"/ctm set");
		p.sendMessage(MS+"/ctm reload");
		p.sendMessage(MS+"/ctm inv");
	}
	
	public static void Forcestop()
	{
		Bukkit.broadcastMessage(MS+"캐치마인드가 강제 종료 되었습니다.");
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
		Bukkit.getScheduler().cancelTasks(Main.instance);
		Forcestoptimer = 0;
		plist.clear();
		check_start = false;
		check_lobbystart = false;
		drawerint = 0;
		drawer = null;
		score.clear();
		round = 1;
		topicstr = null;
		selcolor = 15;
		answerlist.clear();
		check_start = false;
		check_lobbystart = false;
		timertime2 = 0;
	}
	
	public void Setloc(Player p, String[] args)
	{
		if(args.length <= 1)
		{
			p.sendMessage(MS+"/ctm set lobby");
			p.sendMessage(MS+"/ctm set join");
			p.sendMessage(MS+"/ctm set drawing");
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
			else if(args[1].equalsIgnoreCase("drawing"))
			{
			    getConfig().set("Draw_world", p.getLocation().getWorld().getName());
			    getConfig().set("Draw_x", Integer.valueOf(p.getLocation().getBlockX()));
			    getConfig().set("Draw_y", Integer.valueOf(p.getLocation().getBlockY() + 1));
			    getConfig().set("Draw_z", Integer.valueOf(p.getLocation().getBlockZ()));
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
	
	public static void SendAnswerchat(String str)
	{
		for(Player p : answerlist)
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
			p.sendMessage(MS+"이미 게임이 진행중입니다. 현재 라운드 : "+round+" / 10");
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
			me.Bokum.EGM.Main.gamelist.put(p.getName(), "캐치마인드");
			Sendmessage(MS+p.getName()+" 님이 캐치마인드에 참여하셨습니다. 인원 (§e "+plist.size()+"§7 / §c4 §f)");
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
		if(!check_start)
		{
			return;
		}
		Sendmessage(MS+p.getName()+" §f님이 퇴장하셨습니다.");
		if(drawer == p){
			Sendmessage(MS+"출제자가 퇴장했습니다.");
			Bukkit.getScheduler().cancelTask(timernum);
			Timer();
		}
		if(plist.size() == 1)
		{
			try{
				Win(plist.get(0), 0);
			}catch(Exception e){
				Forcestop();
			}
		}
	}
	
	public static void Win(final Player p, int top_score)
	{
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1.5f);
		Sendmessage(MS+"§6"+p.getName()+" §7님이 §6"+top_score+"점§7으로 승리하셨습니다!");
		try{
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
		{
			public void run()
			{
				EconomyResponse r = econ.depositPlayer(p.getName(), 500);
				if (r.transactionSuccess()) {
					p.sendMessage(ChatColor.GOLD + "승리 보상으로 500원을 받으셨습니다.");
				}
				for(Player t : plist){
					EconomyResponse r1 = econ.depositPlayer(t.getName(), 200);
					if (r1.transactionSuccess()) {
						t.sendMessage(ChatColor.GOLD + "플레이 보상으로 200원을 받으셨습니다.");
					}
					try{
						me.Bokum.EGM.Main.Spawn(t);
						} catch(Exception e){
						t.teleport(Main.Lobby);
						}
				}
				Cleardata();
				Bukkit.broadcastMessage(MS+"§6§l캐치마인드§f가 §9"+p.getName()+"§f님의 승리로 종료 됐습니다.");
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
			  p.sendMessage("§7게임 이름 §f: §c캐치마인드");
			  p.sendMessage("§f게임 시작후 그리는 사람을 한명 정합니다.");
			  p.sendMessage("§f그리는 사람은 §2순서대로 §f정해지게 되며");
			  p.sendMessage("§f그림는 사람은 주어진 §6주제에 맞는§f 그림을");
			  p.sendMessage("§b90초 §f동안 그리면 됩니다. ");
			  p.closeInventory();
			  return;
			  
		  case 13:
			  p.sendMessage("§f주제와 상관없는 그림을 그리지 마세요!");
			  p.sendMessage("§f이상한 그림 욕 그림 등등은 처벌을 받을 수 있습니다.");
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
		
		  public static void SetScorelist(){
			  for(int i = 0; i < 9; i++){
				 scorelist.setItem(i, null);
			  }
			  for(int i = 0; i < plist.size(); i++){
				  ItemStack item = new ItemStack(397, 1, (byte) 3);
				  ItemMeta meta = item.getItemMeta();
				  meta.setDisplayName(plist.get(i).getName());
				  List<String> lorelist = new ArrayList<String>();
				  lorelist.add("§c점수 : §6"+score.get(plist.get(i).getName()));
				  meta.setLore(lorelist);
				  item.setItemMeta(meta);
				  scorelist.setItem(i, item);
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
			Bukkit.broadcastMessage(MS+"§e§l캐치마인드§f가 곧 시작됩니다.");
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
							p.getInventory().addItem(paper);
							p.getInventory().addItem(helpitem);
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
							p.sendMessage(MS+"게임이 시작 됐습니다.");
							score.put(p.getName(), 0);
						}
						SetScorelist();
						Timer();
					}
				}
			}, 200L, 200L);
		}
		
		public static void Timer(){
			timertime2 = 0;
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
				public void run(){
					if(--timertime2 < 0){
						timertime2 = 100;
						if(drawer != null){
							for(Player p : plist){
								p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
							}
							drawer.getInventory().clear();
							drawer.teleport(joinpos);
							drawer.getInventory().addItem(paper);
							drawer.getInventory().addItem(helpitem);
							drawer.getInventory().addItem();
							Sendmessage(MS+"시간이 다 되었습니다! \n"+MS+"정답 : §6"+topicstr);
							answerlist.clear();
							RestoreBlock();
							if(++round >= 11){
								Player top = null;
								int top_score = 0;
								for(Player t : plist){
									int sc = score.get(t.getName());
									if(sc > top_score){
										top_score = sc;
										top = t;
									}
								}
								if(top == null){
									Forcestop();
								} else {
									try{
									Win(top, top_score);
									}catch(Exception e){
										Forcestop();
									}
								}
							}
						}
						try{
							if(drawerint >= plist.size()) drawerint = 0;
							drawer = plist.get(drawerint);
							if(++drawerint >= plist.size()){
								drawerint = 0;
							}
						}catch(Exception e){
							Forcestop();
						}
						Sendmessage(MS+drawer.getName()+" 님께서 그리실 차례입니다. §c현재 라운드 ( "+round+" / 10 )");
						drawer.teleport(drawerpos);
						topicstr = topiclist.get(Getrandom(0, topiclist.size()-1));
						ItemMeta meta = topic.getItemMeta();
						meta.setDisplayName("§c주제 §f: §e"+topicstr);
						topic.setItemMeta(meta);
						Inventory di = drawer.getInventory();
						di.setItem(0, brush);
						di.setItem(2, eraser);
						di.setItem(1, color);
						di.setItem(3, topic);
						answerlist.add(drawer);
						drawer.sendMessage(MS+"§c주제 §f: §e"+topicstr);
					}else{
						if(!check_start) Bukkit.getScheduler().cancelTask(timernum);
						for(Player p : plist){
							p.setExp(timertime2 <= 0 ? 0 : timertime2/100f);
						}
					}
				}
			}, 140l, 20l);
		}
		
		@EventHandler
		public void onRightClick(PlayerInteractEvent e)
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
			{
				final Player p = e.getPlayer();
				if(!plist.contains(p) || e.getItem() == null) return;
				if(e.getItem().getType() == Material.REDSTONE_TORCH_OFF){
					Block bl = null;
					try{
						bl = p.getTargetBlock(null, 150);
					}catch(Exception exception){
						return;
					}
					if(bl == null) return;
					if(bl.getType() == Material.WOOL) bl.setTypeIdAndData(35, selcolor, true);
				}
				if(e.getItem().getType() == Material.COMPASS)
						  p.openInventory(gamehelper);
				if(e.getItem().getTypeId() == 265){
					p.sendMessage(MS+"지우개를 사용했습니다.");
					RestoreBlock();
				}
				if(e.getItem().getType() == Material.BOOK) p.openInventory(selectcolor);
				if(e.getItem().getType() == Material.PAPER) p.openInventory(scorelist);
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
				 } else if(e.getInventory().getTitle().equalsIgnoreCase("§c§l색 선택") && e.getSlot() <= 15){
					 selcolor = (byte) (e.getSlot());
					 p.closeInventory();
					 e.setCancelled(true);
				 } else if(e.getInventory().getTitle().equalsIgnoreCase("§c§l점수판")){
					 e.setCancelled(true);
				 }
			}
		}
		
		@EventHandler
		public void onPlayerDamage(EntityDamageEvent e){
			if(!(e.getEntity() instanceof Player)) return;
			if(plist.contains((Player) e.getEntity())) e.setCancelled(true);
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
			public void onChat(PlayerChatEvent e){
				if(!plist.contains(e.getPlayer())) return;
				Player p = e.getPlayer();
				if(answerlist.contains(p)){
					SendAnswerchat("§f[ §6§l정답자 채팅 §f] "+p.getName()+" : §a"+e.getMessage());
					e.setCancelled(true);
					return;
				}
				if(topicstr != null && e.getMessage().equalsIgnoreCase(topicstr)){
					e.setCancelled(true);
					int getscore = 0;
					if(answerlist.size() <= 1){
						getscore = 3;
						if(drawer != null){
							drawer.sendMessage(MS+"정답자가 나와 점수를 받았습니다.");
							score.put(drawer.getName(), score.get(drawer.getName()) + 2);	
						}
					} else if(answerlist.size() <= 2){
						getscore = 2;
					} else{
						getscore = 1;
					}
					for(Player t : plist){
						t.playSound(t.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
					}
					answerlist.add(p);
					score.put(p.getName(), score.get(p.getName()) + getscore);
					Sendmessage(MS+p.getName()+" 님은 정답을 맞추셨습니다!+"+getscore+"점");
					
					SetScorelist();
					if(answerlist.size() == plist.size()){
						Bukkit.getScheduler().cancelTask(timernum);
						Sendmessage(MS+"모든 플레이어가 정답을 맞추었습니다!");
						Timer();
					}
				}
			}
}
