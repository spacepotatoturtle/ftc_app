
package Turtlecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name="Things", group="PushbotPotato")
public class New_Teleop extends LinearOpMode {

    HardwarePushturtl robot = new HardwarePushturtl();

    double MASTER_MULTIPLIER        = 0.4;
    double FORWARDNESS_MULTIPLIER   = 1;
    double STRAFENESS_MULTIPLIER    = 1;
    double TURNYNESS_MULTIPLIER     = 1;
    double ARMPOWER_MULTIPLIER = 0.7;
    double COMBPOWER_MULTIPLIER = 0.4;
    double TRIGGERNESS = 0;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
        while (opModeIsActive()){

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;
            double ARMPOWER    = gamepad1.right_stick_y * ARMPOWER_MULTIPLIER;

            //robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            double RL = -FORWARDNESS - STRAFENESS + TURNYNESS;
            double RR = -FORWARDNESS + STRAFENESS - TURNYNESS;
            double FL = -FORWARDNESS + STRAFENESS + TURNYNESS;
            double FR = -FORWARDNESS - STRAFENESS - TURNYNESS;

            double MAX = Math.abs(Math.max(Math.max(RL, RR), Math.max(FL, FR)));

            robot.rearLeftDrive.setPower(MASTER_MULTIPLIER * RL / MAX);
            robot.rearRightDrive.setPower(MASTER_MULTIPLIER * RR / MAX);
            robot.frontLeftDrive.setPower(MASTER_MULTIPLIER * FL / MAX);
            robot.frontRightDrive.setPower(MASTER_MULTIPLIER * FR / MAX);
            robot.arm.setPower(ARMPOWER_MULTIPLIER * ARMPOWER);

            if (gamepad1.left_trigger > 0.5) {
            //    robot.clawLeft.setPosition(0.25);
            } else {
            //    robot.clawLeft.setPosition(1);
            }

            if (gamepad1.right_trigger > 0.5) {
            //    robot.clawRight.setPosition(0.75);
            } else {
            //    robot.clawRight.setPosition(0);
            }

            /*
            if (gamepad1.right_bumper) {
                robot.comb.setPower(COMBPOWER_MULTIPLIER);
            } else if (gamepad1.left_bumper) {
                robot.comb.setPower(-COMBPOWER_MULTIPLIER);
            } else {
                robot.comb.setPower(0);
            }
            */

            telemetry.addData("Forwardness%3A", FORWARDNESS);
            telemetry.addData("Strafeness%3A", STRAFENESS);
            telemetry.addData("Turnyness%3A", TURNYNESS);
            telemetry.addData("LEFT REAR", robot.rearLeftDrive.getPower());
            telemetry.addData("RIGHT REAR", robot.rearRightDrive.getPower());
            telemetry.addData("LEFT FRONT", robot.frontLeftDrive.getPower());
            telemetry.addData("RIGHT FRONT", robot.frontRightDrive.getPower());
            telemetry.addData("===[IMU VALUES]===", "");
            telemetry.addData("test", robot.imu.getAcceleration());
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