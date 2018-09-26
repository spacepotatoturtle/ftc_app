
package Turtlecode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
--------------------PID Shell----------------------
This is a shell to plan out the architecture of the PID Loop.
This loop is for maintaining velocity.
NOTE: There are position and acceleration recorders, however, using those instead of manually
    calculating the derivative and integral would require more complexity and parts (target
    acceleration, target position, separate error calculation, etc.)
---------------------------------------------------
*/

class PID_Shell {

    private LinearOpMode autonomousMode;
    private HardwarePushturtl robot;
    private Telemetry telemetry;

    PID_Shell(LinearOpMode autonomousMode, HardwarePushturtl robot, Telemetry telemetry) {

        this.autonomousMode = autonomousMode;
        this.robot = robot;
        this.telemetry = telemetry;

    }

    void pidLoop(double meters, double seconds, double precision) {

        ElapsedTime runtime = new ElapsedTime();

        double vTarget = meters / seconds;

        double kP = 1;
        double kD = 1;
        double kI = 1;

        double vMag;
        double err;
        double errP = 0;
        double errS = 0;
        double P;
        double D;
        double I;
        double correction;

        robot.rearLeftDrive.setPower(0);
        robot.rearRightDrive.setPower(0);
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);

        runtime.reset();

        while (autonomousMode.opModeIsActive() &&
              (runtime.seconds() < seconds) &&
              (robot.frontLeftDrive.isBusy() && robot.frontRightDrive.isBusy() && robot.rearRightDrive.isBusy() && robot.rearLeftDrive.isBusy())) {

            vMag = Math.pow(Math.pow(robot.imu.getVelocity().xVeloc, 2) +
                            Math.pow(robot.imu.getVelocity().yVeloc, 2) +
                            Math.pow(robot.imu.getVelocity().zVeloc, 2), 0.5);
            err = vTarget - vMag;
            errS = errS + err;

            P = kP * err;
            D = kD * (err - errP) / precision;
            I = kI * errS * precision;
            correction = P + D + I;

            robot.rearLeftDrive.setPower(robot.rearLeftDrive.getPower() + correction);
            robot.rearRightDrive.setPower(robot.rearRightDrive.getPower() + correction);
            robot.frontLeftDrive.setPower(robot.frontLeftDrive.getPower() + correction);
            robot.frontRightDrive.setPower(robot.frontRightDrive.getPower() + correction);

            errP = vTarget - vMag;

            telemetry.addData("Velocity Error:", robot.imu.getVelocity());
            telemetry.update();

            autonomousMode.sleep((long) precision);
        }

        robot.rearLeftDrive.setPower(0);
        robot.rearRightDrive.setPower(0);
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);
    }
}