package util

object IdGenerator {
    private var appCounter = 0
    private var versionCounter = 0
    private var deviceCounter = 0
    private var fileCounter = 0

    fun nextAppId(): String = "app_${appCounter++}"
    fun nextVersionCode(): String = "v_${versionCounter++}"
    fun nextDeviceId(): String = "device_${deviceCounter++}"
    fun nextFileId(): String = "file_${fileCounter++}"
}
