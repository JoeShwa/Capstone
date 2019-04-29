package blocks;

import control.Globals;
import items.Item;
import processing.core.PImage;

public class Crystal extends LightBlock {
	
	public static PImage tex = Globals.p.loadImage("textures/crystal.png");

	public int getBrightness() {
		return 8;
	}
	
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
		return new items.Crystal(1);
	}

}
