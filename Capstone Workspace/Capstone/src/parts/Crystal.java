package parts;

import items.Item;
import processing.core.PImage;

public class Crystal extends Part {
	
	public Crystal(int x, int y) {
		super(x, y);
	}

	public PImage getTexture() {
		return blocks.Crystal.tex;
	}
	
	public Item getItem() {
		return new items.Crystal();
	}
}
