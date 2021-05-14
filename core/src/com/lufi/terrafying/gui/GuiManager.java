package com.lufi.terrafying.gui;

import java.util.LinkedList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lufi.terrafying.screens.GameScreen;

public class GuiManager implements InputProcessor {
	public static Color backColor = Color.GRAY;
	public static Color frontColor = Color.LIGHT_GRAY;
	public static Color selectColor = Color.RED;
	public static final int MARGIN = 3;
	
	public static final float HUD_SCALE = 3;
	
	private GameScreen gameScreen; 
	private BaseGui currentGui;
	private Hotbar hotbar;
	
	private boolean guiActive;

	
	public GuiManager(GameScreen nGameScreen) {
		gameScreen = nGameScreen;
		guiActive = false;
		hotbar = new Hotbar(gameScreen.world.player.inventory, 9);
	}
	
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {	
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
		} else if(!guiActive) {
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
			
		} else {
			currentGui.mouseDown(screenX, Gdx.graphics.getHeight() - screenY, button, gameScreen.hudCamera);
		}
			
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
