package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.*
import ktx.app.KtxInputAdapter
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class PrizePoolActor(
    object_pool: Context,
    private val center_of_scene: Vector2
) : GameActor(object_pool), KtxInputAdapter {

    init {
        drawPool()
    }

    private fun drawPool() {
        val pool_width = 200.0F
        engine.entity {
            with<TransformComponent> {
                x = center_of_scene.x
                y = center_of_scene.y
                z = 1F
            }
            with<UILabelComponent> {
                height = 30.0F
                width = pool_width
                offsetX = -pool_width / 2
                offsetY = -80.0F
                texts[""] = "pool: 0$"
            }
        }
    }
}