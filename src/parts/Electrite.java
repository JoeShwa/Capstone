package parts;

import items.Item;
import processing.core.PImage;

public class Electrite extends Part {
	
	public Electrite(int x, int y) {
		super(x, y);
	}

	public PImage getTexture() {
		return blocks.Electrite.tex;
	}
	
	public Item getItem() {
		return new items.Electrite();
	}

}
