package Events;

import Control.Main;
import Control.Player;

public class MineDelayEvent extends Event {

	Player player;
	Main m;

	public MineDelayEvent(Player player, Main m) {
		this.player = player;
		this.m = m;
	}

	public void trigger() {
		if (player.canMine && m.mousePressed && m.mouseButton == Main.LEFT) {
			player.leftClick();
		}
	}
}
