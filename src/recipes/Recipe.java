package recipes;

import control.Inventory;
import items.Item;

public abstract class Recipe implements Recipes {

	public String getName() {
		return getResult().getName();
	}

	public Item craft(Inventory inv) {
		Item[] recipe = getReqs();
		if (canCraft(inv)) {
			for (Item item : recipe) {
				inv.useItem(item.getName(), item.amount);
			}
		}
		return getResult();
	}

	public boolean canCraft(Inventory inv) {
		Item[] recipe = getReqs();
		for (Item item : recipe) {
			Item i2 = inv.getItem(item.getName());
			if (i2 == null || i2.amount < item.amount) {
				return false;
			}
		}
		return true;
	}

	public boolean canResearch(Inventory inv) {
		Item[] recipe = getReqs();
		for (Item item : recipe) {
			Item i2 = inv.getItem(item.getName());
			if (i2 == null) {
				return false;
			}
		}
		return true;
	}
}
