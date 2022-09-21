package xyz.solodyankin.characters;

public class Drawable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float scale = 1.f;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width * scale;
    }

    public float getHeight() {
        return height * scale;
    }
}