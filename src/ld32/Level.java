package ld32;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Level {
    
    private ArrayList<Entity> entities;
    private Player player;
    private String nextLevel;
    private boolean finished;
    
    public Level(Player player) {
        entities = new ArrayList<>();
        this.player = player;
        player.setLevel(this);
        finished = false;
        nextLevel = null;
    }
    
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
            String[] playerCoords = line.split(",");
            player = new Player(Integer.parseInt(playerCoords[0]), Integer.parseInt(playerCoords[1]));
            player.setLevel(this);
            
            line = in.readLine();
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
                            entities.add(new JumpPad(x,y));
                            break;
                        case "F":
                            entities.add(new Flag(x,y));
                            break;
                        case "SR":
                            entities.add(new Spikes(x,y,1));
                            break;
                        case "SL":
                            entities.add(new Spikes(x,y,3));
                            break;
                        case "SU":
                            entities.add(new Spikes(x,y,0));
                            break;
                        case "SD":
                            entities.add(new Spikes(x,y,2));
                            break;
                        default:
                            int id = Integer.parseInt(token);
                            if(id != 3)
                                entities.add(new Wall(x, y, id));
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
