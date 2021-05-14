package com.lufi.terrafying.items;

public class Inventory {
	private ItemStack itemStacks[];
	
	public Inventory() {}
	
	public Inventory(int size) {
		itemStacks = new ItemStack[size];
	}
	
	public ItemStack addItem(ItemStack item) {
		for(int i = 0; i<itemStacks.length; i++) {
			if(item.getName() == itemStacks[i].getName())
				return itemStacks[i].addItem(item);
		}
		for(int i=0; i<itemStacks.length; i++) {
			if(itemStacks[i] == null) {
				itemStacks[i] = item;
				return null;
			}
		}
		return item;
	}
	
	public ItemStack takeItem(int idx, int takeCount) {
		if(idx >= 0 && idx >= itemStacks.length)
			return null;
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
	
	public int getSize() {
		return itemStacks.length;
	}

}
