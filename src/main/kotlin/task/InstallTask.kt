package task

import model.Device
import service.installApp

class InstallTask(
    private val device: Device,
    private val fileBytes: ByteArray
) : TaskInterface {

    override fun execute() {
        installApp(device, fileBytes)
    }
}
