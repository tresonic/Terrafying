package com.lufi.terrafying.world;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.lufi.terrafying.items.Item;

public class Block {

	private static HashMap<Integer, Block> blockMap;

	public static final int BLOCK_SIZE = 16;
	public static final String BLOCK_PATH = "blocks";

	public enum MineType {
		NONE, STONELIKE, DIRTLIKE, WOODLIKE,
	}

	private int id;
	private float mineTime;
	private MineType mineType;
	private float emission;
	private boolean collidable;
	private boolean drawable;
	private boolean mineable;
	private boolean hasMeta;
	private String name;

	private Block(int nId, float nMineTime, MineType nMineType, float nEmission, boolean nCollidable, boolean nDrawable,
			boolean nMineable, boolean nHasMeta, String nName) {
		id = nId;
		mineTime = nMineTime;
		mineType = nMineType;
		emission = nEmission;
		collidable = nCollidable;
		drawable = nDrawable;
		mineable = nMineable;
		hasMeta = nHasMeta;
		name = nName;
	}

	private Block(int nId, float nMineTime, MineType nMineType, boolean nCollidable, String nName) {
		this(nId, nMineTime, nMineType, 0, nCollidable, true, true, false, nName);
	}

	public int getId() {
		return id;
	}

	public float getMineTime() {
		return mineTime;
	}

	public MineType getMineType() {
		return mineType;
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

	public boolean getHasMeta() {
		return hasMeta;
	}

	public String getName() {
		return name;
	}

	public static void registerBlocks() {
		blockMap = new HashMap<Integer, Block>();
		int c = 0;
		registerBlock(c++, 0.0f, MineType.NONE, 0, true, true, true, false, "noblock");
		registerBlock(c++, 0.0f, MineType.NONE, 0, true, false, false, false, "barrier");
		registerBlock(c++, 0.0f, MineType.NONE, 0, false, false, false, false, "air");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "stone");
		registerBlock(c++, 1.0f, MineType.DIRTLIKE, 0, true, true, true, false, "dirt");
		registerBlock(c++, 1.0f, MineType.DIRTLIKE, 0, true, true, true, false, "grass");
		registerBlock(c++, 1.0f, MineType.WOODLIKE, 0, false, true, true, true, "chest");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "coal");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "iron");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "gold");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "ruby");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "uranium");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "copper");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "diamond");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "cobble");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "bricks");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "blueclay");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "yellowclay");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "greenclay");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "orangeclay");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "pinkclay");
		registerBlock(c++, 1.0f, MineType.STONELIKE, 0, true, true, true, false, "redclay");
		registerBlock(c++, 1.0f, MineType.WOODLIKE, 0, false, true, true, false, "oakwood");
		registerBlock(c++, 1.0f, MineType.NONE, 0, false, true, true, false, "oakwoodleaves");
		registerBlock(c++, 1.0f, MineType.WOODLIKE, 0, true, true, true, false, "oakwoodplanks");
		registerBlock(c++, 1.0f, MineType.WOODLIKE, 0, true, true, true, false, "craftingtable");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "whitewool");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "bluewool");
		// registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "yellowwool");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "greenwool");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "violetwool");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "redwool");
		registerBlock(c++, 1.0f, MineType.NONE, 0, true, true, true, false, "window");
		registerBlock(c++, 1.0f, MineType.DIRTLIKE, 0, true, true, true, false, "sand");
		registerBlock(c++, 1.0f, MineType.DIRTLIKE, 0, true, true, true, false, "gravel");
	}

	public static void registerBlock(int id, float nMineTime, MineType nMineType, float nEmission, boolean nCollidable,
			boolean nDrawable, boolean nMineable, boolean nHasMeta, String nName) {
		blockMap.put(id,
				new Block(id, nMineTime, nMineType, nEmission, nCollidable, nDrawable, nMineable, nHasMeta, nName));
		Item.registerItem(id, MineType.NONE, 1.0f, nName, true);
	}

	public static int getNumRegisteredBlocks() {
		return blockMap.size();
	}

	public static void loadBlockTextures(AssetManager am) {
		for (Block b : blockMap.values()) {
			if (b.drawable) {
				am.load(BLOCK_PATH + "/" + b.name + ".png", Texture.class);
			}
		}
	}

	public static Block getBlockByName(String name) {
		for (Block b : blockMap.values()) {
			if (b.name == name)
				return b;
		}
		return getBlockByName("noblock");
	}

	public static Block getBlockById(int id) {
		Block b = blockMap.get(id);
		if (b != null)
			return b;
		return getBlockByName("noblock");
	}
}
