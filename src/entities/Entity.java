package entities;

import control.Globals;
import control.World;

public abstract class Entity implements Entities {

	public double x;
	public double y;
	public double z;
	int chunkX;
	int chunkY;
	int chunkZ;
	double xv = 0;
	double yv = 0;
	double zv = 0;
	public boolean isDead = false;
	public boolean newChunk = false;

	protected void kill() {
		isDead = true;
	}

	// Translates properly with world wrapping
	protected void doTranslation() {
		// Determine closest possible wrapping translation to the player
		float tX = (float) x;
		float tY = (float) y;
		float tZ = (float) z;
		for (int i = -Globals.world.sizeX(); i <= Globals.world.sizeX(); i += Globals.world.sizeX()) {
			if (Math.abs(x + i - Globals.player.getX()) < Math.abs(tX - Globals.player.getX())) {
				tX = (float) (x + i);
			}
		}
		for (int i = -Globals.world.sizeY(); i <= Globals.world.sizeY(); i += Globals.world.sizeY()) {
			if (Math.abs(y + i - Globals.player.getY()) < Math.abs(tY - Globals.player.getY())) {
				tY = (float) (y + i);
			}
		}
		for (int i = -Globals.world.sizeZ(); i <= Globals.world.sizeZ(); i += Globals.world.sizeZ()) {
			if (Math.abs(z + i - Globals.player.getZ()) < Math.abs(tZ - Globals.player.getZ())) {
				tZ = (float) (z + i);
			}
		}
		// Do the translation
		Globals.p.translate(tX * 100, tY * 100, tZ * 100);
	}

	protected void wrapPos() {
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
	}

	public void update() {
		double speed = xv * xv + yv * yv + zv * zv;
		if (speed > 1) {
			speed = Math.sqrt(speed);
			xv /= speed;
			yv /= speed;
			zv /= speed;
		}
		x += xv;
		y += yv;
		z += zv;
		wrapPos();
		int ncx = Globals.floor(x) / World.SUBDIV;
		int ncy = Globals.floor(y) / World.SUBDIV;
		int ncz = Globals.floor(z) / World.SUBDIV;
		if (chunkX != ncx || chunkY != ncy || chunkZ != ncz) {
			chunkX = ncx;
			chunkY = ncy;
			chunkZ = ncz;
			newChunk = true;
		}
	}

	// Makes entity not be stuck in wall
	public int[] fixPos() {
		int bestC = 4;
		int[] b = new int[3];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					x -= xv * i;
					y -= yv * j;
					z -= zv * k;
					if (!check() && i + j + k < bestC) {
						bestC = i + j + k;
						b[0] = i;
						b[1] = j;
						b[2] = k;

					}
					x += xv * i;
					y += yv * j;
					z += zv * k;
				}
			}
		}
		if (bestC == 4) {
			b[0] = 1;
			b[1] = 1;
			b[2] = 1;
		}
		x -= xv * b[0];
		y -= yv * b[1];
		z -= zv * b[2];
		return b;
	}

}
