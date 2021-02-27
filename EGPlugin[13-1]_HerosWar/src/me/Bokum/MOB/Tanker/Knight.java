package me.Bokum.MOB.Tanker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.LineEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;

public class Knight extends Ability{

	public Player knight;
	
	public Knight(String playername, Player p){
		super(playername, "기사");
		knight = p;
		ItemStack item = new ItemStack(267, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b기사 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §65~7");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 0.75");
		lorelist.add("§f- §7공격 : §6★★★☆☆");
		lorelist.add("§f- §7기동 : §6★★★★☆");
		lorelist.add("§f- §7지원 : §6★☆☆☆☆");
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
		knight.sendMessage("§6============= §f[ §b기사 §f] - §e직업군 §7: §cTanker §6=============\n"
				+ "§f- §a주스킬 §7: §d돌진\n§7전방으로 돌진합니다. §c점프하면서 쓰세요.\n"
				+ "§f- §a보조스킬 §7: §d검은 장벽\n§74칸이내의 적에게 검은 장벽을 펼쳐 2초간 스킬을 사용할 수 없도록합니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d로켓 런쳐\n§7전방으로 돌진하며 경로상의 적에게 데메지를줍니다..\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d짙은 어둠\n§7검은 장벽의 침묵 지속시간이 1초 증가합니다.");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 6, true);
			p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
		    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
		    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
		}
	}

	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "보조스킬")){
			Cooldown.Setcooldown(p, "보조스킬", 12, true);
			p.getWorld().playSound(p.getLocation(), Sound.SKELETON_HURT, 2.0f, 0.5f);
			ParticleEffect.TOWN_AURA.display(0, 0, 0, 0.1f, 30, p.getLocation(), 30);
		    HelixEffect he = new HelixEffect(Main.effectManager);
		    he.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
		    he.period = 1;
		    he.particles = 20;
		    he.radius = 4;
		    he.setEntity(p);
		    he.start();
		    for(Player t : MobSystem.getEnemy(p)){
		    	if(t.getLocation().distance(p.getLocation()) <= 4){
		    		MobSystem.CancelSkill(t, can_passive ? 3 : 2);
					t.getWorld().playSound(p.getLocation(), Sound.EAT, 2.0f, 0.7f);
		    	}
		    }
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 111, true);
			Location l = p.getLocation();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
				    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
				    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
				    for(Player t : MobSystem.getEnemy(p)){
				    	if(t.getLocation().distance(p.getLocation()) <= 5){
				    		t.damage(7, p);
							t.getWorld().playSound(p.getLocation(), Sound.HURT, 2.0f, 0.7f);
				    	}
				    }
				    DonutEffect dn = new DonutEffect(Main.effectManager);
				    dn.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
				    dn.period = 3;
				    dn.iterations = 20;
					dn.setEntity(p);
					dn.start();
				}
			}, 0l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
				    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
				    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
				    for(Player t : MobSystem.getEnemy(p)){
				    	if(t.getLocation().distance(p.getLocation()) <= 5){
				    		t.damage(8, p);
							t.getWorld().playSound(p.getLocation(), Sound.HURT, 2.0f, 0.7f);
				    	}
				    }
				    DonutEffect dn = new DonutEffect(Main.effectManager);
				    dn.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
				    dn.period = 3;
				    dn.iterations = 20;
					dn.setEntity(p);
					dn.start();
				}
			}, 10l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
				    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
				    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
				    for(Player t : MobSystem.getEnemy(p)){
				    	if(t.getLocation().distance(p.getLocation()) <= 5){
				    		t.damage(6, p);
							t.getWorld().playSound(p.getLocation(), Sound.HURT, 2.0f, 0.7f);
				    	}
				    }
				    DonutEffect dn = new DonutEffect(Main.effectManager);
				    dn.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
				    dn.period = 3;
				    dn.iterations = 20;
					dn.setEntity(p);
					dn.start();
				}
			}, 20l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
				    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
				    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
				    for(Player t : MobSystem.getEnemy(p)){
				    	if(t.getLocation().distance(p.getLocation()) <= 5){
				    		t.damage(6, p);
							t.getWorld().playSound(p.getLocation(), Sound.HURT, 2.0f, 0.7f);
				    	}
				    }
				    DonutEffect dn = new DonutEffect(Main.effectManager);
				    dn.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
				    dn.period = 3;
				    dn.iterations = 20;
					dn.setEntity(p);
					dn.start();
				}
			}, 30l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable(){
				public void run(){
					p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2.0f, 2.5f);
				    p.setVelocity(p.getLocation().getDirection().multiply(2.5D).setY(0.1D));
				    ParticleEffect.CLOUD.display(0f, 0f, 0f, 0.2f, 30, p.getLocation(), 25);
				    for(Player t : MobSystem.getEnemy(p)){
				    	if(t.getLocation().distance(p.getLocation()) <= 5){
				    		t.damage(6, p);
							t.getWorld().playSound(p.getLocation(), Sound.HURT, 2.0f, 0.7f);
				    	}
				    }
				    DonutEffect dn = new DonutEffect(Main.effectManager);
				    dn.particle = de.slikey.effectlib.util.ParticleEffect.SPELL_MOB;
				    dn.period = 3;
				    dn.iterations = 20;
					dn.setEntity(p);
					dn.start();
				}
			}, 40l);
		}
	}
	
	public void onDamaged(EntityDamageEvent e){
		e.setDamage((int) (e.getDamage()*0.75));
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
			if(!can_Ultimate){ knight.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		e.setDamage(MobSystem.Getrandom(5, 6));
	}
}



