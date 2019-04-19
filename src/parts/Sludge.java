package parts;

import control.Globals;
import items.Item;
import items.Tool;
import processing.core.PImage;

public class Sludge extends Part {

	static final int EXP_RAD = 3;
	static final int EXP_POW = 10;

	public Sludge(int x, int y) {
		super(x, y);
	}

	public PImage getTexture() {
		return blocks.Sludge.tex;
	}

	public void move(Tool tool) {
		// Makes sludge act very sticky, with a high static friction coefficient
		xv *= 0.9;
		yv *= 0.9;
		if (Math.abs(xv) < 1) {
			xv = 0;
		}
		if (Math.abs(yv) < 1) {
			yv = 0;
		}
		super.move(tool);
	}

	public Item getItem() {
		return new items.Sludge();
	}

	// Makes sludge explode when heated
	public void heat() {
		Tool tool = (Tool) Globals.player.selItem;
		tool.setPart(x, y, new Air(x, y));
		for (int sx = -EXP_RAD; sx <= EXP_RAD; sx++) {
			for (int sy = -EXP_RAD; sy <= EXP_RAD; sy++) {
				double dist = Math.sqrt(sx * sx + sy * sy);
				if (dist < EXP_RAD && dist != 0) {
					double xh = EXP_POW * sx / dist / dist;
					double yh = EXP_POW * sy / dist / dist;
					tool.getPart(x + sx, y + sy).hitPart(xh, yh);
				}
			}
		}
	}
}
