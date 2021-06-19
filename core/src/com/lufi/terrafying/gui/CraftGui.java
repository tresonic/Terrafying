package com.lufi.terrafying.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.items.Recipe;
import com.lufi.terrafying.world.Block;

public class CraftGui extends BaseGui {
	private static float starty = GuiManager.HEIGHT / 2;
	
	private Inventory inventory;
	private ArrayList<ItemStack> craftableItems;
	private ArrayList<ItemStackGui> craftableItemGuis;
	
	public CraftGui(Inventory nInventory) {
		inventory = nInventory;		
		craftableItemGuis = new ArrayList<ItemStackGui>();
		craftableItems = new ArrayList<ItemStack>();
		updateCraftableItems();
	}
	
	public void updateCraftableItems() {
		craftableItems = Recipe.getCraftableItemStacks(inventory);
		craftableItemGuis.clear();
		for(int i=0; i<craftableItems.size(); i++) {
			craftableItemGuis.add(new ItemStackGui(InventoryGui.STARTX + i%InventoryGui.ITEM_WIDTH * ItemStackGui.SIZE, starty + i/InventoryGui.ITEM_WIDTH * ItemStackGui.SIZE));
		}
	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		for(int i=0; i<craftableItems.size(); i++) {
			craftableItemGuis.get(i).draw(sb, sr, craftableItems.get(i));
		}

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
	public void mouseDown(int x, int y, int button) {
		for(int i=0; i<craftableItems.size(); i++) {
			if(craftableItemGuis.get(i).contains(x, y)) {
				Recipe.doCraft(inventory, craftableItems.get(i).item);
				updateCraftableItems();
			}
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrolled(float amount) {
		// TODO Auto-generated method stub

	}

}
