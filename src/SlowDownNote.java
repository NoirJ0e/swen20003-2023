import bagel.*;

/**
 * Class for slow down notes
 */
public class SlowDownNote extends AbstractNote {
    private final static int INIT_Y = 100;
    private final static String NOTE_TYPE = "Special";

    /**
     * Constructor for slow down notes
     * @param appearanceFrame frame when the note appears
     */
    public SlowDownNote(int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteSlowDown.PNG"), INIT_Y);
    }


    /**
     * Checks the score of the note, marking slow down in Accuracy class
     * @param input input from the user
     * @param accuracy accuracy object
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return special note score if triggered, 1 if missed
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SLOW_DOWN);
            if (score != Accuracy.NOT_SCORED && score != Accuracy.SPECIAL_MISS_FLAG) {
                ShadowDance.resetFrameSpeed();
                setSpeedFactor(-1);
                deactivate();
                return score;
            } else if (score == Accuracy.SPECIAL_MISS_FLAG) {
                System.out.println("Missed SlowDown Note");
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
