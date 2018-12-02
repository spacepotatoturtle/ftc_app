package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Chicken Nugget", group ="AutonomousCode")
public class AutonomousSnippetTesting extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Shell pid_shell = new PID_Shell(this, robot, telemetry);

    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();
        ElapsedTime runtime = new ElapsedTime();

        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        //pid_shell.pidLoop(0, 3, 100);
        //encoderDriver.encoderDrive(0.7, 12, 12, 12, 12, 100);
        //encoderDriver.encoderDrive(0.7, -33, 33, -33, 33, 100);
        //encoderDriver.encoderDrive(0.7, 90, 90, 90, 90, 100);
        //encoderDriver.encoderDrive(0.1, 12, 12, 12, 12, 100);
        //encoderDriver.encoderDrive(0.1, -22, 22, -22, 22, 100);
        //encoderDriver.encoderDrive(0.1, -12, 12, 12, -12, 100);

        encoderDriver.encoderHook(0.5, 5.5, 30);

        sleep(5000);

        //telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
    }
}