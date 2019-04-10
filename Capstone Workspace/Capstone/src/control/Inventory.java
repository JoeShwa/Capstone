package control;

import java.util.TreeMap;

import items.Item;

public class Inventory {
	int size;
	int maxSize;

	static final int REND_X = 8;
	static final int REND_Y = 4;

	TreeMap<String, Item> content;

	public Inventory(int maxSize) {
		this.maxSize = maxSize;
		content = new TreeMap<>();
	}

	public Item[] getItems() {
		Item[] out = new Item[content.size()];
		content.values().toArray(out);
		return out;
	}

	public int getSize() {
		return size;
	}

	public int getMaxSize() {
		return maxSize;
	}

	// Returns how full the inventory is compared to its max capacity, on a scale of
	// 0 to 1
	public double getFullness() {
		return (double) size / maxSize;
	}

	public boolean hasItem(String name) {
		return content.containsKey(name);
	}

	public boolean useItem(String name, int amt) {
		Item item = content.get(name);
		if (item.amount >= amt) {
			item.amount -= amt;
			size -= amt;
			if (item.amount < 1) {
				content.remove(name);
			}
			return true;
		}
		return false;
	}

	public boolean addItem(Item item) {
		if (size + item.amount > maxSize) {
			return false;
		}
		if (content.containsKey(item.getName())) {
			content.get(item.getName()).amount += item.amount;
		} else {
			content.put(item.getName(), item);
		}
		size += item.amount;
		return true;
	}
}
