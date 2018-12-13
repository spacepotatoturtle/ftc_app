
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    double value;
    double err;
    double errP = 0;
    double errS = 0;
    double P;
    double D;
    double I;
    double correction;

    PID_Shell(LinearOpMode autonomousMode, HardwarePushturtl robot, Telemetry telemetry) {

        this.autonomousMode = autonomousMode;
        this.robot = robot;
        this.telemetry = telemetry;

    }

    void pidLoop(DcMotor motor, double target, double updateTime, double initPower, double kP, double kD, double kI) {

        if (autonomousMode.opModeIsActive()) {

            motor.setPower(initPower);

            value = motor.getCurrentPosition();
            err = target - value;
            errS = errS + err;

            P = kP * err;
            D = kD * (err - errP) / updateTime;
            I = kI * errS * updateTime;
            correction = P + D + I;
            motor.setPower(motor.getPower() + correction);

            errP = target - value;

            telemetry.addData("Error:", err);
        }
    }
}