package org.firstinspires.ftc.teamcode.src.drivePrograms.teleop.worlds;

import static com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.src.robotAttachments.subsystems.outtake.Outtake;
import org.firstinspires.ftc.teamcode.src.robotAttachments.subsystems.outtake.StateOuttake;
import org.firstinspires.ftc.teamcode.src.utills.MiscUtils;
import org.firstinspires.ftc.teamcode.src.utills.enums.FreightFrenzyGameObject;
import org.firstinspires.ftc.teamcode.src.utills.enums.FreightFrenzyStateObject;
import org.firstinspires.ftc.teamcode.src.utills.opModeTemplate.GenericOpModeTemplate;
import org.firstinspires.ftc.teamcode.src.utills.opModeTemplate.TeleOpTemplate;

@TeleOp(name = "🟥Red Worlds Drive Program🟥")
public class RedWorldsDriveProgram extends TeleOpTemplate {
    protected BlinkinPattern defaultColor;
    protected BlinkinPattern currentPattern;
    private boolean x_depressed = true;
    private boolean tapeMeasureCtrl = false;
    StateOuttake outtake;
    Outtake Outtake;

    public RedWorldsDriveProgram(){
        defaultColor = BlinkinPattern.RED;
        currentPattern = this.defaultColor;
    }

    public void opModeMain() throws InterruptedException {
        this.initAll();
        Outtake = new Outtake(hardwareMap, GenericOpModeTemplate.bucketServoName, GenericOpModeTemplate.bucketColorSensorName,true);
        outtake = null;

        leds.setPattern(currentPattern);

        slide.autoMode();

        telemetry.addData("Initialization", "finished");
        telemetry.update();

        System.gc();
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            //Declan's controls
            {
                driveTrain.gamepadControl(gamepad1, gamepad2);
                //Carousel Spinner
                spinner.gamepadControl(gamepad1, gamepad2);
            }


            //Eli's controls
            {
                if (!gamepad2.x) {
                    x_depressed = true;
                }
                if (gamepad2.x && x_depressed) {
                    tapeMeasureCtrl = !tapeMeasureCtrl;
                    turret.halt();
                    slide.halt();
                    intake.halt();
                    Outtake.halt();
                    x_depressed = false;
                }

                if (tapeMeasureCtrl) {
                    turret.gamepadControl(gamepad1, gamepad2);

                } else {

                    //Handles Linear Slide Control
                    slide.gamepadControl(gamepad1, gamepad2);

                    //Intake Controls
                    BlinkinPattern proposedPattern = FreightFrenzyGameObject.getLEDColorFromItem(Outtake.gamepadControl(gamepad1, gamepad2));
                    if (proposedPattern != null) {
                        leds.setPattern(proposedPattern);
                    } else {
                        leds.setPattern(this.defaultColor);
                    }


                    intake.gamepadControl(gamepad1, gamepad2);

                    Thread.yield();
                }

            }

        }
    }
}


