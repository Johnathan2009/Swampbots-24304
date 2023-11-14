package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous

public class BackdropBackstageRed extends LinearOpMode{
    private DcMotor         frontLeftMotor = null;

    private DcMotor         frontRightMotor = null;

    private DcMotor         backRightMotor = null;

    private DcMotor         backLeftMotor = null;
    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        Servo pixelArm = hardwareMap.get(Servo.class, "pixel arm");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if(isStopRequested()) return;

        while(opModeIsActive()) {
            frontLeftMotor.setPower(0.5);
            backLeftMotor.setPower(0.5);
            frontRightMotor.setPower(0.5);
            backRightMotor.setPower(0.5);
            sleep(1000);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);
            sleep(500);
            frontLeftMotor.setPower(0.5);
            backLeftMotor.setPower(0.5);
            frontRightMotor.setPower(-0.5);
            backRightMotor.setPower(-0.5);
            sleep(500);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);
            sleep(250);
            frontLeftMotor.setPower(1);
            backLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            backRightMotor.setPower(1);
            sleep(500);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);
            pixelArm.setPosition(-1);
            sleep(1000);
            pixelArm.setPosition(-0.5);
            // todo: write your code here
        }
    }

}