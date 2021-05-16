package com.lufi.terrafying.world;

import java.util.concurrent.ThreadLocalRandom;

import com.lufi.terrafying.util.SimplexNoise;
import com.lufi.terrafying.util.Vector2i;

public class MapGenerator {
	public static void generate(Map map, int width, int height) {
		for(int x=0; x<width/Chunk.CHUNK_SIZE; x++) {
			for(int y=0; y<height/Chunk.CHUNK_SIZE; y++) {
				map.addChunk(new Vector2i(x, y), new Chunk());
			}
		}
		
		SimplexNoise n1 = new SimplexNoise(300, 0.45f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n2 = new SimplexNoise(300, 0.65f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n3 = new SimplexNoise(35, 0.4f, ThreadLocalRandom.current().nextInt());
		
		int stoneLayerHeight = height / 3;
		int dirtLayerHeight = height / 100;
		float smoothingFactor = 0.5f;
		
		// basic map gen with stone + dirt on top
		for(int x=0; x<width; x++) {
			int stoneHeight = (int) ((n1.getNoise(x, 0) * smoothingFactor + 1) * stoneLayerHeight);
			int dirtHeight = (int) ((n2.getNoise(x, 0) + 1) * dirtLayerHeight);
			
			//System.out.println("s: " +  stoneHeight + ", d: " + dirtHeight);
			
			for(int y=0; y<stoneHeight; y++) {
				map.setBlock(x, y, Block.getBlockByName("stone").getId());
			}
			
			for(int y=stoneHeight; y<stoneHeight + dirtHeight; y++) {
				map.setBlock(x, y, Block.getBlockByName("dirt").getId());
			}
			
			for(int y = stoneHeight + dirtHeight; y<height; y++) {
				map.setBlock(x, y, Block.getBlockByName("air").getId());
			}
			
			map.setBlock(x, stoneHeight+dirtHeight, Block.getBlockByName("grass").getId());
		}
		
		// carve caves into stone
		for(int x=1; x<width; x++) {
			double xMul = (-1/(2*x + 0.0000001) + 1)
						* (-1/(2*(width-x) + 0.0000001) + 1);
			
			for(int y=1; y<height; y++) {
				if(((n3.getNoise(x, y) + 1) / 2) 
						* (1 / ((double)height/((double)height*1000) * y + 1)) // higher -> less caves
						* (- 1/(2 * y+0.0000001) + 1) // very bottom -> no caves
						* xMul // closer to left or right edge -> less caves
						> 0.5f)
					map.setBlock(x, y, Block.getBlockByName("air").getId());
			}
		}
		
		// find spawnpoint
		int spawnX = ThreadLocalRandom.current().nextInt(width / 10, width - width/10);
		int spawnY = height;
		for(int i=height; i>0; i--) {
			if(map.getBlock(spawnX, spawnY - 1) != Block.getBlockByName("air").getId())
				break;
			spawnY--;
		}
		map.spawnpoint.set(spawnX * Block.BLOCK_SIZE, spawnY * Block.BLOCK_SIZE);
	}
}
