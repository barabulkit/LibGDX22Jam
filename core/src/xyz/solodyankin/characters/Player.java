package xyz.solodyankin.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Drawable {
    Animation<TextureRegion> walk;
    TextureRegion[] walkFrames;

    Animation<TextureRegion> idle;
    TextureRegion[] idleFrames;

    String currentAnimation;

    PlayerWeapon weapon;
    float weaponRotation = 0.f;

    public Player(String walkSheetPath, String idleSheetPath) {
        Texture walkSheet = new Texture(walkSheetPath);
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth()/12, walkSheet.getHeight());
        this.width = walkSheet.getWidth()/12.f;
        this.height = walkSheet.getHeight();
        int index = 0;
        walkFrames = new TextureRegion[12];
        for(TextureRegion[] x : tmp) {
            for(TextureRegion y : x) {
                walkFrames[index++] = y;
            }
        }
        walk = new Animation<>(0.1f, walkFrames);

        Texture idleSheet = new Texture(idleSheetPath);
        tmp = TextureRegion.split(idleSheet, idleSheet.getWidth()/12, walkSheet.getHeight());
        index = 0;
        idleFrames = new TextureRegion[12];
        for(TextureRegion[] x : tmp) {
            for(TextureRegion y : x) {
                idleFrames[index++] = y;
            }
        }
        idle = new Animation<>(0.1f, idleFrames);
        currentAnimation = "idle";

        weapon = new PlayerWeapon("Player/Spritesheets/player_weapon.png");
        weapon.setX(getX() + (getWidth() / 2));
        weapon.setY(getY());
    }

    public TextureRegion getFrameByTime(float stateTime) {
        if(currentAnimation.equals("idle"))
            return idle.getKeyFrame(stateTime, true);
        else
            return walk.getKeyFrame(stateTime, true);
    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public void draw(SpriteBatch batch, float flipHorizontal, float flipWeaponX, float flipWeaponY, float stateTime) {
        batch.draw(getFrameByTime(stateTime),
                getX(), getY(),
                getWidth()/2.0f, getHeight()/2.0f,
                getWidth(), getHeight(),
                flipHorizontal, 1.0f,
                0.f);

        weapon.setX(getX() + (getWidth() / 3) * flipHorizontal);

        batch.draw(weapon.shootFrames[0], weapon.getX(), getY(),
                getWidth()/3.0f, getHeight()/2.0f,
                weapon.getWidth(), weapon.getHeight(),
                flipWeaponX, flipWeaponY,
                weaponRotation);


    }

    public float getWeaponRotation() {
        return weaponRotation;
    }

    public void setWeaponRotation(float weaponRotation) {
        this.weaponRotation = weaponRotation;
    }

    public PlayerWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(PlayerWeapon weapon) {
        this.weapon = weapon;
    }
}
