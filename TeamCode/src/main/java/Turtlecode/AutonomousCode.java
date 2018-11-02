package Turtlecode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import static Turtlecode.AutonomousConfig.*;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous", group ="AutonomousCode")
public class AutonomousCode extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Shell pid_shell = new PID_Shell(this, robot, telemetry);
    private ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the robot
        initRobot();
        ElapsedTime runtime = new ElapsedTime();

        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final double SCALE_FACTOR = 255;

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        //pid_shell.pidLoop(0, 3, 100);
        /*
        runtime.reset();
        while (runtime.seconds() < 1) {
            robot.hook.setPower(1);
        }
        */
        encoderDriver.encoderHook(0.6, 0, 30);
        sleep(500);
        encoderDriver.encoderDrive(0.7, -13, 13, 13, -13, 100);
        encoderDriver.encoderDrive(0.7, -25, -25, -25, -25, 100);
        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues);
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.update();
        while (colorSensor.blue() > 50) {
            encoderDriver.encoderDrive(0.7, 17, -17, -17, 17, 100);
        }
        encoderDriver.encoderDrive(0.7, -6, -6, -6, -6, 100);

        //while (robot.imu.getAngularOrientation().firstAngle < 90) {
        //robot.frontLeftDrive.setPower(-0.3);
        //robot.rearLeftDrive.setPower(-0.3);
        //robot.frontRightDrive.setPower(0.3);
        //robot.rearLeftDrive.setPower(0.3);
        //}
        //encoderDriver.encoderDrive(0.5, NINETY_DEGREE_TURN, -NINETY_DEGREE_TURN, NINETY_DEGREE_TURN, -NINETY_DEGREE_TURN, 100);

        telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
        colorSensor = robot.hwMap.get(ColorSensor.class, "CS");
    }
}