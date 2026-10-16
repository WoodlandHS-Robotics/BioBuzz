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
    private val intake: DcMotorEx = createDefaultMotor(hardwareMap,"IntakeMotor")

    override fun start() {
        telemetry.addData("Intake init started", "")
    }

    override fun run(data: RunData) {
        telemetry.addData("DpadUp", data.currentGamepadOne.left_trigger)
        intake.power = data.currentGamepadOne.left_trigger.toDouble()
    }
    override val usedButtons = arrayOf(
        GamepadButtons.GP1_LEFT_TRIGGER,
    )
    }