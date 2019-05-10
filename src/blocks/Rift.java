package blocks;

import items.Item;
import processing.core.PImage;

public class Rift extends LightBlock {

	static PImage[] tex = new PImage[3];
	static {
		for (int i = 0; i < tex.length; i++) {
			tex[i] = p.loadImage("textures/rift/" + i + ".png");
		}
	}

	public int getBrightness() {
		return 4;
	}

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
		draw(tex[(int) (Math.random() * tex.length)], x, y, z);
	}

	public Item getItem() {
		return null;
	}

}
