package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class Sludge extends Item {
	
	public Sludge(int amt) {
		super(amt);
	}

	static PImage tex = Globals.p.loadImage("textures/sludge.png");

	public String getName() {
		return "Sludge";
	}
	
	public String getLore() {
		return "A sticky half-liquid, half-solid substance which is difficult to energize due to its nature.\n\n"
				+ "It may be useful in holding things together. It will also explode if heated quickly.";
	}

	public Block getBlock() {
		return new blocks.Sludge();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
