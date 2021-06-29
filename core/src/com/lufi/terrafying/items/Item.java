package com.lufi.terrafying.items;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.Block.MineType;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private boolean blockItem;
	private int damage;

	private MineType mineType;
	private float mineFactor;

	private static String ITEM_PATH = "items";
	private static HashMap<Integer, Item> itemMap = new HashMap<Integer, Item>();

	public Item() {
	}

	private Item(int nId, int nDamage, MineType nMineType, float nMineFactor, String nName, boolean nBlockItem) {
		id = nId;
		mineType = nMineType;
		mineFactor = nMineFactor;
		name = nName;
		blockItem = nBlockItem;
		damage = nDamage;
	}

	public int getId() {
		return id;
	}

	public int getDamage() {
		return damage;
	}
	
	public String getName() {
		return name;
	}

	public boolean getBlockItem() {
		return blockItem;
	}

	public MineType getMineType() {
		return mineType;
	}

	public float getMineFactor() {
		return mineFactor;
	}

	public static void registerItems() {
		int start = getNumRegisteredItems();
		registerItem(++start, 1, MineType.NONE, 1.0f, "stonesword", false);
		registerItem(++start, 0, MineType.STONELIKE, 0.75f, "stonepickaxe", false);
		registerItem(++start, 0, MineType.DIRTLIKE, 0.75f, "stoneshovel", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "stonehoe", false);
		registerItem(++start, 0, MineType.WOODLIKE, 0.75f, "stoneaxe", false);
		registerItem(++start, 2, MineType.NONE, 1.0f, "ironsword", false);
		registerItem(++start, 0, MineType.STONELIKE, 0.5f, "ironpickaxe", false);
		registerItem(++start, 0, MineType.DIRTLIKE, 0.5f, "ironshovel", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "ironhoe", false);
		registerItem(++start, 0, MineType.WOODLIKE, 0.5f, "ironaxe", false);
		registerItem(++start, 3, MineType.NONE, 1.0f, "diamondsword", false);
		registerItem(++start, 0, MineType.STONELIKE, 0.3f, "diamondpickaxe", false);
		registerItem(++start, 0, MineType.DIRTLIKE, 0.3f, "diamondshovel", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "diamondhoe", false);
		registerItem(++start, 0, MineType.WOODLIKE, 0.3f, "diamondaxe", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "bow", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "brush", false);
		registerItem(++start, 0, MineType.NONE, 1.0f, "jetpack", false);
		registerItem(++start, 10, MineType.NONE, 0f, "admintool", false);
		
		
	}

	public static void registerItem(int id, int nDamage, MineType nMineType, float nMineFactor, String nName, boolean nBlockItem) {
		itemMap.put(id, new Item(id, nDamage, nMineType, nMineFactor, nName, nBlockItem));
	}

	public static int getNumRegisteredItems() {
		return itemMap.size();
	}

	public static void loadItemTextures(AssetManager am) {
		for (Item i : itemMap.values()) {
			if (!i.getBlockItem()) {
				am.load(ITEM_PATH + "/" + i.name + ".png", Texture.class);
			}
		}
	}

	public static Texture getItemTexture(int id) {
		Item i = itemMap.get(id);
		if (i.getBlockItem())
			return Terrafying.assetManager.get(Block.BLOCK_PATH + "/" + i.name + ".png", Texture.class);
		else
			return Terrafying.assetManager.get(ITEM_PATH + "/" + i.name + ".png", Texture.class);
	}

	public static Item getItemByName(String name) {
		for (Item i : itemMap.values()) {
			if (i.name == name)
				return i;
		}
		return getItemByName("noblock");
	}

	public static Item getItemById(int id) {
		Item i = itemMap.get(id);
		if (i != null)
			return i;
		return getItemByName("noblock");
	}
}
