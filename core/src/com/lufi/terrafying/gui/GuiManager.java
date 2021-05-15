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
	
	private Vector2 mpos;
	
	private boolean guiActive;

	
	public GuiManager(GameScreen nGameScreen) {
		gameScreen = nGameScreen;
		guiActive = false;
		hotbar = new Hotbar(gameScreen.world.player.inventory, 9);
		mpos = new Vector2();
	}
	
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {	
		if(guiActive && currentGui != null)
			currentGui.draw(sb, sr, delta);
		else
			hotbar.draw(sb, sr, delta);
		
//		sr.begin();
//		sr.setColor(Color.RED);
//		sr.circle(mpos.x, mpos.y, 3);
//		sr.end();
	}
	
	public void resized(int width, int height) {
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
			Vector3 wpos = gameScreen.camera.unproject(new Vector3(screenX, screenY, 0));
			if(button == Input.Buttons.LEFT)
				gameScreen.world.player.wield(wpos.x, wpos.y, gameScreen.world.map, hotbar.getSelectedItem());
			else if(button == Input.Buttons.RIGHT)
				gameScreen.world.player.use(wpos.x, wpos.y, gameScreen.world.map, hotbar.getSelectedItem());
		} else {
			currentGui.mouseDown((int)mpos.x, (int)mpos.y, button);
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
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mpos.set(gameScreen.hudCamera.unproject(new Vector3(screenX, screenY, 0)).x, gameScreen.hudCamera.unproject(new Vector3(screenX, screenY, 0)).y);
		if(!guiActive) {
			
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
