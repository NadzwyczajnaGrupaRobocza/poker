package io.github.nadzwyczajnaGrupaRobocza.texaspoker.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "Drop"
            width = 800
            height = 400
        }
        LwjglApplication(Game(), config).logLevel = Application.LOG_DEBUG
    }
}