package parts;

import control.Globals;

public abstract class Part implements Parts {

	public void draw(int x, int y, int scale) {
		if (getTexture() != null) {
			Globals.p.beginShape();
			Globals.p.noTint();
			Globals.p.texture(getTexture());
			Globals.p.vertex(x, y, 0, 0);
			Globals.p.vertex(x, y + 16 * scale, 0, 1);
			Globals.p.vertex(x + 16 * scale, y + 16 * scale, 1, 1);
			Globals.p.vertex(x + 16 * scale, y, 1, 0);
			Globals.p.endShape();
		}
	}

}
