package blocks;

import java.util.Random;

import control.Globals;
import items.Item;

public class Stream extends Block {

	static Random rand = new Random();

	// Amount of frames per block each bit spends
	static final int FPB = 5;

	public boolean isTrans() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public int getHardness() {
		return 0;
	}

	public void draw(int x, int y, int z) {
		if (Globals.world.isVisible(x, y, z)) {
			bitDown(x, y, z, 1);
			bitUp(x, y, z, 1);
		}
	}

	private void bitDown(int x, int y, int z, int amt) {
		rand.setSeed((p.frameCount / -FPB + y) * 31415926);
		p.fill(255);
		for (int i = 0; i < amt; i++) {
			p.pushMatrix();
			p.translate(x * 100 + 10 + rand.nextFloat() * 80,
					y * 100 + (float) (p.frameCount % FPB + rand.nextFloat() * FPB) / FPB * 100,
					z * 100 + 10 + rand.nextFloat() * 80);
			p.box(10);
			p.popMatrix();
		}
	}

	private void bitUp(int x, int y, int z, int amt) {
		rand.setSeed((p.frameCount / FPB + y) * 31415926 + 1237821);
		p.fill(255);
		for (int i = 0; i < amt; i++) {
			p.pushMatrix();
			p.translate(x * 100 + 10 + rand.nextFloat() * 80,
					y * 100 + 100 - (float) (p.frameCount % FPB + rand.nextFloat() * FPB) / FPB * 100,
					z * 100 + 10 + rand.nextFloat() * 80);
			p.box(10);
			p.popMatrix();
		}
	}

	public Item getItem() {
		return null;
	}

}
