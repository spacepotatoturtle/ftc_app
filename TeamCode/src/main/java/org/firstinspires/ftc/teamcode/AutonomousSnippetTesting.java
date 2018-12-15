
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
This op mode is used to test, debug, or calibrate any portion of the autonomous code individually,
as well as testing new experimental ideas.
*/

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Experimental Autonomous", group ="THE TURTLE")
public class AutonomousSnippetTesting extends LinearOpMode {

    /* Declare OpMode members. */

    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Loop pid_loop = new PID_Loop(this, telemetry);

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

        while (opModeIsActive()) {
            pid_loop.pidLoop(robot.armPhi, 50, 100, 0.3, 0.0001, 0.1, 0);
            sleep(100);
        }

        //telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
    }
}