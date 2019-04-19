package items;

import blocks.Block;
import parts.Part;
import processing.core.PImage;

public class Rock extends Item {

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

	public Part getPart(int x, int y) {
		return new parts.Rock(x, y);
	}
}
