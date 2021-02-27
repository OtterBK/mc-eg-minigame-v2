package me.Bokum.SMG.Minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.Bokum.SMG.Main;

public class Minigame extends JavaPlugin{
	public String gameTitle;
	public List<Player> playerList;
	public Location joinPos;
	public Location startPos;
	public ItemStack helpItem;
	public Inventory helpInventory;
	public int maxPlayer;
	public int minPlayer;
	public boolean isStart;
	public boolean isLobbyStart;
	public boolean isGameEnd;
	public Minigame minigame;
	public int Forcestoptimer;
	public int tasknum[] = new int[10];
	public int tasktime[] = new int[10];
	
	public Minigame(String Title, int max, int min){
		this.gameTitle = Title;
		this.playerList = new ArrayList<Player>();
		this.isStart = false;
		this.isLobbyStart = false;
		this.isGameEnd = false;
		this.Forcestoptimer = 0;
		this.maxPlayer = max;
		this.minPlayer = min;
		setHelpItem();
		for(int i = 0; i < tasknum.length; i++){
			tasknum[i] = -5;
			tasktime[i] = -5;
		}
		minigame = this;
	}
	
	public void gameJoin(Player p){
		
	}
	
	public void gameQuit(Player p){
		
	}
	
	public void clearData(){

	}
	
	public void forceStop(){

	}
	
	public void setHelpItem(){
		List<String> lorelist = new ArrayList<String>();
		
		helpItem = new ItemStack(345, 1);
		ItemMeta meta1 = helpItem.getItemMeta();
		meta1.setDisplayName("§f[ §6게임 도우미 §f]");
		lorelist.clear();
		lorelist.add("§f- §7우클릭시 게임 도움미를 엽니다.");
		meta1.setLore(lorelist);
		helpItem.setItemMeta(meta1);
	}
	
	//↓↓↓ 이벤트
	
	public void onPlayerMove(PlayerMoveEvent e){
		
	}
	
	public void onBlockPlace(BlockPlaceEvent e){
		
	}
	
	public void onBlockBreak(BlockBreakEvent e){
		
	}
	
	public void onEntityDamage(EntityDamageEvent e){
		
	}
	
	public void onQuit(PlayerQuitEvent e){
		
	}
	
	public void onDropItem(PlayerDropItemEvent e){
		
	}
	
	public void onRegainHealth(EntityRegainHealthEvent e){
		
	}
	
	public void onPlayerDeath(PlayerDeathEvent e){
	
	}
	
	public void onInteract(PlayerInteractEvent e){
		
	}
	
	public void onEntityHit(EntityDamageByEntityEvent e){
		
	}
	
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
		
	}
	
	public void onBreakVehicle(VehicleDestroyEvent e){
		
	}
	
	public void onClickInventory(InventoryClickEvent e){
	
	}
	
	public void onEntityExplode(EntityExplodeEvent e) {
		
	}
	
	public void onExplosionPrime(ExplosionPrimeEvent e) {
		
	}
	
	public void onEntityDamagedByBlock(EntityDamageByBlockEvent e){
		
	}
	
	public void onChat(PlayerChatEvent e){
		
	}
}
