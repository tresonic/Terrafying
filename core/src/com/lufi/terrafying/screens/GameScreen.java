package com.lufi.terrafying.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.gui.GuiManager;
import com.lufi.terrafying.net.TerrafyingClient;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.net.TerrafyingServer;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.World;

public class GameScreen implements Screen {
	public final static int viewPortWidth = 50 * Block.BLOCK_SIZE;
	public final Terrafying game;
	
	public TerrafyingClient client;
	public World world;
	public OrthographicCamera camera;
	public OrthographicCamera hudCamera;
	public GuiManager guiManager;
	
	
	public SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	private ShaderProgram lightShader;
	
	private String name, ip;
	
	private float lastTime;
	
	public GameScreen(final Terrafying g, String playerName, String serverAddress) {
		game = g;
		name = playerName;
		ip = serverAddress;
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		world = new World(500, 500);
		client = new TerrafyingClient(world, game.options);
		client.connect(name, ip);
		// world.map.generate();
		
		lastTime = 0;
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));
		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudCamera.position.x = Gdx.graphics.getWidth()/2;
		hudCamera.position.y = Gdx.graphics.getHeight()/2;
		hudCamera.update();
		camera.position.set(world.map.spawnpoint.x, world.map.spawnpoint.y, 0);
		
		lightShader = new ShaderProgram(Gdx.files.internal("vertex.glsl"), Gdx.files.internal("fragment.glsl"));
		spriteBatch.setShader(lightShader);
	}
	
	
	@Override
	public void show() {
		camera.position.x = world.player.posx;
		camera.position.y = world.player.posy;
		guiManager = new GuiManager(this);
		Gdx.input.setInputProcessor(guiManager);
	}

	@Override
	public void render(float delta) {
		if(client.connected == false) {
			game.setScreen(new MainMenuScreen(game));
		}
		
		camera.zoom = game.options.getViewingrange();
		
		ScreenUtils.clear(104 / 255.0f, 205 / 255.0f, 1, 1);
		
		spriteBatch.setShader(lightShader);
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		world.render(delta, camera, shapeRenderer, spriteBatch);
		spriteBatch.setShader(null);
		
		hudCamera.update();
		spriteBatch.setProjectionMatrix(hudCamera.combined);
		shapeRenderer.setProjectionMatrix(hudCamera.combined);
		guiManager.draw(spriteBatch, shapeRenderer, delta);
		
		//System.out.println(Gdx.input.getInputProcessor());
		//System.out.println(Gdx.graphics.getFramesPerSecond());
		
		lastTime += delta;
		if(lastTime >= 0.02f) {
			client.update(camera);
			lastTime = 0;
		}
	}
	

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = viewPortWidth;                 // Viewport of 30 units!
		camera.viewportHeight = viewPortWidth * height/width; // Lets keep things in proportion.
		camera.update();
		
//		hudCamera.viewportWidth = 1280f;
//		hudCamera.viewportHeight = 720f;
//		hudCamera.update();
		
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
		
		client.disconnect();
		TerrafyingServer.the().stop();
	}
	
	public boolean isConnected() {
		return client.connected;
	}

	public boolean isConnecting() {
		return client.connecting;
	}
}
