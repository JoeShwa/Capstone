package blocks;

import control.BlockPos;
import control.Globals;
import control.World;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public abstract class Block implements Blocks {

	static PApplet p;
	public static PShape sh;
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };
	// Slight lighting differences on different sides of blocks makes them easier to
	// see
	static short[] lightMap = { -5, -5, 0, 0, 0, -10 };
	static World world;
	public short light;
	static final int AMBIENCE = 75;
	public static final int BIGGEST_LIGHT = 8;

	public boolean isLight() {
		return false;
	}

	public static void prepBlocks(PApplet p_, World world_) {
		p = p_;
		world = world_;
		sh = p.createShape(PConstants.BOX, 100);
		// I don't know why this needs to be here, but it breaks without it
		sh.getVertexCount();
	}

	public void placeEvent(int x, int y, int z, Block prev) {
		this.light = (short) Math.max(AMBIENCE, prev.light);
		update(x, y, z);
		for (int i = 0; i < dirs.length; i++) {
			int nx = x + dirs[i][0];
			int ny = y + dirs[i][1];
			int nz = z + dirs[i][2];
			world.getBlock(nx, ny, nz).update(nx, ny, nz);
		}
	}

	public void breakEvent(int x, int y, int z) {
		world.setBlock(x, y, z, new Air());
		for (int i = 0; i < dirs.length; i++) {
			int nx = x + dirs[i][0];
			int ny = y + dirs[i][1];
			int nz = z + dirs[i][2];
			world.getBlock(nx, ny, nz).update(nx, ny, nz);
		}
	}

	public boolean isBreakable() {
		return isSolid() || !isTrans();
	}

	// Update triggered when adjacent blocks are placed or destroyed
	public void update(int x, int y, int z) {
		updateVisibility(x, y, z);
	}

	// Updates whether or not the block is visible
	private void updateVisibility(int x, int y, int z) {
		Globals.world.setVisible(false, x, y, z);
		for (int i = 0; i < 6; i++) {
			if (world.getBlock(x + dirs[i][0], y + dirs[i][1], z + dirs[i][2]).isTrans()) {
				Globals.world.setVisible(true, x, y, z);
			}
		}
		if (Globals.world.isVisible(x, y, z) && !Globals.world.isDrawn(x, y, z) && Globals.main.gen.phase > World.DIM_COUNT) {
			Globals.main.addRenderBlock(new BlockPos(x, y, z));
		}
	}

	void draw(PImage tex, int x, int y, int z) {
		if (Globals.world.isVisible(x, y, z)) {
			int px = (int) Math.signum(Globals.player.getX() - x - 0.5);
			int py = (int) Math.signum(Globals.player.getY() - y - 0.5);
			int pz = (int) Math.signum(Globals.player.getZ() - z - 0.5);
			x *= 100;
			y *= 100;
			z *= 100;
			p.pushMatrix();
			p.translate(x + 50, y + 50, z + 50);
			for (int i = 0; i < 6; i++) {
				if ((dirs[i][0] == 0 || dirs[i][0] == px) && (dirs[i][1] == 0 || dirs[i][1] == py)
						&& (dirs[i][2] == 0 || dirs[i][2] == pz) && Globals.world
								.getBlock(x / 100 + dirs[i][0], y / 100 + dirs[i][1], z / 100 + dirs[i][2]).isTrans()) {
					p.beginShape(PConstants.QUADS);
					p.texture(tex);
					p.textureMode(PConstants.NORMAL);
					p.tint(light + lightMap[i]);
					for (int j = 0; j < 4; j++) {
						PVector vec = sh.getVertex(i * 4 + j);
						float mapX;
						float mapY;
						mapX = vec.x * Math.max(Math.abs(dirs[i][2]), Math.abs(dirs[i][1]))
								+ vec.z * Math.abs(dirs[i][0]);
						mapY = vec.y * Math.max(Math.abs(dirs[i][2]), Math.abs(dirs[i][0]))
								+ vec.z * Math.abs(dirs[i][1]);
						mapX = mapX / 100 + 0.5f;
						mapY = mapY / 100 + 0.5f;
						p.vertex(vec.x, vec.y, vec.z, mapX, mapY);
					}
					p.endShape(PConstants.CLOSE);
				}
			}
			p.popMatrix();
		}
	}

}
