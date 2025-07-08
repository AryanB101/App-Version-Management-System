package model

data class AppVersion(
    val versionCode: String,
    val fileUrl: String,
    val minSupportedOsVersion: Int,
    val metadata: Map<String, String> = emptyMap()
)
