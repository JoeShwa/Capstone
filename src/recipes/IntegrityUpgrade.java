package recipes;

import items.Item;

public class IntegrityUpgrade extends Recipe {

	public Item[] getReqs() {
		Item[] out = { new items.UpgradeCore(3), new items.Sludge(10) };
		return out;
	}

	public Item getResult() {
		return new items.IntegrityUpgrade(1);
	}

}
