package com.lufi.terrafying;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.net.TerrafyingServer;
import com.lufi.terrafying.screens.*;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.world.Block;


public class Terrafying extends Game {
	public static AssetManager assetManager;
	public Options options;
	public static BitmapFont font;
	public static Texture fontTex;
	public static BitmapFont guifont;
	public static Texture guifontTex;
	public static Skin skin;
	public Screen mainMenuScreen, pauseScreen, loadingScreen, newMapScreen, optionsScreen;
	public GameScreen gameScreen;
	
	@Override
	public void create () {	
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		options = Options.loadOptions();
		
		mainMenuScreen = new MainMenuScreen(this);
		pauseScreen = new PauseScreen(this);
		newMapScreen = new NewMapScreen(this);
		optionsScreen = new OptionsScreen(this);
		
		fontTex = new Texture(Gdx.files.internal("arial32.png"), true);
		fontTex.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("arial32.fnt"), new TextureRegion(fontTex), false);
		font.getData().setScale(0.3f);
		font.setUseIntegerPositions(false);
		
		guifontTex = new Texture(Gdx.files.internal("arial32.png"), true);
		guifont = new BitmapFont(Gdx.files.internal("arial32.fnt"), new TextureRegion(guifontTex), false);
		guifont.getData().setScale(0.5f);
		
		
		assetManager = new AssetManager();
		Block.loadBlockTextures(assetManager);
		Item.loadItemTextures(assetManager);
		assetManager.load("TerrafyingMensch.png", Texture.class);
		assetManager.load("TestMensch.png", Texture.class);
		assetManager.load("wizard.png", Texture.class);
		assetManager.load("crack.png", Texture.class);
		assetManager.finishLoading();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		getScreen().dispose();
		assetManager.dispose();
		TerrafyingServer.the().stop();
	}
}
