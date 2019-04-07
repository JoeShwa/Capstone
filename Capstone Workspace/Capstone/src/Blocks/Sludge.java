package Blocks;

import Control.Globals;
import Items.Item;
import processing.core.PImage;

public class Sludge extends Block {
	
	static PImage tex = Globals.p.loadImage("textures/sludge.png");

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 40;
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public Item getItem() {
		return new Items.Sludge();
	}

}
