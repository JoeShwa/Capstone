package blocks;

import control.Globals;
import items.Item;
import processing.core.PImage;

public class Portal extends Block {
	
	public static final PImage tex = Globals.p.loadImage("textures/portal.png");

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 10;
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public Item getItem() {
		return new items.Portal(1);
	}

}
