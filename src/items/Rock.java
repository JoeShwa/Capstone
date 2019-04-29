package items;

import blocks.Block;
import processing.core.PImage;

public class Rock extends Item {

	public Rock(int amt) {
		super(amt);
	}

	static PImage tex = blocks.Rock.tex;

	public String getName() {
		return "Rock";
	}
	
	public String getLore() {
		return "Mostly just basic metamorphic rock, quite dense due to all the pressure it's been under.\n\n"
				+ "It's moderately dense, and would make a decent projectile combat.";
	}

	public Block getBlock() {
		return new blocks.Rock();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}
}
