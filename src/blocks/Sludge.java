package blocks;

import control.Globals;
import items.Item;
import processing.core.PImage;

public class Sludge extends Block {
	
	public static PImage tex = Globals.p.loadImage("textures/sludge.png");

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
		return new items.Sludge(1);
	}

}
