import bagel.*;

/**
 * Class for normal notes
 */
public class NormalNote extends AbstractNote {
    private final static int INIT_Y = 100;
    private final static String NOTE_TYPE = "Normal";

    /**
     * Constructor for normal notes
     * @param dir direction of the note (UP, DOWN, LEFT, RIGHT), use to indicate which lane this note belongs to
     * @param appearanceFrame frame when the note appears
     */
    public NormalNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/note" + dir + ".png"), INIT_Y);
    }

    /**
     * Checks the score of the note, marking standard in Accuracy class
     * @param input input from the user
     * @param accuracy accuracy object to be updated
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return score of the note
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(getY(), targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }

        }

        return Accuracy.NOT_SCORED;
    }

    /**
     * Get the type of the note
     * @return Normal
     */
    public String getNoteType() {
        return NOTE_TYPE;
    }

}
