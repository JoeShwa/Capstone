package parts;

import items.Item;
import items.Tool;
import processing.core.PImage;

public class Air extends Part {

	public Air(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public PImage getTexture() {
		return null;
	}
	
	public void move(Tool tool, char dir) {
		// Do nothing
	}

	public Item getItem() {
		return null;
	}

}
