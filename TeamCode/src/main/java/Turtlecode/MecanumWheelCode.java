
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import Turtlecode.HardwarePushturtle;


@TeleOp(name="Things", group="PushbotPotato")
public class MecanumWheelCode extends LinearOpMode {

    HardwarePushturtle robot = new HardwarePushturtle();

    double FORWARDNESS_MULTIPLIER   = 0.1;
    double STRAFENESS_MULTIPLIER   = 0.1;
    double TURNYNESS_MULTIPLIER     = 0.1;

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