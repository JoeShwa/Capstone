package recipes;

import items.Item;

public class EnergyUpgrade extends Recipe {

	public Item[] getReqs() {
		Item[] out = {new items.UpgradeCore(2), new items.Electrite(2)};
		return out;
	}

	public Item getResult() {
		return new items.EnergyUpgrade(1);
	}
	
}
