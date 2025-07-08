package model

import kotlin.math.absoluteValue

class PercentageRolloutStrategy(
    private val rolloutPercent: Int,
    private val seedDevices: Set<String> = emptySet()
) : RolloutStrategy {

    override fun isEligible(device: Device): Boolean {
        return seedDevices.contains(device.deviceId)
                || (device.deviceId.hashCode().absoluteValue % 100 < rolloutPercent)
    }
}
