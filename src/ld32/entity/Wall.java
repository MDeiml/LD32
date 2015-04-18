package ld32.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld32.Level;
import ld32.ResourceLoader;


public class Wall extends Entity {
    
    private int id;
    private BufferedImage tilemap;
    
    public Wall(int x, int y, Level l, int id) {
        super(x, y, l);
        tilemap = ResourceLoader.getImage("dirt.png");
        this.id = id;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        
        int dx1 = (int)(getX() * 64);
        int dy1 = (int)(getY() * 64);
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        int sx1 = (int)(id % 4) * 32;
        int sy1 = (int)(id / 4) * 32;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(tilemap, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }
    
}
