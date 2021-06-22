package com.lufi.terrafying.entities;

import java.io.Serializable;

import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block;

public class Entity implements Serializable {
	public final float WIDTH = Block.BLOCK_SIZE - 2;
	public final float HEIGHT = Block.BLOCK_SIZE * 2 - 6;
	
	public float posx;
	public float posy;
	public transient float speedx;
	public transient float speedy;
	public int id;
	
	public String tex;
	public String name;
	
	protected Vector2i lastMoveDir;
	
	public boolean isPlayer;
	
	public Entity() {
		isPlayer = false;
		lastMoveDir = new Vector2i();
	}
	
	public Entity(float x, float y, int ID, String nName) {
		posx = x; posy = y;
		id = ID;
		isPlayer = false;
		name = nName;
		lastMoveDir = new Vector2i();
	}
	
	public void update(float delta) {
		posx += speedx * delta;
		posy += speedy * delta;
		
		if(speedx > 0)
			lastMoveDir.x = 1;
		else if(speedx < 0)
			lastMoveDir.x = -1;
		
	}
	
	public int getLastMoveDirX() {
		return lastMoveDir.x;
	}

	public boolean isPlayer() {
		return isPlayer;
	}
	
	@Override
	public String toString() {
		return "Entity " + name + " at " + posx + " " + posy;
	}
}
