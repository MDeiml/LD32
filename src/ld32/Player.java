package ld32;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Player extends Entity {
    
    private static final float ANIMATION_LENGTH = 0.085f * 4;
    
    private float animationTime;
    private BufferedImage sprite;
    
    public Player(float x, float y) {
        super(x, y);
        animationTime = 0;
        sprite = ResourceLoader.getImage("player.png");
    }
    
    @Override
    public void update(float delta) {
        animationTime = (animationTime + delta) % ANIMATION_LENGTH;
    }
    
    @Override
    public void render(Graphics g) {
        int dx1 = (int)(getX()*2);
        int dy1 = (int)(getY()*2);
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        
        int frame = (int)(animationTime / ANIMATION_LENGTH * 4);
        int sx1 = frame*32;
        int sy1 = 0;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(sprite, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
}
