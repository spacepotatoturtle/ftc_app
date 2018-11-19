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
        encoderDriver.encoderDrive(0.3, 6, -6, -6, 6, 30);
        encoderDriver.encoderDrive(0.3, -17, -17, -17, -17, 30);
        encoderDriver.encoderDrive(0.3, -2, 2, 2, -2, 30);
        if (NEAR_CRATER) { // it is on the cater rim, rotations are same for both sides
            encoderDriver.encoderDrive(0.3, 48, -48, -48, 48, 30);
            encoderDriver.encoderDrive(0.3, -34, 34, -34, 34, 30);
            encoderDriver.encoderDrive(0.3, -6, 6, 6, -6, 30);
            encoderDriver.encoderDrive(0.3, -40, -40, -40, -40, 30);
            // depot
            robot.depositor.setPosition(0.3);
            sleep(300);
            robot.depositor.setPosition(1);
            encoderDriver.encoderDrive(0.3, 78, 78, 78, 78, 30);
        } else { // it is near the depot
            encoderDriver.encoderDrive(0.3, -40, -40, -40, -40, 30);
            // depot
            robot.depositor.setPosition(0.3);
            sleep(300);
            robot.depositor.setPosition(1);
            encoderDriver.encoderDrive(0.3, 12, -12, 12, -12, 30);
            encoderDriver.encoderDrive(0.3, 4, -4, -4, 4, 30);
            encoderDriver.encoderDrive(0.3, 78, 78, 78, 78, 30);
        }


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
        robot.depositor.setPosition(1);
    }
}