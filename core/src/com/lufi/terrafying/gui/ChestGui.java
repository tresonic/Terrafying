package com.lufi.terrafying.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.ItemStack;

public class ChestGui extends BaseGui {
	private static float starty = GuiManager.HEIGHT / 2;
	
	private Inventory chestInv;
	private ArrayList<ItemStackGui> itemStackGuis;
	
	public ChestGui() {
		itemStackGuis = new ArrayList<ItemStackGui>();
	}
	
	public void setChestInv(Inventory nInv) {
		chestInv = nInv;
		for(int x=0; x<InventoryGui.ITEM_WIDTH; x++) {
			for(int y=0; y<chestInv.getSize()/InventoryGui.ITEM_WIDTH; y++) {
				itemStackGuis.add(new ItemStackGui(InventoryGui.STARTX + x*ItemStackGui.SIZE, starty + y*ItemStackGui.SIZE));
			}
		}
	}
	
	public Inventory getChestInv() {
		return chestInv;
	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		for(int i=0; i<chestInv.getSize(); i++) {
			itemStackGuis.get(i).draw(sb, sr, chestInv.getItemStack(i));
		}
	}
	
	public boolean mouseDown(int x, int y, int button, InventoryGui invGui) {
		int idx = getClickedItemStack(x, y);
		if(idx == -1)
			return false;
		
		if(button == Input.Buttons.LEFT) {
			if(invGui.heldItemStack.count == 0)
				invGui.heldItemStackOffset = itemStackGuis.get(idx).getClickOffset(x, y);
			invGui.heldItemStack = chestInv.changeItemStack(idx, invGui.heldItemStack);
			// System.out.println("chest inv: " + invGui.heldItemStack);
			return true;
		}
		else if(button == Input.Buttons.RIGHT) {
			if(invGui.heldItemStack.count == 0) {
				invGui.heldItemStackOffset = itemStackGuis.get(idx).getClickOffset(x, y);
				invGui.heldItemStack = chestInv.takeItem(idx, chestInv.getItemStack(idx).count/2);
			}
			else if(invGui.heldItemStack.count > 0) {
				ItemStack result = chestInv.addItem(idx, invGui.heldItemStack.takeItem(1));
				if(result.count > 0)
					invGui.heldItemStack.addItem(result);
			}
			return true;
		}
		return false;
	}
	
	public int getClickedItemStack(int x, int y) {
		for(int i=0; i<itemStackGuis.size(); i++) {
			if(itemStackGuis.get(i).contains(x, y)) {
				return i;
			}
		}
		return -1;
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
		// TODO Auto-generated method stub

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
