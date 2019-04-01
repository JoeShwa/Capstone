package Control;

import java.util.TreeMap;
import Items.Item;

public class Inventory {
	int size;
	int maxSize;
	
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
