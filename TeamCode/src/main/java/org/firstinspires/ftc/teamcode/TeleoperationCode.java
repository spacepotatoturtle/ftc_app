
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

@TeleOp(name="Telepathic Turtle", group="THE TURTLE")
public class TeleoperationCode extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();
    EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    PID_Shell pid = new PID_Shell(this, robot, telemetry);

    double MASTER_MULTIPLIER        = 0.6;
    double FORWARDNESS_MULTIPLIER   = 0.7;
    double STRAFENESS_MULTIPLIER    = 1;
    double TURNYNESS_MULTIPLIER     = 0.7;
    double AUXILIARY_TURNYNESS_MULTIPLIER = 0.00002;
    double HOOKPOWER_MULTIPLIER     = 1;
    double ARM_ANGLE_MULTIPLIER     = 0.8;
    double ARM_MAGNITUDE_MULTIPLIER = 0.8;
    double INTAKE_MULTIPLIER        = 1;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        Boolean precisionMode = false;
        Boolean flagMode = false;
        double flagtime = 0;
        ElapsedTime restTime = new ElapsedTime();
        double restPosition = 0;
        waitForStart();

        Context context = hardwareMap.appContext;
        int idColor = context.getResources().getIdentifier("teamColor", "id", context.getPackageName());
        int idFlag = context.getResources().getIdentifier("flaggyFlag", "id", context.getPackageName());
        Switch teamColor = (Switch) (((Activity)context).findViewById(idColor));
        Switch flag = (Switch) (((Activity)context).findViewById(idFlag));

        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
        while (opModeIsActive()){

            if (gamepad1.y && precisionMode) {
                precisionMode = false;
            } else if (gamepad1.a && !precisionMode) {
                precisionMode = true;
            }

            if (precisionMode) {
                MASTER_MULTIPLIER = 0.25;
                telemetry.addData("----------PRECISION MODE----------", "ON");
            } else {
                MASTER_MULTIPLIER = 0.6;
            }

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = (gamepad1.right_stick_x * TURNYNESS_MULTIPLIER) +
                    (gamepad2.right_stick_x * AUXILIARY_TURNYNESS_MULTIPLIER);

            double ARMANGLENESS= gamepad2.right_stick_y * ARM_ANGLE_MULTIPLIER;
            double INTAKENESS  = gamepad2.left_stick_y * INTAKE_MULTIPLIER;

            //robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE;
            //robot.hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            double RL = -FORWARDNESS - STRAFENESS - TURNYNESS;
            double RR = -FORWARDNESS + STRAFENESS + TURNYNESS;
            double FL = -FORWARDNESS + STRAFENESS - TURNYNESS;
            double FR = -FORWARDNESS - STRAFENESS + TURNYNESS;

            double MAX = Math.abs(Math.max(Math.max(RL, RR), Math.max(FL, FR)));

            robot.rearLeftDrive.setPower(MASTER_MULTIPLIER * RL / MAX);
            robot.rearRightDrive.setPower(MASTER_MULTIPLIER * RR / MAX);
            robot.frontLeftDrive.setPower(MASTER_MULTIPLIER * FL / MAX);
            robot.frontRightDrive.setPower(MASTER_MULTIPLIER * FR / MAX);

            if (ARMANGLENESS != 0) {
                robot.armPhi.setPower(ARMANGLENESS);
                restTime.reset();
                // restTime allows the robot to determine the initial position of the arm immediately after the arm stops moving.
            } else {
                while (restTime.milliseconds() < 75) {
                    restPosition = robot.armPhi.getCurrentPosition();
                }
                pid.pidLoop(robot.armPhi, restPosition, 40, 0, 0.0001, 0.1, 0);
            }

            robot.intake.setPower(INTAKENESS);

            if (gamepad1.left_trigger > 0.5) {
                robot.hook.setPower(HOOKPOWER_MULTIPLIER);
            } else if (gamepad1.left_bumper) {
                robot.hook.setPower(-HOOKPOWER_MULTIPLIER);
            } else {
                robot.hook.setPower(0);
            }

            if (gamepad2.dpad_up) {
                robot.armMagnitude.setPower(ARM_MAGNITUDE_MULTIPLIER);
            } else if (gamepad2.dpad_down) {
                robot.armMagnitude.setPower(-ARM_MAGNITUDE_MULTIPLIER);
            } else {
                robot.armMagnitude.setPower(0);
            }

            if (gamepad2.x) {
                robot.release.setPosition(0.4);
            } else {
                robot.release.setPosition(0);
            }

            if (gamepad2.x && flagMode) {
                flagMode = false;
            } else if (gamepad2.b && !flagMode) {
                flagMode = true;
            }

            if (flag.isChecked() && flagMode) {
                flagtime += 0.628318531; // pi/5
                robot.flag.setPosition(((Math.sin(flagtime)) / 9) + 0.4);
            }

            if (teamColor.isChecked()) {
                telemetry.addData("TEAM%3A", "BLUE");
            } else {
                telemetry.addData("TEAM%3A", "RED");
            }

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.rearLeftDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rearRightDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.frontLeftDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.frontRightDrive.getPower());
            telemetry.update();
            sleep(25);
        }
    }
}