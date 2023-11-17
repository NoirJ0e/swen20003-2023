import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Abstract class for all notes
 */
abstract public class AbstractNote {
    private final Image image;
    private final int appearanceFrame;
    private final int speed = 2;
    private int y;
    private final int initY;
    private boolean active = false;
    private boolean completed = false;
    private static double SPEED_FACTOR = 1;

    /**
     * Constructor for the abstract note
     * @param appearanceFrame frame when the note appears
     * @param image image of the note
     * @param y Init y-coordinate of the note
     */
    public AbstractNote(int appearanceFrame, Image image, int y) {
        this.appearanceFrame = appearanceFrame;
        this.image = image;
        this.y = y;
        this.initY = y;
    }

    /**
     * Check if the note is active
     * @return true if the note is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Check if the note is completed
     * @return true if the note is completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Deactivate the note, mark it as complete and no longer exist in the game
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Update the note's Y-coordinate to make it moving in the screen, also spawn new note
     */
    public void update() {
        if (active) {
            y += (int) (speed + SPEED_FACTOR);
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * Draw the note on the screen, won't update the y-coordinate
     * @param x x-coordinate of the note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Get the y-coordinate of the note
     * @return current y-coordinate of the note
     */
    public int getY() {
        return this.y;
    }

    /**
     * Template method for checking the score of the note
     * @param input input from the user
     * @param accuracy accuracy object
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return score of the note
     */
    abstract public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey);

    /**
     * Set the speed factor of the note, affects the Y-coordinates updating step, not multiply but addition
     * @param speedFactor speed factor to be added to the current speed factor
     */
    public void setSpeedFactor(int speedFactor) {
        SPEED_FACTOR += speedFactor;
    }

    /**
     * Get the initial y-coordinate of the note (Y coordinate when the note first appears)
     * @return initial y-coordinate of the note
     */
    public int getInitY() {
        return initY;
    }

    /**
     * Get the type of the note
     * @return note type (Normal, Hold, Special)
     */
    abstract public String getNoteType();


}
