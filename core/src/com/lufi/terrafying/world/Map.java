package com.lufi.terrafying.world;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.lufi.terrafying.Terrafying;

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
		for(int x=0; x<width/Chunk.CHUNK_SIZE; x++) {
			for(int y=0; y<height/Chunk.CHUNK_SIZE; y++) {
				addChunk(new Vector2i(x, y), new Chunk());
			}
		}

		System.out.println("num chunks generated: " + chunks.size());
		
		SimplexNoise n1 = new SimplexNoise(300, 0.45f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n2 = new SimplexNoise(300, 0.65f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n3 = new SimplexNoise(35, 0.4f, ThreadLocalRandom.current().nextInt());
		
		int stoneLayerHeight = height / 3;
		int dirtLayerHeight = height / 100;
		
		// basic map gen with stone + dirt on top
		for(int x=0; x<width; x++) {
			int stoneHeight = (int) ((n1.getNoise(x, 0) + 1) * stoneLayerHeight);
			int dirtHeight = (int) ((n2.getNoise(x, 0) + 1) * dirtLayerHeight);
			
			//System.out.println("s: " +  stoneHeight + ", d: " + dirtHeight);
			
			for(int y=0; y<stoneHeight; y++) {
				setBlock(x, y, Block.STONE.getId());
			}
			
			for(int y=stoneHeight; y<stoneHeight + dirtHeight; y++) {
				setBlock(x, y, Block.DIRT.getId());
			}
			
			for(int y = stoneHeight + dirtHeight; y<height; y++) {
				setBlock(x, y, Block.AIR.getId());
			}
			
			setBlock(x, stoneHeight+dirtHeight, Block.GRASS.getId());
		}
		
		// carve caves into stone
		for(int x=1; x<width; x++) {
			double xMul = (-1/(2*x + 0.0000001) + 1)
						* (-1/(2*(width-x) + 0.0000001) + 1);
			
			for(int y=1; y<height; y++) {
				if(((n3.getNoise(x, y) + 1) / 2) 
						* (1 / ((double)height/((double)height*2000) * y + 1)) // higher -> less caves
						* (- 1/(2 * y+0.0000001) + 1) // very bottom -> no caves
						* xMul // closer to left or right edge -> less caves
						> 0.5f)
					setBlock(x, y, Block.AIR.getId());
			}
		}
		
		// find spawnpoint
		int spawnX = ThreadLocalRandom.current().nextInt(width / 10, width - width/10);
		int spawnY = height;
		for(int i=height; i>0; i--) {
			if(getBlock(spawnX, spawnY - 1) != Block.AIR.getId())
				break;
			spawnY--;
		}
		spawnpoint.set(spawnX * Block.BLOCK_SIZE, spawnY * Block.BLOCK_SIZE);
	}
	
	public void render(OrthographicCamera cam, SpriteBatch sb) {
		Vector2 startpos = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
		Vector2 endpos = new Vector2(cam.position.x + cam.viewportWidth / 2, cam.position.y + cam.viewportHeight);
		
		int startx = MathUtils.clamp((int) (startpos.x / (float) Block.BLOCK_SIZE), 0, width);
		int starty = MathUtils.clamp((int) (startpos.y / (float) Block.BLOCK_SIZE), 0, height);
		int endx = MathUtils.clamp((int) (endpos.x / (float) Block.BLOCK_SIZE) + 1, 0, width);
		int endy = MathUtils.clamp((int) (endpos.y / (float) Block.BLOCK_SIZE) + 1, 0, height);
		
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
	
	private int getBlock(int x, int y) {
		Vector2i chunkId = new Vector2i(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
		Chunk c = chunks.get(chunkId);
		if(c != null) {
			return c.getBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
		} else {
			return Block.AIR.getId();
		}
	}
	
	private void setBlock(int x, int y, int block) {
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
}
