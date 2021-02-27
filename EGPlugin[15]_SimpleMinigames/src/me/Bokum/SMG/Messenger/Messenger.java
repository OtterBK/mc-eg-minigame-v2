package me.Bokum.SMG.Messenger;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Bokum.SMG.Minigame.Minigame;

public class Messenger extends JavaPlugin{
	public static String MS = "¡×f[ ¡×aEG ¡×f] ";
	
	public static void sendMessage(List<Player> playerList, String str, Boolean isMS){
		if(isMS) str = MS+str;
		for(Player p : playerList){
			p.sendMessage(str);
		}
	}
	
	public static void sendSound(List<Player> playerList, Sound sound, float volume, float speed){
		for(Player t : playerList){
			t.playSound(t.getLocation(), sound, volume, speed);
		}
	}
}
