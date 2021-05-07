package com.lufi.terrafying.entities;

public class Entity {
	public float posx;
	public float posy;
	public float speedx;
	public float speedy;
	public int id;
	
	public Entity() {
		
	}
	
	public Entity(float x, float y, int ID) {
		posx = x; posy = y;
		id = ID;
	}
	
	public void update(float delta) {
		posx += speedx * delta;
		posy += speedy * delta;
	}
}
