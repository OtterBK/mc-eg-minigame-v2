package me.Bokum.Supporter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.avaje.ebean.enhance.asm.commons.GeneratorAdapter;

import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.CloudEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;
import me.Bokum.MOB.Utility.Vector3D;

public class Portal extends Ability{

	public Player portal;
	public Location redportal;
	public Location blueportal;
	
	public Portal(String playername, Player p){
		super(playername, "포탈");
		portal = p;
		ItemStack item = new ItemStack(296, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b포탈 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §63~5");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 0.8");
		lorelist.add("§f- §7공격 : §6★☆☆☆☆");
		lorelist.add("§f- §7기동 : §6★★★★☆");
		lorelist.add("§f- §7지원 : §6★★★★☆");
		meta.setLore(lorelist);
		item.setItemMeta(meta);
		p.getInventory().setItem(0 ,item);
		
		item = new ItemStack(265, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c주스킬 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(1 ,item);
		
		item = new ItemStack(264, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c보조스킬 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(2 ,item);
		
		item = new ItemStack(388, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c궁극기 §f]");
		item.setItemMeta(meta);
		
		p.getInventory().setItem(3 ,item);
		
		redportal = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
		blueportal = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
	}
	
	public void description(){
		portal.sendMessage("§6============= §f[ §b파워 §f] - §e직업군 §7: §cExpert §6=============\n"
				+ "§f- §a주스킬 §7: §d블루 포탈\n§710칸내에 바라보는 블럭에 블루포탈을 설치합니다.\n"
				+ "§f- §a보조스킬 §7: §d레드 포탈\n§710칸내에 바라보는 블럭에 레드포탈을 설치합니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d위치 교환\n§720칸내에 바라보는 적과 위치를 교환합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d포탈 게이지 상승\n§7포탈의 쿨타임이 1초 감소합니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Block b = null;
			try{
				b = p.getTargetBlock(null, 10);
			}catch(Exception exception){
				return;
			}
			if(b == null || b.getType() == Material.AIR){
				p.sendMessage(Main.MS+"10칸 이내의 블럭에만 사용이 가능합니다.");
				return;
			}
			if((b.getRelative(0, 1, 0).getType() != Material.AIR && b.getRelative(0, 1, 0).getType() != Material.PORTAL ) || b.getRelative(0, 2, 0).getType() != Material.AIR){
				p.sendMessage(Main.MS+"대상 블럭의 2칸 위에 아무블럭이 없어야합니다.");
				return;
			}
			Cooldown.Setcooldown(p, "주스킬", can_passive? 4 : 5, true);
			blueportal.getBlock().setType(Material.AIR);
			MobSystem.portalloc.remove(blueportal);
			MobSystem.portalloc.remove(redportal);
			blueportal = new Location(b.getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY()+1
					, b.getLocation().getBlockZ());
			MobSystem.portalloc.put(blueportal, redportal);
			MobSystem.portalloc.put(redportal, blueportal);
			blueportal.getBlock().setType(Material.PORTAL);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_HIT, 2.0f, 1.5f);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Block b = null;
			try{
				b = p.getTargetBlock(null, 10);
			}catch(Exception exception){
				return;
			}
			if(b == null || b.getType() == Material.AIR){
				p.sendMessage(Main.MS+"10칸 이내의 블럭에만 사용이 가능합니다.");
				return;
			}
			if((b.getRelative(0, 1, 0).getType() != Material.AIR && b.getRelative(0, 1, 0).getType() != Material.PORTAL ) || b.getRelative(0, 2, 0).getType() != Material.AIR){
				p.sendMessage(Main.MS+"대상 블럭의 2칸 위에 아무블럭이 없어야합니다.");
				return;
			}
			for(int i = 0; i < 3; i++){
				if(b.getRelative(0, -1, 0).getLocation().distance(Main.Core[i]) <= 40){
					p.sendMessage(Main.MS+"점령지의 40칸내에는 포탈을 설치하실 수 없습니다.");
					return;
				}
			}
			Cooldown.Setcooldown(p, "보조스킬", can_passive? 4 : 5, true);
			redportal.getBlock().setType(Material.AIR);
			MobSystem.portalloc.remove(blueportal);
			MobSystem.portalloc.remove(redportal);
			redportal = new Location(b.getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY()+1
					, b.getLocation().getBlockZ());
			MobSystem.portalloc.put(blueportal, redportal);
			MobSystem.portalloc.put(redportal, blueportal);
			redportal.getBlock().setType(Material.PORTAL);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_HIT, 2.0f, 1.5f);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Player t = MobSystem.Getcorsurplayer(p, 20);
			if(t == null || MobSystem.getTeam(p).contains(t)){
				p.sendMessage(Main.MS+"20칸내에 바라보는곳에 적이 없습니다.");
				return;
			}
			Cooldown.Setcooldown(p, "궁극기", 89, true);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 0.5f);
		    ParticleEffect.PORTAL.display(0.1f, 0.1f, 0.1f, 0, 40, p.getLocation(), 25);
		    p.sendMessage(Main.MS+t.getName()+" 님과 위치가 바뀌었습니다.");
			t.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 0.5f);
		    ParticleEffect.PORTAL.display(0.1f, 0.1f, 0.1f, 0, 40, t.getLocation(), 25);
		    t.sendMessage(Main.MS+"포탈의 궁극기에 의해 "+portal.getName()+" 님과 위치가 바뀌었습니다.");
		    Location l = p.getLocation();
		    p.teleport(t.getLocation());
		    t.teleport(l);
		}
	}
	
	public void Active(PlayerItemHeldEvent e){
		if(e.getNewSlot() == 1){
			e.getPlayer().getInventory().setHeldItemSlot(0);
			PrimarySkill(e.getPlayer());
		}
		if(e.getNewSlot() == 2){
			e.getPlayer().getInventory().setHeldItemSlot(0);
			SecondarySkill(e.getPlayer());
		}
		if(e.getNewSlot() == 3){
			if(!can_Ultimate){ portal.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(portal.getInventory().getHeldItemSlot() != 0) return;
		e.setDamage(MobSystem.Getrandom(3, 5));
	}
	
}



