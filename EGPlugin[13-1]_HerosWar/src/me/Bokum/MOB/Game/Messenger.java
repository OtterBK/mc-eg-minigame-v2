package me.Bokum.MOB.Game;

import org.bukkit.entity.Player;

public class Messenger {
	
	public static void SendAllmessage(String string) {
		for(Player p : MobSystem.plist){
			p.sendMessage(string);
		}
	}

}
