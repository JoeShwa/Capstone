package recipes;

import items.Item;

public class Portal extends Recipe {

	public Item[] getReqs() {
		// Item[] out = {new items.StarRock(30), new items.Electrite(5), new
		// items.Gravitium(50)};
		Item[] out = { new items.Gravitium(1) };
		return out;
	}

	public Item getResult() {
		return new items.Portal(1);
	}

}
