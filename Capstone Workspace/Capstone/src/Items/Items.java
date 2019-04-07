package Items;

import Blocks.Block;

public interface Items {

	public String getName();
	
	public String getLore();

	public Block getBlock();

	public void draw(int x, int y);
}
