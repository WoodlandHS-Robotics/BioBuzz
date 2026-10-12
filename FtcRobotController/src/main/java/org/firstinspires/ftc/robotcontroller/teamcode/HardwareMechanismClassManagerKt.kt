package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter
import java.lang.reflect.Modifier

class HardwareMechanismClassManagerKt : ClassFilter {
    companion object {
        private var mechanisms: MutableList<Class<*>> = mutableListOf()

        fun getMechanisms(): List<Class<*>> {
            if (mechanisms.isEmpty()) {
                RobotLog.addGlobalWarningMessage("Something has gone really terribly wrong in the code and you should tell a programmer this has happened right now. Nothing will work until you do.")
            }
            return mechanisms
        }
    }

    override fun filterAllClassesStart() { mechanisms = mutableListOf() }

    override fun filterClass(clazz: Class<*>) {
        if (!HardwareMechanismKt::class.java.isAssignableFrom(clazz)) return
        if (Modifier.isAbstract(clazz.modifiers)) return

        try {
            val companionField = clazz.getField("Companion")
            val companionType = companionField.type
            if (!HardwareMechanismKt.HardwareMechanismSingletonManager::class.java.isAssignableFrom(companionType)) {
                throw Exception()
            }
        } catch (ignored: Exception) {
            RobotLog.addGlobalWarningMessage("Warning: A class' (" + clazz.simpleName + ") companion object does not extend the required superclass.")
            return
        }

        mechanisms.add(clazz)
    }

    override fun filterOnBotJavaClassesStart() {}
    override fun filterExternalLibrariesClassesStart() {}
    override fun filterOnBotJavaClass(clazz: Class<*>?) {}
    override fun filterExternalLibrariesClass(clazz: Class<*>?) {}
    override fun filterAllClassesComplete() {}
    override fun filterOnBotJavaClassesComplete() {}
    override fun filterExternalLibrariesClassesComplete() {}
}
