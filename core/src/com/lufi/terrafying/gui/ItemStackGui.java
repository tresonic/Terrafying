package com.lufi.terrafying.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.world.Block;

public class ItemStackGui {
	public static final float size = (Block.BLOCK_SIZE + GuiManager.MARGIN * 2) * GuiManager.HUD_SCALE;
	
	public Vector2 pos;
	
	public boolean clicked;
	
	public ItemStackGui(float x, float y) {
		pos = new Vector2(x, y);
	}
	
	public void draw(SpriteBatch sb, ShapeRenderer sr, ItemStack is) {
		sr.set(ShapeType.Filled);
		sr.setColor(clicked ? Color.RED : GuiManager.backColor);
		sr.rect(pos.x, pos.y, size, size);
		sr.setColor(GuiManager.frontColor);
		sr.rect(pos.x + GuiManager.MARGIN*GuiManager.HUD_SCALE, pos.y + GuiManager.MARGIN*GuiManager.HUD_SCALE,
				size - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, size - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
		
	}
	
	public boolean contains(float x, float y) {
		return (pos.x <= x && x <= pos.x + size && pos.y <= y && y <= pos.y + size);
	}
}
