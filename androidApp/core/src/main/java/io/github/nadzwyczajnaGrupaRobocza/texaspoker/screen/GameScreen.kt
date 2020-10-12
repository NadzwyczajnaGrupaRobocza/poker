package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.*
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderingSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.UISystem
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.log.debug
import ktx.log.logger


class GameScreen(private val object_pool: Context) : KtxScreen {
    private val log = logger<GameScreen>()
    private val camera: OrthographicCamera = object_pool.inject()
    private val engine: PooledEngine = object_pool.inject()
    private val assets: AssetManager = object_pool.inject()

    // All classes that depends on textures cannot be created at construction time
    // Textures are load just before the show() function is called
    private var actors = arrayListOf<GameActor?>()

    override fun show() {
        createGameScene()
        setupEntityComponentSystems()
    }

    override fun render(delta: Float) {
        updateGameScene(delta)
        engine.update(delta)
    }


    private fun updateGameScene(delta: Float) {
        for (actor in actors) {
            actor?.update(delta)
        }
    }

    private fun createGameScene() {
        log.debug {
            "create game with screen width: ${camera.viewportWidth} height: ${camera.viewportHeight}"
        }
        val centerOfScene = Vector2(
            camera.viewportWidth / 2f,
            camera.viewportHeight / 2f + 30f)

        actors.add(
            TableActor(
                object_pool,
                assets,
                camera.viewportWidth,
                camera.viewportHeight
            )
        )
        actors.add(
            CommunityCardsActor(
                object_pool,
                assets,
                centerOfScene
            )
        )
        actors.add(
            PlayersRingActor(
                object_pool.inject(),
                assets,
                camera.viewportWidth,
                camera.viewportHeight,
                centerOfScene
            )
        )
    }

    private fun setupEntityComponentSystems() {
        engine.apply {
            addSystem(RenderingSystem(object_pool.inject()))
            addSystem(UISystem(object_pool.inject()))
        }
    }
}
