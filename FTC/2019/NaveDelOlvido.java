/*Copyright 2019

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Disabled
public class NaveDelOlvido {

  //Inicializar variables para motores y sensores del robot
  public DcMotor frontLeft = null;
  public DcMotor frontRight = null;
  public DcMotor backLeft = null;
  public DcMotor backRight = null;
  public DcMotor elevador = null;
  public Servo brazo = null;

  private LinearOpMode programa;
  private BNO055IMU imu;
  private Orientation angles;
  private Acceleration gravity;

  public NaveDelOlvido(LinearOpMode programa){
    this.programa = programa;
  }

  //Metodo para buscar motores y servomotores del Expansion y asignarlos a las variables
  public void getHardware(HardwareMap hwMap){
      frontLeft = hwMap.get(DcMotor.class, "left_front_drive");
      frontRight = hwMap.get(DcMotor.class, "right_front_drive");
      backLeft = hwMap.get(DcMotor.class, "left_back_drive");
      backRight = hwMap.get(DcMotor.class, "right_back_drive");
      elevador = hwMap.get(DcMotor.class, "brazo");
      brazo = hwMap.get(Servo.class, "garra");


      frontLeft.setDirection(DcMotor.Direction.REVERSE);
      frontRight.setDirection(DcMotor.Direction.FORWARD);
      backLeft.setDirection(DcMotor.Direction.REVERSE);
      backRight.setDirection(DcMotor.Direction.FORWARD);

      //elevador.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

      BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
      parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
      parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
      parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
      parameters.loggingEnabled      = true;
      parameters.loggingTag          = "IMU";
      parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

      imu = hwMap.get(BNO055IMU.class, "imu");
      imu.initialize(parameters);
      imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
  }

  public void frenar(){
    frontLeft.setPower(0);
    frontRight.setPower(0);
    backLeft.setPower(0);
    backRight.setPower(0);
  }

  public void resetEncoders(){
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    defaultRunmode();
  }

  public void defaultRunmode(){
    frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
  }

  public double getDesviacion(){
    angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    gravity  = imu.getGravity();
    return angles.firstAngle;
  }

  /* Metodos para programacion autonoma */

  //Este metodo movera al robot en linea recta la distancia que se especifique
  public void moverDistanciaRecta(double distancia){
    final int counts = (int)Math.round(distancia * 1631 / 10.16 / Math.PI);
    resetEncoders();
    backLeft.setTargetPosition(counts);
    backRight.setTargetPosition(counts);
    frontLeft.setTargetPosition(counts);
    frontRight.setTargetPosition(counts);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    while (programa.opModeIsActive() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
      double velocidad = 1;
      backLeft.setPower(velocidad);
      backRight.setPower(velocidad);
      frontRight.setPower(velocidad);
      frontLeft.setPower(velocidad);
    }
    frenar();
    defaultRunmode();
  }

  public void movimientoLateral(double distancia){
    final int counts = (int)Math.round(distancia * 1631 / 10.16 / Math.PI);
    resetEncoders();
    backLeft.setTargetPosition(counts);
    backRight.setTargetPosition(-counts);
    frontLeft.setTargetPosition(-counts);
    frontRight.setTargetPosition(counts);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    while (programa.opModeIsActive() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
      double velocidad = 1;
      backLeft.setPower(velocidad);
      backRight.setPower(velocidad);
      frontRight.setPower(velocidad);
      frontLeft.setPower(velocidad);
    }
    frenar();
    defaultRunmode();
  }

  public void setGiroDeNoventaGrados(String direction){

  }

}