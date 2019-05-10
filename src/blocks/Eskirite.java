package blocks;

import items.Item;
import processing.core.PImage;

public class Eskirite extends LightBlock {

	public static PImage tex = p.loadImage("textures/eskirite.png");

	public int getBrightness() {
		return 6;
	}

	public Item getItem() {
		return new items.Eskirite(1);
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public int getHardness() {
		return 70;
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
