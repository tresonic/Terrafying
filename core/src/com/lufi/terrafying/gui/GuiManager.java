package com.lufi.terrafying.gui;

import java.util.LinkedList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.lufi.terrafying.screens.GameScreen;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block;

public class GuiManager implements InputProcessor {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	public static Color backColor = Color.GRAY;
	public static Color frontColor = Color.LIGHT_GRAY;
	public static Color selectColor = Color.RED;
	public static final int MARGIN = 3;
	
	public static final float HUD_SCALE = 3;
	
	private GameScreen gameScreen; 
	private BaseGui currentGui;
	private Hotbar hotbar;
	
	private Vector2i mpos;
	private Vector2 wpos;
	
	private boolean guiActive;

	
	public GuiManager(GameScreen nGameScreen) {
		gameScreen = nGameScreen;
		guiActive = false;
		hotbar = new Hotbar(gameScreen.world.player.inventory, 9);
		mpos = new Vector2i();
		wpos = new Vector2();
	}
	
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {	
		hotbar.update(wpos, gameScreen.world.map, gameScreen.client);
				
		if(guiActive && currentGui != null)
			currentGui.draw(sb, sr, delta);
		else
			hotbar.draw(sb, sr, delta);
	}
	

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.E) {
			if(guiActive)
				currentGui = null;
			else
				currentGui = new InventoryGui(gameScreen.world.player.inventory);
			guiActive = !guiActive;
		} else if(guiActive) {
			currentGui.keyDown(keycode);
		} else if(keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9) {
			hotbar.keyDown(keycode);
		} else {
			gameScreen.world.player.keyDown(keycode);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		gameScreen.world.player.keyUp(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(!guiActive) {
			hotbar.mouseDown((int)wpos.x, (int)wpos.y, button);
			// Vector3 wpos = gameScreen.camera.unproject(new Vector3(screenX, screenY, 0));
//			if(button == Input.Buttons.LEFT)
//				gameScreen.world.map.setBlockAt(wpos.x, wpos.y, Block.getBlockByName("air").getId());

		} else {
			currentGui.mouseDown((int)mpos.x, (int)mpos.y, button);
		}
			
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(!guiActive) {
			hotbar.mouseUp((int)mpos.x, (int)mpos.y, button);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseMoved(screenX, screenY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 uVec = new Vector3(screenX, screenY, 0);
		gameScreen.hudCamera.unproject(uVec);
		mpos.set((int)uVec.x, (int)uVec.y);
		uVec.set(screenX, screenY, 0);
		gameScreen.camera.unproject(uVec);
		wpos.set(uVec.x, uVec.y);
	
		if(!guiActive) {
			hotbar.mouseMoved((int)wpos.x, (int)wpos.y);
		} else {
			currentGui.mouseMoved((int)mpos.x, (int)mpos.y);
		}
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
