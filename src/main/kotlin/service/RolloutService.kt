package service

import model.App
import model.AppVersion
import model.Device
import model.RolloutStrategy
import model.TaskMode

object RolloutService {

    fun isEligibleForRollout(
        app: App,
        targetVersionCode: String,
        device: Device,
        strategy: RolloutStrategy
    ): Boolean {
        val version = app.getVersion(targetVersionCode)
            ?: throw IllegalArgumentException("Version $targetVersionCode not found for app ${app.appId}")

        // 1. Check OS compatibility
        if (device.osVersion < version.minSupportedOsVersion) return false

        // 2. Apply rollout strategy
        return strategy.isEligible(device)
    }

    fun releaseVersion(
        app: App,
        targetVersionCode: String,
        devices: List<Device>,
        strategy: RolloutStrategy,
        mode: TaskMode
    ) {
        require(devices.isNotEmpty()) { "Device list is empty" }

        val targetVersion = app.getVersion(targetVersionCode)
            ?: throw IllegalArgumentException("Target version not found: $targetVersionCode")

        for (device in devices) {
            if (!isAppVersionSupported(app, targetVersionCode, device, strategy)) continue

            when (mode) {
                TaskMode.INSTALL -> {
                    TaskExecutor.executeTask(TaskMode.INSTALL, app, device)
                }
                TaskMode.UPDATE -> {
                    val fromVersion = app.getAllVersions().firstOrNull()
                    if (fromVersion != null) {
                        TaskExecutor.executeTask(TaskMode.UPDATE, app, device, installedVersionCode = fromVersion.versionCode)
                    } else {
                        println("No previous version found for update")
                    }
                }
            }
        }
    }

    fun isAppVersionSupported(
        app: App,
        versionCode: String,
        device: Device,
        strategy: RolloutStrategy
    ): Boolean {
        val version = app.getVersion(versionCode)
            ?: return false

        return device.osVersion >= version.minSupportedOsVersion &&
                strategy.isEligible(device)
    }

    // Checks install support for a specific app version
    fun checkForInstall(version: AppVersion, device: Device): Boolean {
        return device.osVersion >= version.minSupportedOsVersion
    }

    // Overload: checks install support using latest version of the app
    fun checkForInstall(app: App, device: Device): Boolean {
        val latestVersion = app.latestVersion ?: return false
        return device.osVersion >= latestVersion.minSupportedOsVersion
    }

    // Basic: check if a newer version is available and supported
    fun checkForUpdates(
        app: App,
        currentVersionCode: String,
        device: Device
    ): Boolean {
        val current = app.getVersion(currentVersionCode) ?: return false
        val latest = app.latestVersion ?: return false

        // Check if newer and compatible
        return latest.versionCode != current.versionCode &&
                device.osVersion >= latest.minSupportedOsVersion
    }

    // With rollout strategy
    fun checkForUpdates(
        app: App,
        currentVersionCode: String,
        device: Device,
        strategy: RolloutStrategy
    ): Boolean {
        if (!checkForUpdates(app, currentVersionCode, device)) return false
        return strategy.isEligible(device)
    }

}
