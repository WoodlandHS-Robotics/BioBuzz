package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Configuration class specifically for mismatched goBILDA Pinpoint odometry pods (e.g., 4-Bar on X and Swing Arm on Y).
 */
public class MismatchedPinpointConfig {
    public final String name;
    public final ScaledPinpointLocalizer.PodType xPodType;
    public final ScaledPinpointLocalizer.PodType yPodType;
    public final double xPodOffset;
    public final double yPodOffset;
    public final GoBildaPinpointDriver.EncoderDirection xPodDirection;
    public final GoBildaPinpointDriver.EncoderDirection yPodDirection;
    public final DistanceUnit globalDistanceUnit;
    public final DistanceUnit offsetUnits;

    public MismatchedPinpointConfig(
            String name,
            ScaledPinpointLocalizer.PodType xPodType,
            ScaledPinpointLocalizer.PodType yPodType,
            double xPodOffset,
            double yPodOffset,
            GoBildaPinpointDriver.EncoderDirection xPodDirection,
            GoBildaPinpointDriver.EncoderDirection yPodDirection,
            DistanceUnit globalDistanceUnit,
            DistanceUnit offsetUnits
    ) {
        this.name = name;
        this.xPodType = xPodType;
        this.yPodType = yPodType;
        this.xPodOffset = xPodOffset;
        this.yPodOffset = yPodOffset;
        this.xPodDirection = xPodDirection;
        this.yPodDirection = yPodDirection;
        this.globalDistanceUnit = globalDistanceUnit;
        this.offsetUnits = offsetUnits;
    }

    public MismatchedPinpointConfig(
            String name,
            ScaledPinpointLocalizer.PodType xPodType,
            ScaledPinpointLocalizer.PodType yPodType,
            double xPodOffset,
            double yPodOffset,
            GoBildaPinpointDriver.EncoderDirection xPodDirection,
            GoBildaPinpointDriver.EncoderDirection yPodDirection
    ) {
        this(name, xPodType, yPodType, xPodOffset, yPodOffset, xPodDirection, yPodDirection, DistanceUnit.INCH, DistanceUnit.INCH);
    }

    public PinpointConfig toBaseConfig() {
        return new PinpointConfig(c -> {
            c.name.set(name);
            c.xPodOffset.set(xPodOffset);
            c.yPodOffset.set(yPodOffset);
            c.xPodDirection.set(xPodDirection);
            c.yPodDirection.set(yPodDirection);
            c.globalDistanceUnit.set(globalDistanceUnit);
            c.offsetUnits.set(offsetUnits);
        });
    }

    public Localizer createLocalizer(HardwareMap hardwareMap) {
        return ScaledPinpointLocalizer.createForMismatchedPods(hardwareMap, toBaseConfig(), xPodType, yPodType);
    }
}
