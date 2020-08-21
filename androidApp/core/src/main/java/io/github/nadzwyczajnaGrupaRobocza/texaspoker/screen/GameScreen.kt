package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.CollisionSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.TableAnimationSystem
import ktx.app.KtxScreen

class GameScreen(
    private val batch: Batch,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        engine.apply {
            addSystem(
                RenderSystem(
                    batch,
                    camera
                )
            )
            addSystem(TableAnimationSystem(assets, camera.viewportWidth, camera.viewportHeight))
            addSystem(
                CollisionSystem(
                    assets
                )
            )
        }
    }
}
