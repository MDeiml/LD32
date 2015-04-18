package ld32.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld32.Level;
import ld32.ResourceLoader;


public class Spikes extends Entity {
    
    private BufferedImage spritesheet;
    private int id;
    
    public Spikes(float x, float y, Level l, int id) {
        super(x,y,l);
        this.id = id;
        spritesheet = ResourceLoader.getImage("objects.png");
    }
    
    @Override
    public void render(Graphics g) {
        
        
        int dx1 = (int)(getX() * 64);
        int dy1 = (int)(getY() * 64)+4;
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        int sx1 = id * 32;
        int sy1 = 32 * 2;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(spritesheet, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
}
