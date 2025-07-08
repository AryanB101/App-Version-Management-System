package service

import model.AppVersion
import model.Device
import task.TaskInterface
import task.InstallTask
import task.UpdateTask

object TaskFactory {

    fun createInstallTask(
        device: Device,
        version: AppVersion
    ): TaskInterface {
        val fileBytes = FileService.getFile(version.fileUrl)
        return InstallTask(device, fileBytes)
    }

    fun createUpdateTask(
        device: Device,
        fromVersion: AppVersion,
        toVersion: AppVersion
    ): TaskInterface {
        val diffBytes = DiffService.createUpdatePatch(fromVersion, toVersion)
        return UpdateTask(device, diffBytes)
    }
}
