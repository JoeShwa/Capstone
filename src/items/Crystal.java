package items;

import blocks.Block;
import control.Globals;
import parts.Part;
import processing.core.PImage;

public class Crystal extends Item {

	static PImage tex = Globals.p.loadImage("textures/crystal.png");

	public String getName() {
		return "Crystal";
	}

	public String getLore() {
		return "A material with a crystalline structure that appears quite efficient at converting void energy to radiant energy.\n\n"
				+ "It solidifies photons temporarily as it generates light, if heat were removed it may be able to release those solidified photons.";
	}

	public Block getBlock() {
		return new blocks.Crystal();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}
	
	public Part getPart(int x, int y) {
		return new parts.Crystal(x, y);
	}

}
