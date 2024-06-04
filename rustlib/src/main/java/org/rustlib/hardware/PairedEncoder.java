package org.rustlib.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.rustlib.commandsystem.Subsystem;

public class PairedEncoder extends Subsystem implements Encoder {
    private final int polarity;
    DcMotor pairedMotor;
    ElapsedTime timer = new ElapsedTime();
    private int position = 0;
    private int offset = 0;
    private double lastPosition = 0;
    private double lastTimestamp = 0;
    private double velocity = 0;

    public PairedEncoder(DcMotor pairedMotor, boolean reversed) {
        this.pairedMotor = pairedMotor;
        pairedMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pairedMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        polarity = reversed ? -1 : 1;
    }

    public PairedEncoder(DcMotor pairedMotor) {
        this(pairedMotor, false);
    }

    private int calculatePosition() {
        return position;
    }

    @Override
    public int getPosition() {
        return (pairedMotor.getCurrentPosition() + offset) * polarity;
    }

    public void setPosition(int position) {
        offset = -pairedMotor.getCurrentPosition() + position * polarity;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    private double calculateVelocity() {
        double position = getPosition();
        double timestamp = timer.milliseconds();
        double velocity = (position - lastPosition) / (timestamp - lastTimestamp);
        lastPosition = position;
        lastTimestamp = timestamp;
        return velocity;
    }

    @Override
    public void reset() {
        offset = -pairedMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        position = calculatePosition(); // This is done to ensure that the encoder value is being read in every loop, which allows the hubs to do bulk hardware reading
        velocity = calculateVelocity();
    }
}