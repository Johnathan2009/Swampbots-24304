package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        double LaunchServoPos = 0.55;
       // double PixelArmPos = -0.7;
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        Servo Launcher = hardwareMap.get(Servo.class, "Launcher");
        DcMotor Intake = hardwareMap.dcMotor.get("intake");
       // Servo pixelArm = hardwareMap.get(Servo.class, "pixel arm");
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {


            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double turn = gamepad1.right_stick_x;
            double SlowMode = 1 - gamepad1.left_trigger * 0.8;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
            double frontLeftPower = (y + x + turn) / denominator;
            double backLeftPower = (y - x + turn) / denominator;
            double frontRightPower = (y - x - turn) / denominator;
            double backRightPower = (y + x - turn) / denominator;

            frontLeftMotor.setPower(frontLeftPower * SlowMode);
            backLeftMotor.setPower(backLeftPower * SlowMode);
            frontRightMotor.setPower(frontRightPower * SlowMode);
            backRightMotor.setPower(backRightPower * SlowMode);
            if (gamepad1.x == true) {
                LaunchServoPos = 0.75;
            }
            if (gamepad1.y == true) {
                Intake.setPower(1);
            } else if (gamepad1.b == true) {
                Intake.setPower(-0.5);
            } else {
                Intake.setPower(0);
            }
         //   if (gamepad1.right_trigger > 0.1) {

        //        PixelArmPos = PixelArmPos + 0.01;

        //    }
         //       if (gamepad1.right_bumper == true) {

          //          PixelArmPos = PixelArmPos - 0.01;

           //     }
                Launcher.setPosition(LaunchServoPos);
            //    pixelArm.setPosition(PixelArmPos);

                telemetry.addData("FrontRight:", frontRightMotor.getPower());
                telemetry.addData("FrontLeft:", frontLeftMotor.getPower());
                telemetry.addData("BackRight:", backRightMotor.getPower());
                telemetry.addData("BackLeft:", backLeftMotor.getPower());
                telemetry.addData("Slowmode:", SlowMode);
             //   telemetry.addData("Pixel arm servo pos:", pixelArm.getPosition());
                telemetry.update();
            }
        }
    }

