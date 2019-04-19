package parts;

import items.Item;
import processing.core.PImage;

public class Rock extends Part {

	public Rock(int x, int y) {
		super(x, y);
	}

	public PImage getTexture() {
		return blocks.Rock.tex;
	}
	
	public Item getItem() {
		return new items.Rock();
	}

}
