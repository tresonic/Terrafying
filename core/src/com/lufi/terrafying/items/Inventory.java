package com.lufi.terrafying.items;

import java.io.Serializable;

public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;
	private ItemStack itemStacks[];
	
	public Inventory() {}
	
	public Inventory(int size) {
		itemStacks = new ItemStack[size];
		for(int i=0; i<size; i++)
			itemStacks[i] = new ItemStack();
	}
	
	public ItemStack addItem(ItemStack item) {
		for(int i = 0; i<itemStacks.length; i++) {
			if(item.getName().equals(itemStacks[i].getName()) && itemStacks[i].count < ItemStack.STACK_MAX) {
				return itemStacks[i].addItem(item);
			}
		}
		for(int i=0; i<itemStacks.length; i++) {
			if(itemStacks[i].count == 0) {
				itemStacks[i] = item;
				return null;
			}
		}
		return item;
	}
	
	public ItemStack addItem(int idx, ItemStack itemStack) {
		if(itemStacks[idx].getName().equals(itemStack.getName()) || itemStacks[idx].count == 0) {
			return itemStacks[idx].addItem(itemStack);
		} else {
			return itemStack;
		}
	}
	
	public void removeItem(ItemStack nitem) {
		ItemStack item = nitem.clone();
		for(int i=0; i<itemStacks.length; i++) {
			if(itemStacks[i].getId() == item.getId()) {
				if(itemStacks[i].count >= item.count) {
					itemStacks[i].count -= item.count;
					return;
				}
				else {
					
					item.count -= itemStacks[i].count;
					itemStacks[i].count = 0;
				}
			}
		}
	}
	
	public ItemStack takeItem(int idx, int takeCount) {
		if(idx >= 0 && idx >= itemStacks.length)
			return new ItemStack();
		ItemStack taken = itemStacks[idx].takeItem(takeCount);
		return taken;
	}
	
	public ItemStack getItemStack(int idx) {
		if(idx >= 0 && idx < itemStacks.length)
			return itemStacks[idx];
		return null;
	}
	
	public void setItemStack(int idx, ItemStack itemStack) {
		if(idx >= 0 && idx < itemStacks.length)
			itemStacks[idx] = itemStack;
	}
	
	public ItemStack changeItemStack(int idx, ItemStack itemStack) {
		if(itemStacks[idx].getName().equals(itemStack.getName()) && !itemStacks[idx].isFull()) {
			return addItem(idx, itemStack);
		}
		ItemStack result = itemStacks[idx];
		itemStacks[idx] = itemStack;
		return result;
	}
	
	public int getItemCount(Item item) {
		int count = 0;
		for(int i=0; i<itemStacks.length; i++) {
			if(item.getId() == itemStacks[i].getId()) {
				count += itemStacks[i].count;
			}
		}
		return count;
	}
	
	public int getSize() {
		return itemStacks.length;
	}

	public boolean isEmpty() {
		for(int i=0; i<itemStacks.length; i++) {
			if(!itemStacks[i].isEmpty())
				return false;
		}
		return true;
	}
	
	public void clear() {
		for(int i=0; i<itemStacks.length; i++) {
			itemStacks[i] = new ItemStack();
		}
	}
	
	
	@Override
	public String toString() {
		String ret = new String();
		ret += "[Inv: ";
		for(int i=0; i<itemStacks.length; i++) {
			ret += itemStacks[i].toString();
		}
		ret += "]";
		return ret;
	}
}
