import bagel.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for the projectiles which shoot towards the enemies
 */
public class Projectile {
    private int x = 800, y = 600;
    private final Image image = new Image("res/arrow.PNG");
    private final double xSpeed;
    private final double ySpeed;
    private static final int TARGET_SPEED = 6;
    private static final int COLLISION_RANGE = 62;
    private final DrawOptions drawOptions = new DrawOptions();
    private boolean active;

    /**
     * Constructor for the projectile, shoot towards the enemy
     * @param enemy enemy which the projectile shoot towards;
     * used to calculate the rotation and moving speed of this projectile
     */
    public Projectile(Enemy enemy) {
        active = true;
        double xDiff = x - enemy.getX();
        double yDiff = y - enemy.getY();
        double rotation = Math.atan2(yDiff, xDiff) + Math.PI;

        double length = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        double cosValue = xDiff / length;
        double sinValue = yDiff / length;

        xSpeed = TARGET_SPEED * cosValue;
        ySpeed = TARGET_SPEED * sinValue;

        drawOptions.setRotation(rotation);

    }

    /**
     * Update the projectile's x and y coordinates to make it moving in the screen, also check if collide with enemy
     * @param enemies enemies list in the game, used to check if any enemies in the screen
     */
    public void update(ArrayList<Enemy> enemies) {
        if (!active) {
            return;
        }
        draw();
        x -= (int) xSpeed;
        y -= (int) ySpeed;

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy e = iterator.next();
            if (isCollision(e)) {
                iterator.remove(); // Safely remove the inactive projectile
                deactivate();
            }
        }
    }

    /**
     * Draw the projectile on the screen, won't update the x and y coordinates
     */
    public void draw() {
        if (!active) {
            return;
        }
        image.draw(x, y, drawOptions);
    }

    /**
     * Check if the projectile hit the enemy
     * @param enemy enemy which the projectile may collide with
     * @return true if the projectile hit the enemy, false otherwise
     */
    public boolean isCollision(Enemy enemy) {
        boolean collision = false;
        double distance = Math.sqrt(Math.pow((enemy.getX()) - x, 2) + Math.pow(enemy.getY() - y, 2));
        if (distance <= COLLISION_RANGE) {
            collision = true;
        }
        return collision;
    }

    /**
     * Deactivate the projectile, mark it as complete and no longer exist in the game
     */
    public void deactivate() {
        active = false;
    }

}
