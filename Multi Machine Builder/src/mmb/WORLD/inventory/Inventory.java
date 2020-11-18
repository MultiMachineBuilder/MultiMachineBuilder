/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class Inventory {
	static Debugger debug = new Debugger("Inventories");
	public double capacity = 1;
	public List<Item> items = new ArrayList<Item>();
	public boolean insert(Item itm) {
		boolean test = getLeftOverCapacity() >= itm.getVolume();
		if(test) items.add(itm);
		return test;
	}
	public List<Item> getContents(){
		return items;
	}
	public double getUsedVolume() {
		double volume = 0;
		for(int i = 0; i < items.size(); i++) {
			volume += items.get(i).getVolume();
		}
		return volume;
	}
	public double getLeftOverCapacity() {
		return capacity - getUsedVolume();
	}
	public Item extract(Item itm) {
		return extract((Item itm2) -> (itm2.getType() == itm.getType()));
	}
	public Item extract(Predicate<Item> selector) {
		for(int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(selector.test(item)) {
				items.remove(i);
				return item;
			}
		}
		return null;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Capacity: ");
		sb.append(capacity);
		sb.append("\nItems: ");
		sb.append(items.size());
		double inUse = getUsedVolume();
		sb.append("\nUsed voulme: ");
		sb.append(inUse);
		sb.append("\nFree volume: ");
		sb.append(capacity - inUse);
		items.forEach((item) -> {
			sb.append('\n');
			sb.append(item.getName());
		});
		return sb.toString();
	}
}
