package ld32;

import java.awt.Graphics;


public class Util {
    
    public static void renderNumber(int num, Graphics g, int x, int y, int scale) {
        x += scale*4*(int)Math.log10(num);
        do {
            int digit = num % 10;
            g.drawImage(ResourceLoader.getImage("numbers.png"), x, y, x + scale*3, y + scale*5, digit*3, 0, digit*3+3, 5, null);
            x -= scale * 4;
            num /= 10;
        }while (num > 0);
    }
    
}
