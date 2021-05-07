package com.lufi.terrafying;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.lufi.terrafying.screens.*;
import com.lufi.terrafying.world.Block;


public class Terrafying extends Game {
	public static AssetManager assetManager;
	
	
	@Override
	public void create () {
		assetManager = new AssetManager();
		Block.loadBlockTextures(assetManager);
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
	}
}
