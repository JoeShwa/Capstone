package recipes;

import items.Item;

public class Accelerator extends Recipe {

	public Item[] getReqs() {
		Item[] out = { new items.Rock(10), new items.Electrite(1), new items.Sludge(3) };
		return out;
	}

	public Item getResult() {
		return new items.Accelerator(1);
	}

}
