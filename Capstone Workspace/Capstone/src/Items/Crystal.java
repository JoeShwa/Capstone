package Items;

import Blocks.Block;
import Control.Globals;
import processing.core.PImage;

public class Crystal extends Item {

	static PImage tex = Globals.p.loadImage("textures/crystal.png");

	public String getName() {
		return "Crystal";
	}

	public String getLore() {
		return "A material with a crystalline structure that appears quite efficient at converting void energy to radiant energy. It's extremely fragile, and likely unrepurposable.";
	}

	public Block getBlock() {
		return new Blocks.Crystal();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
