package com.lufi.terrafying;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lufi.terrafying.net.TerrafyingServer;
import com.lufi.terrafying.screens.*;
import com.lufi.terrafying.world.Block;


public class Terrafying extends Game {
	public static AssetManager assetManager;
	public static BitmapFont font;
	public static Texture fontTex;
	
	
	@Override
	public void create () {
		
		fontTex = new Texture(Gdx.files.internal("arial32.png"), true);
		fontTex.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("arial32.fnt"), new TextureRegion(fontTex), false);
		font.getData().setScale(0.3f);
		
		assetManager = new AssetManager();
		Block.loadBlockTextures(assetManager);
		assetManager.load("TerrafyingMensch.png", Texture.class);
		assetManager.load("TestMensch.png", Texture.class);
		assetManager.load("wizard.png", Texture.class);
		assetManager.finishLoading();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
		TerrafyingServer.the().stop();
	}
}
