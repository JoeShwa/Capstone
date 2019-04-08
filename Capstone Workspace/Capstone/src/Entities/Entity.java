package Entities;

import Control.Globals;

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

	protected void wrapPos() {
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
	}

}
