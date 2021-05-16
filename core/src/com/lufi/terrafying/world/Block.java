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
	private float emission;
	private boolean collidable;
	private boolean drawable;
	private boolean mineable;
	private String name;
	

	
	private Block(int nId, float nEmission, boolean nCollidable, boolean nDrawable, boolean nMineable, String nName) {
		id = nId;
		emission = nEmission;
		collidable = nCollidable;
		drawable = nDrawable;
		mineable = nMineable;
		name = nName;
	}
	
	private Block(int nId, boolean nCollidable, String nName) {
		this(nId, 0, nCollidable, true, true, nName);
	}
	
	public int getId() {
		return id;
	}
	
	public float getEmission() {
		return emission;
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
		registerBlock(c++, 0, true, true, true, "noblock");
		registerBlock(c++, 0, true, false, false, "barrier");
		registerBlock(c++, 0, false, false, false, "air");
		registerBlock(c++, 0, true, true, true, "stone");
		registerBlock(c++, 0, true, true, true, "dirt");
		registerBlock(c++, 0, true, true, true, "grass");
	}
	
	public static void registerBlock(int id, float nEmission, boolean nCollidable, boolean nDrawable, boolean nMineable, String nName) {
		blockMap.put(id, new Block(id, nEmission, nCollidable, nDrawable, nMineable, nName));
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
		return getBlockByName("noblock");
	}
	
	public static Block getBlockById(int id) {
		Block b = blockMap.get(id);
		if(b != null)
			return b;
		return getBlockByName("noblock");
	}
}
