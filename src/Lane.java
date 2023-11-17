import bagel.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private Keys relevantKey;
    private final int location;

    // ArrayList to store any kind of note
    private final ArrayList<AbstractNote> abstractNotes = new ArrayList<>();

    /**
     * Constructor for the lane
     * @param dir direction of the lane (UP, DOWN, LEFT, RIGHT, SPECIAL)
     * @param location x-coordinates of the lane
     */
    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    /**
     * Get the direction of the lane
     * @return direction of the lane (UP, DOWN, LEFT, RIGHT, SPECIAL)
     */
    public String getType() {
        return type;
    }

    /**
     * updates all the notes in the lane
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (AbstractNote n: abstractNotes) {
            n.update();
        }

        if (!abstractNotes.isEmpty()) {
            AbstractNote currentNote = abstractNotes.get(0);
            int score = currentNote.checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (currentNote.isCompleted()) {
                abstractNotes.remove(0);
                // check if is a bomb
                if (score == Accuracy.BOMB_SCORE) {
                    score = 0;
                    // remove all notes in the screen
                    Iterator<AbstractNote> iterator = abstractNotes.iterator();
                    while (iterator.hasNext()) {
                        AbstractNote n = iterator.next();
                        if (n.isActive() && n.getY() > n.getInitY()) {
                            n.deactivate();
                            iterator.remove();
                        }
                    }
                } else if (score == Accuracy.SPECIAL_MISS_FLAG) {
                    score = 0;
                }
            }
            return score;
        }

        return Accuracy.NOT_SCORED;
    }

    /**
     * adds a note to the lane
     * @param n note to be added
     */
    public void addNote(AbstractNote n) {
        abstractNotes.add(n);
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        return abstractNotes.isEmpty();
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);
        for (AbstractNote n : abstractNotes) {
            n.draw(location);
        }
    }

    /**
     * gets the location of the lane
     * @return x-coordinate of the lane
     */
    public int getLocation() {
        return location;
    }

    /**
     * gets the list of notes in the lane
     * @return list of notes in the lane
     */
    public ArrayList<AbstractNote> getNoteList() {
        return abstractNotes;
    }

}
