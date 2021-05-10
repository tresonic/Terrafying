package com.lufi.terrafying.world;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.lufi.terrafying.Terrafying;

public class Map {

	private int width;
	private int height;
	private int blocks[][];
	
	public Vector2 spawnpoint = new Vector2(250 * Block.BLOCK_SIZE, 150 * Block.BLOCK_SIZE);
	
	public Map(int nWidth, int nHeight) {
		width = nWidth;
		height = nHeight;
		blocks = new int[nWidth][nHeight];		
	}
	
	public void generate() {
		SimplexNoise n1 = new SimplexNoise(300, 0.45f, ThreadLocalRandom.current().nextInt());
		SimplexNoise n2 = new SimplexNoise(300, 0.65f, ThreadLocalRandom.current().nextInt());
		for(int x=0; x<width; x++) {
			int stoneHeight = (int) ((n1.getNoise(x, 0) + 1) * 30);
			int dirtHeight = (int) ((n2.getNoise(x, 0) + 1) * 4);
			
			System.out.println("s: " +  stoneHeight + ", d: " + dirtHeight);
			
			for(int y=0; y<stoneHeight; y++) {
				blocks[x][y] = Block.STONE.getId();
			}
			
			for(int y=stoneHeight; y<stoneHeight + dirtHeight; y++) {
				blocks[x][y] = Block.DIRT.getId();
			}
			
			for(int y = stoneHeight + dirtHeight; y<height; y++) {
				blocks[x][y] = Block.AIR.getId();
			}
			
			blocks[x][stoneHeight+dirtHeight] = Block.GRASS.getId();
		}
		
		int spawnX = ThreadLocalRandom.current().nextInt(width / 10, width - width/10);
		int spawnY = height;
		while(true) {
			if(blocks[spawnX][spawnY - 1] != Block.AIR.getId())
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
				if(Block.getBlockById(blocks[x][y]).getDrawable()) {
					Texture t = Terrafying.assetManager.get("blocks/" + Block.getBlockById(blocks[x][y]).getName() + ".png", Texture.class);
					sb.draw(t , x*Block.BLOCK_SIZE, y*Block.BLOCK_SIZE);
				}
			}
		}
		sb.end();
	}
	
	public int[][] getMapData() {
		return blocks;
	}
	
	public void setMapData(int[][] data) {
		blocks = data;
		width = data.length;
		height = data[0].length;
	}
}
