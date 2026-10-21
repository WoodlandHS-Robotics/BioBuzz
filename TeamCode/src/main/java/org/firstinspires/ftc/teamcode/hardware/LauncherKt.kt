package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DigitalChannel
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.GamepadButtons
import org.firstinspires.ftc.robotcontroller.teamcode.HardwareMechanismKt
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.DriveTrainKt.SPEEDS
import org.firstinspires.ftc.teamcode.misc.DriveMotors
import org.firstinspires.ftc.teamcode.misc.PoseKt
import kotlin.math.abs
import kotlin.math.max

/**
 * Responsible for managing our launching system during TeleOp.
  */
@Config
class LauncherKt private constructor(hardwareMap: HardwareMap, data: InitData, private val telemetry: Telemetry) : HardwareMechanismKt() {
    private val flywheel: DcMotorEx = createDefaultMotor(hardwareMap,"FlywheelMotor")
    private val converter: DcMotorEx = createDefaultMotor(hardwareMap,"ConverterMotor")

    override fun start() {
        telemetry.addData("Launcher init started", "")
    }

    override fun run(data: RunData) {
        telemetry.addData("RightTrigger", data.currentGamepadOne.right_trigger)
        flywheel.power = data.currentGamepadOne.right_trigger.toDouble()
    }

    override fun stop() {
        flywheel.power = 0.0
        converter.power = 0.0
    }

    override val usedButtons = arrayOf(
        GamepadButtons.GP1_RIGHT_TRIGGER,
    )
}