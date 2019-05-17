package blocks;

import items.Item;
import processing.core.PImage;

public class Gravitium extends Block {

	public static PImage tex = p.loadImage("textures/gravitium.png");

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public int getHardness() {
		return 20;
	}

	public Item getItem() {
		return new items.Gravitium(1);
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
