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
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.items.Recipe;
import com.lufi.terrafying.world.Block;

public class InventoryGui extends BaseGui {
	public static final int ITEM_WIDTH = 9;
	
	public static float WIDTH = (ITEM_WIDTH * Block.BLOCK_SIZE + ITEM_WIDTH * GuiManager.MARGIN * 2 + GuiManager.MARGIN) * GuiManager.HUD_SCALE;
	public static float STARTX = GuiManager.WIDTH / 2 - WIDTH / 2;
	float starty = GuiManager.HEIGHT - GuiManager.HEIGHT / 2 - GuiManager.HEIGHT / 3;
	
	private Inventory inventory;
	private ItemStackGui itemStackGuis[];
	
	ItemStack heldItemStack;
	Vector2 heldItemStackPos;
	Vector2 heldItemStackOffset;
	
	enum InvAction {
		CRAFT, CHEST,
	}
	
	InvAction invAction;
	CraftGui craftGui;
	ChestGui chestGui;
	
	boolean metaDirty;
	
	public InventoryGui(Inventory nInventory) {
		inventory = nInventory;
		heldItemStackPos = new Vector2();
		heldItemStackOffset = new Vector2();
		heldItemStack = new ItemStack();
		
		craftGui = new CraftGui(inventory);
		chestGui = new ChestGui();
		metaDirty = false;
		invAction = InvAction.CRAFT;
		
		itemStackGuis = new ItemStackGui[inventory.getSize()];
		for(int x=0; x<ITEM_WIDTH; x++) {
			for(int y=0; y<inventory.getSize()/ITEM_WIDTH; y++) {
				itemStackGuis[x + y*ITEM_WIDTH] = new ItemStackGui(STARTX + x*ItemStackGui.SIZE, starty + y*ItemStackGui.SIZE);
			}
		}
	}
	

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {

		for(int i=0; i<inventory.getSize(); i++) {
			itemStackGuis[i].draw(sb, sr, inventory.getItemStack(i));
		}
		
		
		if(invAction == InvAction.CRAFT) {
			craftGui.draw(sb, sr, delta);
		}
		else if(invAction == InvAction.CHEST) {
			chestGui.draw(sb, sr, delta);
		}
		
		sb.begin();
		if(heldItemStack.count > 0) {
			sb.draw(Item.getItemTexture(heldItemStack.getId()), heldItemStackPos.x - heldItemStackOffset.x, heldItemStackPos.y - heldItemStackOffset.y, 
					ItemStackGui.SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, ItemStackGui.SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
			Terrafying.guifont.draw(sb, String.valueOf(heldItemStack.count),
					heldItemStackPos.x - heldItemStackOffset.x,
					heldItemStackPos.y - heldItemStackOffset.y + GuiManager.MARGIN * GuiManager.HUD_SCALE + Terrafying.guifont.getCapHeight() * Terrafying.guifont.getScaleY());
			
		}
				
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
		heldItemStackPos.set(x, y);
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		int idx = getClickedItemStack(x, y);
		if(idx == -1) {
			if(invAction == InvAction.CRAFT) {
				craftGui.mouseDown(x, y, button);
			}
			else if(invAction == InvAction.CHEST) {
				metaDirty = chestGui.mouseDown(x, y, button, this);
			}
			return;
		}
		if(button == Input.Buttons.LEFT) {
			if(heldItemStack.count == 0)
				heldItemStackOffset = itemStackGuis[idx].getClickOffset(x, y);
			heldItemStack = inventory.changeItemStack(idx, heldItemStack);
			System.out.println(heldItemStack);
		}
		else if(button == Input.Buttons.RIGHT) {
			if(heldItemStack.count == 0) {
				heldItemStackOffset = itemStackGuis[idx].getClickOffset(x, y);
				heldItemStack = inventory.takeItem(idx, inventory.getItemStack(idx).count/2);
			}
			else if(heldItemStack.count > 0) {
				ItemStack result = inventory.addItem(idx, heldItemStack.takeItem(1));
				if(result.count > 0)
					heldItemStack.addItem(result);
			}
		}
		
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}
	
	public int getClickedItemStack(int x, int y) {
		for(int i=0; i<itemStackGuis.length; i++) {
			if(itemStackGuis[i].contains(x, y)) {
				return i;
			}
		}
		return -1;
	}


	@Override
	public void scrolled(float amount) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateCraftGui() {
		craftGui.updateCraftableItems();
	}
}
