package com.lufi.terrafying.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class BaseGui {
	public abstract void draw(SpriteBatch sb, ShapeRenderer sr, float delta);
	public abstract void keyDown(int keycode);
	public abstract void keyUp(int keycode);
	public abstract void mouseMoved(int x, int y);
	public abstract void mouseDown(int x, int y, int button, OrthographicCamera cam);
	public abstract void mouseUp(int x, int y, int button);
}
