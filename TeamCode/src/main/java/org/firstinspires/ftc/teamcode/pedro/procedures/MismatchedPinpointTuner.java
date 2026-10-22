package org.firstinspires.ftc.teamcode.pedro.procedures;

import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.pedro.ScaledPinpointLocalizer;

import java.util.List;

public class MismatchedPinpointTuner extends Procedure {

    public MismatchedPinpointTuner() {
        super("Mismatched Pinpoint Tuner", "A procedure for tuning the Pinpoint localizer with mismatched odometry pods.");
    }

    @Override
    public void run() throws InterruptedException {
        Inputs inputs = inputs("Setup", "Set Pinpoint HardwareMap Name and Pod Types");
        Inputs.Field<String> pinpointName = inputs.s("HardwareMap Name").withDefault("pinpoint");
        Inputs.Field<ScaledPinpointLocalizer.PodType> xPodType = inputs.e("Forward (X) Pod Type", ScaledPinpointLocalizer.PodType.class).withDefault(ScaledPinpointLocalizer.PodType.FOUR_BAR);
        Inputs.Field<ScaledPinpointLocalizer.PodType> yPodType = inputs.e("Strafe (Y) Pod Type", ScaledPinpointLocalizer.PodType.class).withDefault(ScaledPinpointLocalizer.PodType.SWING_ARM);
        awaitInputs(inputs);

        PinpointTuner.PodType tunerPodType = getTunerPodType(xPodType.get(), yPodType.get());

        boolean forwardPodReversed = runOpMode(new PinpointForwardDirection(pinpointName.get(), tunerPodType, java.util.OptionalDouble.empty()));
        boolean strafePodReversed = runOpMode(new PinpointStrafeDirection(pinpointName.get(), tunerPodType, java.util.OptionalDouble.empty()));

        List<Double> offsets = runOpMode(new PinpointOffsets(pinpointName.get(), tunerPodType, java.util.OptionalDouble.empty(), forwardPodReversed, strafePodReversed));

        result("name", pinpointName.get());
        result("xPodType", xPodType.get());
        result("yPodType", yPodType.get());
        result("xPodDirection", forwardPodReversed ? GoBildaPinpointDriver.EncoderDirection.REVERSED : GoBildaPinpointDriver.EncoderDirection.FORWARD);
        result("yPodDirection", strafePodReversed ? GoBildaPinpointDriver.EncoderDirection.REVERSED : GoBildaPinpointDriver.EncoderDirection.FORWARD);
        result("xPodOffset", offsets.get(0));
        result("yPodOffset", offsets.get(1));

        code(Language.JAVA,
                "public static MismatchedPinpointConfig localizerConfig = new MismatchedPinpointConfig(\n" +
                "    \"" + pinpointName.get() + "\",\n" +
                "    ScaledPinpointLocalizer.PodType." + xPodType.get().name() + ",\n" +
                "    ScaledPinpointLocalizer.PodType." + yPodType.get().name() + ",\n" +
                "    " + offsets.get(0) + ", // Physical xPodOffset (inches)\n" +
                "    " + offsets.get(1) + ", // Physical yPodOffset (inches)\n" +
                "    " + (forwardPodReversed ? "GoBildaPinpointDriver.EncoderDirection.REVERSED" : "GoBildaPinpointDriver.EncoderDirection.FORWARD") + ",\n" +
                "    " + (strafePodReversed ? "GoBildaPinpointDriver.EncoderDirection.REVERSED" : "GoBildaPinpointDriver.EncoderDirection.FORWARD") + ",\n" +
                "    DistanceUnit.INCH,\n" +
                "    DistanceUnit.INCH\n" +
                ");\n\n" +
                "public static Localizer createLocalizer(HardwareMap hardwareMap) {\n" +
                "    return localizerConfig.createLocalizer(hardwareMap);\n" +
                "}"
        );
    }

    private static PinpointTuner.PodType getTunerPodType(ScaledPinpointLocalizer.PodType x, ScaledPinpointLocalizer.PodType y) {
        if (x == ScaledPinpointLocalizer.PodType.FOUR_BAR && y == ScaledPinpointLocalizer.PodType.SWING_ARM) {
            return PinpointTuner.PodType.FOUR_BAR_X_SWING_ARM_Y;
        } else if (x == ScaledPinpointLocalizer.PodType.SWING_ARM && y == ScaledPinpointLocalizer.PodType.FOUR_BAR) {
            return PinpointTuner.PodType.SWING_ARM_X_FOUR_BAR_Y;
        } else if (x == ScaledPinpointLocalizer.PodType.FOUR_BAR) {
            return PinpointTuner.PodType.FOUR_BAR;
        } else {
            return PinpointTuner.PodType.SWING_ARM;
        }
    }
}
