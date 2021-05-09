package com.lufi.terrafying.world;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public enum Block {
	
	AIR(1, false, false, "air"),
	STONE(2, true, "stone"),
	DIRT(3, true, "dirt"),
	GRASS(4, true, "grass");
	
	public static final int BLOCK_SIZE = 16;
	
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
	
	private static HashMap<Integer, Block> blockMap;
	
	static {
		blockMap = new HashMap<Integer, Block>();
		for(Block b : Block.values()) {
			blockMap.put(b.getId(), b);
		}
	}
	
	public static void loadBlockTextures(AssetManager am) {
		for(Block b : Block.values()) {
			if(b.drawable) {
				am.load("blocks/" + b.name + ".png", Texture.class);
			}
		}
	}
	
	public static Block getBlockById(int id) {
		return blockMap.get(id);
	}
}
