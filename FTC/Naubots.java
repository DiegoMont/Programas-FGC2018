/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Naubots", group="Iterative Opmode")

public class Naubots extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor barredora = null;
    private DcMotor elevador1 = null;
    private DcMotor elevador2 = null;
    private Servo mechanismServo = null;
    private ColorSensor sensorColor = null;

    //Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        //sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color");
        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        barredora = hardwareMap.get(DcMotor.class, "barredora");
        elevador1 = hardwareMap.get(DcMotor.class, "motor2");
        elevador2 = hardwareMap.get(DcMotor.class, "motor1");
        //mechanismServo = hardwareMap.get(Servo.class, "position_servo");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        barredora.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
    }

     //Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

     //Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
        runtime.reset();
    }
    boolean modoDriver = true;
    boolean click = false;
     //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        double leftPower;
        double rightPower;
        double servoPosition;
        double barredoraPower;
        double elevador1Power;
        double elevador2Power;

        if(gamepad1.right_trigger > 0){
            barredoraPower = 1;
        } else if (gamepad1.left_trigger > 0){
            barredoraPower = -1;
        } else {
            barredoraPower = 0;
        }

        if(gamepad1.start){
            click = true;
        } else if ( !gamepad1.start && click){
            if (modoDriver){
                modoDriver = false;
            } else {
                modoDriver = true;
            }
            click = false;
        }

        if(modoDriver){
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x * 1.5;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0);
            rightPower   = Range.clip(drive - turn, -1.0, 1.0);
        } else {
            leftPower = -gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
        }

        if(gamepad1.dpad_up){
            elevador1Power = 0.3;
            elevador2Power = -0.3;
        } else if(gamepad1.dpad_down){
            elevador1Power = -0.3;
            elevador2Power = 0.3;
        } else {
            elevador1Power = 0;
            elevador2Power = 0;
        }

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        barredora.setPower(barredoraPower);
        elevador1.setPower(elevador1Power);
        elevador2.setPower(elevador2Power);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        /*telemetry.addData("Color R: ", sensorColor.red());
        telemetry.addData("Color G: ", sensorColor.green());
        telemetry.addData("Color B: ", sensorColor.blue());*/
    }

     //Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }

}
