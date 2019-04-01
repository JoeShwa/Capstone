package Items;

import processing.core.PImage;

public class Rock extends Item {

	static PImage tex = p.loadImage("textures/rock.png");
	
	public String getName() {
		return "Rock";
	}
	
	public void draw(int x, int y) {
		draw(tex, x, y);
	}
}
