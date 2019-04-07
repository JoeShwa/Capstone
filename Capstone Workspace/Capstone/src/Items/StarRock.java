package Items;

import Blocks.Block;
import processing.core.PImage;

public class StarRock extends Item {

	static PImage tex = p.loadImage("textures/starrock.png");

	public String getName() {
		return "StarRock";
	}
	
	public String getLore() {
		return "In a flux-state between dark matter and normal matter, its composition is unknown, but it appears to harness the energy of distant stars to produce small amounts of light.";
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

	public Block getBlock() {
		return new Blocks.StarRock();
	}
}
