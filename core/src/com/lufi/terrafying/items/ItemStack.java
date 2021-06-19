package com.lufi.terrafying.items;

import java.io.Serializable;

public class ItemStack implements Serializable, Cloneable {
	public static final int STACK_MAX = 99;
	public Item item;
	public int count;
	
	public ItemStack() {
		count = 0;
	}
	
	public ItemStack(Item nItem, int nCount) {
		item = nItem;
		count = nCount;
	}
	
	/**
	 * add an @link ItemStack to the current one
	 * @param newItem the @link ItemStack to add
	 * @return the @link ItemStack with reduced count, or null if everything fit
	 */
	public ItemStack addItem(ItemStack newItem) {
		// new stack empty
		if(newItem.isEmpty()) {}
		// current stack empty
		else if(isEmpty()) {
			item = newItem.item;
			count = newItem.count;
			newItem.count = 0;
		}
		// item doesn't match
		else if(!item.getName().equals(newItem.item.getName())) {}
		// new stack fits fully
		else if(newItem.count <= getFreeSpace()) {
			count += newItem.count;
			newItem.count = 0;
		}
		// new stack doesn't fit fully, add everything that fits and return the rest
		else {
			int freeSpace = getFreeSpace();
			count += freeSpace;
			newItem.count -= freeSpace;
		}
		if(newItem.count > 0)
			return newItem;
		else 
			return new ItemStack();
		
	}
	
	/**
	 * take items from an @link ItemStack
	 * @param takeCount number of items to take
	 * @return new @link ItemStack with the items taken
	 */
	public ItemStack takeItem(int takeCount) {
		if(takeCount == 0 || count == 0)
			return new ItemStack();
		ItemStack result = new ItemStack(item, count);
		if(takeCount >= count) {
			count = 0;
		}
		else {
			count -= takeCount;
			result.count = takeCount;
		}
		return result;
	}
	
	public boolean isFull() {
		return getFreeSpace() == 0;
	}
	
	public int getFreeSpace() {
		if(count >= STACK_MAX)
			return 0;
		return STACK_MAX - count;
	}
	
	public Item peekItem() {
		return item;
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	public String getName() {
		if(item != null)
			return item.getName();
		return new String();
	}
	
	public int getId() {
		if(item != null)
			return item.getId();
		return 0;
	}
	
	@Override
	public String toString() {
		String ret = new String();
		ret += "[";
		if(item != null)
			ret += item.getName();
		else
			ret += "NULL";
		ret += ":";
		ret += count;
		ret += "]";
		return ret;
	}
	
    public ItemStack clone()
    {
        return new ItemStack(item, count);
    }
}
