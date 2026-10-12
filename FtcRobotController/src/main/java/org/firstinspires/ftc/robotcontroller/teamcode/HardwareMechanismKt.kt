package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

abstract class HardwareMechanismKt {
    abstract fun start()
    abstract fun run(data: RunData)
    abstract fun stop()
    abstract val usedButtons: Array<GamepadButtons>

    open class HardwareMechanismSingletonManager<T : HardwareMechanismKt>(private val constructor: (HardwareMap, InitData, Telemetry) -> T) {
        @Volatile
        private var instance: T? = null

        @Synchronized
        fun getInstance(hardwareMap: HardwareMap, data: InitData, telemetry: Telemetry): T? {
            instance = null
            runCatching {
                instance = constructor(hardwareMap, data, telemetry)
            }
            return instance
        }

        fun getInstance(): T? {
            return instance
        }
    }

    protected fun createDefaultMotor(hardwareMap: HardwareMap, motorName: String): DcMotorEx {
        val motor = hardwareMap.get(DcMotorEx::class.java, motorName)
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        if (motorName.lowercase().contains("left")){
            motor.direction = DcMotorSimple.Direction.REVERSE
        }
        return motor
    }

    protected fun sleep(millis: Long){ kotlin.runCatching { Thread.sleep(millis) } }

    data class InitData(
        val teamColor: TeamColor,
        val driveMode: DriveMode,
        val dashboardEnabled: Boolean,
        val referenceAngle: Double,
    )

    data class RunData(
        val currentGamepadOne: Gamepad,
        val currentGamepadTwo: Gamepad,
        val gamepadOneReference: Gamepad,
        val gamepadTwoReference: Gamepad,
        val imuAngleRad: Double,
    )

    enum class DriveMode {
        FIELD,
        ROBOT,
    }
}
