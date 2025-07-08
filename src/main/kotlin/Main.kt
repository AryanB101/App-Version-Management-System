import model.*
import service.*
import util.IdGenerator

fun main() {
    println("=== App Version Management System ===")

    // Create a new app
    val appId = IdGenerator.nextAppId()
    val app = AppService.registerApp(appId, "PhonePe")

    // Create devices
    val device1 = Device(IdGenerator.nextDeviceId(), OperatingSystem.ANDROID, 11)
    val device2 = Device(IdGenerator.nextDeviceId(), OperatingSystem.ANDROID, 8)
    val device3 = Device(IdGenerator.nextDeviceId(), OperatingSystem.IOS, 15)

    val allDevices = listOf(device1, device2, device3)

    // Upload v1
    val fileV1 = "initial_version".toByteArray()
    val fileUrlV1 = FileService.uploadFile(fileV1)
    val version1 = AppVersion(
        versionCode = IdGenerator.nextVersionCode(),
        fileUrl = fileUrlV1,
        minSupportedOsVersion = 8
    )
    AppService.uploadNewVersion(appId, version1)

    // Upload v2
    val fileV2 = "updated_version_dark_mode".toByteArray()
    val fileUrlV2 = FileService.uploadFile(fileV2)
    val version2 = AppVersion(
        versionCode = IdGenerator.nextVersionCode(),
        fileUrl = fileUrlV2,
        minSupportedOsVersion = 10
    )
    AppService.uploadNewVersion(appId, version2)

    println("\n=== Install Support Check ===")
    println("Device1 can install v2: ${device1.osVersion >= version2.minSupportedOsVersion}")
    println("Device2 can install v2: ${device2.osVersion >= version2.minSupportedOsVersion}")

    println("\n=== checkForInstall() ===")
    println("Device1 (direct version check): ${RolloutService.checkForInstall(version2, device1)}")
    println("Device2 (direct version check): ${RolloutService.checkForInstall(version2, device2)}")
    println("Device3 (latest version check): ${RolloutService.checkForInstall(app, device3)}")

    // Define a 50% rollout strategy with device3 as a guaranteed seed
    val percentStrategy = PercentageRolloutStrategy(
        rolloutPercent = 50,
        seedDevices = setOf(device3.deviceId)
    )

    println("\n=== checkForUpdates() ===")
    println("Device1 (has v1): ${RolloutService.checkForUpdates(app, version1.versionCode, device1)}")
    println("Device2 (has v1): ${RolloutService.checkForUpdates(app, version1.versionCode, device2)}")

    println("Device1 eligible for update (percent strategy): " +
            RolloutService.checkForUpdates(app, version1.versionCode, device1, percentStrategy))


    println("\n=== Beta Rollout Check ===")
    val betaStrategy = BetaRolloutStrategy(setOf(device1.deviceId, device3.deviceId))
    println("Device1 eligible (beta): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device1, betaStrategy))
    println("Device2 eligible (beta): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device2, betaStrategy))
    println("Device3 eligible (beta): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device3, betaStrategy))

    println("\n=== Percentage Rollout Check (50%) ===")
//    val percentStrategy = PercentageRolloutStrategy(rolloutPercent = 50, seedDevices = setOf(device3.deviceId))
    println("Device1 eligible (percent): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device1, percentStrategy))
    println("Device2 eligible (percent): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device2, percentStrategy))
    println("Device3 eligible (percent, seed): " + RolloutService.isEligibleForRollout(app, version2.versionCode, device3, percentStrategy))

    println("\n=== isAppVersionSupported() Checks ===")
    println("Device1: ${RolloutService.isAppVersionSupported(app, version2.versionCode, device1, betaStrategy)}")
    println("Device2: ${RolloutService.isAppVersionSupported(app, version2.versionCode, device2, betaStrategy)}")
    println("Device3: ${RolloutService.isAppVersionSupported(app, version2.versionCode, device3, betaStrategy)}")

    println("\n=== Manual executeTask (install v2 on device2) ===")
    TaskExecutor.executeTask("install", device2, toVersion = version2)

    println("\n=== Manual executeTask (update device1 from v1 to v2) ===")
    TaskExecutor.executeTask("update", device1, fromVersion = version1, toVersion = version2)

    println("\n=== releaseVersion: UPDATE mode to 50% devices ===")
    RolloutService.releaseVersion(
        app = app,
        targetVersionCode = version2.versionCode,
        devices = allDevices,
        strategy = percentStrategy,
        mode = "update"
    )

    println("\n=== releaseVersion: INSTALL mode to beta devices ===")
    RolloutService.releaseVersion(
        app = app,
        targetVersionCode = version2.versionCode,
        devices = allDevices,
        strategy = betaStrategy,
        mode = "install"
    )

    println("\nDone.")
}
