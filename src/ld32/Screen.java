package ld32;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class Screen extends Frame {
    
    private static final String TITLE = "LD32";
    
    private boolean closeRequested;
    private BufferedImage buffer;
    private Graphics bufferGraphics;
    
    public Screen(int w, int h) {
        Dimension size = new Dimension(w, h);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setCloseRequested(true);
            }
        });
        
        buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();
        
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    @Override
    public void update(Graphics g) {
        g.drawImage(buffer, 0, 0, null);
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
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
    
}
