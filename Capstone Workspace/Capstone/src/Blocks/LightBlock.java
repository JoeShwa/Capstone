package Blocks;

import Control.BlockPos;
import Control.Globals;
import Events.EventManager;
import Events.LightEvent;

public abstract class LightBlock extends Block {

	public short getBrightness() {
		return 0;
	}

	public void lightArea(int x, int y, int z, short brightness) {
		light = brightness;
		EventManager.addEvent(new LightEvent(new BlockPos(x, y, z)), 1);
	}

	public void breakLight(int x, int y, int z, int brightness) {
		light = (short) -brightness;
		EventManager.addEvent(new LightEvent(new BlockPos(x, y, z)), 1);
	}

	public void placeEvent(int x, int y, int z, Block prev) {
		super.placeEvent(x, y, z, prev);
		lightArea(x, y, z, getBrightness());
	}

	public void breakEvent(int x, int y, int z) {
		super.breakEvent(x, y, z);
		breakLight(x, y, z, getBrightness());
	}

	public boolean isLight() {
		return true;
	}

}
