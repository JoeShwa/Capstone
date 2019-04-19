package items;

import blocks.Block;
import parts.Part;

public interface Items {

	public String getName();
	
	public String getLore();

	public Block getBlock();
	
	public Part getPart(int x, int y);

	public void draw(int x, int y);
}
