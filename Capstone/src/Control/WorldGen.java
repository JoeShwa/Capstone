package Control;

import Blocks.Air;
import Blocks.Rock;
import Blocks.StarRock;

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
	
	public WorldGen(World world) {
		this.world = world;
	}
	
	public void run() {
		generateDim0(world);
	}
	
	public void generateDim0(World w) {
		// Make everything rock
		for (int x = 0; x < w.sizeX(); x++) {
			for (int y = 0; y < w.sizeY(); y++) {
				for (int z = 0; z < w.sizeZ(); z++) {
					w.newBlock(x, y, z, new Rock());
				}
			}
		}
		phase = 1;
		// Add the caves
		for (int i = 0; i < CAVES; i++) {
			double cx = Math.random() * w.sizeX();
			double cy = Math.random() * w.sizeY();
			double cz = Math.random() * w.sizeZ();
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
				cx = mod(cx, w.sizeX());
				cy = mod(cy, w.sizeY());
				cz = mod(cz, w.sizeZ());
				for (int x = (int) cx - CAVE_RAD; x < (int) cx + CAVE_RAD + 1; x++) {
					for (int y = (int) cy - CAVE_RAD; y < (int) cy + CAVE_RAD + 1; y++) {
						for (int z = (int) cz - CAVE_RAD; z < (int) cz + CAVE_RAD + 1; z++) {
							if ((x - cx) * (x - cx) + (y - cy) * (y - cy) + (z - cz) * (z - cz) < CAVE_RAD * CAVE_RAD) {
								w.newBlock(x, y, z, new Air());
							}
						}
					}
				}
			}
		}
		phase = 2;
		// Used to determine which blocks are on the outline of caves
		w.updateAllFaces();
		// Add the lights
		for (int x = 0; x < w.sizeX(); x++) {
			for (int y = 0; y < w.sizeY(); y++) {
				for (int z = 0; z < w.sizeZ(); z++) {
					if (w.getBlock(x, y, z) instanceof Rock && w.getBlock(x, y, z).visible && Math.random() < 0.04) {
						w.newBlock(x, y, z, new StarRock());
					}
				}
			}
		}
		phase = 3;
		// Run placeEvents on all blocks, now that none are null
		for (int x = 0; x < w.sizeX(); x++) {
			for (int y = 0; y < w.sizeY(); y++) {
				for (int z = 0; z < w.sizeZ(); z++) {
					w.getBlock(x, y, z).placeEvent(x, y, z, w.getBlock(x, y, z).light);
				}
			}
		}
		phase = 4;
	}
}
