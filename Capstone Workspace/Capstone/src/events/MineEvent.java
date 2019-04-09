package events;

import control.Globals;
import control.Main;
import control.Player;

public class MineEvent extends Event {

	Player player;
	Main m;

	public MineEvent(Player player, Main m) {
		this.player = player;
		this.m = m;
	}

	public void trigger() {
		if (player.canMine && m.mousePressed && m.mouseButton == Main.LEFT && Globals.gui.guiState == control.GUI.GAME) {
			player.leftClick();
		}
	}
}
