package parts;

import control.Globals;
import items.Item;
import items.Tool;
import processing.core.PImage;

public class Spawner extends Part implements Trigger {

	Part part;
	Tool tool;

	public Spawner(int x, int y, Tool tool, Part part) {
		super(x, y);
		this.part = part;
		this.tool = tool;
	}

	public PImage getTexture() {
		return part.getTexture();
	}

	public Item getItem() {
		return part.getItem();
	}

	public void activate() {
		if (tool.getPart(x, y + 1) instanceof Air) {
			Item item = part.getItem();
			if (Globals.player.inventory.useItem(item.getName(), 1)) {
				tool.setPart(x, y + 1, item.getPart(x, y + 1));
			}
		}
	}

	public void draw(int x, int y, int scale) {
		super.draw(x, y, scale);
		// Show that the part is a spawner
		Globals.p.noFill();
		Globals.p.strokeWeight(2);
		Globals.p.stroke(255);
		Globals.p.ellipse(x + 8 * scale, y + 8 * scale, 12 * scale, 12 * scale);
	}

	public void move(Tool tool) {
		// Spawners don't move
		xv = 0;
		yv = 0;
	}
}
