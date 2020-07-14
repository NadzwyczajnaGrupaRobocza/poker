package io.github.nadzwyczajnaGrupaRobocza.texaspoker

import android.os.Bundle
import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        setLogLevel(Application.LOG_DEBUG)
        initialize(Game(), config)
    }
}
