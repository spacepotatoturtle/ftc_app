
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Things", group="PushbotPotato")
public class New_Teleop extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double MASTER_MULTIPLIER        = 0.6;
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

            //robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            double RL = FORWARDNESS + STRAFENESS - TURNYNESS;
            double RR = FORWARDNESS - STRAFENESS + TURNYNESS;
            double FL = FORWARDNESS - STRAFENESS - TURNYNESS;
            double FR = FORWARDNESS + STRAFENESS + TURNYNESS;

            double MAX = Math.abs(Math.max(Math.max(RL, RR), Math.max(FL, FR)));

            robot.rearLeftDrive.setPower(MASTER_MULTIPLIER * RL / MAX);
            robot.rearRightDrive.setPower(MASTER_MULTIPLIER * RR / MAX);
            robot.frontLeftDrive.setPower(MASTER_MULTIPLIER * FL / MAX);
            robot.frontRightDrive.setPower(MASTER_MULTIPLIER * FR / MAX);

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.rearLeftDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rearRightDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.frontLeftDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.frontRightDrive.getPower());
            telemetry.addData("===[ENCODER VALUES]===", "");
            telemetry.addData("lf=", robot.frontLeftDrive.getCurrentPosition());
            telemetry.addData("lr=", robot.rearLeftDrive.getCurrentPosition());
            telemetry.addData("rf=", robot.frontRightDrive.getCurrentPosition());
            telemetry.addData("rr=", robot.rearRightDrive.getCurrentPosition());
            telemetry.update();
            sleep(25);
        }
    }
}