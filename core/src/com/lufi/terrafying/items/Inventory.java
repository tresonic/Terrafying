package com.lufi.terrafying.items;

import java.io.Serializable;

public class Inventory implements Serializable {
	private ItemStack itemStacks[];
	
	public Inventory() {}
	
	public Inventory(int size) {
		itemStacks = new ItemStack[size];
		for(int i=0; i<size; i++)
			itemStacks[i] = new ItemStack();
	}
	
	public ItemStack addItem(ItemStack item) {
		for(int i = 0; i<itemStacks.length; i++) {
			if(item.getName() == itemStacks[i].getName() && itemStacks[i].count < ItemStack.STACK_MAX)
				return itemStacks[i].addItem(item);
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
		if(itemStacks[idx].getName() == itemStack.getName() || itemStacks[idx].count == 0) {
			return itemStacks[idx].addItem(itemStack);
		} else {
			return itemStack;
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
		if(itemStacks[idx].getName() == itemStack.getName() && !itemStacks[idx].isFull()) {
			return addItem(idx, itemStack);
		}
		ItemStack result = itemStacks[idx];
		itemStacks[idx] = itemStack;
		return result;
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
}
