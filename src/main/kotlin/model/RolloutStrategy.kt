package model

interface RolloutStrategy {
    fun isEligible(device: Device): Boolean
}
