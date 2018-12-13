package org.firstinspires.ftc.teamcode;
/**
 * Configurations for robot for team 12547
 */
public class AutonomousValues {

    public static final int     ONE_SECOND_IN_MIL                   = 1000;

    /**
     * VuMark configurations
     */

    // Do not update
    public static final String  TAG = "Vuforia VuMark Sample";
    public static final String  VUFORIA_LICENSE_KEY =
            "Aavx5Qz/////AAAAGcwMlzKgX0wGhaL/CWYlFeYtE7dnCKQ/AmzFYGtGHUG3ogKwS0lKfxLktmZubI0SFjkA0xBQx+lc6YrVsLxFiYPanQ0dDL5iHD/hvadlnV1tLcsDCZjLJebarCLU8doOAYoH/aN24ASjgMcsiGnLcgwCFtQGZFU3/8osQG6JYojfVlm52hJD5hGcOorVyHCnu0AWQbfgIHgAJPEy9IcMEjHPkniM0AlynU7CJhTmXOwqlxKsyRMaTFQZq7NRtenUS7Ug9Bva8mPa2MMb4bVURntfetUve8cMffYJOxr5iywNJzNXtpPJUP5yfbGcGRiKZa/DxOMHTfej1d9/8p9cT/btC52zrwmD058MqJ3F9P4B";

    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    // 50 milli-seconds before each servo move, so that the servo does not move too fast and sudden.
    public static final int SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS   = 50;

    public static final double ENCODER_RUN_SPEED                    = 0.1;
    public static final int REST_AFTER_ENCODER_RUN_MIL_SECONDS      = ONE_SECOND_IN_MIL / 4;

    public static final double NINETY_DEGREE_TURN                   = 7 * 3.14159265358979;
    public static final double ONE_DEGREE_TURN                      = NINETY_DEGREE_TURN / 90;

    public static final double COUNTS_PER_MOTOR_REV    = 1120; // if using tetrix motors, set number to 1440 eg: TETRIX Motor Encoder
    public static final double DRIVE_GEAR_REDUCTION    = 0.5;  // This is < 1.0 if geared UP
    public static final double WHEEL_DIAMETER_INCHES   = 4.0;  // For figuring circumference
    public static final double COUNTS_PER_INCH_WHEELS
            = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.14159265358979);

    public static final double COUNTS_PER_INCH_HOOK    = 18.4 * COUNTS_PER_INCH_WHEELS;
}
