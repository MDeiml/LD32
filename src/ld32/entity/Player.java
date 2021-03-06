package ld32.entity;

import ld32.entity.Spikes;
import ld32.entity.Wall;
import ld32.entity.Entity;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import ld32.InputManager;
import ld32.Level;
import ld32.ResourceLoader;
import static ld32.entity.Platform.SPEED;


public class Player extends Entity {
    
    private static final float ANIMATION_LENGTH = 0.085f * 4;
    private static final float EXPLODE_DURATION = 1;
    private static final float SPEED = 6;
    
    private float animationTime;
    private float explodeTime;
    private BufferedImage sprite;
    private boolean direction;
    private boolean lastOnGround;
    private float velY;
    private boolean exploded;
    private boolean inWater;
    private Clip fuss;
    
    public Player(float x, float y, Level l) {
        super(x, y, l);
        animationTime = 0;
        sprite = ResourceLoader.getImage("player.png");
        ResourceLoader.getSound("explode.wav");
        ResourceLoader.getSound("jump.wav");
        ResourceLoader.getSound("splash.wav");
        fuss = ResourceLoader.getSound("fuss.wav");
        direction = true;
        velY = 0;
        exploded = false;
        inWater = false;
    }
    
    @Override
    public void update(float delta, InputManager input) {
        animationTime = (animationTime + delta) % ANIMATION_LENGTH;
        
        float dx, dy = 0;
        if(input.keyDown(KeyEvent.VK_A) | input.keyDown(KeyEvent.VK_LEFT))
            direction = false;
        if(input.keyDown(KeyEvent.VK_D) | input.keyDown(KeyEvent.VK_RIGHT))
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
        boolean inWater1 = false;
        
        for(Entity e : getLevel().getEntities()) {
            if(e instanceof Wall) {
                Wall w = (Wall) e;
                
                float pxr = (w.getX()+1)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-w.getX();
                float pyb = (w.getY()+1)-(getY()+0.1f);
                float pyt = (getY()+1)-w.getY();
                if(pxr > 0 && pxl > 0 && pyb > 0 && pyt > 0) {
                    float px = Math.min(pxr, pxl);
                    float py = Math.min(pyb, pyt);
                    if(px < py+0.075f) {
                        if(pxr < pxl) {
                            setX(w.getX()+0.8f);
                        }else {
                            setX(w.getX()-0.8f);
                        }
                        if(lastOnGround) {
                            explode = true;
                        }else {
                            explodeTime = 0;
                        }
                    }else {
                        if(pyb < pyt) {
                            setY(w.getY()+0.9f);
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
                if(!jp.isExtended()) {
                    float pxr = (jp.getX()+0.8f)-(getX()+0.2f);
                    float pxl = (getX()+0.8f)-(jp.getX()+0.2f);
                    float pyb = (jp.getY()+0.5f)-(getY()+0.1f);
                    float pyt = (getY()+1)-(jp.getY()+0.5f);
                    if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                        jp.extend();
                        velY = -12;
                    }
                }
            }else if(e instanceof Flag) {
                float pxr = (e.getX()+0.75f)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-(e.getX()+0.25f);
                float pyb = (e.getY()+1)-(getY()+0.1f);
                float pyt = (getY()+1)-e.getY();
                
                if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                    getLevel().setFinished(true);
                }
            }else if(e instanceof Water) {
                float pxr = (e.getX()+1)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-e.getX();
                float pyb = (e.getY()+1)-(getY()+0.1f);
                float pyt1 = (getY()+1)-(e.getY()+0.1f);
                float pyt = (getY()+1)-(e.getY()+0.8f);
                
                if(pxr > 0 && pxl > 0 && pyb > 0) {
                    if(pyt1 > 0) {
                        if(!inWater) ResourceLoader.playSound(ResourceLoader.getSound("splash.wav"));
                        inWater1 = true;
                        if(pyt > 0)
                            getLevel().setFinished(true);
                    }
                }
            }else if(e instanceof Spikes) {
                float pxr = (e.getX()+1)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-e.getX();
                float pyb = (e.getY()+1)-(getY()+0.1f);
                float pyt = (getY()+1)-e.getY();
                
                pxr -= 0.1f;
                pxl -= 0.1f;
                pyt -= 0.1f;
                pyb -= 0.1f;
                
                if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                    explode();
                }
            }else if(e instanceof Platform) {
                Platform p = (Platform)e;
                float pxr = (e.getX()+1)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-e.getX();
                float pyb = (e.getY()+0.5f)-(getY()+0.1f);
                float pyt = (getY()+1)-e.getY();
                
                if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                    float px = Math.min(pxr, pxl);
                    float py = Math.min(pyb, pyt);
                    if(px > 0.3 && py > 0.3)
                        explode();
                    if(px < py+0.075f) {
                        if(pxr < pxl) {
                            setX(e.getX()+1);
                        }else {
                            setX(e.getX()-1);
                        }
                        if(lastOnGround) {
                            explode = true;
                        }else {
                            explodeTime = 0;
                        }
                    }else {
                        if(pyb < pyt) {
                            setY(e.getY()+0.5f);
                            velY = Math.max(0, velY);
                        }else {
                            if(velY >= 0) {
                                velY = 0;
                                onGround = true;
                                float s = (p.getDirection() ? Platform.SPEED : -Platform.SPEED);
                                if(p.getDirection() && !p.getDirection1()) {
                                    velY = Platform.SPEED;
                                }
                                if(p.getDirection1()) {
                                    setX(getX() + (s - dx) * delta);
                                }else {
                                    setX(getX() - dx * delta);
                                }
                            }
                            setY(e.getY()-1);
                        }
                    }
                }
            }else if(e instanceof Fireball) {
                float pxr = (e.getX()+0.75f)-(getX()+0.2f);
                float pxl = (getX()+0.8f)-(e.getX()+0.25f);
                float pyb = (e.getY()+0.75f)-(getY()+0.1f);
                float pyt = (getY()+1)-(e.getY()+0.25f);
                
                if(pxr > 0 && pxl > 0 && pyt > 0 && pyb > 0) {
                    explode();
                }
            }
        }
        
        inWater = inWater1;
        
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
        
        if(onGround && (input.keyDown(KeyEvent.VK_SPACE) | input.keyDown(KeyEvent.VK_W) | input.keyDown(KeyEvent.VK_UP))) {
            velY = -8;
            ResourceLoader.playSound(ResourceLoader.getSound("jump.wav"));
        }else {
            velY += delta*20;
        }
        lastOnGround = onGround;
    }
    
    public void explode() {
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

    public boolean isExploded() {
        return exploded;
    }
    
}
