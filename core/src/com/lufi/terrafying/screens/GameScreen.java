package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.world.World;

public class GameScreen implements Screen {
	private final Game game;
	
	private TerrafyingClient client;
	private World world;
	
	private String name, ip;
	
	public GameScreen(final Game g, String playerName, String serverAddress) {
		game = g;
		name = playerName;
		ip = serverAddress;
		client = new TerrafyingClient(world);
		client.connect(name, ip, 30000, 30001);
	}
	
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		
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
		// TODO Auto-generated method stub
		
	}
	
	public boolean isConnected() {
		return client.connected;
	}

	public boolean isConnecting() {
		return client.connecting;
	}
}
