package Blocks;

import processing.core.PImage;

public class Rock extends Block {

	static PImage tex = p.loadImage("rock.png");

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public boolean isLight() {
		return false;
	}

}
