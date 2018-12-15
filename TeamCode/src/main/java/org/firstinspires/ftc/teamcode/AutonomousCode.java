package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.AutonomousValues.*;

/**
This is the main autonomous op mode used in the competition.

LAYOUT OF THE CODE:
The code consists of two branches, depending on the starting position of the robot -- one for the
initial position closer to the crater and one for the position closer to the depot. The branch
chosen depends on the value of a switch in the RC app UI. Each path goes in the order of first
landing from the lander, displacing the correct mineral type among the samples, depositing the team
marker in the depot, and going to the crater. #) will mark the steps the robot takes in each branch.
*/

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Self-Sufficient Turtle", group ="THE TURTLE")
public class AutonomousCode extends LinearOpMode {

    /* Declares OpMode members. */

    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware
    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Loop pid_loop = new PID_Loop(this, telemetry);

    /* Calls the Vuforia engine which is used for the Tensor Flow Object Detection to determine
    types of minerals. */

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {

        /* Initializes robot and TFObjectDetector (as well as Vuforia). */
        // Initialize the robot
        initRobot();
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }


        /* The code calls the value of a switch which belongs in the UI of the Robot Controller app,
        which we use to tell the robot whether it starts closer to the crater or the depot, which
        changes the path the robot takes. */

        Context context = hardwareMap.appContext;
        int id = context.getResources().getIdentifier("craterDistance", "id", context.getPackageName());
        Switch craterDistance = (Switch) (((Activity)context).findViewById(id));


        /* Motors are turned to BRAKE mode, which gives them precision for autonomous. */

        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        /* Waits for the autonomous phase to begin. */

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();


        /* 1) De-attaches from the lander and moves forward and left. Determines which mineral to
        displace. */

        encoderDriver.encoderHook(0.6, 0, 30);
        sleep(500);
        encoderDriver.encoderDrive(0.3, "Strafe", -6, 30);
        encoderDriver.encoderDrive(0.3, "Forward", 14, 30);
        encoderDriver.encoderDrive(0.3, "Strafe", -16, 30);


        /* This value determines represents of times the robot shifts right from the perspective of
        facing the minerals from this point to when it determines which mineral is the gold one.
        This is a convenient and compact way of later separating the robots movements to knock the
        correct mineral WITHOUT clumsy if-else statements. */

        int numShifts;

        /* Activate Tensor Flow Object Detection. The robot detects for an object by moving from
        left to right, stopping and activating the object detection in front of every mineral to
        determine it's "goldness", and logging each one into an array (Minerals). Then it determines
        which one it is most confident is the gold one, and takes a certain path to knock that one
        out. */

        List<Double> Minerals = new ArrayList<>(3);

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            for (int number_of_checks = 1; number_of_checks <= 3;){
                if (tfod.getUpdatedRecognitions().size() != 0) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 1) {
                            double Mineral = 0;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    Mineral = (double) recognition.getConfidence();
                                    //telemetry.addData("Value", Mineral);
                                } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL)) {
                                    Mineral = -((double) recognition.getConfidence());
                                    //telemetry.addData("Silver, Confidence: ", recognition.getConfidence());
                                }
                            }
                            Minerals.add(Mineral);
                        }
                        telemetry.addData("Value", Minerals);
                        telemetry.update();
                    }
                } else {
                    Minerals.add(0.0);
                }
                sleep(5000);
                if (number_of_checks != 3) {
                    encoderDriver.encoderDrive(0.3, "Strafe", 14.5, 30);
                }
                number_of_checks ++;
            }
        }

        /* Determines path based on which mineral is the gold mineral. */

        if (Minerals.get(1) > Minerals.get(2) && Minerals.get(1) > Minerals.get(3)) {
            numShifts = 2;
        } else if (Minerals.get(2) > Minerals.get(1) && Minerals.get(1) > Minerals.get(3)) {
            numShifts = 1;
        } else {
            numShifts = 0;
        }

        /* 2) Moves a certain amount back to the left to align itself with the gold mineral and
        moves forward a bit to displace it. */

        encoderDriver.encoderDrive(0.3, "Strafe", -14.5 * numShifts, 30);
        //encoderDriver.encoderDrive(0.3,"Strafe", 6, 30);
        encoderDriver.encoderDrive(0.3, "Forward", 5, 30);

        /* Now, the path splits into two possibilities, based on the value of the switch on the RC
        app UI. This is because the switch is how we tell the robot which position it starts from
        (the crater or the depot side). */

        if (craterDistance.isChecked()) {

            /* Starts closer to the depot. 3) The robot is currently able to be in three independent
            paths: one for each possible position of the golden mineral sample. In order to merge
            them, all three paths reunite at a common point beyond the sample minerals and in front
            of the depot. */

            encoderDriver.encoderDrive(0.3, "Forward", 36, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -14.5 * (1 - numShifts), 30);

            /* 4) The robot drops the team marker into the depot. */

            robot.flag.setPosition(0.6);
            sleep(2000);

            /* 5) The robot drives to the crater. */

            encoderDriver.encoderDrive(0.3, "Forward", -12, 30);
            encoderDriver.encoderDrive(0.3, "Turn", -12, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -24, 30);
            encoderDriver.encoderDrive(0.3, "Forward", -70, 30);

        } else {

            /* Starts closer to the crater. 3) The robot reunifies the three paths like in the other
            branch, except the reunification position is halfway between the crater and the depot,
            since using the same reunification point as the other branch is blocked by the crater.
            */

            encoderDriver.encoderDrive(0.3, "Forward", -5, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -24 - 14.5 * numShifts, 30);

            /* 4) Turns toward and runs to the depot. */

            encoderDriver.encoderDrive(0.3, "Turn", -34, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", 24, 30);
            encoderDriver.encoderDrive(0.3, "Forward", 52, 30);

            /* 5) Drops team marker. */

            robot.flag.setPosition(0.6);
            sleep(300);

            /* 6) Drives back to the crater. */

            encoderDriver.encoderDrive(0.3, "Forward", -72, 30);
        }

        telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    /*
    This method initializes the encoderDriver class heavily used throughout the autonomous op mode.
    */

    private void initRobot() {
        encoderDriver.init();
    }

    /*
    This method initializes the Vuforia localization engine to be used in the op mode.
    */

    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /*
    Initializes the Tensor Flow Object Detection engine.
    */

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}