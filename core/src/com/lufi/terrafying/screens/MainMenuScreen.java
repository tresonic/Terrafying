package com.lufi.terrafying.screens;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.net.TerrafyingServer;
import com.lufi.terrafying.world.MapLoaderSaver;



public class MainMenuScreen implements Screen {
	private final Game game;
	
	private Stage stage;
	
	private VerticalGroup root;
	
	private Image logoImage;
	private Label titleLabel;
	private Label nameLabel;
	private TextField nameField;
	private Label spaceLabel;	
	private TextButton joinButton;
	private TextField joinIpField;
	private SelectBox<String> mapSelectBox;
	private TextButton hostButton;
	private TextButton newMapButton;
	private TextButton exitButton;
	
	private Skin skin;
	
	
	public MainMenuScreen(final Game g) {
		game = g;
	}
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		root = new VerticalGroup();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		titleLabel = new Label("Terrafying", skin);
		titleLabel.setFontScale(2.f);
		root.addActor(titleLabel);
		
//		logoImage = new Image(new Texture(Gdx.files.internal("icon2.png")));
//		logoImage.setSize(512, 512);
//		root.addActor(logoImage);
		
		nameLabel = new Label("Enter Player Name:", skin);
		root.addActor(nameLabel);
		nameField = new TextField("", skin);
		nameField.setText(String.valueOf((char)ThreadLocalRandom.current().nextInt(65, 91)));
		root.addActor(nameField);
		spaceLabel = new Label("", skin);
		root.addActor(spaceLabel);
		
		HorizontalGroup jGrp = new HorizontalGroup();
		joinButton = new TextButton("Join", skin);
		joinButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if(!nameField.getText().isEmpty())
					game.setScreen(new LoadingScreen(game, nameField.getText(), joinIpField.getText()));
			}
		});
		joinIpField = new TextField("", skin);
		
		new Thread("discover") {
			@Override
			public void run() {
				String s = new String();
				try {
					s = TerrafyingClient.discoverServer();
				} catch (Exception e) {}
				if(!s.isEmpty())
					joinIpField.setText(s);
			}
		}.start();
		
		jGrp.addActor(joinIpField);
		jGrp.addActor(joinButton);
		root.addActor(jGrp);
		
		
		HorizontalGroup hGrp = new HorizontalGroup();
		mapSelectBox = new SelectBox<String>(skin);
		String maps[] = MapLoaderSaver.getAvailableMaps();
		System.out.println(maps.length);
		if(maps.length != 0)
			mapSelectBox.setItems(maps);
		else
			mapSelectBox.setItems("no maps found...");
		hGrp.addActor(mapSelectBox);
		
		hostButton = new TextButton("Host", skin);
		hostButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if(!nameField.getText().isEmpty() && mapSelectBox.getSelected() != "no maps found...") {
					TerrafyingServer.the().start(mapSelectBox.getSelected());
					game.setScreen(new LoadingScreen(game, nameField.getText(), "127.0.0.1"));
				}
			}
		});
		hGrp.addActor(hostButton);
		root.addActor(hGrp);
		
		newMapButton = new TextButton("Create New Map", skin);
		newMapButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new NewMapScreen(game, skin));
			}
		});
		root.addActor(newMapButton);
		
		exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		root.addActor(exitButton);
		
		root.align(Align.center);
		root.setFillParent(true);
		root.fill();

		stage.addActor(root);
	}
	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0,0,0,1);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int x, int y) {
		stage.getViewport().update(x, y, true);
	}
	
	@Override
	public void pause() {
		
	}
	
	@Override
	public void resume() {
		
	}
	
	@Override
	public void hide() {
		
	}
	
	@Override
	public void dispose() {
		
	}
}
