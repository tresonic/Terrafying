package com.lufi.terrafying.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;

public class EntityManager implements Serializable {

	private ArrayList<Entity> entities;
	
	public EntityManager() {
		
		entities = new ArrayList<Entity>();
		
	}
	
	public void interpolateEntites(float delta) {
		for(int i=0; i<entities.size(); i++) {
			if(!entities.get(i).isPlayer)
				entities.get(i).update(delta);
		}
	}
	
	public void updateEntity(Entity entity) {
		for(int i=0; i<entities.size(); i++) {
			if(entities.get(i).id == entity.id) {
				entities.get(i).posx = entity.posx;
				entities.get(i).posy = entity.posy;
				entities.get(i).speedx = entity.speedx;
				entities.get(i).speedy = entity.speedy;
				entities.get(i).lastMoveDir = entity.lastMoveDir;
			}
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);	
	}
	
	public void removeEntity(int id) {
		for(int i=0; i<entities.size(); i++) {
			if(entities.get(i).id == id)
				entities.remove(i);
		}
	}
	
	public void addEntities(ArrayList<Entity> nEntities) {
		entities.addAll(nEntities);
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public int getNumEntites() {
		return entities.size();
	}
	
	public Entity getEntity(int id) {
		for(int i=0; i<entities.size(); i++) {
			if(entities.get(i).id == id)
				return entities.get(i);
		}
		return null;
	}
}
