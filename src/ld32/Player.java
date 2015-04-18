package ld32;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;


public class Player extends Entity {
    
    private static final float ANIMATION_LENGTH = 0.085f * 4;
    private static final float EXPLODE_DURATION = 1;
    private static final float SPEED = 6;
    
    private float animationTime;
    private float explodeTime;
    private BufferedImage sprite;
    private boolean direction;
    private boolean lastOnGround;
    private Level level;
    private float velY;
    private boolean exploded;
    private Clip fuss;
    
    public Player(float x, float y) {
        super(x, y);
        animationTime = 0;
        sprite = ResourceLoader.getImage("player.png");
        ResourceLoader.getSound("explode.wav");
        ResourceLoader.getSound("jump.wav");
        fuss = ResourceLoader.getSound("fuss.wav");
        direction = true;
        velY = 0;
        exploded = false;
    }
    
    @Override
    public void update(float delta, InputManager input) {
        animationTime = (animationTime + delta) % ANIMATION_LENGTH;
        
        float dx, dy = 0;
        if(input.keyDown(KeyEvent.VK_A))
            direction = false;
        if(input.keyDown(KeyEvent.VK_D))
            direction = true;
        
        if(direction)
            dx = SPEED;
        else
            dx = -SPEED;
        
        dy += velY;
        
        setX(getX() + dx * delta);
        setY(getY() + dy * delta);
        
        boolean onGround = false;
        boolean explode = false;
        
        for(Entity e : level.getEntities()) {
            if(e instanceof Wall) {
                Wall w = (Wall) e;
                
                float pxr = (w.getX()+1)-getX();
                float pxl = (getX()+1)-w.getX();
                float pyb = (w.getY()+1)-getY();
                float pyt = (getY()+1)-w.getY();
                if(pxr > 0 && pxl > 0 && pyb > 0 && pyt > 0) {
                    float px = Math.min(pxr, pxl);
                    float py = Math.min(pyb, pyt);
                    if(px < py+0.05f) {
                        if(pxr < pxl) {
                            setX(w.getX()+1);
                        }else {
                            setX(w.getX()-1);
                        }
                        if(lastOnGround) {
                            explode = true;
                        }else {
                            explodeTime = 0;
                        }
                    }else {
                        if(pyb < pyt) {
                            setY(w.getY()+1);
                            velY = Math.max(0, velY);
                        }else {
                            if(velY >= 0) {
                                velY = 0;
                                onGround = true;
                            }
                            setY(w.getY()-1);
                        }
                    }
                }
            }else if(e instanceof JumpPad) {
                JumpPad jp = (JumpPad)e;
                if(jp.isExtended())
                    continue;
                float pxr = (jp.getX()+1)-getX();
                float pxl = (getX()+1)-jp.getX();
                float pyb = (jp.getY()+0.5f)-getY();
                float pyt = (getY()+1)-(jp.getY()+0.5f);
                if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                    jp.extend();
                    velY = -12;
                }
            }
        }
        
        if(explode) {
            if(explodeTime > 0.05 && !fuss.isRunning()) {
                ResourceLoader.playSound(fuss);
            }
            explodeTime = Math.min(explodeTime + delta, EXPLODE_DURATION);
            if(explodeTime == EXPLODE_DURATION)
                explode();
        }else {
            fuss.stop();
            explodeTime = 0;
        }
        
        if(onGround && input.keyDown(KeyEvent.VK_SPACE)) {
            velY = -8;
            ResourceLoader.playSound(ResourceLoader.getSound("jump.wav"));
        }else {
            velY += delta*20;
        }
        lastOnGround = onGround;
    }
    
    private void explode() {
        ResourceLoader.playSound(ResourceLoader.getSound("explode.wav"));
        fuss.stop();
        exploded = true;
    }
    
    @Override
    public void render(Graphics g) {
        int dx1 = (int)(getX()*64);
        int dy1 = (int)(getY()*64)+4;
        int dx2 = dx1 + 64;
        int dy2 = dy1 + 64;
        
        int frame = (int)(animationTime / ANIMATION_LENGTH * 4);
        int sx1 = frame*32;
        int sy1 = direction ? 0 : 32;
        int sx2 = sx1 + 32;
        int sy2 = sy1 + 32;
        
        g.drawImage(sprite, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isExploded() {
        return exploded;
    }
    
}
