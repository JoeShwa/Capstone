package Control;

public abstract class LightBlock extends Block {

	public int getBrightness() {
		return 0;
	}

	public void lightArea(int x, int y, int z, int brightness) {
		for (int lx = -brightness; lx < brightness + 1; lx++) {
			for (int ly = -brightness; ly < brightness + 1; ly++) {
				for (int lz = -brightness; lz < brightness + 1; lz++) {
					double dist = Math.sqrt(lx * lx + ly * ly + lz * lz);
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
		for (int lx = -brightness; lx < brightness + 1; lx++) {
			for (int ly = -brightness; ly < brightness + 1; ly++) {
				for (int lz = -brightness; lz < brightness + 1; lz++) {
					double dist = Math.sqrt(lx * lx + ly * ly + lz * lz);
					if (dist < brightness + 1) {
						world.getBlock(lx + x, ly + y, lz + z).light = AMBIENCE;
					}
				}
			}
		}
		for (int lx = -BIGGEST_LIGHT; lx < BIGGEST_LIGHT + 1; lx++) {
			for (int ly = -BIGGEST_LIGHT; ly < BIGGEST_LIGHT + 1; ly++) {
				for (int lz = -BIGGEST_LIGHT; lz < BIGGEST_LIGHT + 1; lz++) {
					double dist = Math.sqrt(lx * lx + ly * ly + lz * lz);
					Block b = world.getBlock(lx + x, ly + y, lz + z);
					if (dist < BIGGEST_LIGHT + 1 && b.isLight()) {
						LightBlock bl = (LightBlock) b;
						bl.lightArea(lx + x, ly + y, lz + z, bl.getBrightness());
					}
				}
			}
		}
	}

	public boolean isLight() {
		return true;
	}

}
