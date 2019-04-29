package items;

import blocks.Block;
import processing.core.PImage;

public class Electrite extends Item {
	
	public Electrite(int amt) {
		super(amt);
	}

	static PImage tex = blocks.Electrite.tex;

	public String getName() {
		return "Electrite";
	}

	public String getLore() {
		return "A negatively charged compound containing a positively charged fluid with a tendency to attract electricity.\n\n"
				+ "Converts other forms of energy around it into usable electricity.";
	}

	public Block getBlock() {
		return new blocks.Electrite();
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
