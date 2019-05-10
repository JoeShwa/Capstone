package blocks;

import control.Globals;
import items.Item;
import processing.core.PImage;

public class Crystal extends LightBlock {

	public static PImage tex = Globals.p.loadImage("textures/crystal/crystal.png");
	public static PImage tex2 = Globals.p.loadImage("textures/crystal/crystal2.png");
	public boolean isSuper = false;

	public int getBrightness() {
		return 8;
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 20;
	}

	public void draw(int x, int y, int z) {
		if (isSuper) {
			draw(tex2, x, y, z);
		} else {
			draw(tex, x, y, z);
		}
	}

	public void superLight(int x, int y, int z) {
		isSuper = true;
		int brightness = getBrightness();
		for (int lx = -brightness; lx < brightness + 1; lx++) {
			for (int ly = -brightness; ly < brightness + 1; ly++) {
				for (int lz = -brightness; lz < brightness + 1; lz++) {
					double dist = Globals.sqrt(lx * lx + ly * ly + lz * lz);
					if (dist < brightness + 1) {
						short newLight = 255;
						world.getBlock(lx + x, ly + y, lz + z).light = (short) Math
								.max(world.getBlock(lx + x, ly + y, lz + z).light, newLight);
					}
				}
			}
		}
	}

	public Item getItem() {
		return new items.Crystal(1);
	}

}
