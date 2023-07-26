package me.dreig_michihi.addonbackbone.bending.air;

import me.dreig_michihi.addonbackbone.config.annotations.AssociatedAbility;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@AssociatedAbility("XTestAirAbil")
public class TestAirAbilListener implements Listener {
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking()
				&& BendingPlayer.getBendingPlayer(player).getBoundAbility()
				== CoreAbility.getAbility(XTestAirAbil.class))
			new XTestAirAbil(player);
	}
}
