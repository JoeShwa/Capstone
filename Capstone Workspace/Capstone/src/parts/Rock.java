package parts;

import items.Tool;
import processing.core.PImage;

public class Rock extends Part {

	
	public Rock(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public void move(Tool tool, char dir) {
		super.move(tool, dir);
	}

	public PImage getTexture() {
		return blocks.Rock.tex;
	}

}
