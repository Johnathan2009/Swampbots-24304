package org.firstinspires.ftc.teamcode;  //Declares the package this class is in

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  //Imports LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;  //Imports TeleOp
import com.qualcomm.robotcore.hardware.DcMotor;  //Imports DcMotor

@TeleOp() //Declares this class as teleOp, allowing it to be shown in the teleOp section of the driver station
public class TelemetryTest extends LinearOpMode {  //The class of the code.  Extends LinearOpMode, making it a subclass
    DcMotor leftFrontMotor = null;  //Declares front left motor and sets it to null
    DcMotor rightFrontMotor = null;  //Declares front right motor and sets it to null
    DcMotor leftRearMotor = null;  //Declares back left motor and sets it to null
    DcMotor rightRearMotor = null;  //Declares back right motor and sets it to null
    DcMotor liftMotor = null;  //Declares arm lift motor and sets it to null
    @Override  //Overrides certain code
    public void runOpMode() {  //the main method that is called when you run the code
        telemetry.setAutoClear(true);  //sets it to clear the telemetry after telemetry update

        leftFrontMotor = hardwareMap.dcMotor.get("frontLeftMotor");  //Declares front left motor
        rightFrontMotor = hardwareMap.dcMotor.get("frontRightMotor");  //Declares front right motor
        leftRearMotor = hardwareMap.dcMotor.get("backLeftMotor");  //Declares back left motor
        rightRearMotor = hardwareMap.dcMotor.get("backRightMotor");  //Declares back right motor
        liftMotor = hardwareMap.dcMotor.get("liftMotor");  //Declares arm lift motor

        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);  //Sets the front right motor to Reverse
        leftFrontMotor.setDirection(DcMotor.Direction.FORWARD);  //Sets the front left motor to Forward
        rightRearMotor.setDirection(DcMotor.Direction.REVERSE);  //Sets the back right motor to Reverse
        leftRearMotor.setDirection(DcMotor.Direction.FORWARD);  //sets the back left motor to Forward

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  // resets the encoder value to 0 for front left motor
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  // resets the encoder value to 0 for front right motor
        leftRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  // resets the encoder value to 0 for back left motor
        rightRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  // resets the encoder value to 0 for back right motor

        waitForStart();  // Wait for the game to start (driver presses PLAY)

        while(opModeIsActive()) {  //runs while code is running
            telemetry.addData("BackLeftEncoder", leftRearMotor.getCurrentPosition());  //displays the position of the back left motor
            telemetry.addData("BackRightEncoder", rightRearMotor.getCurrentPosition());  //displays the position of the back right motor
            telemetry.addData("FrontLeftEncoder", leftFrontMotor.getCurrentPosition());  //displays the position of the front left motor
            telemetry.addData("FrontRightEncoder", rightFrontMotor.getCurrentPosition());  //displays the position of the front right motor
            telemetry.addData("LiftPos:", liftMotor.getCurrentPosition());  //displays the position of the arm lift motor
            telemetry.update();  // updates the telemetry each time
        }
    }
}