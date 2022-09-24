package xyz.solodyankin.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Bullet extends Drawable {

    //Texture bullet;
    public Sprite bullet;
    Vector2 direction;

    public Bullet(Texture texture, float x, float y, Vector2 direction) {
        this.direction = direction;
        bullet = new Sprite(texture);
        bullet.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        bullet.draw(batch);
    }

    public void addBullet() {

    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public boolean update() {
        boolean res = false;
        if(bullet != null) {
            Vector2 newPos = new Vector2(bullet.getX(), bullet.getY()).add(direction.scl(1.f));
            if(newPos.x > 512) res = true;
            if(newPos.y > 512) res = true;
            if(newPos.x < 0) res = true;
            if(newPos.y < 0) res = true;
            bullet.setPosition(newPos.x, newPos.y);
        }
        return res;
    }
}
