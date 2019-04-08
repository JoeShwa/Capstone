
package Entities;

import Control.Globals;

public class Particle extends Entity {
	
	public void draw() {
		Globals.p.pushMatrix();
		Globals.p.translate((float) x, (float) y, (float) z);
	}
	
	public void update() {
		
	}

}
