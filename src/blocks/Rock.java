package blocks;

import items.Item;
import processing.core.PImage;

public class Rock extends Block {

	public static PImage tex = p.loadImage("textures/rock.png");

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public int getHardness() {
		return 10;
	}

	public Item getItem() {
		return new items.Rock(1);
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
