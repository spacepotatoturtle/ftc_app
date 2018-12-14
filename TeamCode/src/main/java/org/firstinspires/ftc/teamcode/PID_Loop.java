
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
--------------------------------------------PID Loop-------------------------------------------
This class is a PID (Proportional, Integral, Derivative) Loop which gives better control of the
robot in both the autonomous and teleoperational op modes. It can be used with any motor inside
a loop with time increments (such as the teleop) but must be calibrated for three coefficients:
kP, kI, and kD. The parameter updateTime specifies the time taken for the loop to repeat.
-----------------------------------------------------------------------------------------------
*/

class PID_Loop {

    // Initializes variables
    private LinearOpMode autonomousMode;
    private Telemetry telemetry;

    private double value;
    private double err;
    private double errP = 0;
    private double errS = 0;
    private double P;
    private double D;
    private double I;
    private double correction;

    PID_Loop(LinearOpMode autonomousMode, Telemetry telemetry) {

        this.autonomousMode = autonomousMode;
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