package ld32.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld32.InputManager;
import ld32.Level;
import ld32.ResourceLoader;


public class JumpPad extends Entity {
    
    private static final float EXTEND_TIME = 0.5f;
    
    private float extended;
    private BufferedImage spritesheet;
    
    public JumpPad(float x, float y, Level l) {
        super(x, y, l);
        extended = 0;
        spritesheet = ResourceLoader.getImage("objects.png");
        ResourceLoader.getSound("jump2.wav");
    }
    
    @Override
    public void update(float delta, InputManager inp) {
        extended = Math.max(0, extended - delta);
    }
    
    @Override
    public void render(Graphics g) {
        
        int dx1 = (int)(getX() * 64);
        int dy1 = (int)(getY() * 64)+4;
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        int sx1 = (extended == 0) ? 0 : 32;
        int sy1 = 0;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(spritesheet, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
    public boolean isExtended() {
        return extended != 0;
    }
    
    public void extend() {
        extended = EXTEND_TIME;
        ResourceLoader.playSound(ResourceLoader.getSound("jump2.wav"));
    }
    
}
