package ld32.entity;

import java.awt.Graphics;
import ld32.InputManager;
import ld32.Level;
import ld32.ResourceLoader;


public class Water extends Entity {
    
    public static final float ANIMATION_LENGTH = 0.3f;

    private float animationTime;
    
    public Water(float x, float y, Level l) {
        super(x, y, l);
        animationTime = 0;
    }
    
    @Override
    public void update(float delta, InputManager inp) {
        animationTime = (animationTime + delta) % ANIMATION_LENGTH;
    }
    
    @Override
    public void render(Graphics g) {
        int dx1 = (int)(getX() * 64);
        int dy1 = (int)(getY() * 64);
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        int sx1 = (int)(animationTime / ANIMATION_LENGTH * 2 + 2) * 32;
        int sy1 = 32 * 3;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(ResourceLoader.getImage("objects.png"), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
}
