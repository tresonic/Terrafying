package com.lufi.terrafying.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Player extends Entity implements InputProcessor {
	private final int spd = 50;
	
	public Player() {}
	
	public Player(float x, float y, int id) {
		super(x, y, id);
	}


	@Override
	public boolean keyDown(int keycode) {
		//System.out.println("keydown");
		switch(keycode) {
		case Keys.A:
			speedx = -spd;
			break;
		case Keys.D:
			speedx = spd;
			break;
		case Keys.W:
			speedy = spd;
			break;
		case Keys.S:
			speedy = -spd;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.A:
			speedx = 0;
			break;
		case Keys.D:
			speedx = 0;
			break;
		case Keys.W:
			speedy = 0;
			break;
		case Keys.S:
			speedy = 0;
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
