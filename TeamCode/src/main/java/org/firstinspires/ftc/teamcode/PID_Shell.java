
package org.firstinspires.ftc.teamcode;


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

    void pidLoop(double angleTarget, double seconds, double precision) {
        if (autonomousMode.opModeIsActive()) {

            ElapsedTime runtime = new ElapsedTime();

            double kP = 0.3;
            double kD = 0;
            double kI = 0;

            double angle;
            double err;
            double errP = 0;
            double errS = 0;
            double P;
            double D;
            double I;
            double correction;

            robot.rearLeftDrive.setPower(0.3);
            robot.rearRightDrive.setPower(0.3);
            robot.frontLeftDrive.setPower(0.3);
            robot.frontRightDrive.setPower(0.3);

            runtime.reset();

            while (autonomousMode.opModeIsActive() &&
                    (runtime.seconds() < seconds) /*&&
                    (robot.frontLeftDrive.isBusy() && robot.frontRightDrive.isBusy() && robot.rearRightDrive.isBusy() && robot.rearLeftDrive.isBusy())*/) {

                angle = robot.imu.getAngularOrientation().firstAngle;
                err = angleTarget - angle;
                errS = errS + err;

                P = kP * err;
                D = kD * (err - errP) / precision;
                I = kI * errS * precision;
                correction = P + D + I;

                robot.rearLeftDrive.setPower(robot.rearLeftDrive.getPower() - correction);
                robot.rearRightDrive.setPower(robot.rearRightDrive.getPower() + correction);
                robot.frontLeftDrive.setPower(robot.frontLeftDrive.getPower() - correction);
                robot.frontRightDrive.setPower(robot.frontRightDrive.getPower() + correction);

                errP = angleTarget - angle;

                telemetry.addData("Angle Error:", robot.imu.getAngularOrientation());
                telemetry.update();

                autonomousMode.sleep((long) precision);
            }

            robot.rearLeftDrive.setPower(0);
            robot.rearRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
        }
    }
}