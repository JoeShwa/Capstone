package items;

import blocks.Block;

public class Portal extends Item {

	public Portal(int amt) {
		super(amt);
	}

	public Portal() {
		super(0);
	}

	public String getName() {
		return "Portal";
	}

	public String getLore() {
		return "An extremely complex device designed to create and quantumly a pocket dimension capable of existing in two places at once.";
	}

	public Block getBlock() {
		return new blocks.Portal();
	}

	public void draw(int x, int y) {
		draw(blocks.Portal.tex, x, y);

	}

}
