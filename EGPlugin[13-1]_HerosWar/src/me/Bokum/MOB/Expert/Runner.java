package me.Bokum.MOB.Expert;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class Runner extends Ability{

	public Player runner;
	
	public Runner(String playername, Player p){
		super(playername, "러너");
		runner = p;
		ItemStack item = new ItemStack(276, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b러너 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §65~7");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1");
		lorelist.add("§f- §7공격 : §6★★★☆☆");
		lorelist.add("§f- §7기동 : §6★★★★☆");
		lorelist.add("§f- §7지원 : §6★★★☆☆");
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
		runner.sendMessage("§6============= §f[ §b러너 §f] - §e직업군 §7: §cExpert §6=============\n"
				+ "§f- §a주스킬 §7: §d아드레날린\n§7스테미나를 전부 회복합니다."
				+ "§f- §a보조스킬 §7: §d신호기\n§7신호기를 설치하여 파티원들이 신호기의 위치에서 부활할 수 있도록 해줍니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d러쉬\n§715초간 파티원들에게 신속3의 버프를 줍니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d더블 어택\n§7타격시 10%확률로 2번 공격합니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 21, true);
			p.getWorld().playSound(p.getLocation(), Sound.BREATH, 2.0f, 1.0f);
			p.sendMessage(Main.MS+"스테미나를 회복했습니다.");
			p.setExp(1);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "주스킬", 60, true);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 102, true);
			p.getWorld().playSound(p.getLocation(), Sound.MAGMACUBE_JUMP, 2.0f, 0.5f);
		      CloudEffect particle = new CloudEffect(Main.effectManager);
		      particle.setEntity(p);
		      particle.period = 1;
		      particle.iterations = 20;
		      particle.speed = 4;
		      particle.particleRadius = 1f;
		      particle.cloudSize = 2f;
		      particle.start();
			int amt = 1;
			for(int i = 0; i < MobSystem.getEnemy(p).size(); i++){
				final Player t = MobSystem.getEnemy(p).get(i);
				if(t.getLocation().distance(p.getLocation()) <= 7){
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
						public void run(){
							p.teleport(t.getLocation().subtract(t.getEyeLocation().getDirection()));
							t.damage(4, p);
							t.getWorld().playSound(t.getLocation(), Sound.HURT, 1.5f, 1.2f);
						    ParticleEffect.REDSTONE.display(0.3f, 0.3f, 0.3f, 0, 40, t.getLocation(), 25);
						}
					},(amt++*10));
				}
			}
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
			if(!can_Ultimate){ runner.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(can_passive && MobSystem.Getrandom(0, 9) == 0){
			e.setDamage(MobSystem.Getrandom(5, 7)*2);
		    ParticleEffect.LAVA.display(0.3f, 0.3f, 0.3f, 0, 40, e.getEntity().getLocation(), 25);
		    Player d = (Player) e.getDamager();
			d.getWorld().playSound(d.getLocation(), Sound.DIG_STONE, 1.5f, 2.0f);
		    d.sendMessage(Main.MS+"두번 공격했습니다!");
		}else{
			e.setDamage(MobSystem.Getrandom(5, 7));
		}
	}
	
}

