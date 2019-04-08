package Events;

import Blocks.Block;
import Control.BlockPos;
import Control.Globals;

public class LightEvent extends Event {

	BlockPos pos;

	public LightEvent(BlockPos pos) {
		this.pos = pos;
	}

	public void trigger() {
		Block b = Globals.world.getBlock(pos.x, pos.y, pos.z);
		short avg = b.light;
		int amt = 1;
		for (int[] dir : Globals.dirs) {
			Block b2 = Globals.world.getBlock(pos.x + dir[0], pos.y + dir[1], pos.z + dir[2]);
			if (Math.abs(b.light - b2.light) > 20) {
				avg += b2.light;
				amt++;
				EventManager.addEvent(new LightEvent(new BlockPos(pos.x + dir[0], pos.y + dir[1], pos.z + dir[2])), 1);
			}
		}
		avg /= amt;
		b.light = avg;
		for (int[] dir : Globals.dirs) {
			Block b2 = Globals.world.getBlock(pos.x + dir[0], pos.y + dir[1], pos.z + dir[2]);
			if (Math.abs(b.light - b2.light) > 20) {
				b2.light = avg;
			}
		}
	}

}
