package service

import model.Device

fun installApp(device: Device, file: ByteArray) {
    println("Installed app on ${device.deviceId} (OS: ${device.os}, v${device.osVersion})")
}

fun updateApp(device: Device, diff: ByteArray) {
    println("Updated app on ${device.deviceId} (OS: ${device.os}, v${device.osVersion})")
}
