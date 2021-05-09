package com.lufi.terrafying.entities;

public class Entity {
	public float posx;
	public float posy;
	public float speedx;
	public float speedy;
	public int id;
	
	public boolean isPlayer;
	
	public Entity() {
		isPlayer = false;
	}
	
	public Entity(float x, float y, int ID) {
		posx = x; posy = y;
		id = ID;
		isPlayer = false;
	}
	
	public void update(float delta) {
		posx += speedx * delta;
		posy += speedy * delta;
	}
}
