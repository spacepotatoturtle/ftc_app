package Turtlecode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static Turtlecode.AutonomousConfig.*;
import static Turtlecode.HardwarePushturtl.*;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Blue Close to Audience", group ="AutonomousCode")
public class AutonomousCode extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);

    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();

        robot.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        encoderDriver.encoderDrive(4, 12, 12, 100);

        telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
    }
}