package com.lufi.terrafying.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	
	public void render(float delta, OrthographicCamera cam, ShapeRenderer sh, SpriteBatch sb) {
		// System.out.println(delta);
		
		cam.position.x = player.posx;
		cam.position.y = player.posy;
		player.updateAndGetTranslation(0.02f, map);
		
		map.render(cam, sb);
		
		sb.begin();
		for(Entity e : entityManager.getEntities()) {	
			if(e.isPlayer()) {
				Texture t = Terrafying.assetManager.get("wizard.png", Texture.class);
				boolean flip = e.getLastMoveDirX() < 0;
				sb.draw(t, flip ? e.posx + t.getWidth() : e.posx, e.posy, flip ? -t.getWidth() : t.getWidth(), t.getHeight());
				Terrafying.font.draw(sb, e.name, e.posx, e.posy);
			} else {				
				
				
			}			
		}
			
		sb.end();
	}
}
