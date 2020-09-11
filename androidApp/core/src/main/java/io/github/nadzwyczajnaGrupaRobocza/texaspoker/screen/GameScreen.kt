package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.CommunityCardsActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.GameActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.PlayersRingActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.TableActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderingSystem
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import kotlin.collections.arrayListOf

class GameScreen(
    private val batch: Batch,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine,
    private val shape_renderer: ShapeRenderer
) : KtxScreen {

    private val log = logger<GameScreen>()

    // All classes that depends on textures cannot be created at construction time
    // Textures are load just before the show() function is called
    private var actors = arrayListOf<GameActor?>()

    override fun render(delta: Float) {
        updateGameScene(delta)
        engine.update(delta)
    }

    override fun show() {
        createGameScene()

        setupEntityComponentSystems()
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
        actors.add(TableActor(assets, engine, camera.viewportWidth, camera.viewportHeight))
        actors.add(CommunityCardsActor(assets, engine, camera.viewportWidth, camera.viewportHeight))
        actors.add(PlayersRingActor(assets, engine, camera.viewportWidth, camera.viewportHeight))
    }

    private fun setupEntityComponentSystems() {
        engine.apply {
            addSystem(
                RenderingSystem(
                    batch,
                    camera,
                    shape_renderer
                )
            )
        }
    }
}
