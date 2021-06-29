package com.lufi.terrafying.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.util.Options;

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
		viewingrangeSlider = new Slider(0.5f, 1.5f, 0.05f, false, Terrafying.skin);
		currentAction = ACTIONS.None;
		multiplexer = new InputMultiplexer();
	}
	
	@Override
	public void show() {
		
		stage = new Stage();
		root = new Table(Terrafying.skin);
		
		
		Label titleLabel = new Label("Options", Terrafying.skin);
		titleLabel.setFontScale(2);
		root.add(titleLabel).spaceBottom(20).colspan(3);
		root.row();
		

		Label viewingrangeLabel = new Label("FOV", Terrafying.skin);
		root.add(viewingrangeLabel).padRight(5);
		
		//public Slider (float min, float max, float stepSize, boolean vertical, Skin skin)
		viewingrangeSlider.setValue(game.options.getViewingrange());
		viewingrangeSlider.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.options.setViewingrange(viewingrangeSlider.getValue());
			}
			
		});
		root.add(viewingrangeSlider);
		
		
		viewingrangeValue = new Label(String.valueOf(Math.round((viewingrangeSlider.getValue() - 0.5) * 100)) + "%", Terrafying.skin);
		root.add(viewingrangeValue).padLeft(5).width(20);
		root.row();
		
		
		Label controlsLabel = new Label("Controls", Terrafying.skin);
		controlsLabel.setFontScale(2);
		root.add(controlsLabel).spaceBottom(10).colspan(3);
		root.row();
		
		Label leftLabel = new Label("Move left", Terrafying.skin);
		
		root.add(leftLabel).spaceBottom(10);
		
		
		leftButton = new TextButton(Input.Keys.toString(game.options.getKeyLeft()), Terrafying.skin);
		leftButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Left;	
				leftButton.setText("...");
			}
		});
		root.add(leftButton).spaceBottom(10);
		root.row();
		
		Label rightLabel = new Label("Move right", Terrafying.skin);
		
		root.add(rightLabel).spaceBottom(10);
		
		
		rightButton = new TextButton(Input.Keys.toString(game.options.getKeyRight()), Terrafying.skin);
		rightButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Right;	
				rightButton.setText("...");
			}
		});
		root.add(rightButton).spaceBottom(10);
		root.row();
		
		Label jumpLabel = new Label("Jump", Terrafying.skin);
		
		root.add(jumpLabel).spaceBottom(10);
		
		
		jumpButton = new TextButton(Input.Keys.toString(game.options.getKeyJump()), Terrafying.skin);
		jumpButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Jump;	
				jumpButton.setText("...");
			}
		});
		root.add(jumpButton).spaceBottom(10);
		root.row();
		
		Label downLabel = new Label("Down", Terrafying.skin);
		
		root.add(downLabel).spaceBottom(10);
		
		
		downButton = new TextButton(Input.Keys.toString(game.options.getKeyDown()), Terrafying.skin);
		downButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Down;	
				downButton.setText("...");
			}
		});
		root.add(downButton).spaceBottom(10);
		root.row();
		
		
		Label invLabel = new Label("Open Inventory", Terrafying.skin);
		
		root.add(invLabel).spaceBottom(10);
		
		
		invButton = new TextButton(Input.Keys.toString(game.options.getKeyInv()), Terrafying.skin);
		invButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				currentAction = ACTIONS.Inv;	
				invButton.setText("...");
			}
		});
		root.add(invButton).spaceBottom(10);
		root.row();
		
		
		
		TextButton saveButton = new TextButton("save & back", Terrafying.skin);
		saveButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Options.saveOptions(game.options);	
				game.setScreen(game.pauseScreen);
			}
		});
		root.add(saveButton).spaceTop(50).colspan(3);
		root.row();
		
		
		
		
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
			currentAction = ACTIONS.None;
			return true;
		}
		if(currentAction == ACTIONS.Right) {
			game.options.setKeyRight(keycode);
			rightButton.setText(Input.Keys.toString(game.options.getKeyRight()));
			currentAction = ACTIONS.None;
			return true;
		}if(currentAction == ACTIONS.Jump) {
			game.options.setKeyJump(keycode);
			jumpButton.setText(Input.Keys.toString(game.options.getKeyJump()));
			currentAction = ACTIONS.None;
			return true;
		}if(currentAction == ACTIONS.Down) {
			game.options.setKeyDown(keycode);
			downButton.setText(Input.Keys.toString(game.options.getKeyDown()));
			currentAction = ACTIONS.None;
			return true;
		}if(currentAction == ACTIONS.Up) {
			game.options.setKeyUp(keycode);
			upButton.setText(Input.Keys.toString(game.options.getKeyUp()));
			currentAction = ACTIONS.None;
			return true;
		}if(currentAction == ACTIONS.Inv) {
			game.options.setKeyInv(keycode);
			invButton.setText(Input.Keys.toString(game.options.getKeyInv()));
			currentAction = ACTIONS.None;
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