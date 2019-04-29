package entities;

import control.Globals;
import control.World;

public class Bug extends Enemy {

	public Bug(double x, double y, double z) {
		super(x, y, z);
	}

	public void draw() {
		Globals.p.pushMatrix();
		doTranslation();
		Globals.p.fill(255, 150, 50);
		Globals.p.box(100);
		Globals.p.popMatrix();
	}
	
	public void update() {
		int[] b = fixPos();
		xv -= xv * b[0];
		yv -= yv * b[1];
		zv -= zv * b[2];
		double relX = Globals.player.getX() - x;
		double relY = Globals.player.getY() - y;
		double relZ = Globals.player.getZ() - z;
		double len = relX * relX + relY * relY + relZ * relZ;
		if(len < 100) {
			len = Math.sqrt(len) / 0.1;
			relX /= len;
			relY /= len;
			relZ /= len;
			xv += relX;
			yv += relY;
			zv += relZ;
		}
		yv += World.GRAVITY;
		super.update();
	}

	public boolean check() {
		return false;
	}

}
