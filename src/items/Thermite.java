package items;

import blocks.Block;
import processing.core.PImage;

public class Thermite extends Item {
	
	public Thermite(int amt) {
		super(amt);
	}

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

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
