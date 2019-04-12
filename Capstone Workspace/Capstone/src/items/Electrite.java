package items;

import blocks.Block;
import parts.Part;
import processing.core.PImage;

public class Electrite extends Item {
	
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

	public Part getPart(int x, int y) {
		return new parts.Electrite(x, y);
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
