
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.lang.Math;

/*
This is the main teleoperational op mode of the robot.

LAYOUT OF CODE:
The functions of the robot are split among the two drivers -- the first controls how the robot moves
around the field (driving) & the second driver focuses on scoring minerals (arm & intake mechanism).
There are various methods of giving more control to the drivers, mainly based on changing the speeds
of motor movement.
*/

@TeleOp(name="Telepathic Turtle", group="THE TURTLE")
public class TeleoperationCode extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();
    PID_Loop pid = new PID_Loop(this, telemetry);

    // Coefficients to control speed of the various robot functions
    // First driver's function coefficients
    double MASTER_DRIVE_MULTIPLIER          = 0.6;
    double FORWARDNESS_MULTIPLIER           = 0.7;
    double STRAFENESS_MULTIPLIER            = 1;
    double TURNYNESS_MULTIPLIER             = 0.7;
    double AUXILIARY_TURNYNESS_MULTIPLIER   = 0.00002;

    // Second driver's function coefficients
    double HOOKPOWER_MULTIPLIER             = 1;
    double ARM_ANGLE_MULTIPLIER             = 0.8;
    double ARM_MAGNITUDE_MULTIPLIER         = 0.8;
    double INTAKE_MULTIPLIER                = 1;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        ElapsedTime restTime = new ElapsedTime();

        // Initialize modes of movement, variables, and IMU information to be used in teleop
        Boolean precisionMode = false;
        Boolean flagMode = false;
        double flagtime = 0;
        double restPosition = 0;
        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 100);

        waitForStart();

        /*
        This code calls information from the UI of the app, where we implemented switches that set
        values such as team color and the optional flag wave option, which will be used later.
        */
        Context context = hardwareMap.appContext;
        int idColor = context.getResources().getIdentifier("teamColor",
                                                           "id", context.getPackageName());
        int idFlag  = context.getResources().getIdentifier("flaggyFlag",
                                                           "id", context.getPackageName());
        Switch teamColor = (Switch) (((Activity)context).findViewById(idColor));
        Switch flag = (Switch) (((Activity)context).findViewById(idFlag));

        while (opModeIsActive()){


            /*
            ----------------------------------------------------------------------------------------
                                              <DRIVER 1 FUNCTIONS>
            ----------------------------------------------------------------------------------------
            */

            /* Allows the first driver to move with precision when needed by decreasing power of all
            drive motors. */

            if (gamepad1.y && precisionMode) {
                precisionMode = false;
            } else if (gamepad1.a && !precisionMode) {
                precisionMode = true;
            }

            if (precisionMode) {
                MASTER_DRIVE_MULTIPLIER = 0.25;
                telemetry.addData("----------PRECISION MODE----------", "ON");
            } else {
                MASTER_DRIVE_MULTIPLIER = 0.6;
            }


            /* Three components of robot movement: Forwards/Backwards, Left/Right, and Turning.
            NOTE: The turn value of the robot is controlled by the second driver to a slight
            degree for precision and control when the second driver tries to pick up minerals. */

            double FORWARDNESS = gamepad1.left_stick_y   * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x   * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = (gamepad1.right_stick_x * TURNYNESS_MULTIPLIER)
                               + (gamepad2.right_stick_x * AUXILIARY_TURNYNESS_MULTIPLIER);


            /* All three components of robot movement are combined into smooth motion in the motors.
            */

            double RL = -FORWARDNESS - STRAFENESS - TURNYNESS;
            double RR = -FORWARDNESS + STRAFENESS + TURNYNESS;
            double FL = -FORWARDNESS + STRAFENESS - TURNYNESS;
            double FR = -FORWARDNESS - STRAFENESS + TURNYNESS;

            /* MAX value ensures the motors are not told to run beyond their peaks while maintaining
            proportions between three components of movement. */

            double MAX = Math.abs(Math.max(Math.max(RL, RR), Math.max(FL, FR)));

            robot.rearLeftDrive.setPower(MASTER_DRIVE_MULTIPLIER * RL / MAX);
            robot.rearRightDrive.setPower(MASTER_DRIVE_MULTIPLIER * RR / MAX);
            robot.frontLeftDrive.setPower(MASTER_DRIVE_MULTIPLIER * FL / MAX);
            robot.frontRightDrive.setPower(MASTER_DRIVE_MULTIPLIER * FR / MAX);


            /* Telemetry for reference. (Mostly debugging.) */
            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.rearLeftDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rearRightDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.frontLeftDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.frontRightDrive.getPower());

            /*
            ----------------------------------------------------------------------------------------
                                             </ DRIVER 1 FUNCTIONS>
            ----------------------------------------------------------------------------------------

            ----------------------------------------------------------------------------------------
                                              <DRIVER 2 FUNCTIONS>
            ----------------------------------------------------------------------------------------
            */

            /* The arm has two controls: its angle (phi) and length (magnitude).
            NOTE: The BRAKE zero power behavior of the motor for the arm angle is not strong enough
            to hold up the weight of the arm, so a PID loop is used to keep the motor in the same
            place if the driver is not controlling it. */

            double ARMANGLENESS = gamepad2.right_stick_y * ARM_ANGLE_MULTIPLIER;
            if (ARMANGLENESS != 0) {
                robot.armPhi.setPower(ARMANGLENESS);
                restTime.reset();
            } else {
                // Sets the target value as the initial position of arm after going to rest
                while (restTime.milliseconds() < 75) {
                    restPosition = robot.armPhi.getCurrentPosition();
                }
                pid.pidLoop(robot.armPhi, restPosition,
                            40, 0, 0.0001, 0.1, 0);
            }

            if (gamepad2.dpad_up) {
                robot.armMagnitude.setPower(ARM_MAGNITUDE_MULTIPLIER);
            } else if (gamepad2.dpad_down) {
                robot.armMagnitude.setPower(-ARM_MAGNITUDE_MULTIPLIER);
            } else {
                robot.armMagnitude.setPower(0);
            }


            /* The claw-like intake and release mechanism. (For reference, the intake is a
            continuous servo.) */

            double INTAKENESS = gamepad2.left_stick_y * INTAKE_MULTIPLIER;
            robot.intake.setPower(INTAKENESS);

            if (gamepad2.x) {
                robot.release.setPosition(1);
            } else {
                robot.release.setPosition(0.65);
            }

            /*
            ----------------------------------------------------------------------------------------
                                             </ DRIVER 2 FUNCTIONS>
            ----------------------------------------------------------------------------------------

            ----------------------------------------------------------------------------------------
                                             <ADDITIONAL FUNCTIONS>
            ----------------------------------------------------------------------------------------
            */

            /* First driver gets controls for the hook that is used to latch onto the lander in the
            endgame. */

            if (gamepad1.left_trigger > 0.5) {
                robot.hook.setPower(HOOKPOWER_MULTIPLIER);
            } else if (gamepad1.left_bumper) {
                robot.hook.setPower(-HOOKPOWER_MULTIPLIER);
            } else {
                robot.hook.setPower(0);
            }


            /* OPTIONAL: The robot can wave the team flag around with a servo. */

            if (gamepad1.x && flagMode) {
                flagMode = false;
            } else if (gamepad1.b && !flagMode) {
                flagMode = true;
            }

            if (flag.isChecked() && flagMode) {
                flagtime += 0.628318531; // pi/5
                robot.flag.setPosition(((Math.sin(flagtime)) / 9) + 0.4);
            }

            /* Team color telemetry based on switch in UI (for reference). */
            if (teamColor.isChecked()) {
                telemetry.addData("TEAM%3A", "BLUE");
            } else {
                telemetry.addData("TEAM%3A", "RED");
            }

            /*
            ========================================================================================
                                             </ ADDITIONAL FUNCTIONS>
            ========================================================================================
            */

            telemetry.update();
            sleep(25);
        }
    }
}