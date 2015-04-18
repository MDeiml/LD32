package ld32;


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
        long nextUpdate = System.nanoTime();
        while(running && !screen.isCloseRequested()) {
            long now = System.nanoTime();
            if(now >= nextUpdate) {
                nextUpdate += 1000000000/FPS;
                screen.repaint();
            }
        }
        screen.dispose();
    }
}
