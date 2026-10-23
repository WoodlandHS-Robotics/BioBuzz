package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static Follower create(HardwareMap h) {
        return new Follower(
                createLocalizer(h),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
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

    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        Controller primaryTranslationalForward = Controller.proportional(0.1);
        Controller secondaryTranslationalForward = Controller.proportional(0.1);
        Controller primaryTranslationalLateral = Controller.proportional(0.1);
        Controller secondaryTranslationalLateral = Controller.proportional(0.1);

        c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.0));
        c.brake.set(Controller.proportionalFeedforward(0.0));

        c.headingFeedback.set(Controller.proportional(1.0));
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0, 0.0));

        c.linearBrakeCoefficients.set(Matrix.diag(0.0, 0.0));
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.0, 0.0));

        c.maxAchievableForwardVelocity.set(60.0);
        c.maxAchievableStrafeVelocity.set(60.0);
        c.naturalForwardDeceleration.set(60.0);
        c.naturalStrafeDeceleration.set(60.0);
    });
}
