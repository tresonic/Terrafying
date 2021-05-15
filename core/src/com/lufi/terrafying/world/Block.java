package com.lufi.terrafying.world;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.lufi.terrafying.items.Item;

public class Block {
	
	private static HashMap<Integer, Block> blockMap;
	
	public static final int BLOCK_SIZE = 16;
	public static final String BLOCK_PATH = "blocks";
	
	private int id;
	private boolean collidable;
	private boolean drawable;
	private boolean mineable;
	private String name;
	

	
	private Block(int nId, boolean nCollidable, boolean nDrawable, boolean nMineable, String nName) {
		id = nId;
		collidable = nCollidable;
		drawable = nDrawable;
		mineable = nMineable;
		name = nName;
	}
	
	private Block(int nId, boolean nCollidable, String nName) {
		this(nId, nCollidable, true, true, nName);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean getCollidable() {
		return collidable;
	}
	
	public boolean getDrawable() {
		return drawable;
	}
	
	public boolean getMineable() {
		return mineable;
	}
	
	public String getName() {
		return name;
	}

	
	public static void registerBlocks() {
		blockMap = new HashMap<Integer, Block>();
		int c = 0;
		registerBlock(c++, true, false, false, "barrier");
		registerBlock(c++, false, false, false, "air");
		registerBlock(c++, true, true, true, "stone");
		registerBlock(c++, true, true, true, "dirt");
		registerBlock(c++, true, true, true, "grass");
	}
	
	public static void registerBlock(int id, boolean nCollidable, boolean nDrawable, boolean nMineable, String nName) {
		blockMap.put(id, new Block(id, nCollidable, nDrawable, nMineable, nName));
		Item.registerItem(id, nName, true);
	}
	
	public static int getNumRegisteredBlocks() {
		return blockMap.size();
	}
	
	public static void loadBlockTextures(AssetManager am) {
		for(Block b : blockMap.values()) {
			if(b.drawable) {
				am.load(BLOCK_PATH + "/" + b.name + ".png", Texture.class);
			}
		}
	}
	
	public static Block getBlockByName(String name) {
		for(Block b : blockMap.values()) {
			if(b.name == name)
				return b;
		}
		return null;
	}
	
	public static Block getBlockById(int id) {
		return blockMap.get(id);
	}
}
