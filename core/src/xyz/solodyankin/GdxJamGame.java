package xyz.solodyankin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GdxJamGame extends Game implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	GameMap map;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Sprite player;

	boolean moveUp;
	boolean moveDown;
	boolean moveLeft;
	boolean moveRight;

	float camViewportHalfX;
	float camViewportHalfY;

	float mapWidth;
	float mapHeight;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		player = new Sprite(new Texture("player.png"));
		player.setPosition(64, 64);
		mapWidth = Gdx.graphics.getWidth();
		mapHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camViewportHalfX = mapWidth / 8.0f;
		camViewportHalfY = mapHeight / 8.0f;
		camera.setToOrtho(false, mapWidth , mapHeight);
		camera.update();

		GameMap map = new GameMap("SavannaTiled.tmx", mapWidth, mapHeight);
		map.generateBushes();
		map.generateNewMapLayer();
		//tiledMap = new TmxMapLoader().load("SavannaTiled.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map.tiledMap);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		batch.begin();
		player.draw(batch);

		if(moveUp) {
			camera.translate(0, 32 * Gdx.graphics.getDeltaTime());
			//camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth + camViewportHalfX);
			camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY, mapHeight - camViewportHalfY);
		}

		if(moveDown) {
			camera.translate(0, -32 * Gdx.graphics.getDeltaTime());
			//camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth + camViewportHalfX);
			camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY, mapHeight - camViewportHalfY);
		}

		if(moveRight) {
			camera.translate(32 * Gdx.graphics.getDeltaTime(), 0);
			camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
			//camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY, mapHeight + camViewportHalfY);
		}

		if(moveLeft) {
			camera.translate(-32 * Gdx.graphics.getDeltaTime(), 0);
			camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
			//camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY + 1, mapHeight + camViewportHalfY);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.A)
			moveLeft = false;
		if(keycode == Input.Keys.D)
			moveRight = false;
		if(keycode == Input.Keys.W)
			moveUp = false;
		if(keycode == Input.Keys.S)
			moveDown = false;
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.A)
			moveLeft = true;
			//camera.translate(-32 * Gdx.graphics.getDeltaTime(), 0);
		if(keycode == Input.Keys.D)
			moveRight = true;
			//camera.translate(32 * Gdx.graphics.getDeltaTime(), 0);
		if(keycode == Input.Keys.W)
			moveUp = true;
			//camera.translate(0, 32 * Gdx.graphics.getDeltaTime());
		if(keycode == Input.Keys.S)
			moveDown = true;
			//camera.translate(0, -32 * Gdx.graphics.getDeltaTime());
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
