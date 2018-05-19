package Turtlecode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by super on 2/10/2018.
 */

public class HardwarePushturtl {
    /* Public OpMode members. */
    public DcMotor  leftFrontDrive      = null;
    public DcMotor  rightFrontDrive     = null;
    public DcMotor  leftRearDrive       = null;
    public DcMotor  rightRearDrive      = null;
    //public DcMotor  gun                 = null;
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
        leftFrontDrive = hwMap.get(DcMotor.class, "Timmy");
        rightFrontDrive = hwMap.get(DcMotor.class, "Panda");
        leftRearDrive = hwMap.get(DcMotor.class, "Johnny");
        rightRearDrive = hwMap.get(DcMotor.class, "Turtle");
        //gun = hwMap.get(DcMotor.class, "Gun");
        //trigger = hwMap.get(Servo.class, "Trigger");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        //gun.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //gun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //trigger.setPosition(0);
    }
}
