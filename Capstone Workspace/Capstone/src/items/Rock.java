package items;

import blocks.Block;
import processing.core.PImage;

public class Rock extends Item {

	static PImage tex = p.loadImage("textures/rock.png");

	public String getName() {
		return "Rock";
	}
	
	public String getLore() {
		return "Mostly just basic metamorphic rock, quite dense due to all the pressure it's been under.\n\n"
				+ "It seems to be a decently sturdy building material, capable of surviving moderately strong impacts.";
	}

	public Block getBlock() {
		return new blocks.Rock();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}
}
