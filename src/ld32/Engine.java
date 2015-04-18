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
    
    private static final int S_PLAYING = 0;
    private static final int S_DEAD = 1;
    
    private boolean running;
    private Screen screen;
    private int state;
    
    public Engine() {
        screen = new Screen(WIDTH, HEIGHT);
        running = false;
        state = S_PLAYING;
        ResourceLoader.getSound("level_end.wav");
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
            if(now >= nextUpdate) {
                nextUpdate += 1000000000/FPS;
                inp.update();
                if(state == S_PLAYING) {
                    level.update(1f/FPS, inp);
                    level.render(g);
                    if(level.getPlayer().isExploded())
                        state = S_DEAD;
                    if(inp.keyDown(KeyEvent.VK_R) || level.getPlayer().getY() > HEIGHT / 32f + 1) {
                        level.getPlayer().explode();
                    }
                }else if(state == S_DEAD) {
                    g.drawImage(ResourceLoader.getImage("dead.png"), 0, 0, WIDTH, HEIGHT, null);
                    if(inp.keyDown(KeyEvent.VK_SPACE)) {
                        state = S_PLAYING;
                        level = new Level(currentLevel);
                    }
                }
                if(level.isFinished()) {
                    currentLevel = level.getNextLevel();
                    level = new Level(currentLevel);
                    ResourceLoader.playSound(ResourceLoader.getSound("level_end.wav"));
                }
                screen.repaint();
            }
        }
        screen.dispose();
    }
}
