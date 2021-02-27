package me.Bokum.MOB.Attacker;

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

public class Assasin extends Ability{

	public Player assasin;
	public boolean skill2 = false;
	public boolean hiding = false;
	
	public Assasin(String playername, Player p){
		super(playername, "암살자");
		assasin = p;
		ItemStack item = new ItemStack(276, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b암살자 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §67~9");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 2");
		lorelist.add("§f- §7공격 : §6★★★★★");
		lorelist.add("§f- §7기동 : §6★☆☆☆☆");
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
		assasin.sendMessage("§6============= §f[ §b암살자 §f] - §e직업군 §7: §cAttacker §6=============\n"
				+ "§f- §a주스킬 §7: §d은신\n§77초간 모습을 감춥니다. §c플레이어 타격시 은신이 해제됩니다. 팀에게는 은신된 모습으로 안보입니다. 남은시간은 경험치바로 보실 수 있습니다.\n "
				+ "§f- §a보조스킬 §7: §d암살\n§7은신상태에서 타격시 공격력이 2배가 됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d집행\n§7주변 9칸이내에 있는 모든 적의 뒤로 가서 5의 데미지를 주고옵니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d암습\n§7암살사용시 14%확률로 은신 쿨타임 초기화");
	}
	
	public void PrimarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "주스킬")){
			Cooldown.Setcooldown(p, "주스킬", 20, false);
			hiding = true;
			p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2.0f, 4.0f);
			ParticleEffect.SMOKE_LARGE.display(0.1f, 0.1f, 0.1f, 0f, 75, p.getLocation(), 20);
			for(Player t : MobSystem.getEnemy(p)){
				t.hidePlayer(p);
			}
			p.sendMessage(Main.MS+"7초간 모습을 감춥니다.");
			timertime = 11;
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(--timertime > 0){
						p.setExp((float)timertime/10);
						p.playSound(p.getLocation(), Sound.BREATH, 0.4f, 1.5f);
					} else {
						Bukkit.getScheduler().cancelTask(timernum);
						for(Player t : MobSystem.getEnemy(p)){
							t.showPlayer(p);
						}
						hiding = false;
						p.sendMessage(Main.MS+"은신이 해제됐습니다.");
						if(skill2){
							p.sendMessage(Main.MS+"은신이 풀려 암살스킬이 해제 되었습니다.");
							p.playSound(p.getLocation(),Sound.SHOOT_ARROW, 1.0f, 2.5f);
							skill2 = false;
						}
						p.setExp(0);
					}

				}
			}, 0l, 20l);
		}
	}
	
	public void onDamaged(EntityDamageEvent e){
		e.setDamage((int) (e.getDamage()*2));
	}
	
	public void SecondarySkill(final Player p){
		if(Cooldown.Checkcooldown(p, "암살")){
			if(!hiding){
				p.sendMessage(Main.MS+"은신상태에서만 사용이 가능합니다.");
				return;
			}
			Cooldown.Setcooldown(p, "암살", 20, true);
			skill2 = true;
			p.getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_DEATH, 2.0f, 3.0f);
			ParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.1f, 75, p.getLocation(), 20);
		}
	}
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			Cooldown.Setcooldown(p, "궁극기", 156, true);
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
			if(!can_Ultimate){ assasin.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		Player d = (Player) e.getDamager();
		Player p = (Player) e.getEntity();
		e.setDamage(MobSystem.Getrandom(7, 9));
		if(hiding){
			Bukkit.getScheduler().cancelTask(timernum);
			for(Player t : MobSystem.getEnemy(d)){
				t.showPlayer(d);
			}
			d.setExp(0);
			hiding = false;
			d.sendMessage(Main.MS+"은신이 풀렸습니다!");
			if(skill2){
				d.sendMessage(Main.MS+"적의 급소를 공격했습니다! (암살스킬)");
				e.setDamage(MobSystem.Getrandom(14, 18));
				skill2 = false;
			}
		}
	}
	
}
