package com.lufi.terrafying.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.world.Block;

public class InventoryGui extends BaseGui {
	private static final int WIDTH = 9;
	
	private Inventory inventory;
	private ItemStackGui itemStackGuis[];
	
	private ItemStack heldItemStack;
	private Vector2 heldItemStackPos;
	
	public InventoryGui(Inventory nInventory) {
		inventory = nInventory;
		heldItemStackPos = new Vector2();
		heldItemStack = null;
		
		
		float width = (WIDTH * Block.BLOCK_SIZE + WIDTH * GuiManager.MARGIN * 2 + GuiManager.MARGIN) * GuiManager.HUD_SCALE;
		float startx = Gdx.graphics.getWidth() / 2 - width / 2;
		float starty = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 3;
		itemStackGuis = new ItemStackGui[inventory.getSize()];
		for(int x=0; x<WIDTH; x++) {
			for(int y=0; y<inventory.getSize()/WIDTH; y++) {
				itemStackGuis[x + y*WIDTH] = new ItemStackGui(startx + x*ItemStackGui.size, starty + y*ItemStackGui.size);
			}
		}
	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		sb.begin();
		sr.begin();
		
		for(int i=0; i<inventory.getSize(); i++) {
			itemStackGuis[i].draw(sb, sr, inventory.getItemStack(i));
		}
		
		sb.end();
		sr.end();
		
	}

	@Override
	public void keyDown(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyUp(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(int x, int y, int button, OrthographicCamera cam) {
		if(button == Input.Buttons.LEFT) {
			//System.out.println(x + " " + y + ";  " + itemStackGuis[0].pos.x + " " + itemStackGuis[0].pos.y);
			for(int i=0; i<itemStackGuis.length; i++) {
				if(itemStackGuis[i].contains(x, y)) {
					itemStackGuis[i].clicked = !itemStackGuis[i].clicked;
				}
			}
		}
		
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}
}
