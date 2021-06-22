package com.lufi.terrafying.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.items.Recipe;
import com.lufi.terrafying.world.Block;

public class ItemStackGui {
	public static final float SIZE = (Block.BLOCK_SIZE + GuiManager.MARGIN * 2) * GuiManager.HUD_SCALE;
	
	public Vector2 pos;
	public boolean selected;
	
	public ItemStackGui(float x, float y) {
		pos = new Vector2(x, y);
		selected = false;
	}
	
	public void draw(SpriteBatch sb, ShapeRenderer sr, ItemStack is) {
		sr.begin();
		sr.set(ShapeType.Filled);
		sr.setColor(selected ? Color.RED : GuiManager.backColor);
		sr.rect(pos.x, pos.y, SIZE, SIZE);
		sr.setColor(GuiManager.frontColor);
		sr.rect(pos.x + GuiManager.MARGIN*GuiManager.HUD_SCALE, pos.y + GuiManager.MARGIN*GuiManager.HUD_SCALE,
				SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
		sr.end();
		
		if(is.count > 0) {
			sb.begin();
			sb.draw(Item.getItemTexture(is.getId()), pos.x + GuiManager.MARGIN * GuiManager.HUD_SCALE, pos.y + GuiManager.MARGIN * GuiManager.HUD_SCALE,
					SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
			Terrafying.guifont.draw(sb, String.valueOf(is.count), pos.x + GuiManager.MARGIN * GuiManager.HUD_SCALE, 
					pos.y + Terrafying.guifont.getCapHeight() * Terrafying.guifont.getScaleY() + 2 * GuiManager.MARGIN * GuiManager.HUD_SCALE);
//			Terrafying.guifont.draw(sb, is.getName(), pos.x + SIZE / 2, 
//					pos.y + Terrafying.guifont.getCapHeight() * Terrafying.guifont.getScaleY() + 2 * GuiManager.MARGIN * GuiManager.HUD_SCALE);

			sb.end();
		}
	}
//	
//	public void drawCrafting(SpriteBatch sb, ShapeRenderer sr, ItemStack is) {
//		craftResult = Recipe.getRecipe(is);
//		sr.begin();
//		sr.set(ShapeType.Filled);
//		sr.setColor(selected ? Color.RED : GuiManager.backColor);
//		sr.rect(pos.x, pos.y, SIZE, SIZE);
//		sr.setColor(GuiManager.frontColor);
//		sr.rect(pos.x + GuiManager.MARGIN*GuiManager.HUD_SCALE, pos.y + GuiManager.MARGIN*GuiManager.HUD_SCALE,
//				SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
//		sr.end();
//		
//		if(is.count > 0) {
//			sb.begin();
//			sb.draw(Item.getItemTexture(is.getId()), pos.x + GuiManager.MARGIN * GuiManager.HUD_SCALE, pos.y + GuiManager.MARGIN * GuiManager.HUD_SCALE,
//					SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE, SIZE - GuiManager.MARGIN*2*GuiManager.HUD_SCALE);
//			Terrafying.guifont.draw(sb, String.valueOf("x" craftResult), pos.x + 30 + GuiManager.MARGIN * GuiManager.HUD_SCALE, 
//					pos.y + Terrafying.guifont.getCapHeight() * Terrafying.guifont.getScaleY() + 2 * GuiManager.MARGIN * GuiManager.HUD_SCALE);
////			Terrafying.guifont.draw(sb, is.getName(), pos.x + SIZE / 2, 
////					pos.y + Terrafying.guifont.getCapHeight() * Terrafying.guifont.getScaleY() + 2 * GuiManager.MARGIN * GuiManager.HUD_SCALE);
//
//			sb.end();
//		}
//	}

	
	public boolean contains(float x, float y) {
		return (pos.x <= x && x <= pos.x + SIZE && pos.y <= y && y <= pos.y + SIZE);
	}
	
	public Vector2 getClickOffset(float x, float y) {
		return new Vector2(x - pos.x - GuiManager.MARGIN * GuiManager.HUD_SCALE, y - pos.y - GuiManager.MARGIN * GuiManager.HUD_SCALE);
	}
}
