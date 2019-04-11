package items;

import blocks.Block;
import parts.Part;
import processing.core.PImage;

public class StarRock extends Item {

	static PImage tex = p.loadImage("textures/starrock.png");

	public String getName() {
		return "Star Rock";
	}

	public String getLore() {
		return "Rock infused with extra-dimensional energies. It appears to harness the energy of distant stars to produce small amounts of light.\n\n"
				+ "If put under extreme pressure, it may release more than just light.";
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

	public Block getBlock() {
		return new blocks.StarRock();
	}

	public Part getPart(int x, int y) {
		return new parts.StarRock(x, y);
	}
}
