package model

class App(
    val appId: String,
    val appName: String
) {
    private val versions = mutableMapOf<String, AppVersion>()
    var latestVersion: AppVersion? = null
        private set

    fun addVersion(version: AppVersion) {
        versions[version.versionCode] = version
        latestVersion = version
    }

    fun getVersion(versionCode: String): AppVersion? = versions[versionCode]

    fun getAllVersions(): List<AppVersion> = versions.values.toList()
}
