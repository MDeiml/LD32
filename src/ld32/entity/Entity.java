package ld32.entity;

import java.awt.Graphics;
import ld32.InputManager;
import ld32.Level;


public abstract class Entity {
    
    public Entity(float x, float y, Level level) {
        this.x = x;
        this.y = y;
        this.level = level;
    }
    
    private float x;
    private float y;
    private Level level;
    
    public void update(float delta, InputManager input) {}
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

    public Level getLevel() {
        return level;
    }
    
}
