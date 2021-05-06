package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
	private final Game game;
	
	private Stage stage;
	private Skin skin;
	
	private String name, ip;
	private GameScreen gameScreen;
	
	private Label loadField;
	
	public LoadingScreen(final Game g, String playerName, String serverIp) {
		game = g;
		name = playerName;
		ip = serverIp;
		
		stage = new Stage();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		Gdx.input.setInputProcessor(stage);
		
		loadField = new Label("Loading... please wait...", skin);
		stage.addActor(loadField);
		
		gameScreen = new GameScreen(game, playerName, serverIp);
	}
	
	@Override
	public void show() {
		gameScreen.connect();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0,0,0,1);
		if(gameScreen.isConnected())
			game.setScreen(gameScreen);
		stage.act();
		stage.draw();
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

}
