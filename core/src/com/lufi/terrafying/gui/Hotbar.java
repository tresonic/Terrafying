package com.lufi.terrafying.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.gui.InventoryGui.InvAction;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.screens.DeathScreen;
import com.lufi.terrafying.screens.GameScreen;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.Map;

public class Hotbar extends BaseGui {
	private Inventory inventory;

	private ItemStackGui itemStackGuis[];
	private int numSlots;
	private int selectedSlot;

	private boolean digPressed;
	private boolean digging;
	private Vector2i digPos;
	private float curDigTime;
	private float digTime;

	private boolean using;

	public Hotbar(Inventory nInventory, int nNumSlots) {
		inventory = nInventory;
		numSlots = nNumSlots;
		digging = false;
		using = false;
		digPos = new Vector2i();
		itemStackGuis = new ItemStackGui[nNumSlots];
		for (int i = 0; i < numSlots; i++) {
			itemStackGuis[i] = new ItemStackGui(i * ItemStackGui.SIZE, 0);
		}
		selectedSlot = 0;
	}

	public void draw(SpriteBatch sb, ShapeRenderer sr, GameScreen gameScreen, float delta) {
		sb.setProjectionMatrix(gameScreen.camera.combined);
		sb.begin();
		if (digging) {
			int crack_idx = (int) (curDigTime / digTime * 9);
			TextureRegion tex = new TextureRegion(Terrafying.assetManager.get("crack.png", Texture.class), 0,
					crack_idx * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			sb.draw(tex, digPos.x * Block.BLOCK_SIZE, digPos.y * Block.BLOCK_SIZE);
			curDigTime += delta;
		}
		sb.end();
		sb.setProjectionMatrix(gameScreen.hudCamera.combined);

		for (int i = 0; i < itemStackGuis.length; i++) {
			itemStackGuis[i].selected = i == selectedSlot;
			itemStackGuis[i].draw(sb, sr, inventory.getItemStack(i));
		}

		int healthPoints = gameScreen.world.player.getHealth();
		int healthWidth = Terrafying.assetManager.get("heart1.png", Texture.class).getWidth();
		int healthHeight = Terrafying.assetManager.get("heart1.png", Texture.class).getHeight();
		sb.begin();
		for (int i = 0; i < 10; i++) {
			if (i <= healthPoints)
				sb.draw(Terrafying.assetManager.get("heart1.png", Texture.class),
						GuiManager.WIDTH - (i + 1) * healthWidth * GuiManager.HUD_SCALE - (i + 1) * 3,
						GuiManager.SCALED_MARGIN, healthWidth * GuiManager.HUD_SCALE,
						healthHeight * GuiManager.HUD_SCALE);
			else
				sb.draw(Terrafying.assetManager.get("heart0.png", Texture.class),
						GuiManager.WIDTH - (i + 1) * healthWidth * GuiManager.HUD_SCALE - (i + 1) * 3,
						GuiManager.SCALED_MARGIN, healthWidth * GuiManager.HUD_SCALE,
						healthHeight * GuiManager.HUD_SCALE);
		}
		sb.end();

		if (healthPoints <= 0) {
			gameScreen.world.map.updateSpawnPoint();
			gameScreen.game.setScreen(new DeathScreen(gameScreen.game));

		}

	}

	public void update(Vector2 wpos, Vector2i mpos, GuiManager guiManager, EntityManager entityManager, Map map,
			TerrafyingClient client) {
		int idx = getClickedItem(mpos);

		if (idx > -1) {
			digging = false;
			using = false;
			if (digPressed)
				selectedSlot = idx;
			return;
		}

		if (digPressed) {
			int bId = map.getBlockAt(wpos.x, wpos.y);
			if (!Block.getBlockById(bId).getMineable()) {
				digging = false;
				curDigTime = 0;
				return;
			}
			digging = true;
			Vector2i newDigPos = Map.getBlockPos(wpos.x, wpos.y);

			if (!digPos.equals(newDigPos)) {
				if (inventory.getItemStack(selectedSlot).item != null && (Block.getBlockById(bId)
						.getMineType() == inventory.getItemStack(selectedSlot).item.getMineType()
						|| inventory.getItemStack(selectedSlot).item.getMineType() == Block.MineType.NONE))
					digTime = Block.getBlockById(map.getBlockAt(wpos.x, wpos.y)).getMineTime()
							* inventory.getItemStack(selectedSlot).item.getMineFactor();
				else
					digTime = Block.getBlockById(map.getBlockAt(wpos.x, wpos.y)).getMineTime();
				curDigTime = 0;

				digPos = newDigPos;
			} else if (curDigTime >= digTime) {
				if (map.setBlockAt(wpos.x, wpos.y, Block.getBlockByName("air").getId())) {
					Item i = Item.getItemById(bId);
					inventory.addItem(new ItemStack(i, 1));
					client.sendBlockUpdate(wpos.x, wpos.y);
				}

				curDigTime = 0;
				digging = false;

			}
		}

		if (using) {
			ItemStack wieldItem = inventory.getItemStack(selectedSlot);

			if (Block.getBlockById(map.getBlockAt(wpos.x, wpos.y)).getHasMeta() && !map.getMetaLockAt(wpos)) {
				using = false;
				guiManager.invGui.invAction = InvAction.CHEST;
				guiManager.invGui.chestGui.setChestInv(map.getMetaAt(wpos));
				guiManager.guiActive = true;
				guiManager.currentGui = guiManager.invGui;
				guiManager.currentMetaPos.set(wpos);
				map.setMetaLockAt(wpos, true);
				client.sendMetaLock(wpos.x, wpos.y, true);
				return;
			}

			if (wieldItem.count != 0) {
				Vector2i pos = Map.getBlockPos(wpos.x, wpos.y);
				if (wieldItem.item.getBlockItem()
						&& Block.getBlockById(map.getBlockAt(wpos.x, wpos.y)).getName() == "air"
						&& !entityManager.isEntityInBlock(pos.x, pos.y)) {
					boolean hasNeighbor = false;
					for (int x1 = -1; x1 < 2; x1++) {
						for (int y1 = -1; y1 < 2; y1++) {
							if (Block.getBlockById(map.getBlock(pos.x + x1, pos.y + y1)).getCollidable()) {
								hasNeighbor = true;
							}
						}
					}

					if (hasNeighbor) {
						map.setBlockAt(wpos.x, wpos.y, wieldItem.item.getId());
						if (Block.getBlockById(wieldItem.item.getId()).getHasMeta())
							using = false;
						wieldItem.count--;
						client.sendBlockUpdate(wpos.x, wpos.y);
					}
				}
			}
		}
	}

	public int getClickedItem(Vector2i mpos) {
		for (int i = 0; i < itemStackGuis.length; i++) {
			if (itemStackGuis[i].contains(mpos.x, mpos.y)) {
				return i;
			}
		}
		return -1;
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
			digPressed = true;
		} else if (button == Input.Buttons.RIGHT) {
			using = true;
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		if (button == Input.Buttons.LEFT) {
			digPressed = false;
			digging = false;
			curDigTime = 0;
		} else if (button == Input.Buttons.RIGHT)
			using = false;

	}

	public ItemStack getSelectedItem() {
		return inventory.getItemStack(selectedSlot);
	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrolled(float amount) {
		selectedSlot += Math.signum(amount);
		selectedSlot %= numSlots;
		if (selectedSlot < 0)
			selectedSlot = numSlots - 1;
	}

}
