package com.lufi.terrafying;

import com.badlogic.gdx.Game;
import com.lufi.terrafying.screens.*;


public class Terrafying extends Game {
	
	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
