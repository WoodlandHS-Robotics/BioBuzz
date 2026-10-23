package org.firstinspires.ftc.teamcode.opModes

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.pedropathing.api.Paths
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import com.pedropathing.paths.Path
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcontroller.teamcode.TeamColor
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.sensor.LimelightKt
import org.firstinspires.ftc.teamcode.sensor.SensorDeviceKt
import com.pedropathing.api.Paths.line
import com.pedropathing.api.Paths.curve
import com.pedropathing.api.PoseFactory

@Autonomous(name="Automatic Autonomous", group="Competition")
@Disabled
open class UnifiedAutonomous : LinearOpMode() {
    protected var pathToFollow = NavigationPath.Standard
    protected var currentLocation = Locations.Unknown

    private val telemetryM by lazy { MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry) }
    private lateinit var follower: Follower

    override fun runOpMode() {
        /* =========================================================================
         * PEDRO PATHING PATH EXAMPLES & COMMENTED GUIDE:
         *
         * 1. Straight Line Path:
         *    val linePath = line(Pose(0.0, 0.0, 0.0), Pose(-6.0, -30.5, 0.0)).constant(0.0)
         *
         * 2. Curved Path:
         *    val curvePath = curve(Pose(0.0, 0.0, 0.0), Pose(10.0, 10.0, 0.0), Pose(20.0, 0.0, Math.PI / 2)).tangent()
         *
         * 3. Following Paths sequentially:
         *    follower.follow(path1)
         *    while (opModeIsActive() && follower.isBusy()) {
         *        follower.update()
         *        if (follower.atParametricEnd()) {
         *            follower.follow(path2)
         *        }
         *    }
         * ========================================================================= */

        follower = Constants.create(hardwareMap)
        follower.setPose(currentLocation.startPose)

        val poseFactory = PoseFactory.degrees()
        var dest: Pose
        var autoPath: Path

        when (currentLocation) {
            Locations.BlueClose -> {
                dest = poseFactory.of(68.5067, 17.5322, 0.0)
                autoPath = Paths.curve(currentLocation.startPose, dest)
            }
            Locations.BlueFar -> {
                dest = poseFactory.of(112.0427, 101.4671, 0.0)
                autoPath = Paths.line(currentLocation.startPose, dest)
            }
            Locations.RedClose -> {
                dest = poseFactory.of(12.0, 94.0, 210.0)
                autoPath = Paths.curve(currentLocation.startPose, dest)
            }
            Locations.RedFar -> {
                dest = poseFactory.of(12.0, 94.0, 130.0)
                autoPath = Paths.line(currentLocation.startPose, dest)
            }
            else -> {
                autoPath = Paths.line(currentLocation.startPose, currentLocation.startPose)
            }
        }

        telemetryM.addData("INIT STATUS", "READY")
        telemetryM.update()

        waitForStart()

       // follower.follow(path1)
       // var followedSecond = false

        while (opModeIsActive() && follower.isBusy()) {
            follower.update()
            follower.follow(autoPath)

            telemetryM.addData("X", follower.pose().x())
            telemetryM.addData("Y", follower.pose().y())
            telemetryM.addData("Heading", follower.pose().heading())
            telemetryM.update()
        }
    }

    protected enum class Locations(val teamColor: TeamColor, val startPose: Pose) {
        BlueClose(TeamColor.BLUE, Pose(83.0, 9.0, 90.0)),
        BlueFar(TeamColor.BLUE, Pose(83.0, 133.0, -90.0)),
        RedClose(TeamColor.RED, Pose(60.0, 133.0, -90.0)),
        RedFar(TeamColor.RED, Pose(56.0, 8.0, 90.0)),
        Unknown(TeamColor.UNKNOWN, Pose(0.0, 0.0, 0.0))
    }

    protected enum class NavigationPath {
        Standard,
        Alternate
    }
}

@Autonomous(name = "Blue Close Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
class BlueCloseAutonomous : UnifiedAutonomous() {
    init { currentLocation = Locations.BlueClose }
}

@Autonomous(name = "Blue Far Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
class BlueFarAutonomous : UnifiedAutonomous() {
    init { currentLocation = Locations.BlueFar }
}

@Autonomous(name = "Red Close Autonomous", group = "Competition", preselectTeleOp = "Red TeleOp Controls (Robot)")
class RedCloseAutonomous : UnifiedAutonomous() {
    init { currentLocation = Locations.RedClose }
}

@Autonomous(name = "Red Far Autonomous", group = "Competition", preselectTeleOp = "Blue TeleOp Controls (Robot)")
class RedFarAutonomous : UnifiedAutonomous() {
    init { currentLocation = Locations.RedFar }
}
