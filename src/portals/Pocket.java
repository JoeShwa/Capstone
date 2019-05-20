package portals;

import blocks.Block;
import control.Globals;

public class Pocket {

	private Block[][][] blocks;

	public Pocket(int cx, int cy, int cz, int cx2, int cy2, int cz2) {
		blocks = new Block[10][5][10];
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[0].length; y++) {
				for (int z = 0; z < blocks[0][0].length; z++) {
					blocks[x][y][z] = new blocks.Rock();
					blocks[x][y][z].placeEvent(cx + x, y + cy, z + cz, new blocks.Air());
					PortalManager.addPos(new PocketPos(this, x, y, z), cx + x, cy + y, cz + z);
					PortalManager.addPos(new PocketPos(this, x, y, z), cx2 + x, cy2 + y, cz2 + z);
					
				}
			}
		}
		
	}

	public Block getBlock(int x, int y, int z) {
		return blocks[x][y][z];
	}

	public void setBlock(int x, int y, int z, Block b) {
		Block prev = Globals.world.getBlock(x, y, z);
		blocks[x][y][z] = b;
		b.placeEvent(x, y, z, prev);
	}

	public void breakBlock(int x, int y, int z) {
		blocks[x][y][z].breakEvent(x, y, z);
	}

}
