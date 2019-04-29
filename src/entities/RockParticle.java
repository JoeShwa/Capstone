package entities;

import processing.core.PImage;

public class RockParticle extends Particle {
	
	public PImage getTexture() {
		return blocks.Rock.tex;
	}

	public RockParticle(double x, double y, double z, double xv, double yv, double zv) {
		super(x, y, z, xv, yv, zv);
		// TODO Auto-generated constructor stub
	}

}
