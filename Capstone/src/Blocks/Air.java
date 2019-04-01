package Blocks;

import Items.Item;

public class Air extends Block {

	public void draw(int x, int y, int z) {
		// Do nothing
	}
	
	public Item getItem() {
		return null;
	}

	public boolean isTrans() {
		return true;
	}

	public boolean isSolid() {
		return false;
	}
}
