package items;

import blocks.Block;
import parts.Part;
import processing.core.PImage;

public class Thermite extends Item {
	
	static PImage tex = blocks.Thermite.tex;

	public String getName() {
		return "Thermite";
	}

	public String getLore() {
		return "An extremely volatile powder capable of producing immense heat.\n\n"
				+ "If it hits something hard enough, it will ignite and rapidly heat things around it.";
	}

	public Block getBlock() {
		return new blocks.Thermite();
	}

	public Part getPart(int x, int y) {
		return new parts.Thermite(x, y);
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
