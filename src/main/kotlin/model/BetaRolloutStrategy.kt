package model

class BetaRolloutStrategy(
    private val betaDevices: Set<String>
) : RolloutStrategy {
    override fun isEligible(device: Device): Boolean {
        return betaDevices.contains(device.deviceId)
    }
}
