package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.sensor.LimelightKt;
import org.firstinspires.ftc.teamcode.sensor.SensorDeviceKt;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.curve;

@Autonomous(name="Automatic Autonomous", group="Competition")
@Disabled
public class UnifiedAutonomous extends LinearOpMode {
    protected NavigationPath pathToFollow = NavigationPath.Standard;
    protected Locations currentLocation = Locations.Unknown;

    private MultipleTelemetry telemetryM;
    private Follower follower;

    @Override
    public void runOpMode() {
        telemetryM = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        follower = Constants.createFollower(hardwareMap);

        follower.setPose(currentLocation.startPose);

        /* =========================================================================
         * PEDRO PATHING PATH EXAMPLES & COMMENTED GUIDE:
         *
         * 1. Straight Line Path:
         *    Path linePath = line(new Pose(0.0, 0.0, 0.0), new Pose(-6.0, -30.5, 0.0)).constant(0.0);
         *
         * 2. Curved Path:
         *    Path curvePath = curve(new Pose(0.0, 0.0, 0.0), new Pose(10.0, 10.0, 0.0), new Pose(20.0, 0.0, Math.PI / 2)).tangent();
         *
         * 3. Following Paths sequentially:
         *    follower.follow(path1);
         *    while (opModeIsActive() && follower.isBusy()) {
         *        follower.update();
         *        if (follower.atParametricEnd()) {
         *            follower.follow(path2);
         *        }
         *    }
         * ========================================================================= */

        Path path1 = line(currentLocation.startPose, new Pose(-6.0, -30.5, 0.0)).constant(0.0);
        Path path2Blue = line(new Pose(-6.0, -30.5, 0.0), new Pose(-12.0, -31.0, Math.PI)).constant(Math.PI);
        Path path2Red = line(new Pose(-6.0, -30.5, 0.0), new Pose(-12.0, -31.0, -Math.PI)).constant(-Math.PI);

        telemetryM.addData("INIT STATUS", "READY");
        telemetryM.update();

        waitForStart();

        follower.follow(path1);
        boolean followedSecond = false;

        while (opModeIsActive()) {
            follower.update();

            if (follower.atParametricEnd() && !followedSecond) {
                followedSecond = true;
                if (currentLocation == Locations.BlueFar || currentLocation == Locations.BlueClose) {
                    follower.follow(path2Blue);
                } else if (currentLocation == Locations.RedFar || currentLocation == Locations.RedClose) {
                    follower.follow(path2Red);
                }
            }

            telemetryM.addData("X", follower.pose().x());
            telemetryM.addData("Y", follower.pose().y());
            telemetryM.addData("Heading", follower.pose().heading());
            telemetryM.update();
        }
    }

    protected enum Locations {
        BlueClose(TeamColor.BLUE, new Pose(0.0, 0.0, 0.0)),
        BlueFar(TeamColor.BLUE, new Pose(0.0, 0.0, 0.0)),
        RedClose(TeamColor.RED, new Pose(0.0, 0.0, 0.0)),
        RedFar(TeamColor.RED, new Pose(0.0, 0.0, 0.0)),
        Unknown(TeamColor.UNKNOWN, new Pose(0.0, 0.0, 0.0));

        public final TeamColor teamColor;
        public final Pose startPose;

        Locations(TeamColor teamColor, Pose startPose) {
            this.teamColor = teamColor;
            this.startPose = startPose;
        }
    }

    protected enum NavigationPath {
        Standard,
        Alternate
    }

    @Autonomous(name = "Blue Close Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
    public static class BlueCloseAutonomous extends UnifiedAutonomous {
        public BlueCloseAutonomous() { this.currentLocation = Locations.BlueClose; }
    }

    @Autonomous(name = "Blue Far Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
    public static class BlueFarAutonomous extends UnifiedAutonomous {
        public BlueFarAutonomous() { this.currentLocation = Locations.BlueFar; }
    }

    @Autonomous(name = "Red Close Autonomous", group = "Competition", preselectTeleOp = "Red TeleOp Controls (Robot)")
    public static class RedCloseAutonomous extends UnifiedAutonomous {
        public RedCloseAutonomous() { this.currentLocation = Locations.RedClose; }
    }

    @Autonomous(name = "Red Far Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
    public static class RedFarAutonomous extends UnifiedAutonomous {
        public RedFarAutonomous() { this.currentLocation = Locations.RedFar; }
    }
}
