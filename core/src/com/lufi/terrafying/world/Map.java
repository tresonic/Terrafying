package com.lufi.terrafying.world;

import java.io.Serializable;
import java.util.HashMap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.util.Vector2i;

public class Map implements Serializable {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private String name;
	public Vector2 spawnpoint;

	private HashMap<Vector2i, Chunk> chunks;
	private HashMap<Vector2i, Inventory> metadata;
	private HashMap<Vector2i, Boolean> metaLock;

	public Map(String nName, int nWidth, int nHeight) {
		name = nName;
		width = nWidth * Chunk.CHUNK_SIZE;
		height = nHeight * Chunk.CHUNK_SIZE;
		chunks = new HashMap<Vector2i, Chunk>();
		metadata = new HashMap<Vector2i, Inventory>();
		metaLock = new HashMap<Vector2i, Boolean>();
		spawnpoint = new Vector2();
	}

	public void generate() {
		MapGenerator.generate(this, width, height);
	}

	public void render(OrthographicCamera cam, SpriteBatch sb) {
		Vector2 startpos = new Vector2(cam.position.x - cam.viewportWidth * cam.zoom / 2,
				cam.position.y - cam.viewportHeight * cam.zoom / 2);
		Vector2 endpos = new Vector2(cam.position.x + cam.viewportWidth * cam.zoom / 2,
				cam.position.y + cam.viewportHeight * cam.zoom);

		int startx = MathUtils.clamp((int) (startpos.x / (float) Block.BLOCK_SIZE) - 1, 0, width);
		int starty = MathUtils.clamp((int) (startpos.y / (float) Block.BLOCK_SIZE) - 1, 0, height);
		int endx = MathUtils.clamp((int) (endpos.x / (float) Block.BLOCK_SIZE) + 2, 0, width);
		int endy = MathUtils.clamp((int) (endpos.y / (float) Block.BLOCK_SIZE) + 2, 0, height);

		// System.out.println(cam.unproject(new Vector3(Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight(), 0)));
		// System.out.println(cam.unproject(new Vector3(Gdx.input.getX(),
		// Gdx.input.getY(), 0)));
		// System.out.println(startx + " " + endx + "; " + starty + " " + endy);

		sb.setProjectionMatrix(cam.combined);

		sb.begin();

		for (int x = startx; x < endx; x++) {
			for (int y = starty; y < endy; y++) {
				Block block = Block.getBlockById(getBlock(x, y));

				if (block.getDrawable()) {
					Texture t = Terrafying.assetManager.get(Block.BLOCK_PATH + "/" + block.getName() + ".png",
							Texture.class);
					sb.draw(t, x * Block.BLOCK_SIZE, y * Block.BLOCK_SIZE);
				}
			}
		}

		sb.end();
	}

	public void updateSpawnPoint() {
		int x = (int) (spawnpoint.x / Block.BLOCK_SIZE);
		System.out.println(width);
		for (int i = height - 2; i > 1; i--) {
			if (Block.getBlockById(getBlock(x, i - 1)).getCollidable()
					&& !Block.getBlockById(getBlock(x, i - 1)).getName().equals("barrier")) {
				System.out.println(i);
				spawnpoint.y = i * Block.BLOCK_SIZE;
				return;
			}

		}

	}

	public static boolean entityInBlock(Entity e, int bx, int by) {
		float x = bx * Block.BLOCK_SIZE, y = by * Block.BLOCK_SIZE;
		float endx = x + Block.BLOCK_SIZE, endy = y + Block.BLOCK_SIZE;
		
		if (((e.posx > x && e.posx < endx) || (e.posx + e.WIDTH > x && e.posx + e.WIDTH < endx))
			&& ((e.posy >= y && e.posy < endy) || (e.posy + e.HEIGHT > y && e.posy + e.HEIGHT < endy)))
				return true;

		return false;
	}
	
	public boolean isBlockInRangeAt(int blockId, float posx, float posy, int blockRange) {
		int bx = (int) (posx / Block.BLOCK_SIZE);
		int by = (int) (posy / Block.BLOCK_SIZE);
		
		for(int x = bx-blockRange; x<bx+blockRange; x++) {
			for(int y = by-blockRange; y<by+blockRange; y++) {
				if(getBlock(x, y) == blockId)
					return true;
			}
		}
		
		return false;
	}

