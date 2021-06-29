package com.lufi.terrafying.gui;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.util.Vector2i;


//blindcoded(nearly)
public class SnakeGui extends BaseGui {

	Vector2i direction;
	Vector2i food;
	ArrayList<Vector2i> snake;

	private Options options;
	private float tickTime;
	private final float tickSpeed = 0.2f;
	
	private boolean dirChange;

	ThreadLocalRandom random = ThreadLocalRandom.current();

	public final int WIDTH = 16;
	public final int HEIGHT = 16;
	public final float SIZE = InventoryGui.WIDTH / WIDTH;

	public SnakeGui(Options nOptions) {
		dirChange = false;
		options = nOptions;
		snake = new ArrayList<Vector2i>();
		food = new Vector2i();
		direction = new Vector2i();
		restart();

	}

	public void restart() {
		snake.clear();
		snake.add(new Vector2i(random.nextInt(0, WIDTH), random.nextInt(0, HEIGHT)));
		placeFood();
		direction.set(0, 0);
	}

	public void placeFood() {
		boolean foodInSnake = false;
		do {
			foodInSnake = false;
			food.set(random.nextInt(0, WIDTH), random.nextInt(0, HEIGHT));
			for (int i = 0; i < snake.size() - 1; i++) {
				if (food.equals(snake.get(i))) {
					foodInSnake = true;
				}
			}
		} while (foodInSnake == true);
	}

	private void tick() {
		dirChange = false;
		snake.add(0, snake.get(0).clone().add(direction));
		snake.remove(snake.size() - 1);
		Vector2i head = snake.get(0);

		if (head.equals(food)) {
			snake.add(snake.get(snake.size() - 1).clone());
			placeFood();
		}

		boolean lost = false;
		if (head.x >= WIDTH || head.x < 0 || head.y >= HEIGHT || head.y < 0) {
			lost = true;
		}

		for (int i = 1; i < snake.size() - 1; i++) {
			if (head.equals(snake.get(i))) {
				lost = true;
			}

		}
		if (lost == true)
			restart();

	}

	@Override
	public void draw(SpriteBatch sb, ShapeRenderer sr, float delta) {
		sr.begin();
		sr.set(ShapeType.Filled);
		sr.setColor(GuiManager.backColor);
		sr.rect(InventoryGui.STARTX, GuiManager.HEIGHT / 10, InventoryGui.WIDTH, InventoryGui.WIDTH);
		sr.setColor(Color.GREEN);
		for (Vector2i vec : snake) {
			sr.rect(InventoryGui.STARTX + vec.x * SIZE, GuiManager.HEIGHT / 10 + vec.y * SIZE, SIZE, SIZE);
		}
		sr.setColor(Color.RED);
		sr.rect(InventoryGui.STARTX + food.x * SIZE, GuiManager.HEIGHT / 10 + food.y * SIZE, SIZE, SIZE);
		sr.end();

		tickTime += delta;
		if (tickTime >= tickSpeed) {
			tick();
			tickTime = 0;
		}

	}

	@Override
	public void keyDown(int keycode) {
		if (options.getKeyUp() == keycode && !dirChange) {
			if (direction.y != -1) {
				direction.y = 1;
				direction.x = 0;
				dirChange = true;
			}
		} else if (options.getKeyDown() == keycode && !dirChange) {
			if (direction.y != 1) {
				direction.y = -1;
				direction.x = 0;
				dirChange = true;
			}
		} else if (options.getKeyLeft() == keycode && !dirChange) {
			if (direction.x != 1) {
				direction.x = -1;
				direction.y = 0;
				dirChange = true;
			}
		} else if (options.getKeyRight() == keycode && !dirChange) {
			if (direction.x != -1) {
				direction.x = 1;
				direction.y = 0;
				dirChange = true;
			}
		}
	}

	@Override
	public void keyUp(int keycode) {

	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDown(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrolled(float amount) {
		// TODO Auto-generated method stub

	}

}
