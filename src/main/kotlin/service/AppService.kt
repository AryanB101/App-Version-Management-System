package service

import model.App
import model.AppVersion

object AppService {
    private val apps = mutableMapOf<String, App>()

    fun registerApp(appId: String, appName: String): App {
        return apps.getOrPut(appId) { App(appId, appName) }
    }

    fun uploadNewVersion(appId: String, version: AppVersion) {
        require(appId.isNotBlank()) { "App ID cannot be blank" }
        require(version.versionCode.isNotBlank()) { "Version code cannot be blank" }

        val app = apps[appId] ?: throw IllegalArgumentException("App not found: $appId")
        app.addVersion(version)
    }

    fun clear() {
        apps.clear() // useful for test resets
    }
}
