package entities;

import control.Globals;

public abstract class Entity implements Entities {

	public double x;
	public double y;
	public double z;
	double xv = 0;
	double yv = 0;
	double zv = 0;
	public boolean isDead = false;
	
	protected void kill() {
		isDead = true;
	}
	
	// Translates properly with world wrapping
	protected void doTranslation() {
		// Determine closest possible wrapping translation to the player
		float tX = (float) x;
		float tY = (float) y;
		float tZ = (float) z;
		for(int i = -Globals.world.sizeX(); i <= Globals.world.sizeX(); i+= Globals.world.sizeX()) {
			if(Math.abs(x + i - Globals.player.getX()) < Math.abs(tX - Globals.player.getX())) {
				tX = (float) (x + i);
			}
		}
		for(int i = -Globals.world.sizeY(); i <= Globals.world.sizeY(); i+= Globals.world.sizeY()) {
			if(Math.abs(y + i - Globals.player.getY()) < Math.abs(tY - Globals.player.getY())) {
				tY = (float) (y + i);
			}
		}
		for(int i = -Globals.world.sizeZ(); i <= Globals.world.sizeZ(); i+= Globals.world.sizeZ()) {
			if(Math.abs(z + i - Globals.player.getZ()) < Math.abs(tZ - Globals.player.getZ())) {
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

}
