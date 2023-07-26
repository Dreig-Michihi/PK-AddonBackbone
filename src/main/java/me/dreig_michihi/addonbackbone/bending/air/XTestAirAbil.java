package me.dreig_michihi.addonbackbone.bending.air;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.attribute.AttributeModifier;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.dreig_michihi.addonbackbone.util.PluginAbility;
import me.dreig_michihi.addonbackbone.config.annotations.Comment;
import me.dreig_michihi.addonbackbone.config.annotations.Configurable;
import me.dreig_michihi.addonbackbone.config.annotations.Description;
import me.dreig_michihi.addonbackbone.config.annotations.Instructions;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Description("My default Description")
@Instructions("My default Instructions")
public class XTestAirAbil extends AirAbility implements PluginAbility {

	@Comment("inline comment")
	private static @Configurable int Value1 = 123;

	@Comment("""
			More-strokes comment
			 with triple quotes""")
	private static @Configurable("Controls") float Value2 = 0.5F;

	@Comment("Multi-line comment with \\n\nTest\nTest")
	private static @Configurable("Controls") String Value3 = "Test";

	@Comment("""
			One-stroke comment with triple quotes""")
	private static @Configurable double Damage = 1d;

	private @Attribute("Value1") int value1 = Value1;
	private @Attribute("Value2") float value2 = Value2;
	private @Attribute("Value3") String value3 = Value3;
	private @Attribute(Attribute.DAMAGE) double damage = Damage;

	public XTestAirAbil(Player player) {
		super(player);
		player.sendMessage(""+value1);
		Location loc = player.getLocation().add(0,player.getHeight(),0).add(player.getLocation().getDirection().multiply(3));
		ParticleEffect.END_ROD.display(loc, 15, 0.1, 0.1, 0.1, 0.1);
		List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(loc, 3);
		entities.remove(player);
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) entity;
				DamageHandler.damageEntity(living, damage, this);
				ParticleEffect.FLAME.display(living.getEyeLocation(), 30, 0.1, 0.2, 0.1, 0.3);
			}
		}
		start();
		addAttributeModifier(Attribute.DAMAGE, 2, AttributeModifier.ADDITION);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Entity entity : entities) {
					if (entity instanceof LivingEntity) {
						LivingEntity living = (LivingEntity) entity;
						living.setNoDamageTicks(0);
						DamageHandler.damageEntity(living, damage, XTestAirAbil.this);
						ParticleEffect.SOUL_FIRE_FLAME.display(living.getEyeLocation(), 30, 0.15, 0.5, 0.15, 0.3);
					}
				}
				remove();
			}
		}.runTaskLater(ProjectKorra.plugin, 10);
		player.sendMessage(""+value2);
		player.sendMessage(value3);
	}

	@Override
	public void progress() {
	}

	private static @Configurable boolean Enabled = true;
	@Override
	public boolean isEnabled() {
		return Enabled;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public String getName() {
		return "TestAirAbility";
	}

	@Override
	public Location getLocation() {
		return null;
	}
}
