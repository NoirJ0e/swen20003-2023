import bagel.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enemy class for the enemies which spawned at random location in the screen and can only move horizontally
 */
public class Enemy {
    private boolean active;
    private final static int SPEED_FACTOR = 1;
    private static final int SPEED = 1;
    private final static int X_MIN = 100;
    private final static int Y_MIN = 100;
    private final static int X_MAX = 900;
    private final static int Y_MAX = 500;
    private final static int COLLISION_RANGE = 104;
    private int x;
    private final int y;
    private int direction; // 0 for left, 1 for right
    private final Image image = new Image("res/enemy.PNG");

    /**
     * Constructor for the enemy, spawn at random location and move horizontally,
     * will bounce back once it reaches the edge of the screen
     */
    public Enemy() {
        active = true;
        // https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        int x = ThreadLocalRandom.current().nextInt(X_MIN,  X_MAX+ 1);
        int y = ThreadLocalRandom.current().nextInt(Y_MIN,  Y_MAX+ 1);
        // use Point from bagel will make x and y be `final` and have to spawn a bunch of new Point objects while updating
        this.x = x;
        this.y = y;
        direction = ThreadLocalRandom.current().nextInt(0,  2);
    }

    /**
     * Update the enemy's x-coordinate to make it moving in the screen
     */
    public void update() {
        if (!active) {
            return;
        }
        // it will randomly choose direction, and it will only move in x-axis
        draw();
        // moving left
        if (direction == 0) {
            if (x > 100) {
                x -= SPEED * SPEED_FACTOR;
            } else {
                direction = 1;
            }
        } else {
            if (x < 900) {
                x += SPEED * SPEED_FACTOR;
            } else {
                direction = 0;
            }
        }
    }

    /**
     * Draw the enemy only, will not move / update the x-coordinate
     */
    public void draw() {
        if (!active) {
            return;
        }
        // draw based on x-y coordinates
        image.draw(this.x, this.y);
    }

    /**
     * Get the x-coordinate of the enemy
     * @return Current x-coordinate of the enemy
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y-coordinate of the enemy
     * @return Current y-coordinate of the enemy
     */
    public int getY() {
        return this.y;
    }

    /**
     * Deactivate the enemy, mark it as complete and no longer exist in the game
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Check if the enemy been collied with any notes in any lane
     * @param lanes list of lanes that exist in the game
     * @return true if the enemy is collied with any notes, false otherwise
     */
    public boolean isCollision(ArrayList<Lane> lanes) {
        for (Lane lane : lanes) {
            double laneX = lane.getLocation();
            for (AbstractNote n : lane.getNoteList()) {
                if (n.getNoteType().equals("Special")) {
                    continue;
                }
                double noteY = n.getY();
                double distance = Math.sqrt(Math.pow(this.x - laneX,2) + Math.pow(this.y - noteY,2));
                if (distance <= COLLISION_RANGE) {
                    n.deactivate();
                    return true;
                }
            }
        }
        return false;
    }

}
