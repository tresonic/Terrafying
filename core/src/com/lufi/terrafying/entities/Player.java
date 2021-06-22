package com.lufi.terrafying.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.net.TerrafyingClient;

public class Player extends Entity {
	private final float ACCEL_GROUND = 150;
	private final float ACCEL_AIR = 70;
	private final float MAX_SPD = 80;
	private final float JUMP_SPD = 120;
	private final float GRAVITY = -300;
	private final float FRICTION = -10;

	public final static int MAX_HEALTH = 10;
	public final static float REGEN_TIME = 7.5f;
	
	
	private final int INV_SIZE = 27;

	private Vector2i inputDir;
	private boolean inputJump;

	private int jumpsLeft;
	private int jumpCount = 2;

	public Inventory inventory;

	private Options options;

	int health = 10;
	private float regTime;
	
	
	public Player() {
		inputDir = new Vector2i();
		inventory = new Inventory(INV_SIZE);
	}
	
	
	
	public Player(float x, float y, int id, String nName, Options nOptions) {
		super(x, y, id, nName);
		inputDir = new Vector2i();
		inventory = new Inventory(INV_SIZE);
		options = nOptions;
		
	}

	public Vector2 updateAndGetTranslation(float delta, Map map) {
		
		regTime += delta;
		if (regTime >= REGEN_TIME && health < MAX_HEALTH) {
			health++;
			regTime = 0;
		}
		
		
		if (inputDir.x > 0)
			speedx += ((jumpsLeft == jumpCount) ? ACCEL_GROUND : ACCEL_AIR) * delta;
		else if (inputDir.x < 0)
			speedx -= ((jumpsLeft == jumpCount) ? ACCEL_GROUND : ACCEL_AIR) * delta;
		else {
			if ((jumpsLeft == jumpCount)) {
				speedx += FRICTION * speedx * delta;
				if (Math.abs(speedx) < 0.01f)
					speedx = 0;
			}
		}

		if (speedx > MAX_SPD)
			speedx = MAX_SPD;
		else if (speedx < -MAX_SPD)
			speedx = -MAX_SPD;

		if (jumpsLeft > 0 && inputJump) {
			speedy = JUMP_SPD;
			jumpsLeft--;
			inputJump = false;
		}

		speedy += GRAVITY * delta;

		float newposx = posx + speedx * delta;
		float newposy = posy + speedy * delta;

		if (speedx > 0) {
			if (Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy)).getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy + super.HEIGHT / 2))
							.getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy + super.HEIGHT))
							.getCollidable()) {
				newposx = Math.round(posx);
				speedx = 0;
			}
		}

		else if (speedx < 0) {
			if (Block.getBlockById(map.getBlockAt(newposx, posy)).getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx, posy + super.HEIGHT / 2)).getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx, posy + super.HEIGHT)).getCollidable()) {
				newposx = Math.round(posx);
				speedx = 0;
			}
		}

		if (speedy < 0) {
			if (Block.getBlockById(map.getBlockAt(newposx, newposy)).getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH, newposy)).getCollidable()) {
				newposy = ((int) posy);
				jumpsLeft = jumpCount;
				//fall damage (ab 5 blöcke)
				if(speedy <= -222) {
					speedy += 222;
					int healthPointsReduce = (int) speedy;
					healthPointsReduce /= 21.6;
					
					takeDamage(Math.abs(healthPointsReduce));

				}
				speedy = 0;
			}
		} else if (speedy > 0) {
			if (Block.getBlockById(map.getBlockAt(newposx, newposy + super.HEIGHT)).getCollidable() || Block
					.getBlockById(map.getBlockAt(newposx + super.WIDTH, newposy + super.HEIGHT)).getCollidable()) {
				newposy = ((int) posy);
				speedy = 0;
			}
		}

		posx = newposx;
		posy = newposy;

		if (speedx > 0)
			lastMoveDir.x = 1;
		else if (speedx < 0)
			lastMoveDir.x = -1;

		return new Vector2(speedx * delta, speedy * delta);
	}

	public String getName() {
		return name;
	}

	public boolean keyDown(int keycode) {
		if (options.getKeyUp() == keycode) {
			inputDir.y = 1;
		} else if (options.getKeyDown() == keycode) {
			inputDir.y = -1;
		} else if (options.getKeyLeft() == keycode) {
			inputDir.x = -1;
		} else if (options.getKeyRight() == keycode) {
			inputDir.x = 1;
		} else if (options.getKeyJump() == keycode) {
			inputJump = true;
		}

		return true;
	}

	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
			if (inputDir.y == 1)
				inputDir.y = 0;
			break;
		case Keys.S:
			if (inputDir.y == -1)
				inputDir.y = 0;
			break;
		case Keys.A:
			if (inputDir.x == -1)
				inputDir.x = 0;
			break;
		case Keys.D:
			if (inputDir.x == 1)
				inputDir.x = 0;
			break;
		case Keys.SPACE:
			inputJump = false;
			break;
		}
		return true;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int nHealth) {
		health = nHealth;
	}
	
	
	public void takeDamage(int amount) {
		regTime = 0;
		health -= amount;
		
	}

	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
}
