package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.world.LoaderSaver;

public class OptionsScreen implements Screen {

	private Terrafying game;
	
	
	private Stage stage;	
	private Table root;
	
	Label viewingrangeValue;
	final Slider viewingrangeSlider;
	
	public OptionsScreen(final Terrafying nGame) {
		game = nGame;
		viewingrangeSlider = new Slider(0.5f, 1.5f, 0.05f, false, game.skin);
	}
	
	@Override
	public void show() {
		
		stage = new Stage();
		root = new Table(game.skin);
		
		
		Label titleLabel = new Label("Options", game.skin);
		titleLabel.setFontScale(2);
		root.add(titleLabel).spaceBottom(20).colspan(3);
		root.row();
		

		Label viewingrangeLabel = new Label("Zoom", game.skin);
		root.add(viewingrangeLabel).padRight(5);
		
		//public Slider (float min, float max, float stepSize, boolean vertical, Skin skin)
		viewingrangeSlider.setValue(game.options.getViewingrange());
		viewingrangeSlider.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.options.setViewingrange(viewingrangeSlider.getValue());
			}
			
		});
		root.add(viewingrangeSlider);
		
		
		viewingrangeValue = new Label(String.valueOf(Math.round((viewingrangeSlider.getValue() - 0.5) * 100)) + "%", game.skin);
		root.add(viewingrangeValue).padLeft(5).width(20);
		root.row();
		
		
		TextButton saveButton = new TextButton("save options", game.skin);
		saveButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Options.saveOptions(game.options);			
			}
		});
		root.add(saveButton).spaceTop(50).colspan(3);
		root.row();
		
		
		TextButton backButton = new TextButton("back", game.skin);
		backButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.pauseScreen);			
			}
		});
		root.add(backButton).spaceTop(5).colspan(3);
		
		
		
		
		root.setFillParent(true);
		stage.addActor(root);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		viewingrangeValue.setText(String.valueOf(Math.round((viewingrangeSlider.getValue() - 0.5) * 100)) + "%");
		ScreenUtils.clear(0,0,0,1);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}