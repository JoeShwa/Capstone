package items;

import blocks.Block;
import control.Globals;
import processing.core.PImage;

public class InventoryUpgrade extends Tool {

	static PImage tex = Globals.p.loadImage("textures/inventoryupgrade.png");

	public InventoryUpgrade(int amt) {
		super(amt);
	}

	public InventoryUpgrade() {
		super(0);
	}

	public String getName() {
		return "Inventory Upgrade";
	}

	public String getLore() {
		return "Increases inventory space by 50.";
	}

	public Block getBlock() {
		return null;
	}

	public void rightClick() {
		Globals.player.inventory.useItem(getName(), 1);
		if (amount == 0) {
			Globals.player.selItem = null;
		}
		Globals.player.inventory.setMaxSize(Globals.player.inventory.getMaxSize() + 50);
	}

	public void draw(int x, int y) {
		draw(tex, x, y);
	}

}
