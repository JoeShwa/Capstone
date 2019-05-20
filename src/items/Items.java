package items;

import blocks.Block;

public interface Items {

	public String getName();

	public String getLore();

	public Block getBlock();

	public void draw(int x, int y);
}
