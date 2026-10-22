package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.GamepadButtons
import org.firstinspires.ftc.robotcontroller.teamcode.HardwareMechanismKt
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Responsible for managing our intake system during TeleOp.
 */
@Config
class IntakeKt private constructor(hardwareMap: HardwareMap, data: InitData, private val telemetry: Telemetry) : HardwareMechanismKt() {

    companion object : HardwareMechanismSingletonManager<IntakeKt>(::IntakeKt)
    private val converter: DcMotorEx = createDefaultMotor(hardwareMap,"ConverterMotor")
    private val intake: DcMotorEx = createDefaultMotor(hardwareMap, "IntakeMotor")

    override fun start() {
        telemetry.addData("Intake init started", "")
    }

    override fun run(data: RunData) {
        telemetry.addData("Left Trigger", data.currentGamepadOne.left_trigger)
        if (data.currentGamepadOne.left_trigger >= 0.25) {
            intake.power = 0.5
            converter.power = .5
        } else {
            intake.power = 0.0
            converter.power = 0.0
        }
    }

    override fun stop() {
        intake.power = 0.0
    }

    override val usedButtons = arrayOf(
        GamepadButtons.GP1_LEFT_TRIGGER,
    )
}
