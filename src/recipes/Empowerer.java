package recipes;

import items.Item;

public class Empowerer extends Recipe{

	public Item[] getReqs() {
		Item[] out = {new items.Electrite(5), new items.Crystal(15), new items.Sludge(5)};
		return out;
	}

	public Item getResult() {
		return new items.Empowerer(1);
	}

}
