package control;

import entities.Bug;

public class Spawner {

	public static void spawn() {
		for(int i = 0; i < 25; i++) {
			Bug bug = new Bug(Math.random() * Globals.world.sizeX(), 200, Math.random() * Globals.world.sizeZ());
			while(bug.check()) {
				bug.y--;
			}
			if(bug.y < 200) {
				Globals.world.addEntity(bug);
			}
		}
	}
}
