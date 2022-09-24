package xyz.solodyankin.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerWeapon extends Drawable {

    TextureRegion[] shootFrames;

    PlayerWeapon(String shootSpritesheetPath) {
        Texture shootSheet = new Texture(shootSpritesheetPath);
        TextureRegion[][] tmp = TextureRegion.split(shootSheet, shootSheet.getWidth() / 3, shootSheet.getHeight());

        this.width = shootSheet.getWidth() / 3.f;
        this.height = shootSheet.getHeight();

        int index = 0;
        shootFrames = new TextureRegion[3];
        for(TextureRegion[] x : tmp) {
            for(TextureRegion y : x) {
                shootFrames[index++] = y;
            }
        }

    }

}
