import bagel.*;

/**
 * Class for speed up notes
 */
public class SpeedUpNote extends AbstractNote {
    private final static int INIT_Y = 100;
    private final static String NOTE_TYPE = "Special";



    /**
     * Constructor for speed up notes
     * @param dir direction of the note (UP, DOWN, LEFT, RIGHT), use to indicate which lane this note belongs to
     * @param appearanceFrame frame when the note appears
     */
    public SpeedUpNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteSpeedUp.PNG"), INIT_Y);
    }

    /**
     * Checks the score of the note, increase the speed of note of the game
     * @param input input from the user
     * @param accuracy accuracy object
     * @param targetHeight target height of the note
     * @param relevantKey relevant key to be pressed
     * @return special note score if triggered, 1 if missed
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SPEED_UP);
            if (score != Accuracy.NOT_SCORED && score != Accuracy.SPECIAL_MISS_FLAG) {
                deactivate();
                setSpeedFactor(1);
                ShadowDance.setFrameSpeed(2);
                return score;
            } else if (score == Accuracy.SPECIAL_MISS_FLAG) {
                System.out.println("Missed SpeedUp Note");
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
