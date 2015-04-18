package ld32;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;


public class InputManager implements KeyListener {

    private Set<Integer> keys;
    private Set<Integer> lastKeys;
    private Set<Integer> nextKeys;
    
    public InputManager() {
        keys = new HashSet<>();
        lastKeys = new HashSet<>();
        nextKeys = new HashSet<>();
    }
    
    public synchronized boolean key(int keycode) {
        return keys.contains(keycode);
    }
    
    public synchronized boolean keyDown(int keycode) {
        return keys.contains(keycode) && !lastKeys.contains(keycode);
    }
    
    public synchronized boolean keyUp(int keycode) {
        return lastKeys.contains(keycode) && !keys.contains(keycode);
    }
    
    public synchronized void update() {
        lastKeys.clear();
        lastKeys.addAll(keys);
        keys.clear();
        keys.addAll(nextKeys);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        nextKeys.add(e.getKeyCode());
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        nextKeys.remove(e.getKeyCode());
    }
    
}
