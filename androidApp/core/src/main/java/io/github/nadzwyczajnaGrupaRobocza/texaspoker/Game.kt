package io.github.nadzwyczajnaGrupaRobocza.texaspoker

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.MyShapeRenderer
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.LoadingScreen
import ktx.actors.stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import ktx.log.debug
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>() {
    private val context = Context()

    class InGameResolution {
        companion object {
            const val width: Float = 1140F
            const val height: Float = 540F
        }
    }

    override fun create() {
        context.register {
            bindSingleton(this@Game)
            bindSingleton(PooledEngine())
            bindSingleton(MyShapeRenderer())
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(BitmapFont(Gdx.files.internal("data/default.fnt")).apply {
                data.scale(1.05F)
            })
            bindSingleton(AssetManager())
            bindSingleton(OrthographicCamera().apply {
                setToOrtho(false, InGameResolution.width, InGameResolution.height)
            })
            bindSingleton(stage(inject()))
            bindSingleton(GameState())

            addScreen(LoadingScreen(this))
            addScreen(GameScreen(this))
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
