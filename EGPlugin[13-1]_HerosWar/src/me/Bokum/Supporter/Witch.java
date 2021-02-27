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
import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.CloudEffect;
import me.Bokum.MOB.Main;
import me.Bokum.MOB.Ability.Ability;
import me.Bokum.MOB.Game.Cooldown;
import me.Bokum.MOB.Game.MobSystem;
import me.Bokum.MOB.Utility.ParticleEffect;
import me.Bokum.MOB.Utility.Vector3D;

public class Witch extends Ability{

	public Player witch;
	public boolean skill3 = false;
	
	public Witch(String playername, Player p){
		super(playername, "마녀");
		witch = p;
		ItemStack item = new ItemStack(369, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b마녀 §f]");
		List<String> lorelist = new ArrayList<String>();
		lorelist.add("§f- §7공격력 : §66");
		lorelist.add("§f- §7데미지 배율 : §6받은 데미지 x 1");
		lorelist.add("§f- §7공격 : §6★★★★☆");
		lorelist.add("§f- §7기동 : §6☆☆☆☆☆");
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
		witch.sendMessage("§6============= §f[ §b파워 §f] - §e직업군 §7: §cSupporter §6=============\n"
				+ "§f- §a주스킬 §7: §d저주(독)\n§710칸내에 바라보는 적에게 독1에 10초간 중독시킵니다."
				+ "§f- §a보조스킬 §7: §d저주(위더)\n§710칸내에 바라보는 적에게 위더1에  10초간 중독시킵니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d저주(죽음)\n§710칸내에 바라보는 적에게 죽음의 저주를 겁니다. §c초당 3의 체력을 깎습니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d저주 강화\n§7저주의 지속시간 0.5배증가.");
	}
	
	  public void PrimarySkill(Player p)
	  {
	    if (Cooldown.Checkcooldown(p, "주스킬")) {
	      Player t = MobSystem.Getcorsurplayer(p, 10);
	      if ((t == null) || (MobSystem.getTeam(p).contains(t))) {
	        p.sendMessage(Main.MS + "10칸내에 바라보는곳에 적이 없습니다.");
	        return;
	      }
	      Cooldown.Setcooldown(p, "주스킬", 30, true);
	      t.getWorld().playSound(t.getLocation(), Sound.GHAST_SCREAM, 1.5F, 1.7F);
	      p.getWorld().playSound(t.getLocation(), Sound.GHAST_CHARGE, 1.5F, 1.7F);
	      ParticleEffect.SPELL_WITCH.display(0.1F, 0.1F, 0.1F, 0.0F, 35, t.getLocation(), 20.0D);
	      t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, this.can_passive ? 300 : 200, 0));
	      p.sendMessage(Main.MS + t.getName() + " 님에게 저주를 걸었습니다.");
	      t.sendMessage(Main.MS + "마녀에 저주(독)에 걸렸습니다.");
	      ParticleEffect.REDSTONE.display(0.3F, 0.3F, 0.3F, 0.0F, 40, p.getLocation(), 25.0D);
	    }
	  }

	  public void SecondarySkill(Player p) {
	    if (Cooldown.Checkcooldown(p, "보조스킬")) {
	      Player t = MobSystem.Getcorsurplayer(p, 10);
	      if ((t == null) || (MobSystem.getTeam(p).contains(t))) {
	        p.sendMessage(Main.MS + "10칸내에 바라보는곳에 적이 없습니다.");
	        return;
	      }
	      Cooldown.Setcooldown(p, "보조스킬", 30, true);
	      t.getWorld().playSound(t.getLocation(), Sound.GHAST_SCREAM, 1.5F, 1.7F);
	      p.getWorld().playSound(t.getLocation(), Sound.GHAST_CHARGE, 1.5F, 1.7F);
	      ParticleEffect.SPELL_WITCH.display(0.1F, 0.1F, 0.1F, 0.0F, 35, t.getLocation(), 20.0D);
	      t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, this.can_passive ? 300 : 200, 0));
	      p.sendMessage(Main.MS + t.getName() + " 님에게 저주를 걸었습니다.");
	      t.sendMessage(Main.MS + "마녀에 저주(위더)에 걸렸습니다.");
	      ParticleEffect.REDSTONE.display(0.3F, 0.3F, 0.3F, 0.0F, 40, p.getLocation(), 25.0D);
	    }
	  }
	
	public void UltimateSkill(final Player p){
		if(Cooldown.Checkcooldown(p, "궁극기")){
			final Player t = MobSystem.Getcorsurplayer(p, 10);
			if(t == null || MobSystem.getTeam(p).contains(t)){
				p.sendMessage(Main.MS+"10칸내에 바라보는곳에 적이 없습니다.");
				return;
			}
			Cooldown.Setcooldown(p, "궁극기", 144, true);
			t.getWorld().playSound(t.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0f, 0.2f);
			p.getWorld().playSound(t.getLocation(), Sound.GHAST_CHARGE, 1.5f, 1.7f);
			ParticleEffect.SPELL_WITCH.display(0.1f, 0.1f, 0.1f, 0, 35, t.getLocation(), 20);
			p.sendMessage(Main.MS+t.getName()+" 님에게 저주를 걸었습니다.");
			t.sendMessage(Main.MS+"마녀에 저주(죽음)에 걸렸습니다.");
			timertime = can_passive ? 5 : 8;
			timernum = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable(){
				public void run(){
					if(--timertime > 0){
						t.damage(3, p);
						t.getWorld().playSound(t.getLocation(), Sound.HURT, 1.5f, 1.2f);
						ParticleEffect.REDSTONE.display(0.3f, 0.3f, 0.3f, 0, 40, p.getLocation(), 25);
					} else {
						Bukkit.getScheduler().cancelTask(timernum);
					}
				}
			}, 20l, 20l);
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
			if(!can_Ultimate){ witch.sendMessage(Main.MS+"궁극기는 상점에서 구매후 사용가능합니다."); return;}
			e.getPlayer().getInventory().setHeldItemSlot(0);
			UltimateSkill(e.getPlayer());
		}
	}
	
	public void onHit(EntityDamageByEntityEvent e){
		if(witch.getInventory().getHeldItemSlot() != 0) return;
		e.setDamage(6);
	}
	
}




