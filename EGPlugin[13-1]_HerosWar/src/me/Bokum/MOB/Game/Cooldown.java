package me.Bokum.MOB.Game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Bokum.MOB.Main;

public class Cooldown {

	public static HashMap<String, Integer> cooldownlist = new HashMap<String, Integer>(130);
	
	
	public static boolean Checkcooldown(Player p, String str)
	{
		if(!cooldownlist.containsKey(p.getName()+str) || cooldownlist.get(p.getName()+str) <= (int)(java.lang.System.currentTimeMillis()/1000))
		{
			return true;
		}
		p.sendMessage(Main.MS+ChatColor.AQUA+(cooldownlist.get(p.getName()+str)-(int)(java.lang.System.currentTimeMillis()/1000))
				+ChatColor.RESET+"초 후 스킬을 사용하실 수 있습니다.");
		p.playSound(p.getLocation(), Sound.NOTE_SNARE_DRUM, 1.0f, 0.7f);
		return false;
	}
	
	public static void Setcooldown(Player p, String str, int cooldown , boolean count)
	{
		Cooldown.cooldownlist.put(p.getName()+str, (int)(java.lang.System.currentTimeMillis()/1000)+cooldown);
		if(!count) return;
		int slot = 0;
		if(str.equalsIgnoreCase("주스킬")) slot = 1;
		if(str.equalsIgnoreCase("보조스킬")) slot = 2;
		if(str.equalsIgnoreCase("궁극기")) slot = 3;
		Countdown(p, cooldown, str, slot);
	}
	
	public static void SkillCountdown(final Player p, long time, final String skill)
	{
		time *= 20;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"3초후 "+ChatColor.RESET+skill+"이 해제됩니다.");
			}
		}, time-60);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"2초후 "+ChatColor.RESET+skill+"이 해제됩니다.");
			}
		}, time-40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"1초후 "+ChatColor.RESET+skill+"이 해제됩니다.");
			}
		}, time-20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+skill+"능력이 해제됐습니다.");
			}
		}, time);
	}
	
	public static void Countdown(final Player p, long time, final String skill, final int slot)
	{
		final ItemStack item = p.getInventory().getItem(slot);
		if(slot != 0){
			if(item != null){
				ItemMeta meta = item.getItemMeta();
				meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
				item.setItemMeta(meta);
			}
		}
		time *= 20;
		if(time < 60){
			return;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"3초후 "+ChatColor.RESET+skill+"의 쿨타임이 끝납니다.");
			}
		}, time-60);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"2초후 "+ChatColor.RESET+skill+"의 쿨타임이 끝납니다.");
			}
		}, time-40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.AQUA+"1초후 "+ChatColor.RESET+skill+"의 쿨타임이 끝납니다.");
			}
		}, time-20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
		{
			public void run()
			{
				p.sendMessage(Main.MS+ChatColor.RESET+skill+"의 쿨타임이 끝났습니다.");
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2.0f, 1.6f);
				if(slot != 0){
					if(item != null) item.removeEnchantment(Enchantment.ARROW_INFINITE);
				}
			}
		}, time);


	}
}
