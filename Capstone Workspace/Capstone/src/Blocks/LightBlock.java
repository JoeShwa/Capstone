package Blocks;

import Control.Globals;
import Control.Main;

public abstract class LightBlock extends Block {

	public int getBrightness() {
		return 0;
	}

	public void lightArea(int x, int y, int z, int brightness) {
		for (int lx = -brightness; lx < brightness + 1; lx++) {
			for (int ly = -brightness; ly < brightness + 1; ly++) {
				for (int lz = -brightness; lz < brightness + 1; lz++) {
					double dist = Globals.sqrt(lx * lx + ly * ly + lz * lz);
					if (dist < brightness + 1) {
						short newLight = (short) (255 - dist * (255 / brightness));
						world.getBlock(lx + x, ly + y, lz + z).light = (short) Math
								.max(world.getBlock(lx + x, ly + y, lz + z).light, newLight);
					}
				}
			}
		}
	}

	public void breakLight(int x, int y, int z, int brightness) {
		Main.bmStart();
		for (int lx = -brightness; lx < brightness + 1; lx++) {
			for (int ly = -brightness; ly < brightness + 1; ly++) {
				for (int lz = -brightness; lz < brightness + 1; lz++) {
					double dist = Globals.sqrt(lx * lx + ly * ly + lz * lz);
					if (dist < brightness + 1) {
						world.getBlock(lx + x, ly + y, lz + z).light = AMBIENCE;
					}
				}
			}
		}
		int min = -BIGGEST_LIGHT - getBrightness();
		int max = BIGGEST_LIGHT + getBrightness() + 1;
		for (int lx = min; lx < max; lx++) {
			for (int ly = min; ly < max; ly++) {
				for (int lz = min; lz < max; lz++) {
					double dist = Globals.sqrt(lx * lx + ly * ly + lz * lz);
					Block b = world.getBlock(lx + x, ly + y, lz + z);
					if (dist < max && b.isLight()) {
						LightBlock bl = (LightBlock) b;
						bl.lightArea(lx + x, ly + y, lz + z, bl.getBrightness());
					}
				}
			}
		}
		Main.benchmark("break");
	}
	
	public void placeEvent(int x, int y, int z, Block prev) {
		super.placeEvent(x, y, z, prev);
		lightArea(x, y, z, getBrightness());
	}

	public void breakEvent(int x, int y, int z) {
		super.breakEvent(x, y, z);
		breakLight(x, y, z, getBrightness());
	}

	public boolean isLight() {
		return true;
	}

}
