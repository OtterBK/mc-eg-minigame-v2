package me.Bokum.SMG.Utility;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.Bokum.SMG.Minigame.Minigame;

public class Utility extends JavaPlugin{
	
	public static int Getrandom(int min, int max){
		  return (int)(Math.random() * (max-min+1)+min);
	}
	
	public static ItemStack makeItem(int id, int data, int amt, String displayName, List<String> loreList){
		ItemStack item = null;
		if(data == 0){
			item = new ItemStack(id, amt);
		}else{
			item = new ItemStack(id, amt, (short) data);
		}
		if(item == null) return null;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(loreList);
		item.setItemMeta(meta);
		return item;
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
      
      /*public static Minigame getPlayerMinigame(Player p){
    	  if(Main.avoidanvil.playerList.contains(p)) return Main.avoidanvil;
      }*/
      
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
      
  	public static void spawnFireball(Player player, float range){
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(range)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
		Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
		fireball.setShooter(player);
		fireball.setIsIncendiary(false);
	}
      
      public static int Getcursch(Minigame game){
    	  for(int i = 0 ; i < game.tasknum.length; i++){
    		  if(game.tasknum[i] == -5){
    			  return i;
    		  }
    	  }
    	  return 0;
      }
		
      public static void Canceltask(Minigame game, int cur){
    	  Bukkit.getScheduler().cancelTask(game.tasknum[cur]);
    	  game.tasknum[cur] = -5;
    	  game.tasktime[cur] = -5;
      }
}
