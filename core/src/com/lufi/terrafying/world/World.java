package com.lufi.terrafying.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.entities.Player;

public class World {
	public Map map;
	public EntityManager entityManager;
	public Player player;
	
	public World(int nWidth, int nHeight){
		map = new Map(nWidth, nHeight);
		entityManager = new EntityManager();
		
	}
	
	public void render(float delta, ShapeRenderer sh, SpriteBatch sb) {
		//System.out.println(Gdx.input.getInputProcessor());
		entityManager.interpolateEntites(delta);
		
		sh.setColor(Color.BLUE);
		sh.setAutoShapeType(true);
		
		for(Entity e : entityManager.getEntities()) {
			
			if(e instanceof Player) {
				sb.begin();
				sb.draw(Terrafying.assetManager.get("blocks/stone.png", Texture.class), e.posx, e.posy);
				sb.end();
			} else {				
				sh.begin();
				sh.rect(e.posx, e.posy, 10, 10);
				sh.end();
			}
		}
	}
}
