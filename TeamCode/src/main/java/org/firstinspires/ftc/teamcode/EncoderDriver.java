package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.AutonomousValues.COUNTS_PER_INCH_HOOK;
import static org.firstinspires.ftc.teamcode.AutonomousValues.COUNTS_PER_INCH_WHEELS;
import static org.firstinspires.ftc.teamcode.AutonomousValues.COUNTS_PER_MOTOR_REV;
import static org.firstinspires.ftc.teamcode.AutonomousValues.REST_AFTER_ENCODER_RUN_MIL_SECONDS;

/**
 * Encapsulation of the encoder mode drive.
 */
class EncoderDriver {

    private LinearOpMode opMode;
    private HardwarePushturtl robot;
    private Telemetry telemetry;

    EncoderDriver(LinearOpMode opMode, HardwarePushturtl robot, Telemetry telemetry) {
        this.opMode = opMode;
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
    void encoderDrive(double speed, String type, double Inches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        ElapsedTime runtime = new ElapsedTime();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (type.equals("Forward")) {
                newLeftTarget = robot.frontLeftDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget = robot.frontRightDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget = robot.rearLeftDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
            } else if (type.equals("Strafe")) {
                newLeftTarget = robot.frontLeftDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget = robot.frontRightDrive.getCurrentPosition() + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget = robot.rearLeftDrive.getCurrentPosition() + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
            } else if (type.equals("Turn")) {
                newLeftTarget = robot.frontLeftDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget = robot.frontRightDrive.getCurrentPosition() + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget = robot.rearLeftDrive.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition() + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
            } else {
                telemetry.addData("ERROR", "WRONG TYPE FOR ENCODERDRIVER");
                newLeftTarget = robot.frontLeftDrive.getCurrentPosition();
                newRightTarget = robot.frontRightDrive.getCurrentPosition();
                newLeftBackTarget = robot.rearLeftDrive.getCurrentPosition();
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition();
            }

            robot.frontLeftDrive.setTargetPosition(newLeftTarget);
            robot.frontRightDrive.setTargetPosition(newRightTarget);
            robot.rearLeftDrive.setTargetPosition(newLeftBackTarget);
            robot.rearRightDrive.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rearLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rearRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeftDrive.setPower(Math.abs(speed));
            robot.frontRightDrive.setPower(Math.abs(speed));
            robot.rearLeftDrive.setPower(Math.abs(speed));
            robot.rearRightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.frontLeftDrive.isBusy() && robot.frontRightDrive.isBusy() && robot.rearRightDrive.isBusy() && robot.rearLeftDrive.isBusy())) {

            }

            // Display it for the driver.
            // constant value, not needed to see. telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.frontLeftDrive.getCurrentPosition(),
                    robot.frontRightDrive.getCurrentPosition(),
                    robot.rearLeftDrive.getCurrentPosition(),
                    robot.rearRightDrive.getCurrentPosition());
            telemetry.update();

            // Stop all motion;
            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            robot.rearLeftDrive.setPower(0);
            robot.rearRightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // sleep(250);   // optional pause after each move
        }
    }

    //Servo-like Behavior
    void encoderHook(double speed, double hookInches, double timeoutS) {
        int newHookTarget;

        ElapsedTime runtime = new ElapsedTime();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            // NOTE: The code is a modified encoderDriver that allows the motor to behave like a servo
            newHookTarget = (int) (hookInches * COUNTS_PER_INCH_HOOK);
            robot.hook.setTargetPosition(newHookTarget);

            // Turn On RUN_TO_POSITION
            robot.hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.hook.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.hook.isBusy())) {
                telemetry.addData("Target ", newHookTarget);
                telemetry.addData("Current Position ", robot.hook.getCurrentPosition());
                telemetry.update();
            }

                // Display it for the driver.
                // constant value, not needed to see. telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                //telemetry.addData("Path2", "Running at %7d :%7d",
                    //robot.hook.getCurrentPosition());
                //telemetry.update();

                // Stop all motion;
            robot.hook.setPower(0);

                // Turn off RUN_TO_POSITION
            robot.hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                // sleep(250);   // optional pause after each move
        }
    }

    //Servo-like Behavior
    void encoderArmAngle(double speed, double armRadians, double timeoutS) {
        int newAngleTarget;

        ElapsedTime runtime = new ElapsedTime();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newAngleTarget = (int) (armRadians * COUNTS_PER_MOTOR_REV);
            robot.armPhi.setTargetPosition(newAngleTarget);

            // Turn On RUN_TO_POSITION
            robot.armPhi.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.armPhi.isBusy())) {
                robot.armPhi.setPower(speed);
            }

            // Display it for the driver.
            // constant value, not needed to see. telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            //telemetry.addData("Path2", "Running at %7d :%7d",
            //robot.hook.getCurrentPosition());
            //telemetry.update();

            // Stop all motion;
            robot.armPhi.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.armPhi.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // sleep(250);   // optional pause after each move
        }
    }

    // Apply zero power to the motors to stop.
    void stopMotorsAndRestShortly() {
        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opMode.sleep(REST_AFTER_ENCODER_RUN_MIL_SECONDS);
    }

    void init() {
                /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(opMode.hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.frontLeftDrive.getCurrentPosition(),
                robot.frontRightDrive.getCurrentPosition(),
                robot.rearLeftDrive.getCurrentPosition(),
                robot.rearRightDrive.getCurrentPosition());
        telemetry.update();
    }
}