package io.github.nadzwyczajnaGrupaRobocza.texaspoker.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "TexasPoker"
            width = 1200
            height = 800
        }
        LwjglApplication(Game(), config).logLevel = Application.LOG_DEBUG
    }
}