package portals;

import blocks.Block;
import control.Globals;

public class Pocket {
	
	public int cx, cy, cz;
	Block[][][] blocks;
	
	public Pocket(int cx, int cy, int cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		blocks = new Block[10][5][10];
		for(int x = 0; x < blocks.length; x++) {
			for(int y = 0; y < blocks[0].length; y++) {
				for(int z = 0; z < blocks[0][0].length; z++) {
					blocks[x][y][z] = new blocks.Rock();
				}
			}
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
		return blocks[x - cx][y - cy][z - cz];
	}
	
	public void setBlock(int x, int y, int z, Block b) {
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
		Block prev = Globals.world.getBlock(x, y, z);
		blocks[x - cx][y - cy][z - cz] = b;
		b.placeEvent(x, y, z, prev);
	}
	
	public void breakBlock(int x, int y, int z) {
		blocks[x - cx][y - cy][z - cz].breakEvent(x, y, z);
	}

}
