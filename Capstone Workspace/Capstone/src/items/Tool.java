package items;

import blocks.Block;
import control.Globals;
import parts.Part;

public class Tool extends Item {

	private String name;
	private Part[][] parts;

	public Tool() {
		name = "Apparatus " + (int) (Math.random() * 100000);
		parts = new Part[16][16];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				int rand = (int) (Math.random() * 3);
				switch (rand) {
				case 0:
					parts[x][y] = new parts.Rock();
					break;
				case 1:
					parts[x][y] = new parts.Air();
					break;
				case 2:
					parts[x][y] = new parts.StarRock();
					break;
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getLore() {
		return "How are you even viewing this screen?";
	}

	public Block getBlock() {
		return null;
	}

	public void draw(int x, int y) {
		p.noStroke();
		// Rescale and refit position to render
		x *= 256;
		y *= 256;
		y += 28;
		// Draw each part
		for (int px = 0; px < parts.length; px++) {
			for (int py = 0; py < parts[0].length; py++) {
				Part part = parts[px][py];
				if (part == null) {
					Globals.p.fill(0);
				} else {
					Globals.p.fill(255);
				}
				part.draw(x + px * 16, y + py * 16, 1);
			}
		}
	}

}
