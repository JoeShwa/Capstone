package Items;

import Blocks.Block;
import processing.core.PImage;

public class StarRock extends Item {

	static PImage tex = p.loadImage("textures/starrock.png");

	public String getName() {
		return "StarRock";
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

	public Block getBlock() {
		return new Blocks.StarRock();
	}
}
