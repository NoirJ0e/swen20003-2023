import bagel.*;

/**
 * Class for bomb notes
 */
public class BombNote extends AbstractNote {
    private final static int INIT_Y = 100;
    private final static String NOTE_TYPE = "Special";

    /**
     * Constructor for bomb notes
     * @param dir direction of the note (UP, DOWN, LEFT, RIGHT), use to indicate which lane this note belongs to
     * @param appearanceFrame frame when the note appears
     */
    public BombNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteBomb.PNG"), INIT_Y);
    }

    /**
     * Checks the score of the note, marking bomb in Accuracy class
     * @param input input from the user
     * @param accuracy accuracy object
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return -1 if triggered, 0 otherwise
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.BOMB);
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }
        }
        return Accuracy.NOT_SCORED;
    }

    /**
     * Get the type of the note
     * @return Special
     */
    public String getNoteType() {
        return NOTE_TYPE;
    }
}