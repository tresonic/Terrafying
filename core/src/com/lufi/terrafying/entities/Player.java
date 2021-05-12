package com.lufi.terrafying.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.world.Vector2i;

public class Player extends Entity implements InputProcessor {
	private final float ACCEL_GROUND = 150;
	private final float ACCEL_AIR = 70;
	private final float MAX_SPD = 80;
	private final float JUMP_SPD = 60;
	private final float GRAVITY = -120;
	private final float FRICTION = -10;
	
	private Vector2i inputDir;
	private boolean inputJump;
	
	private float accelx;
	private float accely;
	
	private boolean onGround;

	public Player() {
		inputDir = new Vector2i();
		onGround = true;
	}
	
	public Player(float x, float y, int id, String nName) {
		super(x, y, id, nName);
		accelx = 0;
		accely = 0;
		inputDir = new Vector2i();
		onGround = true;
	}

	public Vector2 updateAndGetTranslation(float delta) {
		//System.out.println(delta);
		if(inputDir.x > 0)
			speedx += (onGround ? ACCEL_GROUND : ACCEL_AIR) * delta;
		else if(inputDir.x < 0)
			speedx += (onGround ? -ACCEL_GROUND : -ACCEL_AIR) * delta;
		else {
			if(onGround) {
				speedx += FRICTION * speedx * delta;
				if (Math.abs(speedx) < 0.01f)
					speedx = 0;
			}
		}
		
		if(speedx > MAX_SPD)
			speedx = MAX_SPD;
		else if(speedx < -MAX_SPD)
			speedx = -MAX_SPD;
			

		if(onGround && inputJump) {
			//if(speedy == 0) {
				speedy = JUMP_SPD;
			//}
		}
		

		
		//speedy += GRAVITY * delta;
		
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
			inputDir.y = 1;
			break;
		case Keys.S:
			inputDir.y = -1;
			break;
		case Keys.A:
			inputDir.x = -1;
			break;
		case Keys.D:
			inputDir.x = 1;
			break;
		case Keys.SPACE:
			inputJump = true;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.W:
			if(inputDir.y == 1)
				inputDir.y = 0;
			break;
		case Keys.S:
			if(inputDir.y == -1)
				inputDir.y = 0;
			break;
		case Keys.A:
			if(inputDir.x == -1)
				inputDir.x = 0;
			break;
		case Keys.D:
			if(inputDir.x == 1)
				inputDir.x = 0;
			break;
		case Keys.SPACE:
			inputJump = false;
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
