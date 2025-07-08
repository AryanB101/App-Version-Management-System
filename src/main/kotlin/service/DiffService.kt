package service

import model.AppVersion

object DiffService {
    fun createDiffPack(sourceFile: ByteArray, targetFile: ByteArray): ByteArray {
        // we assume the diff is just the targetFile.
        return targetFile
    }

    fun createUpdatePatch(fromVersion: AppVersion, toVersion: AppVersion): ByteArray {
        val fromFile = FileService.getFile(fromVersion.fileUrl)
        val toFile = FileService.getFile(toVersion.fileUrl)
        return createDiffPack(fromFile, toFile)
    }
}
