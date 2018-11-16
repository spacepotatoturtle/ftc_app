package Turtlecode;

/**
 * Created by super on 4/19/2018.
 */

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Configurations for robot for team 12547
 */
public class AutonomousConfig {

    public static final int     ONE_SECOND_IN_MIL                   = 1000;
    public static final int     FIVE_SECONDS_IN_MIL                 = 5 * 1000; // 5 seconds

    /**
     * Set the team to the right color. BLUE or RED
     */
    public static int           TEAM_COLOR;
    public static boolean       NEAR_CRATER                         = false;
    /**
     * VuMark configurations
     */

    // Do not update
    public static final String  TAG = "Vuforia VuMark Sample";
    public static final String  VUFORIA_LICENSE_KEY =
            "Ac91sD3/////AAAAGSPvdhZYS0r1sQZSgPIqSDw2+8qYbU3ItiAGMo3p6u968Veqoa+BQvQ9TCJcsympdrdBAg0Q/sk3ctnS1KMjB93g7FSSTmAIbCx58u4HkhnipznO/S1npXm/aw+9e1zvEuiWmC37k01vi6rcFQlGNpTf0wlvYLdDyYnXj1ZjWQahvgI71SVOnjUzUWiDqb5KqTC6y6tHy76fr0VUKNskaXMILMyFTtMa/cAT79d5pnrScfIKXruQ+iv763BnePgxHheNZSQplT0ospS5AXXnDOvfc7y9E08ec9RhE64Ld6hADeaLX0X8FbZ/N8BWP5zCZRIN741SlvU7KoqPjayk/P846lLAqmn9Mum2blZH9Fzz";

    /**
     * Jewel hook servo configurations
     */
    public static final double JEWEL_ARM_VERTICAL_SERVO_POSITION    = 0.675;
    public static final double JEWEL_ARM_HORIZONTAL_SERVO_POSITION  = 0.1;
    public static final double JEWEL_ARM_SERVO_MOVING_STEP_CHANGE   = 0.025;

    // 50 milli-seconds before each servo move, so that the servo does not move too fast and sudden.
    public static final int SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS   = 50;

    /**
     * Color sensor configuration
     */

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    public static final double SCALE_FACTOR                         = 255;

    /**
     * AutonomousCode mode driving configurations
     */
    // *** two motors do not move evenly. Apply the factor below to the right wheel. ***
    public static final double AUTONOMOUSE_RIGHT_WHEEL_POWER_FACTOR = 1;
    // use 0.4 for the tournaments. 0.27 is for the floor

    public static final double ENCODER_RUN_SPEED                    = 0.1;
    public static final int REST_AFTER_ENCODER_RUN_MIL_SECONDS      = ONE_SECOND_IN_MIL / 4;

    public static final double NINETY_DEGREE_TURN                   = 7 * 3.14159265358979;
    public static final double ONE_DEGREE_TURN                      = NINETY_DEGREE_TURN / 90;

    public static final double COUNTS_PER_MOTOR_REV    = -1120; // if using tetrix motors, set number to 1440 eg: TETRIX Motor Encoder
    public static final double DRIVE_GEAR_REDUCTION    = 0.5;  // This is < 1.0 if geared UP
    public static final double WHEEL_DIAMETER_INCHES   = 4.0;  // For figuring circumference
    public static final double COUNTS_PER_INCH_WHEELS
            = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.14159265358979);

    public static final double COUNTS_PER_INCH_HOOK    = 27.7 * COUNTS_PER_INCH_WHEELS;
}
