package com.lufi.terrafying.world;

import java.util.concurrent.ThreadLocalRandom;

import com.lufi.terrafying.util.SimplexNoise;
import com.lufi.terrafying.util.Vector2i;

public class MapGenerator {
	public static void generate(Map map, int width, int height) {
		System.out.println("gen map width/height: " + width + "/" + height);
		for(int x=0; x<width/Chunk.CHUNK_SIZE; x++) {
			for(int y=0; y<height/Chunk.CHUNK_SIZE; y++) {
				map.addChunk(new Vector2i(x, y), new Chunk());
			}
		}
		
		System.out.println("chunks in map: " + map.getNumChunks());
		
		SimplexNoise n1 = new SimplexNoise(300, 0.45f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n2 = new SimplexNoise(300, 0.65f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n3 = new SimplexNoise(35, 0.4f, ThreadLocalRandom.current().nextInt());
		
		
		
		int stoneLayerHeight = height / 3;
		int dirtLayerHeight = height / 100;
		float smoothingFactor = 0.5f;
		
		
		int nextTree = 15;
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
				if(map.getBlock(x, y) == 0)
					map.setBlock(x, y, Block.getBlockByName("air").getId());
			}
			
			map.setBlock(x, stoneHeight+dirtHeight, Block.getBlockByName("grass").getId());
			
			if(nextTree-- <= 0) {
				map.setBlock(x, stoneHeight+dirtHeight, Block.getBlockByName("dirt").getId());
				placeTree(map, x, stoneHeight+dirtHeight+1);
				nextTree = ThreadLocalRandom.current().nextInt(3, 50);
			}
		}
		
		// add ores to mapgen
		genOres(map, width, height);
		
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
	
	private static void genOres(Map map, int width, int height) {
		SimplexNoise ironNoise = new SimplexNoise(12, 0.4f, ThreadLocalRandom.current().nextInt());
		SimplexNoise copperNoise = new SimplexNoise(10, 0.4f, ThreadLocalRandom.current().nextInt());
		SimplexNoise goldNoise = new SimplexNoise(10, 0.4f, ThreadLocalRandom.current().nextInt());
		SimplexNoise rubyNoise = new SimplexNoise(8, 0.4f, ThreadLocalRandom.current().nextInt());
		SimplexNoise uraniumNoise = new SimplexNoise(8, 0.4f, ThreadLocalRandom.current().nextInt());
		SimplexNoise diamondNoise = new SimplexNoise(8, 0.4f, ThreadLocalRandom.current().nextInt());
		
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				if(map.getBlock(x, y) == Block.getBlockByName("stone").getId()) {
					if(ironNoise.getNoise(x, y) > 0.17)
						map.setBlock(x, y, Block.getBlockByName("iron").getId());
					if(copperNoise.getNoise(x, y) > 0.17)
						map.setBlock(x, y, Block.getBlockByName("copper").getId());
					if(goldNoise.getNoise(x, y) > 0.20)
						map.setBlock(x, y, Block.getBlockByName("gold").getId());
					if(rubyNoise.getNoise(x, y) > 0.23)
						map.setBlock(x, y, Block.getBlockByName("ruby").getId());
					if(uraniumNoise.getNoise(x, y) > 0.22)
						map.setBlock(x, y, Block.getBlockByName("uranium").getId());
					if(diamondNoise.getNoise(x, y) > 0.24)
						map.setBlock(x, y, Block.getBlockByName("diamond").getId());
				}
			}
		}
		
	}
	
	private static void placeTree(Map map, int posx, int posy) {
		int trunkHeight = 4;
		
		for(int y = posy; y < posy + trunkHeight; y++) {
			map.setBlock(posx, y, Block.getBlockByName("oakwood").getId());
		}
		
		posy += trunkHeight;
		
		for(int x=-2; x<3; x++) {
			for(int y=-1; y<3; y++) {
				if(map.getBlock(posx + x, posy + y) !=  Block.getBlockByName("oakwood").getId() && !(y == 2 && (x == -2 || x == 2))) {
					map.setBlock(posx + x, posy + y, Block.getBlockByName("oakwoodleaves").getId());
				}
			}
		}
	}
}
