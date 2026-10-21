package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.localization.Localizer;
import com.pedropathing.localization.MotionState;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Custom Localizer wrapper around Pedro Pathing's PinpointLocalizer to support
 * mismatched odometry pods on the goBILDA Pinpoint Odometry Computer (e.g. Swingarm and 4-Bar).
 */
public class ScaledPinpointLocalizer implements Localizer {

    /**
     * Supported goBILDA odometry pod types and their ticks-per-mm resolutions.
     */
    public enum PodType {
        SWING_ARM(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD, 13.26291192),
        FOUR_BAR(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD, 19.89436789);

        public final GoBildaPinpointDriver.GoBildaOdometryPods pinpointPodType;
        public final double ticksPerMm;

        PodType(GoBildaPinpointDriver.GoBildaOdometryPods pinpointPodType, double ticksPerMm) {
            this.pinpointPodType = pinpointPodType;
            this.ticksPerMm = ticksPerMm;
        }
    }

    private final PinpointLocalizer baseLocalizer;
    private final double xScale;
    private final double yScale;
    private MotionState motionState;

    /**
     * Creates a ScaledPinpointLocalizer with explicit scale factors.
     *
     * @param hardwareMap the hardware map
     * @param config the PinpointConfig object
     * @param xScale scale factor for the X axis (use 1.0 if X pod matches Pinpoint config podType)
     * @param yScale scale factor for the Y axis (e.g., 2.0/3.0 if Pinpoint config is Swingarm and Y is 4-Bar)
     */
    public ScaledPinpointLocalizer(HardwareMap hardwareMap, PinpointConfig config, double xScale, double yScale) {
        this.baseLocalizer = new PinpointLocalizer(hardwareMap, config);
        this.xScale = xScale;
        this.yScale = yScale;
        update();
    }

    /**
     * Creates a ScaledPinpointLocalizer with default scale factors of 1.0.
     *
     * @param hardwareMap the hardware map
     * @param config the PinpointConfig object
     */
    public ScaledPinpointLocalizer(HardwareMap hardwareMap, PinpointConfig config) {
        this(hardwareMap, config, 1.0, 1.0);
    }

    /**
     * Creates a ScaledPinpointLocalizer automatically configured for mismatched pod types.
     *
     * @param hardwareMap the hardware map
     * @param baseConfig the base PinpointConfig containing raw geometry offsets
     * @param xPodType the pod type installed on the X axis (forward/backward)
     * @param yPodType the pod type installed on the Y axis (strafe)
     * @return a configured ScaledPinpointLocalizer instance
     */
    public static ScaledPinpointLocalizer createForMismatchedPods(
            HardwareMap hardwareMap,
            PinpointConfig baseConfig,
            PodType xPodType,
            PodType yPodType
    ) {
        GoBildaPinpointDriver.GoBildaOdometryPods basePodEnum = xPodType.pinpointPodType;
        double baseTicksPerMm = xPodType.ticksPerMm;

        double xScale = baseTicksPerMm / xPodType.ticksPerMm; // 1.0
        double yScale = baseTicksPerMm / yPodType.ticksPerMm; // 2.0 / 3.0 for 4-Bar on Y when base is Swingarm

        double actualXOffset = baseConfig.xPodOffset.get();
        double actualYOffset = baseConfig.yPodOffset.get();

        PinpointConfig adjustedConfig = new PinpointConfig(c -> {
            c.name.set(baseConfig.name.get());
            c.podType.set(basePodEnum);
            c.xPodOffset.set(actualXOffset / yScale);
            c.yPodOffset.set(actualYOffset / xScale);
            c.xPodDirection.set(baseConfig.xPodDirection.get());
            c.yPodDirection.set(baseConfig.yPodDirection.get());
            if (baseConfig.offsetUnits.get() != null) {
                c.offsetUnits.set(baseConfig.offsetUnits.get());
            }
            if (baseConfig.globalDistanceUnit.get() != null) {
                c.globalDistanceUnit.set(baseConfig.globalDistanceUnit.get());
            }
            if (baseConfig.encoderResolutionUnit.get() != null) {
                c.encoderResolutionUnit.set(baseConfig.encoderResolutionUnit.get());
            }
            if (baseConfig.resetMode.get() != null) {
                c.resetMode.set(baseConfig.resetMode.get());
            }
        });

        return new ScaledPinpointLocalizer(hardwareMap, adjustedConfig, xScale, yScale);
    }

    @Override
    public void update() {
        baseLocalizer.update();
        MotionState rawState = baseLocalizer.state();

        if (rawState == null) {
            return;
        }

        Pose p = rawState.pose();
        Velocity v = rawState.velocity();

        Pose scaledPose = new Pose(
                p.x() * xScale,
                p.y() * yScale,
                p.heading()
        );

        Velocity scaledVelocity = new Velocity(
                v.vx * xScale,
                v.vy * yScale,
                v.omega
        );

        motionState = MotionState.ofVelocity(scaledPose, scaledVelocity);
    }

    @Override
    public MotionState state() {
        return motionState;
    }

    @Override
    public void setPose(Pose pose) {
        Pose unscaledPose = new Pose(
                pose.x() / xScale,
                pose.y() / yScale,
                pose.heading()
        );

        baseLocalizer.setPose(unscaledPose);
        motionState = MotionState.ofVelocity(pose, Velocity.zero());
    }

    /**
     * Resets position and recalibrates the IMU via the underlying Pinpoint driver.
     */
    public void reset() {
        baseLocalizer.reset();
        update();
    }

    public PinpointLocalizer getBaseLocalizer() {
        return baseLocalizer;
    }

    public double getXScale() {
        return xScale;
    }

    public double getYScale() {
        return yScale;
    }
}

