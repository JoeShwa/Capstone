package control;

import entities.Bug;

public class Spawner {

	public static void spawn() {
		for(int i = 0; i < 100; i++) {
			Bug bug = new Bug(Math.random() * Globals.world.sizeX(), 100, Math.random() * Globals.world.sizeZ());
			while(bug.check()) {
				bug.y++;
			}
			if(bug.y > 100) {
				Globals.world.addEntity(bug);
			}
		}
	}
}
