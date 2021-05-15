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
	private String name;
	

	
	private Block(int nId, boolean nCollidable, boolean nDrawable, String nName) {
		id = nId;
		collidable = nCollidable;
		drawable = nDrawable;
		name = nName;
	}
	
	private Block(int nId, boolean nCollidable, String nName) {
		this(nId, nCollidable, true, nName);
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
	
	public String getName() {
		return name;
	}
	
	public static void registerBlocks() {
		blockMap = new HashMap<Integer, Block>();
		registerBlock(1, false, false, "air");
		registerBlock(2, true, true, "stone");
		registerBlock(3, true, true, "dirt");
		registerBlock(4, true, true, "grass");
	}
	
	public static void registerBlock(int id, boolean nCollidable, boolean nDrawable, String nName) {
		blockMap.put(id, new Block(id, nCollidable, nDrawable, nName));
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
