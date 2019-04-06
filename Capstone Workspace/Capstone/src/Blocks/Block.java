package Blocks;

import Control.World;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public abstract class Block implements Blocks {

	static PApplet p;
	static PShape sh;
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };
	boolean[] faces = new boolean[6];
	public boolean visible;
	static World world;
	public short light;
	static final int AMBIENCE = 75;
	static final int BIGGEST_LIGHT = 4;

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

	public void placeEvent(int x, int y, int z, short light) {
		this.light = (short) Math.max(AMBIENCE, light);
		updateFaces(x, y, z);
		for (int i = 0; i < dirs.length; i++) {
			int nx = x + dirs[i][0];
			int ny = y + dirs[i][1];
			int nz = z + dirs[i][2];
			world.getBlock(nx, ny, nz).updateFaces(nx, ny, nz);
		}
	}

	public void breakEvent(int x, int y, int z) {
		world.setBlock(x, y, z, new Air());
		for (int i = 0; i < dirs.length; i++) {
			int nx = x + dirs[i][0];
			int ny = y + dirs[i][1];
			int nz = z + dirs[i][2];
			world.getBlock(nx, ny, nz).updateFaces(nx, ny, nz);
		}
	}

	public boolean isBreakable() {
		return isSolid() || !isTrans();
	}

	public void updateFaces(int x, int y, int z) {
		visible = false;
		for (int i = 0; i < 6; i++) {
			if (world.getBlock(x + dirs[i][0], y + dirs[i][1], z + dirs[i][2]).isTrans()) {
				faces[i] = true;
				visible = true;
			}
		}
	}

	void draw(PImage tex, int x, int y, int z) {
		if (visible) {
			x *= 100;
			y *= 100;
			z *= 100;
			p.pushMatrix();
			p.translate(x + 50, y + 50, z + 50);
			// p.shape(sh);
			for (int i = 0; i < 6; i++) {
				if (faces[i]) {
					p.beginShape(PConstants.QUADS);
					p.texture(tex);
					p.textureMode(PConstants.NORMAL);
					p.tint(light);
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
