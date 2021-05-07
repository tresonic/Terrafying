package com.lufi.terrafying.world;

public class Map {

	private int width;
	private int height;
	private int blocks[][];
	
	public Map(int nWidth, int nHeight) {
		width = nWidth;
		height = nHeight;
		blocks = new int[nWidth][nHeight];		
	}
	
	public void generate() {
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				blocks[x][y] = Block.AIR.getId();
			}
		}
	}
	
	
}
