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

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Self-Sufficient Turtle", group ="THE TURTLE")
public class AutonomousCode extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushturtl robot = new HardwarePushturtl();   // Use a Pushbot's hardware
    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private PID_Shell pid_shell = new PID_Shell(this, robot, telemetry);

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the robot
        initRobot();
        ElapsedTime runtime = new ElapsedTime();

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        Context context = hardwareMap.appContext;
        int id = context.getResources().getIdentifier("craterDistance", "id", context.getPackageName());
        Switch craterDistance = (Switch) (((Activity)context).findViewById(id));

        robot.frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        encoderDriver.encoderHook(0.6, 0, 30);
        sleep(500);
        encoderDriver.encoderDrive(0.3, "Strafe", -6, 30);
        encoderDriver.encoderDrive(0.3, "Forward", 14, 30);
        encoderDriver.encoderDrive(0.3, "Strafe", -16, 30);

        int numShifts; // Number of times the robot shifts right (from phone side) based on the orientation of the minerals

        /** Activate Tensor Flow Object Detection. */

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

        if (Minerals.get(1) > Minerals.get(2) && Minerals.get(1) > Minerals.get(3)) {
            numShifts = 2;
        } else if (Minerals.get(2) > Minerals.get(1) && Minerals.get(1) > Minerals.get(3)) {
            numShifts = 1;
        } else {
            numShifts = 0;
        }

        encoderDriver.encoderDrive(0.3, "Strafe", -14.5 * numShifts, 30);
        //encoderDriver.encoderDrive(0.3,"Strafe", 6, 30);
        encoderDriver.encoderDrive(0.3, "Forward", 5, 30);
        if (craterDistance.isChecked()) {
            // If the robot is on the side further away from the crater
            encoderDriver.encoderDrive(0.3, "Forward", 36, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -14.5 * (1 - numShifts), 30);
            // depot
            robot.flag.setPosition(0.6);
            sleep(2000);
            //robot.flag.setPosition(0);
            encoderDriver.encoderDrive(0.3, "Forward", -12, 30);
            encoderDriver.encoderDrive(0.3, "Turn", -12, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -24, 30);
            encoderDriver.encoderDrive(0.3, "Forward", -70, 30);
        } else {
            // If the robot is on the closer side
            encoderDriver.encoderDrive(0.3, "Forward", -5, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", -24 - 14.5 * numShifts, 30);
            encoderDriver.encoderDrive(0.3, "Turn", -34, 30);
            encoderDriver.encoderDrive(0.3, "Strafe", 24, 30);
            encoderDriver.encoderDrive(0.3, "Forward", 52, 30);
            // depot
            robot.flag.setPosition(0.6);
            sleep(300);
            //robot.flag.setPosition(0);
            encoderDriver.encoderDrive(0.3, "Forward", -72, 30);
        }

        //while (robot.imu.getAngularOrientation().firstAngle < 90) {
        //robot.frontLeftDrive.setPower(-0.3);
        //robot.rearLeftDrive.setPower(-0.3);
        //robot.frontRightDrive.setPower(0.3);
        //robot.rearLeftDrive.setPower(0.3);
        //}
        //encoderDriver.encoderDrive(0.5, NINETY_DEGREE_TURN, -NINETY_DEGREE_TURN, NINETY_DEGREE_TURN, -NINETY_DEGREE_TURN, 100);

        telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        encoderDriver.init();
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}