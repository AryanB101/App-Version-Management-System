package model

data class Device(
    val deviceId: String,
    val os: OperatingSystem,
    val osVersion: Int
)

enum class OperatingSystem {
    ANDROID,
    IOS
}