	public HashMap<Vector2i, Inventory> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<Vector2i, Inventory> nMetadata) {
		metadata = nMetadata;
	}

	public Inventory getMeta(int x, int y) {
		return metadata.get(new Vector2i(x, y));
	}

	public Inventory getMeta(Vector2i pos) {
		return metadata.get(pos);
	}

	public Inventory getMetaAt(float x, float y) {
		return getMeta((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE);
	}

	public Inventory getMetaAt(Vector2 pos) {
		return getMeta((int) pos.x / Block.BLOCK_SIZE, (int) pos.y / Block.BLOCK_SIZE);
	}

	public Inventory setMeta(int x, int y, Inventory inv) {
		return metadata.put(new Vector2i(x, y), inv);
	}

	public Inventory setMeta(Vector2i pos, Inventory inv) {
		return metadata.put(pos, inv);
	}

	public Inventory setMetaAt(float x, float y, Inventory inv) {
		return setMeta((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE, inv);
	}

	public Inventory setMetaAt(Vector2 pos, Inventory inv) {
		return setMeta((int) pos.x / Block.BLOCK_SIZE, (int) pos.y / Block.BLOCK_SIZE, inv);
	}

	public HashMap<Vector2i, Boolean> getMetaLockData() {
		return metaLock;
	}

	public void setMetaLockData(HashMap<Vector2i, Boolean> nMetaLockData) {
		metaLock = nMetaLockData;
	}

	public boolean getMetaLock(Vector2i pos) {
		return metaLock.get(pos);
	}

	public boolean getMetaLock(int x, int y) {
		return getMetaLock(new Vector2i(x, y));
	}

	public boolean getMetaLockAt(Vector2 pos) {
		return getMetaLock((int) pos.x / Block.BLOCK_SIZE, (int) pos.y / Block.BLOCK_SIZE);
	}

	public boolean getMetaLockAt(float x, float y) {
		return getMetaLock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE);
	}

	public void setMetaLock(Vector2i pos, boolean lock) {
		metaLock.put(pos, lock);
	}

	public void setMetaLock(int x, int y, boolean lock) {
		metaLock.put(new Vector2i(x, y), lock);
	}

	public void setMetaLockAt(Vector2 pos, boolean lock) {
		setMetaLock((int) pos.x / Block.BLOCK_SIZE, (int) pos.y / Block.BLOCK_SIZE, lock);
	}

	public void setMetaLockAt(float x, float y, boolean lock) {
		setMetaLock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE, lock);
	}

	public int getBlock(int x, int y) {
		if (x <= 0 || y <= 0)
			return Block.getBlockByName("barrier").getId();
		Vector2i chunkId = new Vector2i(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
		Chunk c = chunks.get(chunkId);
		if (c != null) {
			return c.getBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
		} else {
			return Block.getBlockByName("barrier").getId();
		}
	}

	public int getBlockAt(float x, float y) {
		return getBlock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE);
	}

	public boolean setBlockAt(float x, float y, int block) {
		return setBlock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE, block);
	}

	public boolean setBlock(int x, int y, int block) {
		Vector2i chunkId = new Vector2i(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
		Chunk c = chunks.get(chunkId);

		Vector2i pos = new Vector2i(x, y);
		if (Block.getBlockById(block).getHasMeta()) {
			metaLock.put(pos, false);
			if (metadata.get(pos) == null) {
				metadata.put(pos, new Inventory(27));
			} else {
				if (metadata.get(pos).isEmpty()) {
					metadata.remove(pos);
					metadata.put(pos, new Inventory(27));
				} else {
					return false;
				}
			}
		} else {
			if (metadata.get(pos) != null) {
				if (metadata.get(pos).isEmpty()) {
					metadata.remove(pos);
					metaLock.remove(pos);
				} else {
					return false;
				}
			}
		}

		if (c != null) {
			c.setBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE, block);
			return true;
		}
		return false;
	}

	public Vector2i getChunkIdAt(float x, float y) {
		return new Vector2i((int) x / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE,
				(int) y / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE);
	}

	public Vector2i getChunkIdAt(Vector2 vec) {
		Vector2i v = new Vector2i(((int) vec.x) / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE,
				((int) vec.y) / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE);
		return v;
	}

	public Chunk getChunkAt(float x, float y) {
		return chunks.get(getChunkIdAt(x, y));
	}

	public Chunk getChunkAt(Vector2 vec) {
		return chunks.get(getChunkIdAt(vec));
	}

	public Chunk getChunkAtChunkId(Vector2i id) {
		return chunks.get(id);
	}

	public void addChunk(Vector2i chunkId, Chunk chunk) {
		chunks.put(chunkId, chunk);
	}

	public void removeChunk(Vector2i chunkId) {
		chunks.remove(chunkId);
	}

	public static Vector2i getBlockPos(float x, float y) {
		return new Vector2i((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE);
	}

	public String getName() {
		return name;
	}

	public int getNumChunks() {
		return chunks.size();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int nHeight) {
		height = nHeight;
	}

	public void setWidth(int nWidth) {
		width = nWidth;
	}
}
