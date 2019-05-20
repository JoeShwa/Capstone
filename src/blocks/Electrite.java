package blocks;

import control.Globals;
import events.EventManager;
import events.ThermiteElectriteEvent;
import items.Item;
import processing.core.PImage;

public class Electrite extends Block {

	public static final PImage tex = p.loadImage("textures/electrite.png");

	// How much energy the block is storing
	public int energy = 0;

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 25;
	}

	// Makes thermite burn after 1 second and generate 1024 energy
	public void update(int x, int y, int z) {
		super.update(x, y, z);
		for (int[] dir : dirs) {
			if (Globals.world.getBlock(x + dir[0], y + dir[1], z + dir[2]) instanceof Thermite) {
				EventManager.addEvent(new ThermiteElectriteEvent(x + dir[0], y + dir[1], z + dir[2], this), 60);
			}
		}
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public Item getItem() {
		return new items.Electrite(1);
	}
}
