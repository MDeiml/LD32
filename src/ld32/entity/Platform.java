package ld32.entity;

import java.awt.Graphics;
import ld32.InputManager;
import ld32.Level;
import ld32.ResourceLoader;


public class Platform extends Entity {
    
    public static final float SPEED = 3;
            
    private boolean direction;
    private boolean direction1;
    
    public Platform(float x, float y, Level l, boolean d) {
        super(x, y, l);
        direction = true;
        direction1 = d;
    }
    
    @Override
    public void update(float delta, InputManager inp) {
        if(direction1)
            setX(getX() + (direction ? SPEED : -SPEED) * delta);
        else
            setY(getY() + (direction ? SPEED : -SPEED) * delta);
        
        for(Entity e : getLevel().getEntities()) {
            if(e instanceof Wall) {
                Wall w = (Wall) e;
                
                float pxr = (w.getX()+1)-getX();
                float pxl = (getX()+1)-w.getX();
                float pyb = (w.getY()+1)-getY();
                float pyt = (getY()+0.5f)-w.getY();
                if(pxr > 0 && pxl > 0 && pyb > 0 && pyt > 0) {
                    if(direction1) {
                        if(pxr < pxl) {
                            setX(w.getX()+1);
                            direction = true;
                        }else {
                            setX(w.getX()-1);
                            direction = false;
                        }
                    }else {
                        if(pyt < pyb) {
                            setY(w.getY()-0.5f);
                            direction = false;
                        }else {
                            setY(w.getY()+1);
                            direction = true;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        int dx1 = (int)(getX() * 64);
        int dy1 = (int)(getY() * 64);
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        int sx1 = 32;
        int sy1 = 32 * 3;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(ResourceLoader.getImage("objects.png"), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public boolean getDirection() {
        return direction;
    }

    public boolean getDirection1() {
        return direction1;
    }
    
}