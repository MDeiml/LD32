package ld32;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


public class ResourceLoader {
    
    private static HashMap<File, BufferedImage> images = new HashMap<>();
    private static HashMap<File, Clip> clips = new HashMap<>();
    
    public static BufferedImage getImage(String filename) {
        File file = new File("./res/"+filename);
        if(images.containsKey(file))
            return images.get(file);
        else {
            BufferedImage img = loadImage(file);
            images.put(file, img);
            return img;
        }
    }
    
    private static BufferedImage loadImage(File file) {
        System.out.println("Loading "+file+"...");
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not load resource "+file, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return img;
    }
    
    public static Clip getSound(String filename) {
        File file = new File("./res/"+filename);
        if(clips.containsKey(file))
            return clips.get(file);
        else {
            Clip clip = loadSound(file);
            clips.put(file, clip);
            return clip;
        }
    }
    
    private static Clip loadSound(File file) {
        System.out.println("Loading "+file+"...");
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            clip.open(inputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not load resource "+file, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return clip;
    }
    
    public static void playSound(final Clip clip) {
        if(clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
