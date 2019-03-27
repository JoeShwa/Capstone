package Control;

import processing.core.PImage;

public class StarRock extends LightBlock {

	static PImage tex = p.loadImage("starrock.png");

	public int getBrightness() {
		return 4;
	}

	public void draw(int x, int y, int z) {
		draw(tex, x, y, z);
	}

	public void placeEvent(int x, int y, int z, short light) {
		super.placeEvent(x, y, z, light);
		lightArea(x, y, z, getBrightness());
	}

	public void breakEvent(int x, int y, int z) {
		super.breakEvent(x, y, z);
		breakLight(x, y, z, getBrightness());
	}

	public boolean isTrans() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

}
