package com.lufi.terrafying.gui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.world.Block;

public class Hotbar extends BaseGui {
	private Inventory inventory;
	
	private ItemStackGui itemStackGuis[];
	private int numSlots;
	private int selectedSlot;

	
	public Hotbar(Inventory nInventory, int nNumSlots) {
		inventory = nInventory;
		numSlots = nNumSlots;
		itemStackGuis = new ItemStackGui[nNumSlots];
		for(int i=0; i<numSlots; i++) {
			itemStackGuis[i] = new ItemStackGui(i * ItemStackGui.SIZE, 0);
		}
		selectedSlot = 0;
	}
	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		for(int i=0; i<itemStackGuis.length; i++) {
			itemStackGuis[i].selected = i == selectedSlot;
			itemStackGuis[i].draw(sb, sr, inventory.getItemStack(i));
		}
	}

	@Override
	public void keyDown(int keycode) {
		selectedSlot = keycode - Keys.NUM_1;	
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
	public void mouseDown(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}
	
	public ItemStack getSelectedItem() {
		return inventory.getItemStack(selectedSlot);
	}

}
