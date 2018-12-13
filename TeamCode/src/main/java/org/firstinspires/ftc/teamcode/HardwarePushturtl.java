package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

/**
 * Created by super on 2/10/2018.
 */

public class HardwarePushturtl {
    /* Public OpMode members. */
    public DcMotor frontLeftDrive = null;
    public DcMotor frontRightDrive = null;
    public DcMotor rearLeftDrive = null;
    public DcMotor rearRightDrive = null;
    public DcMotor hook = null;
    public DcMotor armPhi = null;
    public DcMotor armMagnitude = null;
    public BNO055IMU imu = null;
    public CRServo intake = null;
    public Servo release = null;
    public Servo flag = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushturtl()
    {
    //
    }

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontLeftDrive = hwMap.get(DcMotor.class, "FL");
        frontRightDrive = hwMap.get(DcMotor.class, "FR");
        rearLeftDrive = hwMap.get(DcMotor.class, "RL");
        rearRightDrive = hwMap.get(DcMotor.class, "RR");
        hook = hwMap.get(DcMotor.class, "HOOK");
        armPhi = hwMap.get(DcMotor.class, "PHI");
        armMagnitude = hwMap.get(DcMotor.class, "EXT");
        imu = hwMap.get(BNO055IMU.class, "IMU");
        release = hwMap.get(Servo.class, "RELEASE");
        intake = hwMap.get(CRServo.class, "INTAKE");
        flag = hwMap.get(Servo.class, "FLAG");
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        rearLeftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rearRightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        hook.setDirection(DcMotor.Direction.FORWARD);
        armPhi.setDirection(DcMotor.Direction.FORWARD);
        armPhi.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMagnitude.setDirection(DcMotor.Direction.FORWARD);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(0);
        release.setPosition(0.65);
        flag.setPosition(0);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }
}
