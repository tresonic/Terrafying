package com.lufi.terrafying.items;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.world.Block;

public class Item {
	 private int id;
	 private String name;
	 private boolean blockItem;
	 
	 
	 
	 private static String ITEM_PATH = "items";
	 private static HashMap<Integer, Item> itemMap = new HashMap<Integer, Item>();
	 
	 public Item() {}
	 
	 private Item(int nId, String nName, boolean nBlockItem) {
		 id = nId;
		 name = nName;
		 blockItem = nBlockItem;
	 }
	 
	 public int getId() {
		 return id;
	 }
	 
	 public String getName() {
		 return name;
	 }
	 
	 public boolean getBlockItem() {
		 return blockItem;
	 }
	 
	public static void registerItems() {
		int start = getNumRegisteredItems();
		registerItem(++start, "testitem", false);
	}
	
	public static void registerItem(int id, String nName, boolean nBlockItem) {
		itemMap.put(id, new Item(id, nName, nBlockItem));
	}
	
	public static int getNumRegisteredItems() {
		return itemMap.size();
	}
	
	public static void loadItemTextures(AssetManager am) {
		for(Item i : itemMap.values()) {
			if(!i.getBlockItem()) {
				am.load(ITEM_PATH + "/" + i.name + ".png", Texture.class);
			}
		}
	}
	
	public static Texture getItemTexture(int id) {
		Item i = itemMap.get(id);
		if(i.getBlockItem())
			return Terrafying.assetManager.get(Block.BLOCK_PATH + "/" + i.name + ".png", Texture.class);
		else
			return Terrafying.assetManager.get(ITEM_PATH + "/" + i.name + ".png", Texture.class);
	}
	
	public static Item getItemByName(String name) {
		for(Item i : itemMap.values()) {
			if(i.name == name)
				return i;
		}
		return null;
	}
	
	public static Item getItemById(int id) {
		return itemMap.get(id);
	}
}
