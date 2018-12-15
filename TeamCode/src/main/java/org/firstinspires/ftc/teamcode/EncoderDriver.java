package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.AutonomousValues.COUNTS_PER_INCH_HOOK;
import static org.firstinspires.ftc.teamcode.AutonomousValues.COUNTS_PER_INCH_WHEELS;

/**
Separate class that contains encoder drive mode methods used heavily in the autonomous op mode.
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
    Main encoder drive method (drives all wheel motors a certain distance). In this method, there
    are three forms of movement: Forward/Backward, Left/Right, and Turning, which are defined in a
    string parameter. Then, the method takes a distance parameter in inches which determines the
    distance (or angle) that it moves by.
    NOTE: Each type of movement is generated by the wheels spinning against each other in the same
    way the teleoperation op mode does.
    */
    void encoderDrive(double speed, String type, double Inches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        ElapsedTime runtime = new ElapsedTime();

        /* Ensures that the opmode is still active. */

        if (opMode.opModeIsActive()) {

            /* First, the method determines the new target position of the wheel from the old one.
            */

            if (type.equals("Forward")) {

                /* Forward/Backwards type of movement. Negative Inches parameter goes backwards,
                positive Inches parameter goes forwards. */

                newLeftTarget      = robot.frontLeftDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget     = robot.frontRightDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget  = robot.rearLeftDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);

            } else if (type.equals("Strafe")) {

                /* Left/Right type of movement. Negative Inches parameter goes left, positive Inches
                parameter goes right. */

                newLeftTarget      = robot.frontLeftDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget     = robot.frontRightDrive.getCurrentPosition()
                                   + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget  = robot.rearLeftDrive.getCurrentPosition()
                                   + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);

            } else if (type.equals("Turn")) {

                /* Turning type of movement. Negative Inches parameter turns left, positive Inches
                parameter turns right. */

                newLeftTarget      = robot.frontLeftDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightTarget     = robot.frontRightDrive.getCurrentPosition()
                                   + (int) (-Inches * COUNTS_PER_INCH_WHEELS);
                newLeftBackTarget  = robot.rearLeftDrive.getCurrentPosition()
                                   + (int) (Inches * COUNTS_PER_INCH_WHEELS);
                newRightBackTarget = robot.rearRightDrive.getCurrentPosition()
                                   + (int) (-Inches * COUNTS_PER_INCH_WHEELS);

            } else {

                /* Error message in case of wrong implementation of method. */

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

            /* Turns on RUN_TO_POSITION, allowing it to use the target position determined
            previously. */

            robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rearLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rearRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            /* Having determined the position target, each wheel motor starts rotating until one of
            the following occurs:
                1) the op mode terminates.
                2) the safety runtime parameter is exceeded.
                3) all wheels have reached the target position specified. */

            runtime.reset();
            robot.frontLeftDrive.setPower(Math.abs(speed));
            robot.frontRightDrive.setPower(Math.abs(speed));
            robot.rearLeftDrive.setPower(Math.abs(speed));
            robot.rearRightDrive.setPower(Math.abs(speed));

            while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.frontLeftDrive.isBusy() && robot.frontRightDrive.isBusy()
                        && robot.rearRightDrive.isBusy() && robot.rearLeftDrive.isBusy())) {

                // NOTE: We use (isBusy() && isBusy()) in the loop test, which means that when
                // EITHER motor hits its target position, the motion will stop.  This is "safer" in
                // the event that the robot will always end the motion as soon as possible. However,
                // if you require that BOTH motors have finished their moves before the robot
                // continues onto the next step, use (isBusy() || isBusy()) in the loop test.

            }

            /* Stops all motion once one of the above three conditions is exceeded. */

            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            robot.rearLeftDrive.setPower(0);
            robot.rearRightDrive.setPower(0);

            /* Telemetry for debugging. */

            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.frontLeftDrive.getCurrentPosition(),
                    robot.frontRightDrive.getCurrentPosition(),
                    robot.rearLeftDrive.getCurrentPosition(),
                    robot.rearRightDrive.getCurrentPosition());
            telemetry.update();

            /* Turns of RUN_TO_POSITION. */
            robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rearLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rearRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /*
    Secondary method that controls the hook for dismounting from the lander. Instead of basing on a
    distance, however, it goes to an absolute position, much like a servo, which is better suited
    for a situation where the hook is attempting to go to an unchanging length that is long enough
    to allow the robot to mount/dismount from the lander.
    */

    void encoderHook(double speed, double hookInches, double timeoutS) {
        int newHookTarget;

        ElapsedTime runtime = new ElapsedTime();

        if (opMode.opModeIsActive()) {

            /* NOTE: The code does not factor the current position of the robot, unlike the
            encoderDrive method. Otherwise, this method is nearly identical. */

            newHookTarget = (int) (hookInches * COUNTS_PER_INCH_HOOK);
            robot.hook.setTargetPosition(newHookTarget);

            robot.hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.hook.setPower(Math.abs(speed));

            while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.hook.isBusy())) {
                telemetry.addData("Target ", newHookTarget);
                telemetry.addData("Current Position ", robot.hook.getCurrentPosition());
                telemetry.update();
            }

            robot.hook.setPower(0);

            robot.hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    void init() {

        /* Initialize the drive system variables. The init() method of the hardware class does all
        the work here. */

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

        /* Sends telemetry message to indicate successful Encoder reset */

        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.frontLeftDrive.getCurrentPosition(),
                robot.frontRightDrive.getCurrentPosition(),
                robot.rearLeftDrive.getCurrentPosition(),
                robot.rearRightDrive.getCurrentPosition());
        telemetry.update();
    }
}