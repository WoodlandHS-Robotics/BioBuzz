package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
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
    //private val leftLift: Servo = hardwareMap.get(Servo::class.java, "leftLift")
    //private val rightLift: Servo = hardwareMap.get(Servo::class.java, "rightLift")

    override fun start() {
        telemetry.addData("Intake init started", "")
        //leftLift.position = .3
        //rightLift.position = 1 - leftLift.position
    }

    override fun run(data: RunData) {
        telemetry.addData("Left Trigger", data.currentGamepadOne.left_trigger)
        if (data.currentGamepadOne.left_trigger >= 0.25) {
            intake.power = 0.6
            converter.power = .5
            //leftLift.position = .5
            //rightLift.position = 1 - leftLift.position
        } else {
            intake.power = 0.0
            converter.power = 0.0
            //leftLift.position = .3
            //rightLift.position = 1 - leftLift.position
        }
    }

    override fun stop() {
        intake.power = 0.0
    }

    override val usedButtons = arrayOf(
        GamepadButtons.GP1_LEFT_TRIGGER,
    )
}
