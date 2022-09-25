package xyz.solodyankin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import xyz.solodyankin.characters.Bullet;
import xyz.solodyankin.characters.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class GdxJamGame extends Game implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	GameMap map;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	TiledMapTileLayer collisionLayer;
	Player player;

	boolean moveUp;
	boolean moveDown;
	boolean moveLeft;
	boolean moveRight;

	float camViewportHalfX;
	float camViewportHalfY;

	Pixmap cursor;

	float mapWidth;
	float mapHeight;

	float stateTime = 0.f;

	List<Bullet> bulletList;
	Texture bulletTexture;
	float flipPlayerX;
	float flipWeaponY;

	@Override
	public void create () {
		cursor = new Pixmap(Gdx.files.internal("Cursor/cursor.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0,0));
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		player = new Player("Player/Spritesheets/player_run.png",
				"Player/Spritesheets/player_idle.png");

		//bullet = new Bullet("Player/Spritesheets/bullet.png");
		bulletList = new ArrayList<>();
		bulletTexture = new Texture("Player/Spritesheets/bullet.png");

		player.setX(mapWidth/2);
		player.setY(mapHeight / 2);

		mapWidth = Gdx.graphics.getWidth();
		mapHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camViewportHalfX = mapWidth / 8.0f;
		camViewportHalfY = mapHeight / 8.0f;
		camera.setToOrtho(false, mapWidth / 4.0f, mapHeight / 4.0f);
		camera.update();

		GameMap map = new GameMap("SavannaTiled.tmx", mapWidth, mapHeight);
		map.generateBushes();
		collisionLayer = map.generateNewMapLayer();
		//tiledMap = new TmxMapLoader().load("SavannaTiled.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map.tiledMap);
		Gdx.input.setInputProcessor(this);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		stateTime += Gdx.graphics.getDeltaTime();

		batch.begin();
		flipPlayerX = 1.0f;

		if(moveUp) {
			float newPos = player.getY() + 32 * Gdx.graphics.getDeltaTime();
			if(!isBlocked(player.getX(), newPos + player.getHeight())) {
				player.setY(newPos);
				player.setY(MathUtils.clamp(player.getY(), 0, mapHeight - player.getHeight()));

				camera.position.set(camera.position.x, player.getY(), 0);
				camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY, mapHeight - camViewportHalfY);
			}
		}

		if(moveDown) {
			float newPos = player.getY() - 32 * Gdx.graphics.getDeltaTime();
			if(!isBlocked(player.getX(), newPos)) {
				player.setY(newPos);
				player.setY(MathUtils.clamp(player.getY(), 0, mapHeight - player.getHeight()));

				camera.position.set(camera.position.x, player.getY(), 0);
				camera.position.y = MathUtils.clamp(camera.position.y, camViewportHalfY, mapHeight - camViewportHalfY);
			}
		}

		if(moveRight) {
			float newPos = player.getX() + 32 * Gdx.graphics.getDeltaTime();
			if(!isBlocked(newPos + player.getWidth(), player.getY())) {
				player.setX(newPos);
				player.setX(MathUtils.clamp(player.getX(), 0, mapWidth - player.getWidth()));

				camera.position.set(player.getX(), camera.position.y, 0);
				camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
			}
		}

		if(moveLeft) {
			float newPos = player.getX() - 32 * Gdx.graphics.getDeltaTime();
			if(!isBlocked(newPos, player.getY())) {
				player.setX(newPos);
				player.setX(MathUtils.clamp(player.getX(), 0, mapWidth - player.getWidth()));

				camera.position.set(player.getX(), camera.position.y, 0);
				camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
			}
		}

		Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 vec2 = camera.unproject(vec);
		float x = vec2.x;
		float y = vec2.y;
		float playerX = player.getX();
		float playerY = player.getY();

		float degs = (float) Math.toDegrees(Math.atan2((playerY - y), (playerX - x)));

		float flipWeaponX;
		flipWeaponY = 1.0f;

		if(x < playerX) {
			flipPlayerX = -1.0f;
			flipWeaponX = -1.f;
		}
		else {
			flipWeaponX = -1.f;
			flipWeaponY = -1.f;
		}

		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		/*bulletList.removeIf(Bullet::update);

		for(Bullet bullet : bulletList) {
			//if(bullet.update()) bulletList.remove(bullet);
			bullet.draw(batch);
		}*/
		ListIterator<Bullet> iter = bulletList.listIterator();
		while(iter.hasNext()) {
			Bullet bullet = iter.next();
			if(bullet.update()) {
				iter.remove();
			}
			else bullet.draw(batch);
		}

		player.setWeaponRotation(degs);
		player.draw(batch, flipPlayerX, flipWeaponX, flipWeaponY, stateTime);

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		cursor.dispose();
		bulletTexture.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.A) {
			moveLeft = false;
		}
		if(keycode == Input.Keys.D) {
			moveRight = false;
		}
		if(keycode == Input.Keys.W) {
			moveUp = false;
		}
		if(keycode == Input.Keys.S) {
			moveDown = false;
		}

		if(!moveUp && !moveDown && !moveLeft && !moveRight) player.setCurrentAnimation("idle");
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.A) {
			moveLeft = true;
			player.setCurrentAnimation("walk");
		}
		if(keycode == Input.Keys.D) {
			moveRight = true;
			player.setCurrentAnimation("walk");
		}
		if(keycode == Input.Keys.W) {
			moveUp = true;
			player.setCurrentAnimation("walk");
		}
		if(keycode == Input.Keys.S) {
			moveDown = true;
			player.setCurrentAnimation("walk");
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			Vector3 vec2 = camera.unproject(vec);
			float x = vec2.x;
			float y = vec2.y;

			Vector2 mousePosition = new Vector2(x, y);
			Vector2 playerPosition = new Vector2(player.getX(), player.getY());
			Vector2 direction = mousePosition.sub(playerPosition).nor();

			Vector2 offsetVector = new Vector2(1, 0).rotateDeg(player.getWeaponRotation());

			float bulletX = (float) ((player.getX() + player.getWidth() / 3.0) + (16 * offsetVector.x * -1));
			float bulletY = (float) ((player.getY() + player.getHeight() / 2.0) + (8 * offsetVector.y * -1));

			bulletList.add(new Bullet(bulletTexture,
					bulletX,
					bulletY,
					direction));
		}
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

	private boolean isBlocked(float x, float y) {
		TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));

		if(cell != null)
			return cell.getTile().getProperties().containsKey("blocked");
		return false;
	}
}
