package Blocks;

import Items.Item;

public interface Blocks {

	boolean isTrans();

	boolean isSolid();

	boolean isLight();
	
	int getHardness();

	void draw(int x, int y, int z);
	
	Item getItem();
}
