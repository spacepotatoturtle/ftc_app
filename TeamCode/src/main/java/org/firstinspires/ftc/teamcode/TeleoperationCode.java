
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.teamcode.EncoderDriver;

@TeleOp(name="Telepathic Turtle", group="THE TURTLE")
public class TeleoperationCode extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();
    EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);

    double MASTER_MULTIPLIER        = 0.4;
    double FORWARDNESS_MULTIPLIER   = 0.7;
    double STRAFENESS_MULTIPLIER    = 1;
    double TURNYNESS_MULTIPLIER     = 0.7;
    double HOOKPOWER_MULTIPLIER     = 1;
    double ARM_ANGLE_MULTIPLIER     = 0.5;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        double position = 0;
        encoderDriver.encoderArmAngle(1, position, 100);
        waitForStart();

        Context context = hardwareMap.appContext;
        int id = context.getResources().getIdentifier("teamColor", "id", context.getPackageName());
        Switch teamColor = (Switch) (((Activity)context).findViewById(id));

        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
        while (opModeIsActive()){

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;
            double HOOKPOWER   = gamepad2.right_stick_y * HOOKPOWER_MULTIPLIER;

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
            robot.hook.setPower(HOOKPOWER);

            if (gamepad1.left_trigger > 0.5) {
                robot.clawLeft.setPosition(0.6);
            } else {
                robot.clawLeft.setPosition(0.5);
            }

            if (gamepad1.right_trigger > 0.5) {
                robot.clawRight.setPosition(0.5);
            } else {
                robot.clawRight.setPosition(0.6);
            }

            if (gamepad1.dpad_down) {
                position += ARM_ANGLE_MULTIPLIER;
            } else if (gamepad1.dpad_up) {
                position -= ARM_ANGLE_MULTIPLIER;
            }
            encoderDriver.encoderArmAngle(1, position, 100);

            if (gamepad1.dpad_left) {
                robot.armMagnitude.setPower(0.4);
            } else if (gamepad1.dpad_right) {
                robot.armMagnitude.setPower(-0.4);
            } else {
                robot.armMagnitude.setPower(0);
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