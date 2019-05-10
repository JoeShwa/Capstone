package items;

import blocks.Block;
import processing.core.PImage;

public class Thermite extends Item {
	
	public Thermite(int amt) {
		super(amt);
	}
	
	public Thermite() {
		super(0);
	}

	static PImage tex = blocks.Thermite.tex;

	public String getName() {
		return "Thermite";
	}

	public String getLore() {
		return "An extremely volatile powder capable of producing immense heat.\n\n"
				+ "If ignited by Electrite, it may generate small amounts of energy.";
	}

	public Block getBlock() {
		return new blocks.Thermite();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
