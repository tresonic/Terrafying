package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.World;

public class GameScreen implements Screen {
	private final int viewPortWidth = 50 * Block.BLOCK_SIZE;
	private final Game game;
	
	private TerrafyingClient client;
	private World world;
	private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer sh;
	
	private String name, ip;
	
	private float lastTime;
	
	public GameScreen(final Game g, String playerName, String serverAddress) {
		game = g;
		name = playerName;
		ip = serverAddress;
		sh = new ShapeRenderer();
		world = new World(500, 500);
		client = new TerrafyingClient(world);
		client.connect(name, ip);
		// world.map.generate();
		
		lastTime = 0;
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(viewPortWidth, viewPortWidth * ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));
		camera.position.set(world.map.spawnpoint.x, world.map.spawnpoint.y, 0);
	}
	
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(world.player);
		camera.position.x = world.player.posx;
		camera.position.y = world.player.posy;
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		world.render(delta, camera, sh, spriteBatch);
		
		//System.out.println(Gdx.input.getInputProcessor());
		//System.out.println(Gdx.graphics.getFramesPerSecond());
		
		lastTime += delta;
		if(lastTime >= 0.02f) {
			client.update();
			lastTime = 0;
		}
	}
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		
		
	}
	
	public boolean isConnected() {
		return client.connected;
	}

	public boolean isConnecting() {
		return client.connecting;
	}
}
