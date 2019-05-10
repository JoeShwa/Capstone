package events;

import blocks.Air;
import blocks.Electrite;
import blocks.Thermite;
import control.Globals;

public class ThermiteElectriteEvent extends Event {
	
	int x, y, z;
	Electrite elect;

	public ThermiteElectriteEvent(int x, int y, int z, Electrite elect) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.elect = elect;
	}
	
	public void trigger() {
		if(Globals.world.getBlock(x, y, z) instanceof Thermite) {
			elect.energy += 1024;
			if(elect.energy > 1024) {
				elect.energy = 1024;
			}
			Globals.world.setBlock(x, y, z, new Air());
		}
		if(Globals.player.energy < Globals.player.maxEnergy) {
			int dif = Globals.player.maxEnergy - Globals.player.energy;
			if(elect.energy >= dif) {
				elect.energy -= dif;
				Globals.player.energy += dif;
			} else {
				Globals.player.energy += elect.energy;
				elect.energy = 0;
			}
		}
	}

}
