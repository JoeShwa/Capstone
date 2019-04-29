package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class Empowerer extends Tool {
	
	static PImage tex = Globals.p.loadImage("textures/empowerer.png");

	public Empowerer(int amt) {
		super(amt);
	}

	public String getName() {
		return "Empowerer";
	}

	public String getLore() {
		return "A specialized tool for infusing photons with energy and beaming them to blocks.";
	}

	public Block getBlock() {
		return null;
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
