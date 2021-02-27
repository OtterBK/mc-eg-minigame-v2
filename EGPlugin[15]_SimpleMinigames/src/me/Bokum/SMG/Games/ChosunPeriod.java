package me.Bokum.SMG.Games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Bokum.SMG.Main;
import me.Bokum.SMG.Messenger.Messenger;
import me.Bokum.SMG.Minigame.Minigame;
import me.Bokum.SMG.Utility.Cooldown;

public class ChosunPeriod extends Minigame{
	public Cooldown cooldown;
	public Location randomStartPos[] = new Location[12];
	
	public ChosunPeriod(String title, int max, int min){
		super(title, max, min);
		cooldown = new Cooldown(this);
		
		helpInventory = Bukkit.createInventory(null, 27, "§c§l도우미");
		
		ItemStack item = new ItemStack(34, 1);
		ItemMeta meta2 = item.getItemMeta();
		meta2.setDisplayName("§6장식");
		item.setItemMeta(meta2);
		for(int i = 0; i <= 9; i++){
			helpInventory.setItem(i, item);
		}
		for(int i = 17; i < 27; i++){
			helpInventory.setItem(i, item);
		}
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e플레이 방법");
		item.setItemMeta(meta2);
		helpInventory.setItem(11, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta2 = item.getItemMeta();
		meta2.setDisplayName("§e게임 규칙");
		item.setItemMeta(meta2);
		helpInventory.setItem(13, item);
		
		loadConfig();
	}
	
	public void loadConfig(){
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("joinPos");
			this.joinPos = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 참여 지점이 설정되지 않았습니다.");
		}
		try{
			ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("startPos");
			this.startPos = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
		}catch(Exception e){
			Main.instance.getLogger().info(gameTitle+" 게임의 시작 지점이 설정되지 않았습니다.");
		}
		for(int i = 0; i < 12; i++){
			try{
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("rs"+i);
				this.randomStartPos[i] = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z"));
			}catch(Exception e){
				Main.instance.getLogger().info(gameTitle+" 게임의 +"+i+"랜덤시작 지점이 설정되지 않았습니다.");
			}
		}
	}
	
	public void setLoc(Player p, String args[]){
		if(args.length == 2){
			p.sendMessage("/smg set csp join");
			p.sendMessage("/smg set csp start");
			p.sendMessage("/smg set csp rs 1~12");
		}else if(args.length == 3){
			if(args[2].equalsIgnoreCase("join")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("joinPos")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("joinPos");
					Main.instance.saveConfig();
				}
				ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("joinPos");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY()+1);
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("start")){
				if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
					Main.instance.getConfig().createSection(gameTitle);
					Main.instance.saveConfig();
				}
				if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("startPos")){
					Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("startPos");
					Main.instance.saveConfig();
				}ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("startPos");
				sec.set("world", p.getWorld().getName());
				sec.set("x", p.getLocation().getBlockX());
				sec.set("y", p.getLocation().getBlockY()+1);
				sec.set("z", p.getLocation().getBlockZ());
				Main.instance.saveConfig();
				loadConfig();
			}else if(args[2].equalsIgnoreCase("rs")){
				if(args.length <= 3){
					p.sendMessage(Messenger.MS+"1~12의 숫자를 입력해주세요.");
				} else {
					try{
						int num = Integer.valueOf(args[3]);
						if(num < 1 && num > 12){
							p.sendMessage(Messenger.MS+"1~12까지만 지정하실 수 있습니다.");
						} else {
							num--;
							if(!Main.instance.getConfig().isConfigurationSection(gameTitle)){
								Main.instance.getConfig().createSection(gameTitle);
								Main.instance.saveConfig();
							}
							if(!Main.instance.getConfig().getConfigurationSection(gameTitle).isConfigurationSection("rs"+num)){
								Main.instance.getConfig().getConfigurationSection(gameTitle).createSection("rs"+num);
								Main.instance.saveConfig();
							}ConfigurationSection sec = Main.instance.getConfig().getConfigurationSection(gameTitle).getConfigurationSection("rs"+num);
							sec.set("world", p.getWorld().getName());
							sec.set("x", p.getLocation().getBlockX());
							sec.set("y", p.getLocation().getBlockY()+1);
							sec.set("z", p.getLocation().getBlockZ());
							Main.instance.saveConfig();
							loadConfig();
						}
					}catch(Exception e){
						p.sendMessage(Messenger.MS+"숫자만 입력해주세요. 입력한 문자 : "+args[3]);
					}
				}
			}
		}
	}
}
