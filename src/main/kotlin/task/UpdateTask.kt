package task

import model.Device
import service.updateApp

class UpdateTask(
    private val device: Device,
    private val diffBytes: ByteArray
) : TaskInterface {

    override fun execute() {
        updateApp(device, diffBytes)
    }
}
