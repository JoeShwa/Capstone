package Blocks;

import Control.Globals;
import Items.Item;
import processing.core.PImage;

public class Crystal extends LightBlock {
	
	static PImage tex = Globals.p.loadImage("textures/crystal.png");

	public short getBrightness() {
		return 5000;
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
		return new Items.Crystal();
	}

}
