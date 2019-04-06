package Items;

import Blocks.Block;
import processing.core.PImage;

public class Rock extends Item {

	static PImage tex = p.loadImage("textures/rock.png");

	public String getName() {
		return "Rock";
	}

	public Block getBlock() {
		return new Blocks.Rock();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}
}
