package com.lufi.terrafying.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.world.Vector2i;

public class Player extends Entity implements InputProcessor {
	private final int MAX_SPEED = 50;
	

	public Player() {
	}
	
	public Player(float x, float y, int id, String nName) {
		super(x, y, id, nName);
	}

	public Vector2 updateAndGetTranslation(float delta) {
		posx += speedx * delta;
		posy += speedy * delta;
		
		if(speedx > 0)
			lastMoveDir.x = 1;
		else if(speedx < 0)
			lastMoveDir.x = -1;
		
		return new Vector2(speedx * delta, speedy * delta);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.W:
			speedy = MAX_SPEED;
			break;
		case Keys.S:
			speedy = -MAX_SPEED;
			break;
		case Keys.A:
			speedx = -MAX_SPEED;
			break;
		case Keys.D:
			speedx = MAX_SPEED;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.W:
			if(speedy == MAX_SPEED)
				speedy = 0;
			break;
		case Keys.S:
			if(speedy == -MAX_SPEED)
				speedy = 0;
			break;
		case Keys.A:
			if(speedx == -MAX_SPEED)
				speedx = 0;
			break;
		case Keys.D:
			if(speedx == MAX_SPEED)
				speedx = 0;
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
