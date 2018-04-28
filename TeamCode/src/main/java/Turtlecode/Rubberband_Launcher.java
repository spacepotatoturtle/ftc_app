/*
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import Turtlecode.HardwarePushturtl;

@TeleOp(name="Things", group="PushbotPotato")
public class Rubberband_Launcher extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double FORWARDNESS_MULTIPLIER   = 0.7;
    double STRAFENESS_MULTIPLIER    = 0.5;
    double TURNYNESS_MULTIPLIER     = 0.5;
    double GUNNYNESS_MULTIPLIER     = 0.1;
    double TRIGGERNESS = 0;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){

            robot.gun.setPower(0.0);

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;

            //robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.gun.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            robot.leftRearDrive.setPower((FORWARDNESS - STRAFENESS - TURNYNESS) / 3);
            robot.rightRearDrive.setPower((FORWARDNESS - STRAFENESS + TURNYNESS) / 3);
            robot.leftFrontDrive.setPower((FORWARDNESS + STRAFENESS - TURNYNESS) / 3);
            robot.rightFrontDrive.setPower((FORWARDNESS + STRAFENESS + TURNYNESS) / 3);

            if (gamepad1.left_bumper){
                robot.gun.setPower(-GUNNYNESS_MULTIPLIER);
            }
            else if (gamepad1.right_bumper) {
                robot.gun.setPower(GUNNYNESS_MULTIPLIER);
            }

            if (gamepad1.right_trigger == 0 && TRIGGERNESS > 0) {
                TRIGGERNESS -= 0.05;
            } else if (gamepad1.right_trigger != 0 && TRIGGERNESS < 1){
                TRIGGERNESS += 0.05;
            }

            robot.trigger.setPosition(TRIGGERNESS);

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.leftRearDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rightRearDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.leftFrontDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.rightFrontDrive.getPower());
            telemetry.addData("TURRET", robot.gun.getPower());
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
*/