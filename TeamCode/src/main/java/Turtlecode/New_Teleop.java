
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import Turtlecode.HardwarePushturtl;

@TeleOp(name="Things", group="PushbotPotato")
public class New_Teleop extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double FORWARDNESS_MULTIPLIER   = 1;
    double STRAFENESS_MULTIPLIER    = 1;
    double TURNYNESS_MULTIPLIER     = 1;
    double GUNNYNESS_MULTIPLIER     = 0.1;
    double TRIGGERNESS = 0;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;

            //robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            robot.leftRearDrive.setPower((FORWARDNESS - STRAFENESS - TURNYNESS) / 3);
            robot.rightRearDrive.setPower((FORWARDNESS - STRAFENESS + TURNYNESS) / 3);
            robot.leftFrontDrive.setPower((FORWARDNESS + STRAFENESS - TURNYNESS) / 3);
            robot.rightFrontDrive.setPower((FORWARDNESS + STRAFENESS + TURNYNESS) / 3);

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.leftRearDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rightRearDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.leftFrontDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.rightFrontDrive.getPower());
            telemetry.addData("===[ENCODER VALUES]===", "");
            telemetry.addData("lf=", robot.leftFrontDrive.getCurrentPosition());
            telemetry.addData("lr=", robot.leftRearDrive.getCurrentPosition());
            telemetry.addData("rf=", robot.rightFrontDrive.getCurrentPosition());
            telemetry.addData("rr=", robot.rightRearDrive.getCurrentPosition());
            telemetry.update();
            sleep(25);
        }
    }
}