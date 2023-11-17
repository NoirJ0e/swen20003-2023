import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/**
 * Class for the guardian which shoots projectiles towards the enemies
 */
public class Guardian {
    private final Image image = new Image("res/guardian.PNG");
    private final Point location = new Point(800, 600);

    private final ArrayList<Projectile> projectiles = new ArrayList<>();


    /**
     * If enemy exists in the screen, user can input a key to shoot project towards the enemy
     * to eliminate it
     * @param input input from the user
     * @param enemies enemies list in the game, used to check if any enemies in the screen
     */
    public void update(Input input, ArrayList<Enemy> enemies) {
        draw();
        if (input.wasPressed(Keys.LEFT_SHIFT)) {
            // deal with the first enemy ever
            if (!enemies.isEmpty())
                // trying to add projectile
                projectiles.add(new Projectile(enemies.get(0)));
        }

        projectiles.forEach(projectile -> projectile.update(enemies));

        // check if collision with enemy, if so, remove the enemy and projectile

    }

    /**
     * Draw the guardian and all the projectiles
     */
    public void draw() {
        image.draw(location.x, location.y);
        projectiles.forEach(Projectile::draw);
    }

}
