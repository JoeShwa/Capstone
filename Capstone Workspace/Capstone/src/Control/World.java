package Control;

import Blocks.Block;

public class World {

	// Stores block data
	private Block[][][] blocks;
	
	public static final double GRAVITY = 0.01;
	public static final int DIM_COUNT = 2;

	public World(int x, int y, int z) {
		blocks = new Block[x][y][z];
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
		return blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())];
	}
	
	public Block getBlock(double x, double y, double z) {
		return getBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z));
	}

	public void setBlock(int x, int y, int z, Block b) {
		Block prev = getBlock(x, y, z);
		blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())] = b;
		b.placeEvent(x, y, z, prev);
	}
	
	public void setBlock(double x, double y, double z, Block b) {
		setBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z), b);
	}

	public void newBlock(int x, int y, int z, Block b) {
		blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())] = b;
	}

	public void breakBlock(int x, int y, int z) {
		getBlock(x, y, z).breakEvent(x, y, z);
	}
	
	public void breakBlock(double x, double y, double z) {
		breakBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z));
	}
}
