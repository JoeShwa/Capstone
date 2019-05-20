package items;

import blocks.Block;

public class Eskirite extends Item {

	public Eskirite(int amt) {
		super(amt);
	}

	public Eskirite() {
		super(0);
	}

	public String getName() {
		return "Eskirite";
	}

	public String getLore() {
		return "A lightweight but highly resilient substance with a slight glow.\n\n" + "<purpose>";
	}

	@Override
	public Block getBlock() {
		return new blocks.Eskirite();
	}

	@Override
	public void draw(int x, int y) {
		draw(blocks.Eskirite.tex, x, y);
	}

}
