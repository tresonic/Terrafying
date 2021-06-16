package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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

public class OptionsScreen implements Screen, InputProcessor {

	private Terrafying game;
	
	
	private Stage stage;	
	private Table root;
	
	Label viewingrangeValue;
	final Slider viewingrangeSlider;
	TextButton leftButton;
	TextButton rightButton;
	TextButton jumpButton;
	TextButton downButton;
	TextButton upButton;
	TextButton invButton;
	
	
	enum ACTIONS {
		Left, Right, Jump, Up, Down, Inv, None 
	}
	private ACTIONS currentAction;
	
	private InputMultiplexer multiplexer;
	
	public OptionsScreen(final Terrafying nGame) {
		game = nGame;
		viewingrangeSlider = new Slider(0.5f, 1.5f, 0.05f, false, game.skin);
		currentAction = ACTIONS.None;
		multiplexer = new InputMultiplexer();
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
		
		
		Label controlsLabel = new Label("Controls", game.skin);
		controlsLabel.setFontScale(2);
		root.add(controlsLabel).spaceBottom(10).colspan(3);
		root.row();
		
		Label leftLabel = new Label("Move left", game.skin);
		
		root.add(leftLabel).spaceBottom(10);
		
		
		leftButton = new TextButton(Input.Keys.toString(game.options.getKeyLeft()), game.skin);
		leftButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Left;	
				leftButton.setText("...");
			}
		});
		root.add(leftButton).spaceBottom(10);
		root.row();
		
		Label rightLabel = new Label("Move right", game.skin);
		
		root.add(rightLabel).spaceBottom(10);
		
		
		rightButton = new TextButton(Input.Keys.toString(game.options.getKeyRight()), game.skin);
		rightButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Right;	
				rightButton.setText("...");
			}
		});
		root.add(rightButton).spaceBottom(10);
		root.row();
		
		Label jumpLabel = new Label("Jump", game.skin);
		
		root.add(jumpLabel).spaceBottom(10);
		
		
		jumpButton = new TextButton(Input.Keys.toString(game.options.getKeyJump()), game.skin);
		jumpButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Jump;	
				jumpButton.setText("...");
			}
		});
		root.add(jumpButton).spaceBottom(10);
		root.row();
		
		Label downLabel = new Label("Down", game.skin);
		
		root.add(downLabel).spaceBottom(10);
		
		
		downButton = new TextButton(Input.Keys.toString(game.options.getKeyDown()), game.skin);
		downButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Down;	
				downButton.setText("...");
			}
		});
		root.add(downButton).spaceBottom(10);
		root.row();
		
		
		Label invLabel = new Label("Open Inventory", game.skin);
		
		root.add(invLabel).spaceBottom(10);
		
		
		invButton = new TextButton(Input.Keys.toString(game.options.getKeyRight()), game.skin);
		invButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Inv;	
				invButton.setText("...");
			}
		});
		root.add(invButton).spaceBottom(10);
		root.row();
		
		
		
		TextButton saveButton = new TextButton("save options", game.skin);
		saveButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Options.saveOptions(game.options);	
				game.setScreen(game.pauseScreen);
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
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
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

	@Override
	public boolean keyDown(int keycode) {
		if(currentAction == ACTIONS.Left) {
			game.options.setKeyLeft(keycode);
			leftButton.setText(Input.Keys.toString(game.options.getKeyLeft()));
			return true;
		}
		if(currentAction == ACTIONS.Right) {
			game.options.setKeyRight(keycode);
			rightButton.setText(Input.Keys.toString(game.options.getKeyRight()));
			return true;
		}if(currentAction == ACTIONS.Jump) {
			game.options.setKeyJump(keycode);
			jumpButton.setText(Input.Keys.toString(game.options.getKeyJump()));
			return true;
		}if(currentAction == ACTIONS.Down) {
			game.options.setKeyDown(keycode);
			downButton.setText(Input.Keys.toString(game.options.getKeyDown()));
			return true;
		}if(currentAction == ACTIONS.Up) {
			game.options.setKeyUp(keycode);
			upButton.setText(Input.Keys.toString(game.options.getKeyUp()));
			return true;
		}if(currentAction == ACTIONS.Inv) {
			game.options.setKeyInv(keycode);
			invButton.setText(Input.Keys.toString(game.options.getKeyInv()));
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
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