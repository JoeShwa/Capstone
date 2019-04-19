package blocks;

import items.Item;
import processing.core.PImage;

public class StarRock extends LightBlock {

	public static PImage tex = p.loadImage("textures/starrock.png");

	public int getBrightness() {
		return 4;
	}

	public Item getItem() {
		return new items.StarRock();
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public int getHardness() {
		return 30;
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
