package items;

import blocks.Block;

public class Gravitium extends Item {

	public Gravitium(int amt) {
		super(amt);
	}

	public Gravitium() {
		super(0);
	}

	public String getName() {
		return "Gravitium";
	}

	public String getLore() {
		return "A substance imbued with dark matter on a quantum level, capable of warping space-time.";
	}

	public Block getBlock() {
		return new blocks.Gravitium();
	}

	public void draw(int x, int y) {
		draw(blocks.Gravitium.tex, x, y);

	}

}
