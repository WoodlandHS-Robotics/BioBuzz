package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Tuning {

    /**
     * Creates a PinpointConfig with physical geometry offsets.
     *
     * @param pinpointName hardwareMap name for the Pinpoint device (e.g. "pinpoint")
     * @param xOffset physical perpendicular offset from center of rotation to Y pod line (inches)
     * @param yOffset physical perpendicular offset from center of rotation to X pod line (inches)
     * @param xReversed whether the forward (X) pod encoder direction is reversed
     * @param yReversed whether the strafe (Y) pod encoder direction is reversed
     * @return configured PinpointConfig instance
     */
    public static PinpointConfig createPinpointConfig(String pinpointName, double xOffset, double yOffset, boolean xReversed, boolean yReversed) {
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
     * Creates a Localizer instance for your robot's setup:
     * - Forward / X axis: 4-Bar pod
     * - Strafe / Y axis: Swing Arm pod
     */
    public static Localizer createLocalizer(HardwareMap hardwareMap, PinpointConfig config) {
        return ScaledPinpointLocalizer.createForMismatchedPods(
                hardwareMap,
                config,
                ScaledPinpointLocalizer.PodType.FOUR_BAR,
                ScaledPinpointLocalizer.PodType.SWING_ARM
        );
    }
}
