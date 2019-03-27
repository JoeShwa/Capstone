package Control;

import java.util.TreeSet;

import Items.Item;

public class Inventory {
	int size;
	int maxSize;
	TreeSet<Item> content;
	
	public Inventory(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public Item[] getItems() {
		return (Item[]) content.toArray();
	}
	
	public boolean addItem(Item item) {
		if(size + item.amount > maxSize) {
			return false;
		}
		if(content.contains(item)) {
			
		}
		return true;
	}
}
