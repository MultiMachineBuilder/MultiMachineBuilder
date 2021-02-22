/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;


import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class HashInventory implements Inventory{

	private SelfSet<ItemEntry, ItemStack> contents = new HashSelfSet<>();
	
			
			
	@Override
	@Nullable public ItemStack withdraw(ItemEntry slot) {
		return withdraw(slot, 1);
	}

	@Override
	@Nullable public ItemStack withdraw(ItemEntry slot, int amount) {
		ItemStack src = contents.get(slot);
		if(src == null) {
			return null;
		}
		int result;
		if(src.amount <= amount) {
			//The slot has too few items
			result = src.amount;
			contents.remove(src);
		}else {
			result = amount;
			src.amount -= amount;
		}
		volume -= slot.volume(result);
		return new ItemStack(slot, result);
	}

	@Override
	public int put(ItemEntry item, int amount) {
		int takenAmount = amount;
		
		double remainVolume = capacity-volume;
		if(remainVolume <= 0) return 0;
		
		int remainAmount = (int) (remainVolume / item.volume());
		if(remainAmount <= 0) return 0;
		if(remainAmount < takenAmount) takenAmount = remainAmount;
				
		double takenVolume = item.volume(remainAmount);
		ItemStack stack = contents.get(item);
		if(stack == null) {
			//Create a new stack
		}else {
			//Use existing stack
		}
	}

	

	@Override
	public ItemToken retrieve(ItemEntry item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(ItemEntry item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack withdrawByType(ItemType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack withdrawByType(ItemType type, int amount) {
		for(ItemEntry entry)
	}

	

	@Override
	public Set<ItemStack> contents() {
		return Collections.unmodifiableSet(contents.values());
	}
	//Run out of items
	//Run out of queue space
	//Run out of amount
	@Override
	public void withdraw(int amount, Queue<ItemStack> q, Predicate<ItemEntry> p) {
		Iterator<ItemStack> stacks = contents.iterator();
		int remain = amount;
		while(stacks.hasNext()) {
			ItemStack next = stacks.next();
			if(!p.test(next.item)) continue; //rejected, continue
			int consume = next.amount;
			if(next.amount <= remain) 
				consume = next.amount; //Run out of items to withdraw
			if(consume > remain) 
				consume = remain; //Run out of amount
			ItemStack prototype = new ItemStack(next.item, consume);
			if(q.offer(prototype)) {
				//accepted
				next.amount -= consume;
				remain -= consume;
			}else{
				//run out of queue space
				return;
			}
			if(next.amount == 0) 
				//entry: run out of items
				stacks.remove();
			if(remain == 0) 
				//run out of requested amount
				return;
		}
		//run out of items
	}

	@Override
	public boolean isEmpty() {
		return contents.isEmpty();
	}
	
	//Capacity
	private double capacity = 2;
	@Override
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	@Override
	public double capacity() {
		return capacity;
	}
	private double volume = 0;
	@Override
	public double volume() {
		return volume;
	}

	@Override
	public void put(Queue<ItemStack> q) {
		while(!q.isEmpty()) {
			ItemStack next = q.element();
			
			int amtToInsert = next.amount;
			
			double remainVolume = capacity-volume;
			if(remainVolume <= 0) return;
			
			int remainAmount = (int) (remainVolume / next.item.volume());
			if(remainAmount <= 0) return;
			if(remainAmount < amtToInsert) amtToInsert = remainAmount;
			
			double takenVolume = amtToInsert.volume(remainAmount);
			
		}
	}

	@Override
	public void putdel(Queue<ItemStack> q) {
		// TODO Auto-generated method stub
		
	}
}
