package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class IntegrityUpgrade extends Tool {

	static PImage tex = Globals.p.loadImage("textures/integrityupgrade.png");

	public IntegrityUpgrade(int amt) {
		super(amt);
	}

	public IntegrityUpgrade() {
		super(0);
	}

	public String getName() {
		return "Integrity Upgrade";
	}

	public String getLore() {
		return "Increases maximum integrity by 512.";
	}

	public Block getBlock() {
		return null;
	}

	public void rightClick() {
		Globals.player.inventory.useItem(getName(), 1);
		if (amount == 0) {
			Globals.player.selItem = null;
		}
		Globals.player.maxInteg += 512;
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
