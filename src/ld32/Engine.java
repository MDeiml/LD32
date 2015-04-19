package ld32;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.newdawn.easyogg.OggClip;


public class Engine {
    
    private static final int FPS = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int COUNTDOWN_LENGTH = 3;
    private static final float INTRO_LENGTH = 4*3;
    
    private static final int S_PLAYING = 0;
    private static final int S_DEAD = 1;
    private static final int S_COUNTDOWN = 2;
    private static final int S_INTRO = 3;
    private static final int S_MAIN_MENU = 4;
    private static final int S_CONTROLS = 5;
    private static final int S_HIGHSCORE = 6;
    
    private boolean running;
    private Screen screen;
    private int state;
    private float timer;
    private float countdown;
    private int selection;
    private int highscore;
    private int highscoreHardcore;
    private boolean hardcore;
    
    public Engine() {
        screen = new Screen(WIDTH, HEIGHT);
        running = false;
        state = S_INTRO;
        countdown = 0;
        timer = 0;
        
        File highscoreFile = new File("./highscores");
        if(highscoreFile.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(highscoreFile));
                highscore = Integer.parseInt(in.readLine());
                highscoreHardcore = Integer.parseInt(in.readLine());
                in.close();
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
                highscore = 0;
                highscoreHardcore = 0;
            }
        }else {
            highscore = 0;
            highscoreHardcore = 0;
        }
        ResourceLoader.getSound("level_end.wav");
        ResourceLoader.getSound("select.wav");
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
                    if(level.getPlayer().isExploded()) {
                        if(hardcore) {
                            state = S_MAIN_MENU;
                            currentLevel = "level1";
                            level = new Level(currentLevel);
                            selection = 0;
                        }else {
                            state = S_DEAD;
                        }
                    }
                    if(inp.keyDown(KeyEvent.VK_R) || level.getPlayer().getY() > HEIGHT / 32f + 1) {
                        level.getPlayer().explode();
                    }
                }else if(state == S_DEAD) {
                    g.drawImage(ResourceLoader.getImage("dead.png"), 0, 0, WIDTH, HEIGHT, null);
                    if(inp.keyDown(KeyEvent.VK_SPACE)) {
                        state = S_COUNTDOWN;
                        countdown = 0;
                        level = new Level(currentLevel);
                    }else if(inp.keyDown(KeyEvent.VK_ESCAPE)) {
                        state = S_MAIN_MENU;
                        currentLevel = "level1";
                        level = new Level(currentLevel);
                        selection = 0;
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
                        state = S_MAIN_MENU;
                        selection = 0;
                        continue;
                    }
                    g.drawImage(ResourceLoader.getImage("intro"+id+".png"), 0, 0, WIDTH, HEIGHT, null);
                }else if(state == S_MAIN_MENU) {
                    if(inp.keyDown(KeyEvent.VK_S) | inp.keyDown(KeyEvent.VK_DOWN)) {
                        selection = (selection + 1) % 5;
                        ResourceLoader.playSound(ResourceLoader.getSound("select.wav"));
                    }
                    if(inp.keyDown(KeyEvent.VK_W) | inp.keyDown(KeyEvent.VK_UP)){
                        selection = (selection + 4) % 5;
                        ResourceLoader.playSound(ResourceLoader.getSound("select.wav"));
                    }
                    if(inp.keyDown(KeyEvent.VK_SPACE) | inp.keyDown(KeyEvent.VK_ENTER)) {
                        ResourceLoader.playSound(ResourceLoader.getSound("select.wav"));
                        switch(selection) {
                            case 0:
                                state = S_COUNTDOWN;
                                timer = 0;
                                hardcore = false;
                                countdown = 0;
                                break;
                            case 1:
                                state = S_HIGHSCORE;
                                break;
                            case 2:
                                state = S_CONTROLS;
                                break;
                            case 3:
                                state = S_COUNTDOWN;
                                hardcore = true;
                                timer = 0;
                                countdown = 0;
                                break;
                            case 4:
                                running = false;
                                break;
                        }
                    }
                    level.render(g);
                    g.drawImage(ResourceLoader.getImage("main_menu.png"), 0, 0, WIDTH, HEIGHT, null);
                    g.drawImage(ResourceLoader.getImage("selector.png"), 105*2, 68*2+selection*22*2, 32, 32, null);
                }else if(state == S_CONTROLS) {
                    level.render(g);
                    g.drawImage(ResourceLoader.getImage("controls.png"), 0, 0, WIDTH, HEIGHT, null);
                    if(inp.keyDown(KeyEvent.VK_SPACE) | inp.keyDown(KeyEvent.VK_ENTER) | inp.keyDown(KeyEvent.VK_ESCAPE)) {
                        ResourceLoader.playSound(ResourceLoader.getSound("select.wav"));
                        state = S_MAIN_MENU;
                    }
                }else if(state == S_HIGHSCORE) {
                    level.render(g);
                    g.drawImage(ResourceLoader.getImage("highscore.png"), 0, 0, WIDTH, HEIGHT, null);
                    Util.renderNumber(highscore, g, 287*2, 130*2+1, 5);
                    Util.renderNumber(highscoreHardcore, g, 287*2, 151*2+1, 5);
                    if(inp.keyDown(KeyEvent.VK_SPACE) | inp.keyDown(KeyEvent.VK_ENTER) | inp.keyDown(KeyEvent.VK_ESCAPE)) {
                        ResourceLoader.playSound(ResourceLoader.getSound("select.wav"));
                        state = S_MAIN_MENU;
                    }
                }
                if(level.isFinished()) {
                    state = S_COUNTDOWN;
                    countdown = 0;
                    currentLevel = level.getNextLevel();
                    if(currentLevel.equals("END")){
                        state = S_HIGHSCORE;
                        selection = 0;
                        if(hardcore) {
                            if(timer < highscoreHardcore || highscoreHardcore == 0) {
                                highscoreHardcore = (int)timer;
                            }
                        }else {
                            if(timer < highscore || highscore == 0) {
                                highscore = (int)timer;
                            }
                        }
                        currentLevel = "level1";
                        level = new Level(currentLevel);
                    }else {
                        level = new Level(currentLevel);
                        ResourceLoader.playSound(ResourceLoader.getSound("level_end.wav"));
                    }
                }
                screen.repaint();
            }
        }
        
        try {
            PrintWriter out = new PrintWriter("./highscores");
            out.println(highscore);
            out.println(highscoreHardcore);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        music.stop();
        screen.dispose();
    }
}
