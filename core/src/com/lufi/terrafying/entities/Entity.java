package com.lufi.terrafying.entities;

import com.lufi.terrafying.world.Vector2i;

public class Entity {
	public float posx;
	public float posy;
	public float speedx;
	public float speedy;
	public int id;
	
	protected Vector2i lastMoveDir;
	
	public boolean isPlayer;
	
	public Entity() {
		isPlayer = false;
		lastMoveDir = new Vector2i();
	}
	
	public Entity(float x, float y, int ID) {
		posx = x; posy = y;
		id = ID;
		isPlayer = false;
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
}
