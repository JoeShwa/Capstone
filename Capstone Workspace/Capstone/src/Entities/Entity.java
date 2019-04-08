package Entities;

import Control.Globals;

public abstract class Entity implements Entities {

	double x;
	double y;
	double z;

	private void wrapPos() {
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
	}

}
