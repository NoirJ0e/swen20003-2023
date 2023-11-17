import bagel.*;
import java.util.Iterator;

import java.util.ArrayList;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {
    /**
     * Constants for the score of each accuracy type, to avoid magic number
     */

    /**
     * Perfect condition score
     */
    public static final int PERFECT_SCORE = 10;
    /**
     * Good condition score
     */
    public static final int GOOD_SCORE = 5;
    /**
     * Bad condition score
     */
    public static final int BAD_SCORE = -1;
    /**
     * Miss condition score
     */
    public static final int MISS_SCORE = -5;
    /**
     * User have no input condition score, skip won't do anything
     */
    public static final int NOT_SCORED = 0;
    /**
     * Special note score when triggered successfully (Except BOMB)
     */
    public static final int SPECIAL_SCORE = 15;
    /**
     * Special flag to indicate the special note is missed
     */
    public static final int SPECIAL_MISS_FLAG = 1;
    /**
     * Special note score when triggered successfully (BOMB)
     */
    public static final int BOMB_SCORE = -1;
    /**
     * Messages to be displayed when the note accuracy is perfect
     */
    public static final String PERFECT = "PERFECT";
    /**
     * Messages to be displayed when the note accuracy is good
     */
    public static final String GOOD = "GOOD";
    /**
     * Messages to be displayed when the note accuracy is bad
     */
    public static final String BAD = "BAD";
    /**
     * Messages to be displayed when the note accuracy is miss
     */
    public static final String MISS = "MISS";
    /**
     * Messages to be displayed when the speedup note is triggered
     */
    public static final String SPEED_UP = "SPEED UP";
    /**
     * Messages to be displayed when the slowdown note is triggered
     */
    public static final String SLOW_DOWN = "SLOW DOWN";
    /**
     * Messages to be displayed when the double score note is triggered
     */
    public static final String DOUBLE = "Double SCORE";
    /**
     * Messages to be displayed when the boom note is triggered
     */
    public static final String BOMB = "LANE CLEAR";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;
    private static int SCORE_FACTOR = 1;
    private boolean DOUBLE_SCORE_FLAG = false;
    private static final int DOUBLE_SCORE_DURATION = 480;
    private final ArrayList<Integer> doubleScoreFrames = new ArrayList<>();


    /**
     * Set the accuracy type of the note, used to display the accuracy info
     * @param accuracy Accuracy type, e.g. PERFECT, GOOD, BAD, MISS
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Evaluate the score of special notes, e.g. SpeedUp
     * @param height Current height of the note
     * @param targetHeight Target height of the note
     * @param triggered Corresponding key been pressed or not
     * @param type Type of the special note
     * @return Score of the note, if is MISS will return 1, if the type is BOMB will return -1, otherwise return special note score
     */
    public int evaluateSpecialScore(int height, int targetHeight, boolean triggered, String type) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= GOOD_RADIUS) {
                setAccuracy(type);
                if (type.equals(BOMB)) {
                    return BOMB_SCORE;
                } else {
                    return SCORE_FACTOR * SPECIAL_SCORE;
                }
            } else {
                return SPECIAL_MISS_FLAG;
            }
        } else {
            if (height >= Window.getHeight()) {
                return SPECIAL_MISS_FLAG;
            }
        }

        return NOT_SCORED;
    }

    /**
     * Evaluate score for normal / holding notes
     * @param height Current height of the note
     * @param targetHeight Target height of the note
     * @param triggered Corresponding key been pressed or not
     * @return Score of the note
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return SCORE_FACTOR * PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return SCORE_FACTOR * GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return SCORE_FACTOR * BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return SCORE_FACTOR * MISS_SCORE;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    /**
     * Set current score factor multiply by 2 once, due to the double score note effect can be stack
     */
    public void setDoubleScore() {
        SCORE_FACTOR *= 2;
    }

    /**
     * Divide the current score factor by 2 once
     */
    public void resetScoreFactor() {
        SCORE_FACTOR /= 2;
    }

    /**
     * Set the double score flag to true, if true, score will be at least doubled
     */
    public void setDoubleScoreFlag() {
        DOUBLE_SCORE_FLAG = true;
    }

    /**
     * Set the double score flag to false, all double score note effect will be removed
     */
    private void endDoubleScoreFlag() {
        DOUBLE_SCORE_FLAG = false;
    }

    /**
     * Update the notes position in lane, also will check the accuracy of the note if corresponding key been pressed
     */
    public void update() {
        frameCount += ShadowDance.getFrameSpeed();
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    (double) Window.getWidth() /2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    (double) Window.getHeight() /2);
        }

        if (DOUBLE_SCORE_FLAG) {
            Iterator<Integer> iterator = doubleScoreFrames.iterator();
            while (iterator.hasNext()) {
                Integer frame = iterator.next();
                if (ShadowDance.getCurrFrame() - frame >= DOUBLE_SCORE_DURATION) {
                    resetScoreFactor();
                    iterator.remove();
                }
            }
            endDoubleScoreFlag();
        }
    }

    /**
     * Add the frame of double score effect start to the list, as double score effect can be stacked,
     * need to keep track of the start frame of each effect
     * @param frame Start frame of the double score effect
     */
    public void addDoubleScoreNotes(Integer frame) {
        doubleScoreFrames.add(frame);
    }
}
