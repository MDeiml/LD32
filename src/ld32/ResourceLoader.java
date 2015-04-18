package ld32;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class ResourceLoader {
    
    private static HashMap<File, BufferedImage> images = new HashMap<>();
    
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
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return img;
    }
}
