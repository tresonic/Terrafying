package com.lufi.terrafying.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.world.Block;

public class Hotbar extends BaseGui {
	private Inventory inventory;
	private int numSlots;
	private int selectedSlot;

	
	public Hotbar(Inventory nInventory, int nNumSlots) {
		inventory = nInventory;
		numSlots = nNumSlots;
		selectedSlot = 0;
	}
	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		sr.begin();
		sb.begin();
		sr.set(ShapeType.Filled);
		for(int i=0; i<numSlots; i++) {
			sr.setColor(i == selectedSlot ? GuiManager.selectColor : GuiManager.backColor);
			sr.rect((i * Block.BLOCK_SIZE + i * GuiManager.MARGIN * 2) * GuiManager.HUD_SCALE, 0,
					(Block.BLOCK_SIZE + GuiManager.MARGIN * 2) * GuiManager.HUD_SCALE, (Block.BLOCK_SIZE + GuiManager.MARGIN * 2) * GuiManager.HUD_SCALE);
			sr.setColor(GuiManager.frontColor);
			sr.rect((i * (Block.BLOCK_SIZE + 2 * GuiManager.MARGIN) + GuiManager.MARGIN) * GuiManager.HUD_SCALE, GuiManager.MARGIN * GuiManager.HUD_SCALE,
					Block.BLOCK_SIZE * GuiManager.MARGIN, Block.BLOCK_SIZE * GuiManager.MARGIN);
		}
		sr.end();
		sb.end();
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

}
