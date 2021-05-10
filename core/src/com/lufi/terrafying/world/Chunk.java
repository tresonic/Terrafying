package com.lufi.terrafying.world;

public class Chunk {
	public static final int CHUNK_SIZE = 32;
	
	private int[][] blocks;
	
	public Chunk() {
		blocks = new int[CHUNK_SIZE][CHUNK_SIZE];
	}
	
	public int getBlock(int x, int y) {
		return blocks[x][y];
	}
	
	public void setBlock(int x, int y, int block) {
		blocks[x][y] = block;
	}
	
	public int[][] getBlocks() {
		return blocks;
	}
	
	public void setBlocks(int[][] nBlocks) {
		blocks = nBlocks;
	}
}
