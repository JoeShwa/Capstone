package entities;

import processing.core.PImage;

public class RockParticle extends Particle {

	public PImage getTexture() {
		return blocks.Rock.tex;
	}

	public RockParticle(double x, double y, double z, double xv, double yv, double zv) {
		super(x, y, z, xv, yv, zv);
	}

	public void hit() {

	}

	public void hit(Entity e) {
		e.kill();
		kill();
	}

	public void update() {
		super.update();
		int[] b = fixPos();
		xv -= xv * 2 * b[0];
		yv -= yv * 2 * b[1];
		zv -= zv * 2 * b[2];
		if (b[0] == 1 || b[1] == 1 || b[2] == 1) {
			xv *= 0.75;
			yv *= 0.75;
			zv *= 0.75;
		}
	}

}
