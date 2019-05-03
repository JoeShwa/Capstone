package control;

import blocks.*;

public class WorldGen extends Thread {

	World world;
	public int phase = 0;
	static final int CAVES = 25;
	static final int CAVE_LEN = 40;
	static final int CAVE_RAD = 7;

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
		for (int x = 0; x < world.sizeX(); x++) {
			for (int y = 0; y < world.sizeY(); y++) {
				for (int z = 0; z < world.sizeZ(); z++) {
					Block b = world.getBlock(x, y, z);
					b.placeEvent(x, y, z, b);
					b.isDrawn = false;
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
		for (int x = 0; x < world.sizeX(); x++) {
			for (int z = 0; z < world.sizeY(); z++) {
				int height = (int) -(Globals.p.noise((float) x / 20, (float) z / 20) * 20) + maxY - 1;
				int y;
				for (y = maxY - 1; y > height; y--) {
					if (Math.random() < 0.995) {
						world.newBlock(x, y, z, new Sludge());
					} else {
						world.newBlock(x, y, z, new Electrite());
					}
				}
				int tx = x;
				int tz = z;
				if (Math.random() < 0.01) {
					while (Math.random() < 0.8) {
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
						world.newBlock(tx, y, tz, new Crystal());
					}
				}
			}
		}
		addPortal(minY, maxY);
	}

	public void generateDim0(int minY, int maxY) {
		// Make everything rock
		for (int x = 0; x < world.sizeX(); x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = 0; z < world.sizeZ(); z++) {
					world.newBlock(x, y, z, new Rock());
				}
			}
		}
		// Add the caves
		for (int i = 0; i < CAVES; i++) {
			double cx = Math.random() * world.sizeX();
			double cy = Math.random() * (maxY - minY) + minY;
			double cz = Math.random() * world.sizeZ();
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
				cx = mod(cx, world.sizeX());
				cy = mod(cy - minY, maxY - minY) + minY;
				cz = mod(cz, world.sizeZ());
				for (int x = (int) cx - CAVE_RAD; x < (int) cx + CAVE_RAD + 1; x++) {
					for (int y = (int) cy - CAVE_RAD; y < (int) cy + CAVE_RAD + 1; y++) {
						for (int z = (int) cz - CAVE_RAD; z < (int) cz + CAVE_RAD + 1; z++) {
							if (y < maxY && y >= minY && (x - cx) * (x - cx) + (y - cy) * (y - cy)
									+ (z - cz) * (z - cz) < CAVE_RAD * CAVE_RAD) {
								world.newBlock(x, y, z, new Air());
							}
						}
					}
				}
			}
		}
		// Used to determine which blocks are on the outline of caves
		world.updateAllVisibilty();
		// Add the Star Rock and Thermite
		for (int x = 0; x < world.sizeX(); x++) {
			for (int y = 0; y < world.sizeY() - 1; y++) {
				for (int z = 0; z < world.sizeZ(); z++) {
					if (world.getBlock(x, y, z) instanceof Rock) {
						if (world.getBlock(x, y, z).isVisible) {
							if (Math.random() < 0.04) {
								world.newBlock(x, y, z, new StarRock());
							}
						} else {
							if (Math.random() < 0.02) {
								world.newBlock(x, y, z, new Thermite());
							}
						}
					}
				}
			}
		}
		// Add all necessary rift blocks
		for (int x = 0; x < world.sizeX(); x++) {
			for (int z = 0; z < world.sizeZ(); z++) {
				world.newBlock(x, maxY - 1, z, new Rift());
			}
		}
		addPortal(minY, maxY - 1);
	}

	void addPortal(int minY, int maxY) {
		for (int x = 0; x < world.sizeX(); x++) {
			for (int z = 0; z < world.sizeZ(); z++) {
				if (Math.abs(x - world.sizeX() / 2) + Math.abs(z - world.sizeZ() / 2) > 10) {
					world.newBlock(x, minY, z, new Rift());
				} else {
					for (int y = minY; y < maxY; y++) {
						if (Math.random() < 0.99) {
							world.newBlock(x, y, z, new Air());
						} else {
							world.newBlock(x, y, z, new Rift());
						}
						if (x - world.sizeX() / 2 == 0 && z - world.sizeZ() / 2 == 0) {
							world.newBlock(x, y, z, new Stream());
						}
					}
				}
			}
		}

	}
}
