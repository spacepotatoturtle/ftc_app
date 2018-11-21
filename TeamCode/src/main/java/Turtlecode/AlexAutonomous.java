package Turtlecode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static Turtlecode.AutonomousConfig.*;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AlexAutonomous", group ="AutonomousCode")
public class AlexAutonomous extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Shell pid_shell = new PID_Shell(this, robot, telemetry);
    private ColorSensor colorSensor;

    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();
        ElapsedTime runtime = new ElapsedTime();

        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        // while (runtime.seconds() < 1) {
        //    robot.frontLeftDrive.setPower(0.1);
        //    robot.frontRightDrive.setPower(0.1);
        //    robot.rearLeftDrive.setPower(0.1);
        //    robot.rearRightDrive.setPower(0.1);
        //}

        //pid_shell.pidLoop(0, 3, 100);
        //encoderDriver.encoderDrive(0.7, 84, 84, 84, 84, 100);
        //encoderDriver.encoderDrive(0.7, -33, 33, -33, 33, 100);
        //encoderDriver.encoderDrive(0.7, 90, 90, 90, 90, 100);
        //encoderDriver.encoderDrive(0.1, 12, 12, 12, 12, 100);
        //encoderDriver.encoderDrive(0.1, -22, 22, -22, 22, 100);
        //encoderDriver.encoderDrive(0.1, -12, 12, 12, -12, 100);

        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues);

        telemetry.addData("Alpha", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);
        sleep(5000);

        //telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
        colorSensor = robot.hwMap.get(ColorSensor.class, "CS");
    }
}