package parts;

import control.Globals;
import items.Item;
import items.Tool;
import processing.core.PImage;

public class Thermite extends Part {

	public Thermite(int x, int y) {
		super(x, y);
	}

	public PImage getTexture() {
		return blocks.Thermite.tex;
	}

	public Item getItem() {
		return new items.Thermite();
	}

	// Heats adjacent blocks and destroys self
	public void hitPart(double hxv, double hyv) {
		if (Math.abs(hxv) + Math.abs(hyv) > 3) {
			Tool tool = (Tool) (Globals.player.selItem);
			tool.setPart(x, y, new Air(x, y));
			for (int[] dir : dirs2D) {
				tool.getPart(x + dir[0], y + dir[1]).heat();
			}
		} else {
			super.hitPart(hxv, hyv);
		}
	}
}
