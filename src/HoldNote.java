import bagel.*;

/**
 * Class for hold notes
 */
public class HoldNote extends AbstractNote {

    private static final int HEIGHT_OFFSET = 82;
    private static final String NOTE_TYPE = "Hold";
    private final static  int INIT_Y = 24;
    private boolean holdStarted = false;

    /**
     * Constructor for hold notes
     * @param dir direction of the note (UP, DOWN, LEFT, RIGHT), use to indicate which lane this note belongs to
     * @param appearanceFrame frame when the note appears
     */
    public HoldNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/holdNote" + dir + ".png"), INIT_Y);
    }

    /**
     * Mark the hold note as started, waiting to release
     */
    public void startHold() {
        holdStarted = true;
    }


    /**
     * scored twice, once at the start of the hold and once at the end
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return getY() + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return getY() - HEIGHT_OFFSET;
    }

    /**
     * Get the type of the note
     * @return Hold
     */
    public String getNoteType() {
        return NOTE_TYPE;
    }
}
