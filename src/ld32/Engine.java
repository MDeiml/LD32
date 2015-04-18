package ld32;

import java.awt.Graphics;
import java.awt.event.KeyEvent;


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
    }
    
    public void start() {
        if(running)
            return;
        
        running = true;
        run();
    }
    
    public void run() {
        Level level = new Level("test_level");
        level.addEntity(new JumpPad(5,4));
        
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
                }else if(state == S_DEAD) {
                    g.drawImage(ResourceLoader.getImage("dead.png"), 0, 0, WIDTH, HEIGHT, null);
                    if(inp.keyDown(KeyEvent.VK_SPACE)) {
                        state = S_PLAYING;
                        level = new Level("test_level");
                    }
                }
                screen.repaint();
            }
        }
        screen.dispose();
    }
}
