package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.Terrafying;

public class LoadingScreen implements Screen {
	private final Terrafying game;
	
	private Stage stage;
	
	
	private boolean connected;
	private GameScreen gameScreen;
	
	private Label loadField;
	
	public LoadingScreen(final Terrafying g, String playerName, String serverIp) {
		game = g;
		connected = false;
		
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		loadField = new Label("Loading... please wait...", game.skin);
		stage.addActor(loadField);
		
		gameScreen = new GameScreen(game, playerName, serverIp);
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0,0,0,1);
		if(!gameScreen.isConnected() && !gameScreen.isConnecting())
			game.setScreen(game.mainMenuScreen);
		if(gameScreen.isConnected()) {
			game.gameScreen = gameScreen;
			game.setScreen(gameScreen);	
		}
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
