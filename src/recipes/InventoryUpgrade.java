package recipes;

import items.Item;

public class InventoryUpgrade extends Recipe {

	public Item[] getReqs() {
		Item[] out = { new items.UpgradeCore(1), new items.Rock(30) };
		return out;
	}

	public Item getResult() {
		return new items.InventoryUpgrade(1);
	}

}
