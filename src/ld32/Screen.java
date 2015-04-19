package ld32;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class Screen extends Frame {
    
    private static final String TITLE = "James Bomb";
    
    private boolean closeRequested;
    private BufferedImage buffer;
    private Graphics bufferGraphics;
    private InputManager input;
    private BufferedImage background;
    
    public Screen(int w, int h) {
        Dimension size = new Dimension(w, h);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setIconImage(ResourceLoader.getImage("ld.png"));
        
        input = new InputManager();
        addKeyListener(input);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setCloseRequested(true);
            }
        });
        
        buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();
        background = ResourceLoader.getImage("background.png");
        
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    @Override
    public void update(Graphics g) {
        g.drawImage(buffer, 0, 0, null);
        bufferGraphics.drawImage(background, 0, 0, null);
    }

    public synchronized boolean isCloseRequested() {
        return closeRequested;
    }

    public synchronized void setCloseRequested(boolean closeRequested) {
        this.closeRequested = closeRequested;
    }

    public Graphics getBufferGraphics() {
        return bufferGraphics;
    }

    public InputManager getInput() {
        return input;
    }
    
}
