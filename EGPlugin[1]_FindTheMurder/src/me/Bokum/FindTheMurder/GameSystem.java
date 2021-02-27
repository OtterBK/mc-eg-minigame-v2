package me.Bokum.FindTheMurder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GameSystem extends JavaPlugin
  implements Listener
{
  public static int schtime = 0;
  public static int schtmp = 0;
  public static GameSystem instance;
  public static boolean isday;
  public static int daycnt = 0;
  public static String doctortarget = null;
  public static String detectivetarget = null;
  public static boolean uc_police = false;
  public static boolean uc_soldier = true;
  public static boolean uc_spy = false;
  public static boolean uc_reporter = false;
  public static int uc_guardiancnt = 0;
  public static boolean checkstart = false;
  public static String uc_guardian1 = null;
  public static String uc_guardian2 = null;
  public static boolean lobbystart = false;
  public static String astarget = null;
  public static Player assasin;
  public static List<Player> mlist = new ArrayList<Player>();
  public static List<Player> tmplist = new ArrayList<Player>();

  public static void Joingame(Player player)
  {
    if (Main.list.contains(player))
    {
      player.sendMessage(Main.MS + "이미 게임에 참가중 입니다.");
      return;
    }
    if (checkstart)
    {
      player.sendMessage(Main.MS + "이미 게임이 시작되었습니다.");
      return;
    }
    if (Main.list.size() >= 13)
    {
      player.sendMessage(Main.MS + "이미 최대인원인 13명의 플레이어가 참가중입니다.");
      return;
    }
    player.teleport(Main.joinpos);
    Main.list.add(player);
	player.getInventory().clear();
	player.getInventory().setHelmet(null);
	player.getInventory().setChestplate(null);
	player.getInventory().setLeggings(null);
	player.getInventory().setBoots(null);
    me.Bokum.EGM.Main.gamelist.put(player.getName(), "살인마를 찾아라4");
    player.getInventory().setItem(8, Main.helpitem);
    Main.SendMessage(Main.MS + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET + "[ " + ChatColor.GREEN + Main.list.size() + " / 5 " + ChatColor.RESET + "]");
    if ((Main.list.size() >= 5) && (!lobbystart))
    {
      lobbystart = true;
      Main.Startgame();
    }
	for(Player t : Main.list){
		t.playSound(t.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f);
	}
  }

  public static int Getrandom(int num)
  {
    return (int)(Math.random() * num);
  }

  public static void ForceEnd()
  {
    Bukkit.broadcastMessage(Main.MS + "살인마를 찾아라4 게임이 강제 종료 되었습니다.");
    schtime = 0;
    schtmp = 0;
    isday = false;
    daycnt = 0;
    doctortarget = null;
    detectivetarget = null;
    uc_police = false;
    uc_soldier = true;
    uc_spy = false;
    uc_reporter = false;
    uc_guardiancnt = 0;
    checkstart = false;
    uc_guardian1 = null;
    uc_guardian2 = null;
    lobbystart = false;
    for (int i = 0; i < Main.list.size(); i++)
    {
        try{
      	  me.Bokum.EGM.Main.Spawn(Main.list.get(i));
        } catch(Exception e){
      	  Main.list.get(i).teleport(Main.Lobby);
        }
    }
    mlist.clear();
    Main.list.clear();
    Main.plist.clear();
    Main.jlist.clear();
    Main.glist.clear();
    astarget = null;
    tmplist.clear();
    assasin = null;
    Main.chestlist.clear();
    Main.Cancelalltasks();
  }

  public static void End()
  {
	Bukkit.getScheduler().cancelTasks(Main.instance);
	Main.Forcestoptimer = 0;
    schtime = 0;
    schtmp = 0;
    Main.chestlist.clear();
    isday = false;
    daycnt = 0;
    doctortarget = null;
    detectivetarget = null;
    uc_police = false;
    uc_soldier = true;
    uc_spy = false;
    uc_reporter = false;
    uc_guardiancnt = 0;
    checkstart = false;
    uc_guardian1 = null;
    uc_guardian2 = null;
    lobbystart = false;
    mlist.clear();
    Main.list.clear();
    Main.plist.clear();
    Main.jlist.clear();
    Main.glist.clear();
    tmplist.clear();
    astarget = null;
    assasin = null;
  }

  public static void Setjob()
  {
    tmplist.clear();
    tmplist.addAll(Main.list);
    if (tmplist.size() < 5)
    {
      Main.SendMessage(Main.MS + "게임 진행에 필요한 최소인원이 충족하지 않는 버그가 발생했습니다.");
      ForceEnd();
    }
    for (int i = 0; i < Main.list.size(); i++)
    {
      Player setplayer = (Player)Main.list.get(i);
      Main.plist.put(setplayer.getName(), setplayer);
    }
    int random = Getrandom(tmplist.size());
    SetMurder((Player)tmplist.get(random));
    random = Getrandom(tmplist.size());
    SetDoctor((Player)tmplist.get(Getrandom(tmplist.size())));
    random = Getrandom(tmplist.size());
    SetPolice((Player)tmplist.get(Getrandom(tmplist.size())));
    random = Getrandom(tmplist.size());
    SetSoldier((Player)tmplist.get(Getrandom(tmplist.size())));
    random = Getrandom(tmplist.size());
    SetSpy((Player)tmplist.get(Getrandom(tmplist.size())));
    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetNice_Civilian((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetDetective((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetReporter((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetMagician((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetGuadian((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetCivilian((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetAssasin((Player)tmplist.get(Getrandom(tmplist.size())));
    }

    if (tmplist.size() != 0)
    {
      random = Getrandom(tmplist.size());
      SetPrisoner((Player)tmplist.get(Getrandom(tmplist.size())));
    }
    
    Main.SendMessage(Main.MS + "직업이 정해졌습니다.");
    Main.SendMessage(Main.MS + "직업에 대한 설명을 보시려면 " + 
      ChatColor.AQUA + "/ftm help 를 입력해주세요.");
    Main.SendMessage(Main.MS + "직업리스트를 보시려면 /ftm list 를 입력해주세요.");
    Mixlist();
  }

  public static void Mixlist()
  {
    if (Main.glist.size() == 0)
    {
      Bukkit.getLogger().info("직업리스트가 비어있는 상태에서 Mixgist 메소드를 호출할 수 없습니다.");
    }
    else
    {
      List keylist = new ArrayList();
      List valuelist = new ArrayList();
      keylist.addAll(Main.glist.keySet());
      valuelist.addAll(Main.glist.values());
      Main.glist.clear();
      while (keylist.size() > 0)
      {
        int ri = Getrandom(keylist.size());
        Main.glist.put((String)keylist.get(ri), (String)valuelist.get(ri));
        keylist.remove(ri);
        valuelist.remove(ri);
      }
    }
  }

  public static void dayinit()
  {
    Main.vplist.clear();
    Main.vlist.clear();
    detectivetarget = null;
    uc_police = false;
  }

  public static void nightinit()
  {
    doctortarget = null;
    uc_spy = false;
    uc_guardiancnt = 0;
    uc_guardian1 = null;
    uc_guardian2 = null;
  }

  public static void Setvotelist()
  {
    String topname = null;
    int top_int = 0;
    boolean bothtopcheck = false;
    Set keys = Main.vlist.keySet();
    Iterator it = keys.iterator();
    while (it.hasNext())
    {
      String name = (String)it.next();
      if (top_int < ((Integer)Main.vlist.get(name)).intValue())
      {
        top_int = ((Integer)Main.vlist.get(name)).intValue();
        topname = name;
        bothtopcheck = false;
      } else {
        if (top_int != ((Integer)Main.vlist.get(name)).intValue())
          continue;
        bothtopcheck = true;
      }
    }
    if ((bothtopcheck) || (topname == null))
    {
      Main.SendMessage(Main.MS + "최다투표를 받은 플레이어가 존재하지 않으므로 투표가 취소 됐습니다.");
    }
    else
    {
      Main.SendMessage(Main.MS + topname + " 님이 최다투표를 받으셨습니다. ( " + top_int + "표 )");
      if (Checkucguardian(topname))
      {
        Main.SendMessage(Main.MS + "하지만 보디가드의 능력으로 인하여 이 플레이어의 직업을 밝혀낼 수 없었습니다!");
      }
      else if (!Main.plist.containsKey(topname))
      {
        Main.SendMessage(Main.MS + "최다 투표를 받은 플레이어가 현재는 게임에 참가중이 아니므로 취소 됐습니다.");
      }
      else
      {
    	String topJob = Getjob(topname); 
    	if(topJob.equalsIgnoreCase("배신자")){
    		for(int i = 0; i < mlist.size(); i++){
    			if(mlist.get(i).getName().equalsIgnoreCase(topname)){
    				topJob = "배신자(살인마팀)";
    			} else {
    				topJob = "배신자(시민팀)";
    			}
    		}
    	}
        Main.SendMessage(Main.MS + topname + " 님의 직업은 " + topJob + " 입니다!");
        Main.glist.put(topname, topJob);
      }
      Main.SendMessage(Main.MS + "/ftm list 를 입력시 직업리스트를 보실 수 있습니다.");
    }
  }

  public static boolean Checkucguardian(String name)
  {
    if (uc_guardian1 != null)
    {
      if (uc_guardian1.equalsIgnoreCase(name))
      {
        return true;
      }
    }
    if (uc_guardian2 != null)
    {
      if (uc_guardian2.equalsIgnoreCase(name))
      {
        return true;
      }
    }
    return false;
  }

  public static boolean Checktarget(Player player, String target)
  {
    if (target == null)
    {
      player.sendMessage(Main.MS + "타겟을 지정해주세요.");
      return true;
    }
    if (!Main.plist.containsKey(target))
    {
      player.sendMessage(Main.MS + ChatColor.AQUA + target + ChatColor.GRAY + "님은 게임에 참여중이 아닙니다.");
      return true;
    }
    return false;
  }

  public static boolean Checkday(Player player)
  {
    if (daycnt == 1)
    {
      player.sendMessage(Main.MS + "첫째 날에는 사용이 불가능한 능력입니다.");
      return true;
    }
    return false;
  }

  public static String Getjob(String target)
  {
    return (String)Main.jlist.get(target);
  }

	public static void Use(Player player, String target)
	{
		switch(Getjob(player.getName()))
		{
			case "살인마":
				UseMurder(player); return; 
			case "의사":
				UseDoctor(player, target); return; 
			case "경찰":
				UsePolice(player, target); return; 
			case "군인":
				UseSoldier(player); return; 
			case "스파이":
				UseSpy(player, target); return; 
			case "탐정":
				UseDetective(player, target); return; 
			case "모범시민":
				UseNice_Civilian(player); return; 
			case "기자":
				UseReporter(player, target); return; 
			case "마술사":
				UseMagician(player, target); return; 
			case "보디가드":
				UseGuadian(player, target); return; 
			case "시민":
				UseCivilian(player); return; 
			case "배신자":
				UseAssasin(player); return; 
		}
	}

  public static void UseMurder(Player player)
  {
    player.sendMessage(Main.MS + "살인마는 사용할 능력이 없습니다. 밤을 이용하여 모든 플레이어를 죽이세요.");
  }

  public static void UseDoctor(Player player, String target)
  {
    if (doctortarget != null)
    {
      player.sendMessage(Main.MS + "이미 능력을 사용하였습니다. 다음날까지 기다려주세요.");
      return;
    }
    if (Checktarget(player, target))
    {
      return;
    }
    if (isday)
    {
      player.sendMessage(Main.MS + "의사의 능력은 밤에만 사용이 가능합니다.");
    }
    else if (target.equalsIgnoreCase(player.getName()))
    {
      player.sendMessage(Main.MS + "자신에게는 사용이 불가능합니다.");
    }
    else
    {
      doctortarget = target;
      player.sendMessage(Main.MS + target + " 님이 사망시 치료합니다.");
    }
  }

  public static void UsePolice(Player player, String target)
  {
    if (uc_police)
    {
      player.sendMessage(Main.MS + "이미 능력을 사용하였습니다. 다음날까지 기다려주세요.");
      return;
    }
    if (Checktarget(player, target))
    {
      return;
    }
    if (Checkday(player))
    {
      return;
    }
    if (isday)
    {
      uc_police = true;
      if (Getjob(target).equalsIgnoreCase("살인마"))
      {
        player.sendMessage(Main.MS + target + " 님은 살인마 입니다! 이 사실을 모두에게 알리세요!");
      }
      else
      {
        player.sendMessage(Main.MS + target + " 님은 살인마가 아닙니다.");
      }
    }
    else
    {
      player.sendMessage(Main.MS + "경찰의 능력은 낮에만 사용이 가능합니다.");
    }
  }

  public static void UseSoldier(Player player)
  {
    player.sendMessage(Main.MS + "군인은 사용할 능력이 없습니다. 하지만 죽어도 1번 부활합니다.");
  }

  public static void UseSpy(Player player, String target)
  {
    if (uc_spy)
    {
      player.sendMessage(Main.MS + "이미 능력을 사용하였습니다. 다음날까지 기다려주세요.");
      return;
    }
    if (Checktarget(player, target))
    {
      return;
    }
    if (isday)
    {
      player.sendMessage(Main.MS + "스파이의 능력은 밤에만 사용이 가능합니다.");
    }
    else
    {
      uc_spy = true;
      if (Getjob(target).equalsIgnoreCase("살인마"))
      {
        mlist.add(player);
        player.sendMessage(Main.MS + target + " 님은 살인마 였습니다!");
        player.sendMessage(Main.MS + target + " 님과 접선에 성공 하였습니다!");
        ((Player)Main.plist.get(target)).sendMessage(ChatColor.RED + "스파이 " + ChatColor.AQUA + player.getName() + ChatColor.RED + " 님이 당신과 접선했습니다.");
        Givespyitem(player);
        if (Getsurviver() <= 0)
        {
          Main.Murderwin();
        }
      }
      else
      {
        player.sendMessage(Main.MS + target + " 님은 살인마가 아닙니다.");
        player.sendMessage(Main.MS + target + " 님의 직업은 " + Getjob(target) + " 입니다.");
        if (Getjob(target).equalsIgnoreCase("군인"))
        {
          ((Player)Main.plist.get(target)).sendMessage(Main.MS + "스파이 " + player.getName() + " 님이 당신을 조사하였습니다!");
        }
      }
    }
  }

  public static void UseNice_Civilian(Player player)
  {
    player.sendMessage(Main.MS + "모범시민은 투표시에 능력이 자동적으로 활성화가 됩니다.");
  }

  public static void UseDetective(Player player, String target)
  {
    if (isday)
    {
      if (Checktarget(player, target))
      {
        return;
      }
      if (detectivetarget != null)
      {
        player.sendMessage(Main.MS + "이미 능력을 사용하였습니다. 다음날까지 기다려주세요.");
        return;
      }
      detectivetarget = target;
      player.sendMessage(Main.MS + "이제 " + target + " 님이 밤에 무엇을 하시는지 볼 수 있습니다.");
    }
    else
    {
      if (detectivetarget == null)
      {
        player.sendMessage(Main.MS + "낮에 누구를 조사할 지 지정하지 않아 능력사용이 불가능합니다.");
        return;
      }
      if(!Main.jlist.containsKey(target))
      {
    	 player.sendMessage(Main.MS+"조사 대상이 게임에 참여중이지 않습니다.");
    	 return;
      }
      player.sendMessage(ChatColor.AQUA + detectivetarget + " 님의 x좌표 : " + ((Player)Main.plist.get(target)).getLocation().getBlockX());
      player.sendMessage(ChatColor.AQUA + detectivetarget + " 님의 y좌표 : " + ((Player)Main.plist.get(target)).getLocation().getBlockY());
      player.sendMessage(ChatColor.AQUA + detectivetarget + " 님의 z좌표 : " + ((Player)Main.plist.get(target)).getLocation().getBlockZ());
      player.sendMessage(ChatColor.AQUA + detectivetarget + " 님의 체력 : " + ((Player)Main.plist.get(target)).getHealth());
      if (((Player)Main.plist.get(target)).getItemInHand().hasItemMeta())
      {
        player.sendMessage(ChatColor.AQUA + detectivetarget + " 님이 들고 있는 무기 : " + ((Player)Main.plist.get(target)).getItemInHand().getItemMeta().getDisplayName());
      }
      else
      {
        player.sendMessage(ChatColor.AQUA + detectivetarget + " 님이 들고 있는 무기 : 인식할 수 없습니다.");
      }
    }
  }

  public static void UseReporter(Player player, String target)
  {
    if (Checktarget(player, target))
    {
      return;
    }
    if (uc_reporter)
    {
      player.sendMessage(Main.MS + "이미 능력을 사용하셨습니다. 기자의 능력은 1번만 사용이 가능합니다.");
      return;
    }
    if (isday)
    {
      player.sendMessage(Main.MS + "기자의 능력은 밤에만 사용이 가능합니다.");
    }
    else
    {
      uc_reporter = true;
      Main.SendMessage(Main.MS + "기자가 " + target + " 님을 조사했습니다.");
      Main.SendMessage(Main.MS + target + " 님의 직업은 " + Getjob(target) + "입니다!!!");
      Main.glist.put(target, Getjob(target));
    }
  }

	public static void UseMagician(Player player, String target)
	{
		if(Checktarget(player, target))
		{
			return;
		}
		if(isday)
		{
			player.sendMessage(Main.MS+"마술사의 능력은 낮에는 사용이 불가능합니다.");
		}
		if(daycnt > 2){
			player.sendMessage(Main.MS+"마술사의 능력은 3번째날부터 사용이 불가능합니다.");
		}
		else
		{
			if(target.equalsIgnoreCase(player.getName()))
			{
				player.sendMessage(Main.MS+"자신에게는 사용이 불가능합니다.");
				return;
			}
			player.sendMessage(Main.MS+target+" 님의 직업인 "+Getjob(target)+"을(를) 뺐었습니다.");
			Main.plist.get(target).sendMessage(Main.MS+"마술사가 당신의 직업을 빼었습니다.");
			switch(Getjob(target))
			{
				case "살인마":
					Player tmpplayer = Main.plist.get(target);
					Removeplayerfl(Main.plist.get(target));
					tmpplayer.sendMessage(Main.MS+"당신은 살인마였기 때문에 사망하게 됩니다.");
					tmpplayer.setHealth(0);
					SetMurder(player);
					return; 
				case "의사":
					SetDoctor(player); break; 
				case "경찰":
					SetPolice(player); break; 
				case "군인":
					SetSoldier(player); break; 
				case "스파이":
					SetSpy(player); return; 
				case "탐정":
					SetDetective(player); break; 
				case "모범시민":
					SetNice_Civilian(player); break; 
				case "기자":
					SetReporter(player); break; 
				case "마술사":
					SetMagician(player); break; 
				case "보디가드":
					SetGuadian(player); break; 
				case "시민":
					SetCivilian(player); break; 
				case "배신자":
					SetAssasin(player);
					player.sendMessage(Main.MS+"§6"+target+"님은 배신자였습니다.");
					if(mlist.contains(Main.plist.get(target))){
						player.sendMessage(target+"§7님은 배신을 한 배신자였기 때문에 당신은 살인마팀이 됩니다.");
					} else {
						player.sendMessage(target+"§7님은 배신을 하지않은 배신자였기 때문에 당신은 살인마팀이 되지 않습니다.");
					}
					return; 
				case "탈옥수":
					player.sendMessage(Main.MS+"탈옥수의 직업은 빼앗을 수 없습니다.");; return; 
				default:
					player.sendMessage(Main.MS+target+" 님은 할당되지 않은 직업입니다. - 버그 의심"); break;
			}
			if(mlist.contains(Main.plist.get(target)))
			{
				mlist.remove(Main.plist.get(target));
				Main.SendMessage(Main.MS+"살인마팀이었던 "+target+" 님이 마술사에게 직업을 빼았겨 시민이 되었습니다!");
			}
			Main.jlist.put(target, "시민");
			Main.glist.put(target, "시민");
		}
	}

  public static void UseGuadian(Player player, String target)
  {
    if (Checktarget(player, target))
    {
      return;
    }
    if (uc_guardiancnt >= 2)
    {
      player.sendMessage(Main.MS + "이미 능력을 2번 사용 하셨습니다. 다음날을 기다리세요");
      return;
    }
    if (isday)
    {
      player.sendMessage(Main.MS + "보디가드의 능력은 밤에만 사용이 가능합니다.");
    }
    else
    {
      if (uc_guardiancnt == 1)
      {
        uc_guardian1 = target;
      }
      if (uc_guardiancnt == 2)
      {
        uc_guardian2 = target;
      }
      player.sendMessage(Main.MS + target + " 님에게 능력을 사용하였습니다.");
      player.sendMessage(Main.MS + "이 플레이어는 다음날 낮에 투표로 인하여 직업이 발설되지 않습니다.");
      uc_guardiancnt += 1;
    }
  }

  public static void UseCivilian(Player player)
  {
    player.sendMessage(Main.MS + "시민은 아무런 능력이 없습니다.");
  }

  public static void UseAssasin(Player player)
  {
    player.sendMessage(Main.MS + "당신은 1번째날에 살인을 하여 시민팀을 배신할지 아니면 \n§7배신하지 않고 살인마와 싸울지 정할수 있습니다..");
  }

  public static void SetMurder(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "살인마");
    Main.glist.put(player.getName(), "?");
    mlist.add(player);
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
    Givemurderitem(player);
  }

  public static void SetDoctor(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "의사");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetPolice(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "경찰");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
    Givepoliceitem(player);
  }

  public static void SetSoldier(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "군인");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
    uc_soldier = true;
    uc_reporter = false;
  }

  public static void SetSpy(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "스파이");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetDetective(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "탐정");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetNice_Civilian(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "모범시민");
    Main.glist.put(player.getName(), "모범시민");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
    player.sendMessage(Main.MS + "당신의 직업이 직업리스트에 모범시민으로 올려졌습니다.");
  }

  public static void SetReporter(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "기자");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
    uc_reporter = false;
  }

  public static void SetMagician(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "마술사");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetGuadian(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "보디가드");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetCivilian(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "시민");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static void SetAssasin(Player player)
  {
    astarget = null;
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "배신자");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");

    assasin = player;
  }
  
  public static void SetPrisoner(Player player)
  {
	    tmplist.remove(player);
	    player.setHealth(player.getMaxHealth());
	    Main.jlist.put(player.getName(), "탈옥수");
	    Main.glist.put(player.getName(), "탈옥수");
	    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
	      (String)Main.jlist.get(player.getName()) + " 입니다.");
	    player.sendMessage(Main.MS + "당신의 직업이 직업리스트에 탈옥수로 올려졌습니다.");
	    Main.SendMessage(Main.MS+"탈옥수 "+player.getName()+" 가 있습니다. 조심하세요!");
	    player.getInventory().addItem(Main.Makeitem(282, "탈옥수의 검", 5, 6));
	    mlist.add(player);
  }

  public static void SetJoker(Player player)
  {
    tmplist.remove(player);
    player.setHealth(player.getMaxHealth());
    Main.jlist.put(player.getName(), "조커");
    Main.glist.put(player.getName(), "?");
    player.sendMessage(Main.MS + "당신의 직업은 " + ChatColor.GOLD + 
      (String)Main.jlist.get(player.getName()) + " 입니다.");
  }

  public static int Getsurviver()
  {
    return Main.list.size() - mlist.size();
  }

  public static void Murderdead(Player player)
  {
    Main.SendMessage(Main.MS + ChatColor.GRAY + "살인마팀인 " + ChatColor.RED + player.getName() + ChatColor.GRAY + " 님이 사망했습니다.");
    String temp = Getjob(player.getName());
    Removeplayerfl(player);
    if (mlist.size() <= 0)
    {
      Main.Civilwin();
    }
    else if (temp.equalsIgnoreCase("살인마"))
    {
      Main.SendMessage(Main.MS + "살인마는 사망하였지만 아직 살인마의 동료가 남아있습니다!");
    }
    else
    {
      Main.SendMessage(Main.MS + "아직 살인마의 동료가 남아있습니다!");
    }
  }

  public static void Reverse(Player player)
  {
    player.teleport(Main.Startpos[Getrandom(Main.Startpos.length)]);
    player.setHealth(player.getMaxHealth());
    player.sendMessage(Main.MS + "부활 하셨습니다.");
    player.sendMessage(Main.MS + "인벤토리는 초기화 됩니다.");
  }

  public static void Civildead(Player player)
  {
    Main.SendMessage(Main.MS + ChatColor.RED + player.getName() + ChatColor.GRAY + " 님이 사망했습니다.");
    Removeplayerfl(player);
    if (Getsurviver() <= 0)
    {
      Main.Murderwin();
    }
  }

  public static void Removeplayerfl(Player player)
  {
    Main.list.remove(player);
    Main.plist.remove(player.getName());
    Main.glist.remove(player.getName());
    Main.jlist.remove(player.getName());
    if (mlist.contains(player))
    {
      mlist.remove(player);
    }
  }

  public static boolean CheckReverse(Player player)
  {
    if (doctortarget != null)
    {
      if (doctortarget.equalsIgnoreCase(player.getName()))
      {
        doctortarget = null;
        Main.SendMessage(Main.MS + "의사의 치료로 인하여 " + player.getName() + " 님이 죽지 않았습니다.");
        return true;
      }
    }
    else if ((Getjob(player.getName()).equalsIgnoreCase("군인")) && (uc_soldier))
    {
      uc_soldier = false;
      Main.SendMessage(Main.MS + "군인인 " + player.getName() + " 님이 살인마의 공격을 버텨냈습니다!");
      Main.SendMessage(Main.MS + player.getName() + " 님의 직업이 시민으로 변경됩니다.");
      Main.jlist.put(player.getName(), "시민");
      Main.glist.put(player.getName(), "시민");
      return true;
    }
    return false;
  }

  public static void Quitplayer(Player player)
  {
    if (!Main.list.contains(player))
    {
      return;
    }
    if (checkstart)
    {
      if (mlist.contains(player))
      {
        Murderdead(player);
      }
      else
      {
        Civildead(player);
      }
      Main.SendMessage(Main.MS + ChatColor.AQUA + player.getName() + " 님이 퇴장 하셨습니다.");
    }
    else
    {
      Main.list.remove(player);
      Main.SendMessage(Main.MS + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " 님이 퇴장 했습니다. " + ChatColor.RESET + "[ " + ChatColor.GREEN + Main.list.size() + " / 6 " + ChatColor.RESET + "]");
    }
  }

  public static void Givespyitem(Player player)
  {
    ItemStack item = Main.Makeitem(283, "스파이 검", 7, 7);
    player.getInventory().addItem(item);
  }

  public static void Givemurderitem(Player player)
  {
    ItemStack item = new ItemStack(351, 1, (byte) 1);
	  ItemMeta meta = item.getItemMeta();
	  meta.setDisplayName("§c피 묻은 너클");
	  List<String> lorelist = new ArrayList<String>();
	  String damage = "§7공격력: 7";
	  lorelist.add(damage);
	  meta.setLore(lorelist);
	  item.setItemMeta(meta);
    for(int i = 0; i < 4; i++){
    	player.getInventory().addItem(Main.chestitem[16]);
    }
    player.getInventory().addItem(item);
  }

  public static void GiveBasicitem()
  {
    ItemStack item = new ItemStack(Material.PUMPKIN_PIE, 50);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(ChatColor.GOLD + "맛있는 호박 파이");
    item.setItemMeta(meta);
    for (int i = 0; i < Main.list.size(); i++)
    {
      ((Player)Main.list.get(i)).getInventory().addItem(new ItemStack[] { item });
    }
  }
  
	public static String getHandItemName(Player p){
		ItemStack item = p.getItemInHand();
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return "meta없음";
		return item.getItemMeta().getDisplayName();
	}

  public static void Givepoliceitem(Player player)
  {
    ItemStack item = new ItemStack(Material.STICK, 3);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(ChatColor.AQUA + "경찰봉");
    meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
    List lorelist = new ArrayList();
    lorelist.add(ChatColor.GRAY + "경찰에게 주어지는 무기로");
    lorelist.add(ChatColor.GRAY + "이 무기로 때린 플레이어는 3초간");
    lorelist.add(ChatColor.GRAY + "움직이지 못한다. (경찰만 사용가능)");
    meta.setLore(lorelist);
    item.setItemMeta(meta);
    player.getInventory().addItem(new ItemStack[] { item });
  }
}