package service

import model.App
import model.Device
import model.TaskMode
import task.TaskInterface

object TaskExecutor {

    fun execute(task: TaskInterface) {
        task.execute()
    }

    fun executeTask(
        mode: TaskMode,
        app: App,
        device: Device,
        installedVersionCode: String? = null
    ) {
        val toVersion = app.latestVersion
        if (toVersion == null) {
            println("No version available for app ${app.appName}")
            return
        }

        when (mode) {
            TaskMode.INSTALL -> {
                if (!RolloutService.checkForInstall(toVersion, device)) {
                    println("Device ${device.deviceId} not compatible with version ${toVersion.versionCode}")
                    return
                }
                val task = TaskFactory.createInstallTask(device, toVersion)
                execute(task)
            }

            TaskMode.UPDATE -> {
                if (installedVersionCode == null) {
                    println("Cannot update without knowing installed version")
                    return
                }

                val fromVersion = app.getVersion(installedVersionCode)
                if (fromVersion == null) {
                    println("Installed version $installedVersionCode not found in app ${app.appName}")
                    return
                }

                if (!RolloutService.checkForUpdates(app, installedVersionCode, device)) {
                    println("No update available for device ${device.deviceId}")
                    return
                }

                val task = TaskFactory.createUpdateTask(device, fromVersion, toVersion)
                execute(task)
            }
        }
    }
}
