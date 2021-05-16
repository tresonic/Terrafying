package com.lufi.terrafying.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
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
	
	private final int INV_SIZE = 27;
	
	private Vector2i inputDir;
	private boolean inputJump; 
	
	private int jumpsLeft;
	private int jumpCount = 2;	
	
	public Inventory inventory;

	public Player() {
		inputDir = new Vector2i();
		inventory = new Inventory(INV_SIZE);
	}
	
	public Player(float x, float y, int id, String nName) {
		super(x, y, id, nName);
		inputDir = new Vector2i();
		inventory = new Inventory(INV_SIZE);
		inventory.addItem(new ItemStack(Item.getItemByName("stone"), 50));
		inventory.addItem(new ItemStack(Item.getItemByName("grass"), 50));
		inventory.addItem(new ItemStack(Item.getItemByName("light"), 50));
	}

	public Vector2 updateAndGetTranslation(float delta, Map map) {
		//System.out.println(delta);
		if(inputDir.x > 0)
			speedx += ((jumpsLeft == jumpCount) ? ACCEL_GROUND : ACCEL_AIR) * delta;
		else if(inputDir.x < 0)
			speedx -= ((jumpsLeft == jumpCount) ? ACCEL_GROUND : ACCEL_AIR) * delta;
		else {
			if((jumpsLeft == jumpCount)) {
				speedx += FRICTION * speedx * delta;
				if (Math.abs(speedx) < 0.01f)
					speedx = 0;
			}
		}
		
		if(speedx > MAX_SPD)
			speedx = MAX_SPD;
		else if(speedx < -MAX_SPD)
			speedx = -MAX_SPD;
			

		if(jumpsLeft > 0 && inputJump) {
			speedy = JUMP_SPD;
			jumpsLeft--;
			inputJump = false;
		}
		
		speedy += GRAVITY * delta;
		
		float newposx = posx + speedx * delta;
		float newposy = posy + speedy * delta;
		
		if(speedx > 0) {
			if(Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy)).getCollidable() 
				|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy + super.HEIGHT / 2)).getCollidable()
				|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH + 1, posy + super.HEIGHT)).getCollidable()) 
			{
				newposx = Math.round(posx);
				speedx = 0;
			}
		}
		
		else if(speedx < 0) {
			if(Block.getBlockById(map.getBlockAt(newposx, posy)).getCollidable()
				|| Block.getBlockById(map.getBlockAt(newposx, posy + super.HEIGHT / 2)).getCollidable()
				|| Block.getBlockById(map.getBlockAt(newposx, posy + super.HEIGHT)).getCollidable()) 
			{
				newposx = Math.round(posx);
				speedx = 0;
			}
		}
		
		if(speedy < 0) {
			if(Block.getBlockById(map.getBlockAt(newposx, newposy)).getCollidable()
				|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH, newposy)).getCollidable()) 
			{
				newposy = ((int)posy);
				jumpsLeft = jumpCount;
				speedy = 0;
			}
		}
		else if(speedy > 0) {
			if(Block.getBlockById(map.getBlockAt(newposx, newposy + super.HEIGHT)).getCollidable()
					|| Block.getBlockById(map.getBlockAt(newposx + super.WIDTH, newposy + super.HEIGHT)).getCollidable()) 
			{
				newposy = ((int)posy);
				speedy = 0;
			}
		}
		
		
		posx = newposx;
		posy = newposy;
		
		if(speedx > 0)
			lastMoveDir.x = 1;
		else if(speedx < 0)
			lastMoveDir.x = -1;
		
		return new Vector2(speedx * delta, speedy * delta);
	}
	
	public boolean wield(float x, float y, Map map, ItemStack wieldItem) {
		int bId = map.getBlockAt(x, y);
		if(!Block.getBlockById(bId).getMineable()) {
			return false;
		}
		map.setBlockAt(x, y, Block.getBlockByName("air").getId());
		Item i = Item.getItemById(bId);
		inventory.addItem(new ItemStack(i, 1));
		return true;
	}
	
	public boolean use(float x, float y, Map map, ItemStack wieldItem) {
		if(wieldItem.count == 0)
			return false;
		
		if(wieldItem.item.getBlockItem()) {
			if(Block.getBlockById(map.getBlockAt(x, y)).getName() != "air")
				return false;
			
			boolean hasNeighbor = false;
			Vector2i pos = Map.getBlockPos(x, y);
			for(int x1=-1; x1<2; x1++) {
				for(int y1=-1; y1<2; y1++) {
					if(Block.getBlockById(map.getBlock(pos.x + x1, pos.y + y1)).getCollidable()) {
						hasNeighbor = true;
					}
				}
			}
			
			if(!hasNeighbor)
				return false;
			
			map.setBlockAt(x, y, wieldItem.item.getId());
			wieldItem.count--;
			return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

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
