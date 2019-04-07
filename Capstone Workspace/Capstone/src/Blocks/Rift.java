package Blocks;

import Items.Item;
import processing.core.PImage;

public class Rift extends Block {

	static PImage tex = p.loadImage("textures/rift.png");
	
	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 0;
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
		
	}

	public Item getItem() {
		return null;
	}

}
