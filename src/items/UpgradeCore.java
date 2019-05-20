package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class UpgradeCore extends Item {

	static PImage tex = Globals.p.loadImage("textures/upgradecore.png");

	public UpgradeCore(int amt) {
		super(amt);
	}

	public UpgradeCore() {
		super(0);
	}

	public String getName() {
		return "Upgrade Core";
	}

	public String getLore() {
		return "An alloy imbued with special properties useful in enhancing the capabilities of oneself.";
	}

	public Block getBlock() {
		return null;
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
