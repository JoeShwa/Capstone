package Items;

import Blocks.Block;
import Control.Globals;
import processing.core.PImage;

public class Sludge extends Item {
	
	static PImage tex = Globals.p.loadImage("textures/sludge.png");

	public String getName() {
		return "Sludge";
	}
	
	public String getLore() {
		return "A sticky half-liquid, half-solid substance which is difficult to energize due to its nature.";
	}

	public Block getBlock() {
		return new Blocks.Sludge();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
