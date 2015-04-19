package ld32;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import org.newdawn.easyogg.OggClip;


public class Engine {
    
    private static final int FPS = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int COUNTDOWN_LENGTH = 3;
    private static final float INTRO_LENGTH = 2*3;
    
    private static final int S_PLAYING = 0;
    private static final int S_DEAD = 1;
    private static final int S_COUNTDOWN = 2;
    private static final int S_INTRO = 3;
    
    private boolean running;
    private Screen screen;
    private int state;
    private float timer;
    private float countdown;
    
    public Engine() {
        screen = new Screen(WIDTH, HEIGHT);
        running = false;
        state = S_INTRO;
        countdown = 0;
        timer = 0;
        ResourceLoader.getSound("level_end.wav");
        ResourceLoader.getImage("intro1.png");
        ResourceLoader.getImage("intro2.png");
        ResourceLoader.getImage("intro3.png");
        ResourceLoader.getImage("numbers.png");
    }
    
    public void start() {
        if(running)
            return;
        
        running = true;
        run();
    }
    
    public void run() {
        OggClip music = null;
        try {
            music = new OggClip(new FileInputStream("./res/music.ogg"));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        music.loop();
        
        String currentLevel = "level1";
        Level level = new Level(currentLevel);
        
        Graphics g = screen.getBufferGraphics();
        InputManager inp = screen.getInput();
        
        long nextUpdate = System.nanoTime();
        while(running && !screen.isCloseRequested()) {
            long now = System.nanoTime();
            if(now-nextUpdate > 1000000000/FPS*2) {
                nextUpdate = now;
            }
            if(now >= nextUpdate) {
                nextUpdate += 1000000000/FPS;
                inp.update();
                if(state == S_PLAYING) {
                    level.update(1f/FPS, inp);
                    timer += 1f/FPS;
                    level.render(g);
                    Util.renderNumber((int)timer, g, 50, 50, 4);
                    if(level.getPlayer().isExploded())
                        state = S_DEAD;
                    if(inp.keyDown(KeyEvent.VK_R) || level.getPlayer().getY() > HEIGHT / 32f + 1) {
                        level.getPlayer().explode();
                    }
                }else if(state == S_DEAD) {
                    g.drawImage(ResourceLoader.getImage("dead.png"), 0, 0, WIDTH, HEIGHT, null);
                    if(inp.keyDown(KeyEvent.VK_SPACE)) {
                        state = S_COUNTDOWN;
                        countdown = 0;
                        level = new Level(currentLevel);
                    }
                }else if(state == S_COUNTDOWN) {
                    countdown = Math.min(countdown + 1f/FPS, COUNTDOWN_LENGTH);
                    int number = COUNTDOWN_LENGTH - (int)(countdown);
                    level.render(g);
                    Util.renderNumber((int)timer, g, 50, 50, 4);
                    g.drawImage(ResourceLoader.getImage("numbers.png"), 325, 175, 475, 425, number*3, 0, number*3+3, 5, screen);
                    if(countdown >= COUNTDOWN_LENGTH) {
                        state = S_PLAYING;
                    }
                }else if(state == S_INTRO) {
                    countdown = Math.min(countdown + 1f/FPS, INTRO_LENGTH);
                    int id = (int)(countdown/INTRO_LENGTH*3)+1;
                    if(id >= 4) {
                        state = S_COUNTDOWN;
                        countdown = 0;
                        continue;
                    }
                    g.drawImage(ResourceLoader.getImage("intro"+id+".png"), 0, 0, WIDTH, HEIGHT, null);
                }
                if(level.isFinished()) {
                    state = S_COUNTDOWN;
                    countdown = 0;
                    currentLevel = level.getNextLevel();
                    if(currentLevel.equals("END"))
                        running = false;
                    else {
                        level = new Level(currentLevel);
                        ResourceLoader.playSound(ResourceLoader.getSound("level_end.wav"));
                    }
                }
                screen.repaint();
            }
        }
        screen.dispose();
    }
}
