package Items;

import Blocks.Block;
import Control.Globals;
import processing.core.PImage;

public class Crystal extends Item {
	
	static PImage tex = Globals.p.loadImage("textures/crystal.png");

	public String getName() {
		return "Crystal";
	}

	public Block getBlock() {
		return new Blocks.Crystal();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
