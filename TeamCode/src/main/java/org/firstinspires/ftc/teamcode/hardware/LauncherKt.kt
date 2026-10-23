package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.GamepadButtons
import org.firstinspires.ftc.robotcontroller.teamcode.HardwareMechanismKt
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Responsible for managing our launching system during TeleOp.
  */
@Config
class LauncherKt private constructor(hardwareMap: HardwareMap, data: InitData, private val telemetry: Telemetry) : HardwareMechanismKt() {

    companion object : HardwareMechanismSingletonManager<LauncherKt>(::LauncherKt)

    private val flywheel: DcMotorEx = createDefaultMotor(hardwareMap,"FlywheelMotor")
    private val kicker: Servo = hardwareMap.get(Servo::class.java, "kickerServo")


    override fun start() {
        kicker.position = 0.5
    }

    override fun run(data: RunData) {
        telemetry.addData("RightTrigger", data.currentGamepadOne.right_trigger)
        if (data.currentGamepadOne.right_trigger >= 0.25) {
            telemetry.addData("kicker pos", kicker.position)
            telemetry.addData("flywheel vel", flywheel.velocity)
            flywheel.velocity = -1700.0
            if (flywheel.velocity  <= -1675) {
                kicker.position = 1.0
            }
        }
        else {
            kicker.position = 0.5
            flywheel.velocity = 0.0
        }
    }

    override fun stop() {
        flywheel.power = 0.0
    }

    override val usedButtons = arrayOf(
        GamepadButtons.GP1_RIGHT_TRIGGER,
        GamepadButtons.GP1_RIGHT_BUMPER,
    )
}
