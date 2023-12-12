package org.firstinspires.ftc.teamcode;  //calls the package it is located in

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  //Imports the linearOpMode class
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;  //Imports the teleOp class
import com.qualcomm.robotcore.hardware.DcMotor;  //Imports the DcMotor class
import com.qualcomm.robotcore.hardware.DcMotorSimple;  //Imports the DcMotorSimple class
import com.qualcomm.robotcore.hardware.Servo;  //Imports the Servo class

@TeleOp  //Tells you what mode this extends, and allows you to call it on the driver station
public class MecanumTeleOp extends LinearOpMode {  //creates the mecanum teleOp class. It is a sub class of LinearOpMode, as it extends the class

    @Override  //Overrides certain code
    public void runOpMode() throws InterruptedException {  //creates a method that runs op mode. Does not catch the interrupted exception error

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");  //Calls the front left motor from configuration
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");  //Calls the back left motor from configuration
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");  //Calls the front right motor from configuration
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");  //Calls the back right motor from configuration
        DcMotor liftMotor = hardwareMap.dcMotor.get("liftMotor");  //Calls the arm lift motor from configuration
        DcMotor liftLeft = hardwareMap.dcMotor.get("liftLeft");  //Calls the left lift climb motor from configuration
        DcMotor liftRight = hardwareMap.dcMotor.get("liftRight");  //Calls the right lift climb motor from configuration
        DcMotor Intake = hardwareMap.dcMotor.get("intake");  //Calls the intake motor from configuration

        Servo Launcher = hardwareMap.get(Servo.class, "Launcher");  //Calls the launcher Servo from configuration
        double LaunchServoPos = 0.55;  //Creates a variable that can get and change the position of the servo anywhere in the code

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);  //sets the front left motor to reverse when going forward
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);  //sets the back left motor to reverse when going forward
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);  //sets left lift motor to reverse when going forward
        liftRight.setDirection(DcMotorSimple.Direction.FORWARD);  //sets right lift motor to forward when going forward

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  //allows the motor to hold position when there is zero power on that motor

        waitForStart();  //waits for the program to start

        if (isStopRequested()) return;  //if you stop the program, it returns and ends the program

        while (opModeIsActive()) {  //while the op mode is running, do this

            double y = -gamepad1.left_stick_y;  //creates a number when we use the y position of the left joystick, y value must be reversed, as indicated by the -
            double x = gamepad1.left_stick_x * 1.1;  //creates a number when we use the x position of the left joystick, the * 1.1 counteracts imperfect strafing
            double turn = gamepad1.right_stick_x;  //creates a number when we use the x position of the right joystick
            double SlowMode = 1 - gamepad1.left_trigger * 0.8;  //creates a number that reads the left triggers number, and multiplies it by 0.8, the lowest number possible is 0.2

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);  //creates a number that will divide the motors so the power doesn't exceed 1

            double frontLeftPower = (y + x + turn) / denominator;  //creates the power we need to go for the front left motor mecanum wheel.
            double backLeftPower = (y - x + turn) / denominator;  //creates the power we need to go for the back left motor mecanum wheel.
            double frontRightPower = (y - x - turn) / denominator;  //creates the power we need to go for the front right motor mecanum wheel.
            double backRightPower = (y + x - turn) / denominator;  //creates the power we need to go for the back right motor mecanum wheel.

            frontLeftMotor.setPower(frontLeftPower * SlowMode);  //sets the power of the front left motor and multiplies it by slow mode
            backLeftMotor.setPower(backLeftPower * SlowMode);  //sets the power of the back left motor and multiplies it by slow mode
            frontRightMotor.setPower(frontRightPower * SlowMode);  //sets the power of the front right motor and multiplies it by slow mode
            backRightMotor.setPower(backRightPower * SlowMode);  //sets the power of the back right motor and multiplies it by slow mode

            if (gamepad1.x) {  //detects whether x was pressed on game pad 1
                LaunchServoPos = 0.75;  //changes the launch servo pos variable to a number to launch the paper airplane
            }

            if (gamepad1.y) {  //detects whether y was pressed on game pad 1
                Intake.setPower(1);  //sets the intake to shoot pixels out
            } else if (gamepad1.b) {  //detects whether b was pressed on game pad 1 if y was not pressed
                Intake.setPower(-0.5);  //sets the intake to take pixels in
            } else {  //runs if the conditions met on the previous if statements were not met
                Intake.setPower(0);  //sets the intake to do nothing
            }

            if (gamepad1.right_trigger > 0.1 && liftMotor.getCurrentPosition() > 250) {  //detects whether the power of the right trigger is greater than 0.1 and the arm lift motor's position is greater than 0
               liftMotor.setPower(-0.5);  //sets the power to lower the arm
            } else if(gamepad1.right_bumper && liftMotor.getCurrentPosition() < 4200) {  //detects whether the right bumper was pressed and the arm lift motor's position is less than 4200
                liftMotor.setPower(0.5);  //sets the power to raise the arm
                liftLeft.setPower(-0.4);  //sets the left lift to unravel
                liftRight.setPower(-0.4);  //sets the right lift to unravel
                }
            else {  //runs if the conditions met on the previous if statements were not met
                liftMotor.setPower(0);  //sets the arm lift motor to not move
            }

            if (gamepad1.dpad_up) {  //detects whether the dpad up is pressed
                liftLeft.setPower(1);  //sets the left lift to pull up
                liftRight.setPower(1);  //sets the right lift to pull up

                liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  //allows the left lift motor to hold position when the power is 0
                liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  //allows the right lift motor to hold position when the power is 0
            }else if(gamepad1.dpad_down){  //detects whether the dpad down is pressed
                liftLeft.setPower(-0.4);  //sets the left lift to drop slowly for fine tuning
                liftRight.setPower(-0.4);  //sets the right lift to drop slowly for fine tuning
            }else {  //runs if the previous if statements are not met
            liftRight.setPower(0);  //sets the right lift motor to do nothing
            liftLeft.setPower(0);  //sets the left lift motor to do nothing
            }

            Launcher.setPosition(LaunchServoPos);  //sets the launcher servo's position to the launch servo pos variable

            telemetry.addData("FrontRight:", frontRightMotor.getPower());  //adds a line of data that reads the front right motor's power
            telemetry.addData("FrontLeft:", frontLeftMotor.getPower());  //adds a line of data that reads the front left motor's power
            telemetry.addData("BackRight:", backRightMotor.getPower());  //adds a line of data that reads the back right motor's power
            telemetry.addData("BackLeft:", backLeftMotor.getPower());  //adds a line of data that reads the back left motor's power
            telemetry.addData("LiftPos:", liftMotor.getCurrentPosition());  //adds a line of data that gets the arm lift motor's current position
            telemetry.addData("IS WORKING", gamepad1.dpad_up);  //adds a line of data the tells you whether the dpad up is pressed
            telemetry.addData("Slow mode:", SlowMode);  //adds a line of data telling you the slow mode's variable number

            telemetry.update();  //updates the telemetry every time this code is read.
        }
    }
}
