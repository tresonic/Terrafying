package com.lufi.terrafying.world;

import java.io.Serializable;
import java.util.HashMap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.util.Vector2i;

public class Map implements Serializable {

	private int width;
	private int height;
	private String name;
	private HashMap<Vector2i, Chunk> chunks;
	private HashMap<Vector2i, Inventory> metadata;
	
	public Vector2 spawnpoint = new Vector2(250 * Block.BLOCK_SIZE, 150 * Block.BLOCK_SIZE);
	
	public Map(String nName, int nWidth, int nHeight) {
		name = nName;
		width = nWidth * Chunk.CHUNK_SIZE;
		height = nHeight * Chunk.CHUNK_SIZE;
		chunks = new HashMap<Vector2i, Chunk>();
		metadata = new HashMap<Vector2i, Inventory>();
	}
	
	public void generate() {
		MapGenerator.generate(this, width, height);
	}
	
	public void render(OrthographicCamera cam, SpriteBatch sb) {
		Vector2 startpos = new Vector2(cam.position.x - cam.viewportWidth * cam.zoom / 2, cam.position.y - cam.viewportHeight * cam.zoom / 2);
		Vector2 endpos = new Vector2(cam.position.x + cam.viewportWidth * cam.zoom/ 2, cam.position.y + cam.viewportHeight * cam.zoom);
		
		int startx = MathUtils.clamp((int) (startpos.x / (float) Block.BLOCK_SIZE) - 1, 0, width);
		int starty = MathUtils.clamp((int) (startpos.y / (float) Block.BLOCK_SIZE) - 1, 0, height);
		int endx = MathUtils.clamp((int) (endpos.x / (float) Block.BLOCK_SIZE) + 2, 0, width);
		int endy = MathUtils.clamp((int) (endpos.y / (float) Block.BLOCK_SIZE) + 2, 0, height);
		
		//System.out.println(cam.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0)));
		//System.out.println(cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
		//System.out.println(startx + "  " + endx + ";  " + starty + "  " + endy);
		
		
		sb.setProjectionMatrix(cam.combined);

		sb.begin();
				
		for(int x=startx; x<endx; x++) {
			for(int y=starty; y<endy; y++) {
				Block block = Block.getBlockById(getBlock(x, y));
								
				if(block.getDrawable()) {
					Texture t = Terrafying.assetManager.get(Block.BLOCK_PATH + "/" + block.getName() + ".png", Texture.class);
					sb.draw(t , x*Block.BLOCK_SIZE, y*Block.BLOCK_SIZE);
				}
			}
		}

		sb.end();
	}
	
	public Inventory getMeta(int x, int y) {
		return metadata.get(new Vector2i(x, y));
	}
	
	public Inventory getMeta(Vector2i pos) {
		return metadata.get(pos);
	}
	
	public int getBlock(int x, int y) {
		if(x <= 0 || y <= 0)
			return Block.getBlockByName("barrier").getId();
		Vector2i chunkId = new Vector2i(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
		Chunk c = chunks.get(chunkId);
		if(c != null) {
			return c.getBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
		} else {
			return Block.getBlockByName("barrier").getId();
		}
	}
	
	public int getBlockAt(float x, float y) {
		return getBlock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE);
	}
	
	public void setBlockAt(float x, float y, int block) {
		setBlock((int) x / Block.BLOCK_SIZE, (int) y / Block.BLOCK_SIZE, block);
	}
	
	public void setBlock(int x, int y, int block) {
		Vector2i chunkId = new Vector2i(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
		Chunk c = chunks.get(chunkId);
		
		Vector2i pos = new Vector2i(x, y);
		if(Block.getBlockById(block).getHasMeta()) {
			if(metadata.get(pos) == null) {
				metadata.put(pos, new Inventory(27));
			} else {
				if(metadata.get(pos).isEmpty()) {
					metadata.remove(pos);
					metadata.put(pos,  new Inventory(27));
				} else {
					return;
				}
			}
		} else {
			if(metadata.get(pos) != null) {
				if(metadata.get(pos).isEmpty()) {
					metadata.remove(pos);
				} else {
					return;
				}
			}
		}
		
		if(c != null) {
			c.setBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE, block);
		} 
	}
	
	public Vector2i getChunkIdAt(float x, float y) {
		return new Vector2i((int) x / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE, (int) y / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE);
	}
	
	public Vector2i getChunkIdAt(Vector2 vec) {
		Vector2i v = new Vector2i(((int) vec.x) / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE, ((int) vec.y) / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE);
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
		return new Vector2i((int)x / Block.BLOCK_SIZE, (int)y / Block.BLOCK_SIZE);
	}

	public String getName() {
		return name;
	}
	
	public int getNumChunks() {
		return chunks.size();
	}
}
