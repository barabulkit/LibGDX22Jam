package xyz.solodyankin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import xyz.solodyankin.GdxJamGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 32 * 16;
		config.height = 32 * 16;
		new LwjglApplication(new GdxJamGame(), config);
	}
}
