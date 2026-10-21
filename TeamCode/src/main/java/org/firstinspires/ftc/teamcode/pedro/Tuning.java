package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Tuning {

    /**
     * Example Pinpoint configuration for mismatched odometry pods:
     * Forward (X axis): Swing Arm pod
     * Strafe (Y axis): 4-Bar pod
     */
    public static PinpointConfig createMismatchedPinpointConfig(String pinpointName, double xOffset, double yOffset, boolean xReversed, boolean yReversed) {
        return new PinpointConfig(c -> {
            c.name.set(pinpointName);
            c.xPodOffset.set(xOffset);
            c.yPodOffset.set(yOffset);
            c.xPodDirection.set(xReversed ? GoBildaPinpointDriver.EncoderDirection.REVERSED : GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(yReversed ? GoBildaPinpointDriver.EncoderDirection.REVERSED : GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });
    }

    /**
     * Creates a Localizer instance wrapped in ScaledPinpointLocalizer for 1 Swing Arm (X) and 1 4-Bar (Y) pod.
     */
    public static Localizer createMismatchedLocalizer(HardwareMap hardwareMap, PinpointConfig config) {
        return ScaledPinpointLocalizer.createForMismatchedPods(
                hardwareMap,
                config,
                ScaledPinpointLocalizer.PodType.SWING_ARM,
                ScaledPinpointLocalizer.PodType.FOUR_BAR
        );
    }
}
