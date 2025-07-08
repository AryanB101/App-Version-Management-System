package service

import model.AppVersion
import model.Device
import task.TaskInterface

object TaskExecutor {
    fun execute(task: TaskInterface) {
        task.execute()
    }

    fun executeTask(
        mode: String,
        device: Device,
        fromVersion: AppVersion? = null,
        toVersion: AppVersion
    ) {
        if (device.deviceId.isBlank()) {
            println("Invalid device ID")
            return
        }

        if (toVersion.versionCode.isBlank()) {
            println("Invalid toVersion")
            return
        }

        val task = when (mode.lowercase()) {
            "install" -> {
                TaskFactory.createInstallTask(device, toVersion)
            }
            "update" -> {
                if (fromVersion == null) {
                    println("Cannot perform update: fromVersion is null")
                    return
                }
                TaskFactory.createUpdateTask(device, fromVersion, toVersion)
            }
            else -> {
                println("Unknown task type: $mode")
                return
            }
        }
        execute(task)
    }
}
