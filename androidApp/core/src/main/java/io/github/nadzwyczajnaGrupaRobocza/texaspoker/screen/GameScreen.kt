package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.CollisionSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(private val batch: Batch,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine) : KtxScreen {

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        engine.entity {
            with<TransformComponent>{ bounds.set(0f, 0f, camera.viewportWidth, camera.viewportHeight) }
            with<RenderComponent>{
                sprite.setRegion(assets[TextureAtlasAssets.Game].findRegion("concept"))
                z = -1
            }
        }
        engine.apply {
            addSystem(
                RenderSystem(
                    batch,
                    camera
                )
            )
            addSystem(
                CollisionSystem(
                    assets
                )
            )
        }
    }
}