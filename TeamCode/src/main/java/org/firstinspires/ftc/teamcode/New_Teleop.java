
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name="TELE-OP", group="PushbotPotato")
public class New_Teleop extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double MASTER_MULTIPLIER        = 0.4;
    double FORWARDNESS_MULTIPLIER   = 0.7;
    double STRAFENESS_MULTIPLIER    = 1;
    double TURNYNESS_MULTIPLIER     = 0.7;
    double HOOKPOWER_MULTIPLIER     = 1;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
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
            //robot.arm.setPower(ARMPOWER);

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
                robot.armLeft.setPosition(0);
            } else {
                robot.armLeft.setPosition(1);
            }

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.rearLeftDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rearRightDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.frontLeftDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.frontRightDrive.getPower());
            telemetry.addData("ArmLeft", robot.armLeft.getPosition());
            telemetry.update();
            sleep(25);
        }
    }
}