package service

import util.IdGenerator

object FileService {
    private val fileStorage = mutableMapOf<String, ByteArray>()
    private var fileCounter = 0

    fun uploadFile(fileContent: ByteArray): String {
        val fileId = IdGenerator.nextFileId()
        fileStorage[fileId] = fileContent
        return fileId
    }

    fun getFile(fileUrl: String): ByteArray {
        return fileStorage[fileUrl]
            ?: throw IllegalArgumentException("File not found: $fileUrl")
    }

    fun clear() {
        fileStorage.clear()
        fileCounter = 0
    }
}
