package parts;

import items.Tool;
import processing.core.PImage;

public class StarRock extends Part {

	
	public StarRock(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public void move(Tool tool, char dir) {
		super.move(tool, dir);
	}

	public PImage getTexture() {
		return blocks.StarRock.tex;
	}

}
