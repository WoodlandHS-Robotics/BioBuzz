package org.firstinspires.ftc.teamcode.opModes

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import com.pedropathing.paths.Path
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcontroller.teamcode.TeamColor
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.sensor.LimelightKt
import org.firstinspires.ftc.teamcode.sensor.SensorDeviceKt
import com.pedropathing.api.Paths.line
import com.pedropathing.api.Paths.curve

@Autonomous(name="Automatic Autonomous", group="Competition")
@Disabled
open class UnifiedAutonomous : LinearOpMode() {
    protected var pathToFollow = NavigationPath.Standard
    protected var currentLocation = Locations.Unknown

    private val telemetryM by lazy { MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry) }
    private lateinit var follower: Follower

    override fun runOpMode() {
        follower = Constants.createFollower(hardwareMap)
        follower.setPose(currentLocation.startPose)

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

        val path1 = line(currentLocation.startPose, Pose(-6.0, -30.5, 0.0)).constant(0.0)
        val path2Blue = line(Pose(-6.0, -30.5, 0.0), Pose(-12.0, -31.0, Math.PI)).constant(Math.PI)
        val path2Red = line(Pose(-6.0, -30.5, 0.0), Pose(-12.0, -31.0, -Math.PI)).constant(-Math.PI)

        telemetryM.addData("INIT STATUS", "READY")
        telemetryM.update()

        waitForStart()

        follower.follow(path1)
        var followedSecond = false

        while (opModeIsActive()) {
            follower.update()

            if (follower.atParametricEnd() && !followedSecond) {
                followedSecond = true
                if (currentLocation == Locations.BlueFar || currentLocation == Locations.BlueClose) {
                    follower.follow(path2Blue)
                } else if (currentLocation == Locations.RedFar || currentLocation == Locations.RedClose) {
                    follower.follow(path2Red)
                }
            }

            telemetryM.addData("X", follower.pose().x())
            telemetryM.addData("Y", follower.pose().y())
            telemetryM.addData("Heading", follower.pose().heading())
            telemetryM.update()
        }
    }

    protected enum class Locations(val teamColor: TeamColor, val startPose: Pose) {
        BlueClose(TeamColor.BLUE, Pose(0.0, 0.0, 0.0)),
        BlueFar(TeamColor.BLUE, Pose(0.0, 0.0, 0.0)),
        RedClose(TeamColor.RED, Pose(0.0, 0.0, 0.0)),
        RedFar(TeamColor.RED, Pose(0.0, 0.0, 0.0)),
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
