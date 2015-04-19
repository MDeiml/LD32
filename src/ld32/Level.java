package ld32;

import ld32.entity.JumpPad;
import ld32.entity.Player;
import ld32.entity.Flag;
import ld32.entity.Spikes;
import ld32.entity.Wall;
import ld32.entity.Entity;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ld32.entity.Decoration;
import ld32.entity.Fireball;
import ld32.entity.Platform;
import ld32.entity.Water;


public class Level {
    
    private ArrayList<Entity> entities;
    private Player player;
    private String nextLevel;
    private boolean finished;
    
    public void update(float delta, InputManager input) {
        player.update(delta, input);
        for(Entity e : entities) {
            e.update(delta, input);
        }
    }
    
    public void render(Graphics g) {
        for(Entity e : entities) {
            e.render(g);
        }
        player.render(g);
    }
    
    public void addEntity(Entity e) {
        entities.add(e);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    
    public Level(String filename) {
        entities = new ArrayList<>();
        player = null;
        finished = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader("./res/"+filename));
            
            String line = in.readLine();
            nextLevel = line;
            
            int y = 0;
            while((line = in.readLine()) != null) {
                int x = 0;
                String[] tokens = line.split(" ");
                for(String token : tokens) {
                    if(token.isEmpty())
                        continue;
                    
                    switch(token) {
                        case "P":
                            entities.add(new JumpPad(x,y,this));
                            break;
                        case "F":
                            entities.add(new Flag(x,y,this));
                            break;
                        case "SR":
                            entities.add(new Spikes(x,y,this,1));
                            break;
                        case "SL":
                            entities.add(new Spikes(x,y,this,3));
                            break;
                        case "SU":
                            entities.add(new Spikes(x,y,this,0));
                            break;
                        case "SD":
                            entities.add(new Spikes(x,y,this,2));
                            break;
                        case "X":
                            player = new Player(x,y,this);
                            break;
                        case "=":
                            entities.add(new Platform(x, y, this, true));
                            break;
                        case "|":
                            entities.add(new Platform(x, y, this, false));
                            break;
                        case "B":
                            entities.add(new Decoration(x,y,12,this));
                            break;
                        case "FV":
                            entities.add(new Fireball(x, y, this, false));
                            break;
                        case "FH":
                            entities.add(new Fireball(x, y, this, true));
                            break;
                        case "W":
                            entities.add(new Water(x, y, this));
                            break;
                        case "_":
                            break;
                        default:
                            int id = Integer.parseInt(token);
                            entities.add(new Wall(x, y, this, id));
                    }
                    x++;
                }
                y++;
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not load level "+filename, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
}
