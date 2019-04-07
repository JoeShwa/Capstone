package Blocks;

import processing.core.PImage;
import Items.Item;

public class Rock extends Block {

	static PImage tex = p.loadImage("textures/rock.png");

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public int getHardness() {
		return 10;
	}

	public Item getItem() {
		return new Items.Rock();
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
