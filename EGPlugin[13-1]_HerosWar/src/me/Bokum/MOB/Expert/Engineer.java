package me.Bokum.MOB.Expert;

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

public class Engineer extends Ability{

	public Player engineer;
	public boolean power = false;
	
	
	public Engineer(String playername, Player p){
		super(playername, "엔지니어");
		engineer = p;
		ItemStack item = new ItemStack(257, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b엔지니어 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §64~6");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 0.8");
		lorelist.add("§f- §7공격 : §6★★★☆☆");
		lorelist.add("§f- §7기동 : §6★★★★☆");
		lorelist.add("§f- §7지원 : §6☆☆☆☆☆");
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
	}
	
	public void description(){
		engineer.sendMessage("§6============= §f[ §b엔지니어 §f] - §e직업군 §7: §cExpert §6=============\n"
				+ "§f- §a주스킬 §7: §d소형 헬리콥터\n§75초간 비행합니다."
				+ "§f- §a보조스킬 §7: §d파워 장갑\n§710초간 공격력이 2상승합니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d물질 이동기\n§7100칸 이내에 바라보는 곳으로 이동합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d파워 건전지\n§7대나무 헬리콥터, 수퍼장갑의 지속시간이 3초 증가합니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 24, true);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 2.0f, 1.0f);
		    ParticleEffect.SMOKE_LARGE.display(0.1f, 0.1f, 0.1f, 0, 40, p.getLocation(), 25);
			p.sendMessage(Main.MS+"소형 헬리콥터를 사용합니다.");
			p.setAllowFlight(true);
			p.setFlying(true);
			Cooldown.SkillCountdown(p, can_passive ? 8 : 5, "소형 헬리콥터 사용");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.setAllowFlight(false);
					p.setFlying(false);
					p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 2.0f);
				}
			}, can_passive ? 160 : 100);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 31, true);
			p.getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 2.0f);
			ParticleEffect.CRIT_MAGIC.display(0.1f, 0.1f, 0.1f, 0, 40, p.getLocation(), 25);
			p.sendMessage(Main.MS+"파워 장갑을 사용합니다.");
			power = true;
			Cooldown.SkillCountdown(p, can_passive ? 13 : 10, "파워 장갑 사용");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					power = false;
					p.getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_DEATH, 2.0f, 2.0f);
				}
			}, can_passive ? 260 : 200);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Block b = p.getTargetBlock(null, 100);
			if(b == null && b.getType() == Material.AIR){
				p.sendMessage(Main.MS+"거리가 너무 멉니다.");
				return;
			}
			Cooldown.Setcooldown(p, "궁극기", 79, true);
			p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 0.5f);
		    ParticleEffect.PORTAL.display(0.1f, 0.1f, 0.1f, 0, 40, p.getLocation(), 25);
			b.getWorld().playSound(b.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 0.5f);
		    ParticleEffect.PORTAL.display(0.1f, 0.1f, 0.1f, 0, 40, b.getLocation(), 25);
		    b.getLocation().setPitch(p.getLocation().getPitch());
		    b.getLocation().setYaw(p.getLocation().getYaw());
		    b.getLocation().setY(b.getLocation().getY()+1);
		    p.teleport(b.getLocation());
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
			if(!can_Ultimate){ engineer.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(power){
			e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.SKELETON_DEATH, 2.0f, 0.5f);
		    ParticleEffect.TOWN_AURA.display(0.1f, 0.1f, 0.1f, 0, 40, e.getEntity().getLocation(), 25);
			e.setDamage(MobSystem.Getrandom(6, 8));
		}else{
			e.setDamage(MobSystem.Getrandom(4, 6));
		}
	}
	
}


