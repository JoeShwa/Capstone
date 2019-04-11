package blocks;

import items.Item;
import processing.core.PImage;

public class Thermite extends Block {
	
	public static final PImage tex = p.loadImage("textures/thermite.png");

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 20;
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public Item getItem() {
		return new items.Thermite();
	}

}
