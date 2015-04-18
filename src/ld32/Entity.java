package ld32;

import java.awt.Graphics;


public abstract class Entity {
    
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    private float x;
    private float y;
    
    public void update(float delta) {}
    public void render(Graphics graphics) {}

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
}
