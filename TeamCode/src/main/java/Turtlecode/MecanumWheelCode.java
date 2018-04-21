
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import Turtlecode.HardwarePushturtl;

@TeleOp(name="Things", group="PushbotPotato")
public class MecanumWheelCode extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double FORWARDNESS_MULTIPLIER   = 0.5;
    double STRAFENESS_MULTIPLIER   = 0.5;
    double TURNYNESS_MULTIPLIER     = 0.5;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;

            robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            robot.leftRearDrive.setPower((FORWARDNESS - STRAFENESS - TURNYNESS) / 3);
            robot.rightRearDrive.setPower((FORWARDNESS - STRAFENESS + TURNYNESS) / 3);
            robot.leftFrontDrive.setPower((FORWARDNESS + STRAFENESS - TURNYNESS) / 3);
            robot.rightFrontDrive.setPower((FORWARDNESS + STRAFENESS + TURNYNESS) / 3);

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.update();

            sleep(25);
        }
    }
}