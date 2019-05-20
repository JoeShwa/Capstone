package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class EnergyUpgrade extends Tool {

	static PImage tex = Globals.p.loadImage("textures/energyupgrade.png");

	public EnergyUpgrade(int amt) {
		super(amt);
	}

	public EnergyUpgrade() {
		super(0);
	}

	public String getName() {
		return "Energy Upgrade";
	}

	public String getLore() {
		return "Increases maximum energy by 8192.";
	}

	public Block getBlock() {
		return null;
	}

	public void rightClick() {
		Globals.player.inventory.useItem(getName(), 1);
		if (amount == 0) {
			Globals.player.selItem = null;
		}
		Globals.player.maxEnergy += 8192;
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
