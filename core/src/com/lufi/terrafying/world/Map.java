package com.lufi.terrafying.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.util.SimplexNoise;
import com.lufi.terrafying.util.Vector2i;

public class Map {

	private int width;
	private int height;
	private HashMap<Vector2i, Chunk> chunks;
	
	public Vector2 spawnpoint = new Vector2(250 * Block.BLOCK_SIZE, 150 * Block.BLOCK_SIZE);
	
	public Map(int nWidth, int nHeight) {
		width = nWidth * Chunk.CHUNK_SIZE;
		height = nHeight * Chunk.CHUNK_SIZE;
		chunks = new HashMap<Vector2i, Chunk>();		
	}
	
	public void generate() {
		MapGenerator.generate(this, width, height);
	}
	
	public void render(OrthographicCamera cam, SpriteBatch sb) {
		Vector2 startpos = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
		Vector2 endpos = new Vector2(cam.position.x + cam.viewportWidth / 2, cam.position.y + cam.viewportHeight);
		
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
}
