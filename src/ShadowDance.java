import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Solution for Project 2 Part B, extends from Example Solution given by University of Melbourne
 *
 * @author ZILIN XU 1262248
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int END_MSG_HEIGHT = 500;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final static String CSV_FILE_1 = "res/level1.csv";
    private final static String CSV_FILE_2 = "res/level2.csv";
    private final static String CSV_FILE_3 = "res/level3.csv";
    /**
     * Font source path used in the game
     */
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static String TEST_CSV = "res/Test_With_DoubleScore.csv";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private final Font END_MSG_FONT = new Font(FONT_FILE, 24);
    private static final String INSTRUCTIONS = "  SELECT LEVEL WITH\n      NUMBER KEYS    \n\n           1 2 3";
    private static final String END_MSG = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static int CLEAR_SCORE;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private Accuracy accuracy;
    private ArrayList<Lane> lanes;
    private int score;
    private static int currFrame;
    private static int FRAME_SPEED = 1;
    private Track track;
    private boolean started;
    private boolean finished;
    private boolean paused;
    private boolean spawnGuardian = false;

    private ArrayList<Enemy> enemies;
    private Guardian guardian;

    /**
     * Constructor for the game
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }




    private void readCsv(String file) {

        // re-initialise the variables for each level
        accuracy = new Accuracy();
        lanes = new ArrayList<>();
        enemies = new ArrayList<>();
        guardian = new Guardian();

        score = 0;
        currFrame = 0;
        started = false;
        finished = false;



        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes.add(lane);
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;

                    for (Lane l: lanes) {
                        if (l.getType().equals(dir)) {
                            lane = l;
                            break;
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                NormalNote normalNote = new NormalNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(normalNote);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(holdNote);
                                break;
                            case "BombNote":
                                BombNote bombNote = new BombNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(bombNote);
                                break;
                            case "DoubleScore":
                                DoubleScoreNote doubleScoreNote = new DoubleScoreNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(doubleScoreNote);
                                break;
                            case "SpeedUp":
                                SpeedUpNote speedUpNote = new SpeedUpNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(speedUpNote);
                                break;
                            case "SlowDown":
                                SlowDownNote slowDownNote = new SlowDownNote(Integer.parseInt(splitText[2]));
                                lane.addNote(slowDownNote);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

            if (input.wasPressed(Keys.NUM_1)) {
                readCsv(CSV_FILE_1);
                track = new Track("res/track1.wav");
                CLEAR_SCORE = 150;
                started = true;
                track.start();
            } else if (input.wasPressed(Keys.NUM_2)) {
                readCsv(CSV_FILE_2);
                track = new Track("res/track2.wav");
                CLEAR_SCORE = 400;
                started = true;
                track.start();
            } else if (input.wasPressed(Keys.NUM_3)) {
                readCsv(CSV_FILE_3);
                track = new Track("res/track3.wav");
                CLEAR_SCORE = 350;
                started = true;
                spawnGuardian = true;
                track.start();
            } else if (input.wasPressed(Keys.T)) {
                readCsv(TEST_CSV);
                started = true;
            }
        } else if (finished) {
            // end screen
            if (score >= CLEAR_SCORE) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        (double) WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        (double) WINDOW_HEIGHT/2);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        (double) WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        (double) WINDOW_HEIGHT/2);
            }

            END_MSG_FONT.drawString(END_MSG,
                    (double) WINDOW_WIDTH/2 - END_MSG_FONT.getWidth(END_MSG)/2,
                    END_MSG_HEIGHT);

            if (input.wasPressed(Keys.SPACE)) {
                started = false;
            }

        } else {
            // gameplay

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    track.pause();
                }

                lanes.forEach(Lane::draw);
                if (spawnGuardian) {
                    guardian.draw();
                    enemies.forEach(Enemy::draw);
                }

            } else {
                currFrame += FRAME_SPEED;

                if (spawnGuardian) {
                    guardian.update(input, enemies);
                    if (currFrame % 600 == 0 && !finished) {
                        enemies.add(new Enemy());
                    }
                    enemies.forEach(Enemy::update);

                    // loop each lane to check collision with enemies, if so, remove the enemy and note
                    // also update enemy
                    Iterator<Enemy> iterator = enemies.iterator();
                    while (iterator.hasNext()) {
                        Enemy e = iterator.next();
                        if (e.isCollision(lanes)) {
                            iterator.remove(); // Safely remove the inactive projectile
                            e.deactivate();
                        }
                    }
                }

                lanes.forEach(lane -> score+=lane.update(input, accuracy));



                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    track.pause();
                }
            }
        }

    }

    /**
     * Get the current frame of the game
     * @return current frame of the game
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    private boolean checkFinished() {
        for (Lane lane : lanes) {
            if (!lane.isFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the frame speed of the game
     * @param speed new frame speed
     */
    public static void setFrameSpeed(int speed) {
        FRAME_SPEED = speed;
    }

    /**
     * Reset the frame speed of the game to 1
     */
    public static void resetFrameSpeed() {
        FRAME_SPEED = 1;
    }

    /**
     * Get the frame speed of the game
     * @return current frame speed of the game
     */
    public static int getFrameSpeed() {
        return FRAME_SPEED;
    }

}
