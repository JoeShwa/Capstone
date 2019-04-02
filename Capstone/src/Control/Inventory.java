package Control;

import java.util.TreeMap;
import Items.Item;

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
	
	public boolean useItem(String name, int amt) {
		Item item = content.get(name);
		if(item.amount >= amt) {
			item.amount -= amt;
			if(item.amount < 1) {
				content.remove(name);
			}
			return true;
		}
		return false;
	}
	
	public boolean addItem(Item item) {
		if(size + item.amount > maxSize) {
			return false;
		}
		if(content.containsKey(item.getName())) {
			content.get(item.getName()).amount += item.amount;
		} else {
			content.put(item.getName(), item);
		}
		return true;
	}
}
