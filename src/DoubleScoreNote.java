import bagel.*;

/**
 * Class for double score notes
 */
public class DoubleScoreNote extends AbstractNote {
    private final static int INIT_Y = 100;
    private final static String NOTE_TYPE = "Special";

    /**
     * Constructor for double score notes
     * @param dir direction of the note (UP, DOWN, LEFT, RIGHT), use to indicate which lane this note belongs to
     * @param appearanceFrame frame when the note appears
     */
    public DoubleScoreNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/note2x.PNG"), INIT_Y);
    }


    /**
     * Checks the score of the note, marking double score flag in Accuracy class
     * if triggered, will also add to the double score notes list in Accuracy and handle the effect in Accuracy class
     * @param input input from the user
     * @param accuracy accuracy object
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return special note score if triggered, 1 if missed
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.DOUBLE);
            if (score != Accuracy.NOT_SCORED && score != Accuracy.SPECIAL_MISS_FLAG) {
                accuracy.setDoubleScore();
                accuracy.setDoubleScoreFlag();
                accuracy.addDoubleScoreNotes(ShadowDance.getCurrFrame());
                deactivate();
                return score;
            } else if (score == Accuracy.SPECIAL_MISS_FLAG) {
                System.out.println("Missed Double Score Note");
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
