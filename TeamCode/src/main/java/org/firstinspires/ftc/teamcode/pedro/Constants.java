package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }

    /**
     * Mismatched Pinpoint odometry pod configuration:
     * - Forward (X axis): 4-Bar pod
     * - Strafe (Y axis): Swing Arm pod
     */
    public static MismatchedPinpointConfig localizerConfig = new MismatchedPinpointConfig(
            "pinpoint",
            ScaledPinpointLocalizer.PodType.FOUR_BAR,  // Forward (X axis)
            ScaledPinpointLocalizer.PodType.SWING_ARM, // Strafe (Y axis)
            -1.75, // Physical xPodOffset (inches)
            -4.0,  // Physical yPodOffset (inches)
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            DistanceUnit.INCH,
            DistanceUnit.INCH
    );

    /**
     * Creates a Localizer instance configured for mismatched odometry pods.
     */
    public static Localizer createLocalizer(HardwareMap hardwareMap) {
        return localizerConfig.createLocalizer(hardwareMap);
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftBack");
        c.backRightName.set("rightBack");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.manualBrakeMode.set(true);
    });

    public static MecanumConstants x;
}