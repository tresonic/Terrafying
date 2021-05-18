package com.lufi.terrafying.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.Map;

public class Hotbar extends BaseGui {
	private Inventory inventory;

	private ItemStackGui itemStackGuis[];
	private int numSlots;
	private int selectedSlot;

	private boolean digging;
	private boolean using;

	public Hotbar(Inventory nInventory, int nNumSlots) {
		inventory = nInventory;
		numSlots = nNumSlots;
		digging = false;
		using = false;
		itemStackGuis = new ItemStackGui[nNumSlots];
		for (int i = 0; i < numSlots; i++) {
			itemStackGuis[i] = new ItemStackGui(i * ItemStackGui.SIZE, 0);
		}
		selectedSlot = 0;
	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		for (int i = 0; i < itemStackGuis.length; i++) {
			itemStackGuis[i].selected = i == selectedSlot;
			itemStackGuis[i].draw(sb, sr, inventory.getItemStack(i));
		}
	}

	public void update(Vector2 wpos, Map map, TerrafyingClient client) {
		if (digging) {
			int bId = map.getBlockAt(wpos.x, wpos.y);			
			if (Block.getBlockById(bId).getMineable()) {
				map.setBlockAt(wpos.x, wpos.y, Block.getBlockByName("air").getId());
				Item i = Item.getItemById(bId);
				inventory.addItem(new ItemStack(i, 1));
				client.sendBlockUpdate(wpos.x, wpos.y);
			}
		}

		if (using) {
			ItemStack wieldItem = inventory.getItemStack(selectedSlot);
			if (wieldItem.count != 0) {
				if (wieldItem.item.getBlockItem()
						&& Block.getBlockById(map.getBlockAt(wpos.x, wpos.y)).getName() == "air") {
					boolean hasNeighbor = false;
					Vector2i pos = Map.getBlockPos(wpos.x, wpos.y);
					for (int x1 = -1; x1 < 2; x1++) {
						for (int y1 = -1; y1 < 2; y1++) {
							if (Block.getBlockById(map.getBlock(pos.x + x1, pos.y + y1)).getCollidable()) {
								hasNeighbor = true;
							}
						}
					}

					if (hasNeighbor) {
						map.setBlockAt(wpos.x, wpos.y, wieldItem.item.getId());
						wieldItem.count--;
						client.sendBlockUpdate(wpos.x, wpos.y);
					}
				}
			}
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
		if (button == Input.Buttons.LEFT) {
			digging = true;
		}
		else if(button == Input.Buttons.RIGHT) {
			using = true;
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		if (button == Input.Buttons.LEFT)
			digging = false;
		else if(button == Input.Buttons.RIGHT)
			using = false;

	}

	public ItemStack getSelectedItem() {
		return inventory.getItemStack(selectedSlot);
	}

}
