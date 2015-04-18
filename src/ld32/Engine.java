package ld32;

import java.awt.Graphics;


public class Engine {
    
    private static final int FPS = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    private boolean running;
    private Screen screen;
    
    public Engine() {
        screen = new Screen(WIDTH, HEIGHT);
        running = false;
    }
    
    public void start() {
        if(running)
            return;
        
        running = true;
        run();
    }
    
    public void run() {
        Player p = new Player(100,100);
        long nextUpdate = System.nanoTime();
        Graphics g = screen.getBufferGraphics();
        while(running && !screen.isCloseRequested()) {
            long now = System.nanoTime();
            if(now >= nextUpdate) {
                nextUpdate += 1000000000/FPS;
                p.update(1f/FPS);
                p.render(g);
                screen.repaint();
            }
        }
        screen.dispose();
    }
}
