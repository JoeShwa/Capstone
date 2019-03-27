package Control;

import Blocks.Block;

public class World {

	// Stores block data
	private Block[][][] blocks;
	static final int CAVES = 20;
	static final int CAVE_LEN = 30;
	static final int CAVE_RAD = 5;

	public World(int x, int y, int z) {
		blocks = new Block[x][y][z];
	}

	private int mod(int n1, int n2) {
		if (n1 < 0) {
			n1 += n2;
		}
		return n1 % n2;
	}

	public void updateAllFaces() {
		for (int x = 0; x < sizeX(); x++) {
			for (int y = 0; y < sizeY(); y++) {
				for (int z = 0; z < sizeZ(); z++) {
					getBlock(x, y, z).updateFaces(x, y, z);
				}
			}
		}
	}

	public int sizeX() {
		return blocks.length;
	}

	public int sizeY() {
		return blocks[0].length;
	}

	public int sizeZ() {
		return blocks[0][0].length;
	}

	public Block getBlock(int x, int y, int z) {
		return blocks[mod(x, sizeX())][mod(y, sizeY())][mod(z, sizeZ())];
	}

	public void setBlock(int x, int y, int z, Block b) {
		short light = getBlock(x, y, z).light;
		blocks[mod(x, sizeX())][mod(y, sizeY())][mod(z, sizeZ())] = b;
		b.placeEvent(x, y, z, light);
	}

	public void newBlock(int x, int y, int z, Block b) {
		blocks[mod(x, sizeX())][mod(y, sizeY())][mod(z, sizeZ())] = b;
	}

	public void breakBlock(int x, int y, int z) {
		getBlock(x, y, z).breakEvent(x, y, z);
	}
}
