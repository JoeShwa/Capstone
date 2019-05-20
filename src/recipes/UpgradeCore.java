package recipes;

import items.Item;

public class UpgradeCore extends Recipe {

	public Item[] getReqs() {
		Item[] out = { new items.StarRock(5), new items.Crystal(5) };
		return out;
	}

	public Item getResult() {
		return new items.UpgradeCore(1);
	}

}
