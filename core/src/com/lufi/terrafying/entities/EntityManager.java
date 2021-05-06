package com.lufi.terrafying.entities;

import com.badlogic.gdx.utils.Array;

public class EntityManager {

	private Array<Entity> entities;
	
	public EntityManager() {
		
		entities = new Array<Entity>();
		
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);	
	}
}
