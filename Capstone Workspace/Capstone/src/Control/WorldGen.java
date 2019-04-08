package Control;

import Blocks.*;

public class WorldGen extends Thread {

	World world;
	int phase = 0;
	static final int CAVES = 20;
	static final int CAVE_LEN = 30;
	static final int CAVE_RAD = 5;

	private double mod(double n1, double n2) {
		if (n1 < 0) {
			n1 += n2;
		}
		return n1 % n2;
	}

	public void noise(double x, double y) {

	}

	public WorldGen(World world) {
		this.world = world;
	}

	public void run() {
		fillAir(0, 0, 0, 100, 200, 100);
		phase = 0;
		generateDim0(100, 200);
		phase++;
		generateDim1(0, 100);
		phase++;
		// Run placeEvents on all blocks, now that none are null
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int y = 0; y < Globals.world.sizeY(); y++) {
				for (int z = 0; z < Globals.world.sizeZ(); z++) {
					Globals.world.getBlock(x, y, z).placeEvent(x, y, z, Globals.world.getBlock(x, y, z));
				}
			}
		}
		phase++;
	}

	// Fill an area with air
	public void fillAir(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					world.newBlock(x, y, z, new Air());
				}
			}
		}
	}

	public void generateDim1(int minY, int maxY) {
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int z = 0; z < Globals.world.sizeY(); z++) {
				int height = (int) -(Globals.p.noise((float) x / 20, (float) z / 20) * 20) + maxY;
				int y;
				for (y = maxY; y > height; y--) {
					world.setBlock(x, y, z, new Sludge());
				}
				int tx = x;
				int tz = z;
				if (Math.random() < 0.02) {
					while (Math.random() < 0.9) {
						y--;
						if (Math.random() < 0.3) {
							if (Math.random() > 0.5) {
								tx++;
							} else {
								tx--;
							}
						}
						if (Math.random() < 0.3) {
							if (Math.random() > 0.5) {
								tz++;
							} else {
								tz--;
							}
						}
						world.setBlock(tx, y, tz, new Crystal());
					}
				}
			}
		}
	}

	public void generateDim0(int minY, int maxY) {
		// Make everything rock
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = 0; z < Globals.world.sizeZ(); z++) {
					Globals.world.setBlock(x, y, z, new Rock());
				}
			}
		}
		// Add the caves
		for (int i = 0; i < CAVES; i++) {
			double cx = Math.random() * Globals.world.sizeX();
			double cy = Math.random() * (maxY - minY) + minY;
			double cz = Math.random() * Globals.world.sizeZ();
			double vx = Math.random() * 2 - 1;
			double vy = Math.random() * 2 - 1;
			double vz = Math.random() * 2 - 1;
			int len = (int) (Math.random() * CAVE_LEN * 0.5 + CAVE_LEN * 0.75);
			for (int j = 0; j < len; j++) {
				vx += Math.random() * 0.5 - 0.25;
				vy += Math.random() * 0.5 - 0.25;
				vz += Math.random() * 0.5 - 0.25;
				cx += vx;
				cy += vy;
				cz += vz;
				cx = mod(cx, Globals.world.sizeX());
				cy = mod(cy - minY, maxY - minY) + minY;
				cz = mod(cz, Globals.world.sizeZ());
				for (int x = (int) cx - CAVE_RAD; x < (int) cx + CAVE_RAD + 1; x++) {
					for (int y = (int) cy - CAVE_RAD; y < (int) cy + CAVE_RAD + 1; y++) {
						for (int z = (int) cz - CAVE_RAD; z < (int) cz + CAVE_RAD + 1; z++) {
							if (y < maxY && y >= minY && (x - cx) * (x - cx) + (y - cy) * (y - cy)
									+ (z - cz) * (z - cz) < CAVE_RAD * CAVE_RAD) {
								Globals.world.newBlock(x, y, z, new Air());
							}
						}
					}
				}
			}
		}
		// Used to determine which blocks are on the outline of caves
		Globals.world.updateAllFaces();
		// Add the lights
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int y = 0; y < Globals.world.sizeY() - 1; y++) {
				for (int z = 0; z < Globals.world.sizeZ(); z++) {
					if (Globals.world.getBlock(x, y, z) instanceof Rock && Globals.world.getBlock(x, y, z).isVisible
							&& Math.random() < 0.04) {
						Globals.world.newBlock(x, y, z, new StarRock());
					}
				}
			}
		}
		// Add all necessary rift blocks
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int z = 0; z < Globals.world.sizeZ(); z++) {
				Globals.world.setBlock(x, maxY - 1, z, new Rift());
			}
		}
		addPortal(minY);
	}

	void addPortal(int y) {
		for (int x = 0; x < Globals.world.sizeX(); x++) {
			for (int z = 0; z < Globals.world.sizeZ(); z++) {
				if (Math.abs(x - Globals.world.sizeX() / 2) + Math.abs(z - Globals.world.sizeZ() / 2) > 50) {
					Globals.world.newBlock(x, y, z, new Rift());
				}
			}
		}

	}
}
