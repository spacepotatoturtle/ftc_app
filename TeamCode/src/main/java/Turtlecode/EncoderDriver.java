package Turtlecode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static Turtlecode.AutonomousConfig.AUTONOMOUSE_RIGHT_WHEEL_POWER_FACTOR;
import static Turtlecode.AutonomousConfig.COUNTS_PER_INCH;
import static Turtlecode.AutonomousConfig.REST_AFTER_ENCODER_RUN_MIL_SECONDS;

/**
 * Encapsulation of the encoder mode drive.
 */
class EncoderDriver {

    private LinearOpMode autonomousMode;
    private HardwarePushturtl robot;
    private Telemetry telemetry;

    EncoderDriver(LinearOpMode autonomousMode, HardwarePushturtl robot, Telemetry telemetry) {
        this.autonomousMode = autonomousMode;
        this.robot = robot;
        this.telemetry = telemetry;
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    void encoderDrive(double speed, double leftFrontInches, double rightFrontInches, double leftRearInches, double rightRearInches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        ElapsedTime runtime = new ElapsedTime();

        // Ensure that the opmode is still active
        if (autonomousMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftFrontDrive.getCurrentPosition() + (int) (leftFrontInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightFrontDrive.getCurrentPosition() + (int) (rightFrontInches * COUNTS_PER_INCH);
            newLeftBackTarget = robot.leftRearDrive.getCurrentPosition() + (int) (leftRearInches * COUNTS_PER_INCH);
            newRightBackTarget = robot.rightRearDrive.getCurrentPosition() + (int) (rightRearInches * COUNTS_PER_INCH);
            robot.leftFrontDrive.setTargetPosition(newLeftTarget);
            robot.rightFrontDrive.setTargetPosition(newRightTarget);
            robot.leftRearDrive.setTargetPosition(newLeftBackTarget);
            robot.rightRearDrive.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFrontDrive.setPower(Math.abs(speed));
            robot.rightFrontDrive.setPower(Math.abs(speed));
            robot.leftRearDrive.setPower(Math.abs(speed));
            robot.rightRearDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (autonomousMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy() && robot.rightRearDrive.isBusy() && robot.leftRearDrive.isBusy())) {

            }

            // Display it for the driver.
            // constant value, not needed to see. telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.leftFrontDrive.getCurrentPosition(),
                    robot.rightFrontDrive.getCurrentPosition(),
                    robot.leftRearDrive.getCurrentPosition(),
                    robot.rightRearDrive.getCurrentPosition());
            telemetry.update();

            // Stop all motion;
            robot.leftFrontDrive.setPower(0);
            robot.rightFrontDrive.setPower(0);
            robot.leftRearDrive.setPower(0);
            robot.rightRearDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // sleep(250);   // optional pause after each move
        }
    }

    // Apply zero power to the motors to stop.
    void stopMotorsAndRestShortly() {
        robot.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        autonomousMode.sleep(REST_AFTER_ENCODER_RUN_MIL_SECONDS);
    }

    void init() {
                /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(autonomousMode.hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.leftFrontDrive.getCurrentPosition(),
                robot.rightFrontDrive.getCurrentPosition(),
                robot.leftRearDrive.getCurrentPosition(),
                robot.rightRearDrive.getCurrentPosition());
        telemetry.update();
    }
}