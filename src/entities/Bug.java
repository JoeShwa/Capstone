package entities;

import control.Globals;
import control.Player;
import control.World;

public class Bug extends Enemy {

	boolean isTracking = false;

	public Bug(double x, double y, double z) {
		super(x, y, z);
	}

	public void draw() {
		Globals.p.pushMatrix();
		doTranslation();
		short light = Globals.world.getBlock(x, y, z).light;
		Globals.p.fill(255 * light / 255, 150 * light / 255, 50 * light / 255);
		Globals.p.sphereDetail(10);
		Globals.p.sphere(40);
		Globals.p.noFill();
		Globals.p.stroke(255 * light / 255, 0, 0);
		Globals.p.strokeWeight(1);
		Globals.p.sphere(50);
		Globals.p.popMatrix();
	}

	public void triggerNear(double relX, double relY, double relZ) {
		double len = relX * relX + relY * relY + relZ * relZ;
		if (len < 10 * 10) {
			isTracking = true;
			len = Math.sqrt(len) / 0.008;
			relX /= len;
			relY /= len;
			relZ /= len;
			xv += relX;
			yv += relY;
			zv += relZ;
			if (checkPlayer(Globals.player)) {
				xv -= relX * 100;
				yv -= relY * 100;
				zv -= relZ * 100;
				Globals.player.hurt(64);
				Globals.player.xv += relX * 50;
				Globals.player.yv += relY * 50;
				Globals.player.zv += relZ * 50;
			}
		}
	}

	public void update() {
		if (!isTracking) {
			xv *= 0.9;
			zv *= 0.9;
		}
		yv += World.GRAVITY;
		super.update();
		int[] b = fixPos();
		xv -= xv * b[0];
		yv -= yv * b[1];
		zv -= zv * b[2];
		if (b[1] == 1) {
			yv -= 0.2;
		}
		isTracking = false;
	}

	public boolean onPoint(double x, double y, double z) {
		double[] rels = Globals.relPos(this.x, this.y, this.z, x, y, z);
		// return Math.abs(rels[0]) < 0.5 && Math.abs(rels[1]) < 0.5 &&
		// Math.abs(rels[2]) < 0.5;
		return rels[0] * rels[0] + rels[1] * rels[1] + rels[2] * rels[2] < 0.5 * 0.5;
	}

	public boolean checkPlayer(Player p) {
		return (x - p.getX()) * (x - p.getX()) + (y - p.getY()) * (y - p.getY()) + (z - p.getZ()) * (z - p.getZ()) < 0.9
				* 0.9;
	}

	public boolean check() {
		for (double xc = -0.5; xc <= 0.5; xc++) {
			for (double yc = -0.5; yc <= 0.5; yc++) {
				for (double zc = -0.5; zc <= 0.5; zc++) {
					if (Globals.world.getBlock(x + xc, y + yc, z + zc).isSolid()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
