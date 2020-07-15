package io.github.nadzwyczajnaGrupaRobocza.texaspoker

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import ktx.log.debug
import ktx.log.logger

private val log = logger<Game>()

/*
TODO:
* Add player dummy icons
* Add ui to set number of players
* Visualize player at the table in ellipse shape
 */
class Game : KtxGame<KtxScreen>() {
    private val context = Context()

    override fun create() {
        context.register {
            bindSingleton(this@Game)
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(BitmapFont())
            bindSingleton(AssetManager())
            bindSingleton(OrthographicCamera().apply {
                setToOrtho(false, 800f, 480f) })
            bindSingleton(PooledEngine())

            addScreen(
                LoadingScreen(
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject()
                )
            )
            addScreen(
                GameScreen(
                    inject(),
                    inject(),
                    inject(),
                    inject()
                )
            )
        }
        setScreen<LoadingScreen>()
        super.create()
    }

    override fun dispose() {
        log.debug { "Entities in engine: ${context.inject<PooledEngine>().entities.size()}" }
        context.dispose()
        super.dispose()
    }
}
