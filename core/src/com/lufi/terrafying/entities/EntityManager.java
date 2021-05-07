package com.lufi.terrafying.entities;

import com.badlogic.gdx.utils.Array;

public class EntityManager {

	private Array<Entity> entities;
	
	public EntityManager() {
		
		entities = new Array<Entity>();
		
	}
	
	
	public void updateEntity(Entity entity) {
		for(int i=0; i<entities.size; i++) {
			if(entities.get(i).id == entity.id) {
				entities.set(i, entity);
			}
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);	
	}
	
	public void addEntities(Array<Entity> nEntities) {
		entities.addAll(nEntities);
	}
	
	public Array<Entity> getEntities() {
		return entities;
	}
}
