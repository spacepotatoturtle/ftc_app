package Turtlecode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
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
    public DcMotor arm = null;
    public BNO055IMU imu = null;
    //public Servo clawLeft = null;
    //public Servo clawRight = null;
    //public DcMotor comb = null;
    //public Servo    trigger             = null;

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
        arm = hwMap.get(DcMotor.class, "ARM");
        imu = hwMap.get(BNO055IMU.class, "IMU");
        //clawLeft = hwMap.get(Servo.class, "CLAWL");
        //clawRight = hwMap.get(Servo.class, "CLAWR");
        //comb = hwMap.get(DcMotor.class, "COMB");
        //trigger = hwMap.get(Servo.class, "Trigger");
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        rearLeftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rearRightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        arm.setDirection(DcMotor.Direction.FORWARD);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //clawLeft.setPosition(0);
        //clawRight.setPosition(0);
        //comb.setDirection(DcMotor.Direction.FORWARD);
        //comb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //trigger.setPosition(0);
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
