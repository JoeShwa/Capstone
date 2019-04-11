package parts;

import items.Item;
import items.Tool;
import processing.core.PImage;

public class StarRock extends Part {

	public StarRock(int x, int y) {
		super(x, y);
	}

	public void move(Tool tool) {
		super.move(tool);
	}

	public PImage getTexture() {
		return blocks.StarRock.tex;
	}
	
	public Item getItem() {
		return new items.StarRock();
	}

}
