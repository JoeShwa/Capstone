package items;

import blocks.Block;
import blocks.Crystal;
import control.Globals;
import processing.core.PImage;

public class Empowerer extends Tool {

	static PImage tex = Globals.p.loadImage("textures/empowerer.png");

	public Empowerer(int amt) {
		super(amt);
	}

	public Empowerer() {
		super(0);
	}

	public String getName() {
		return "Empowerer";
	}

	public String getLore() {
		return "A specialized tool for infusing photons with additional energy and beaming them to blocks.";
	}

	public Block getBlock() {
		return null;
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

	public void rightClick() {
		int[] hit = Globals.player.scan(false);
		if (hit != null) {
			Block b = Globals.world.getBlock(hit[0], hit[1], hit[2]);
			if (Globals.player.energy >= 256 && b instanceof Crystal) {
				Crystal crystal = (Crystal) b;
				if (!crystal.isSuper) {
					Globals.player.energy -= 256;
					crystal.superLight(hit[0], hit[1], hit[2]);
				}
			} else if(Globals.player.energy >= 1024 && b instanceof blocks.Portal) {
				blocks.Portal portal = (blocks.Portal) b;
				Globals.player.energy -= 1024;
				portal.breakEvent(hit[0], hit[1], hit[2]);
				new portals.Pocket(hit[0], hit[1] - 10, hit[2], hit[0], hit[1] - 25, hit[2]);
			}
		}
	}

}
