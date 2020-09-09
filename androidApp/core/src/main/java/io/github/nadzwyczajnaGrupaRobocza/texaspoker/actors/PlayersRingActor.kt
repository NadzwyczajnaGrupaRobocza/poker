package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.assets.AssetManager
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class PlayersRingActor(
    assets: AssetManager,
    engine: Engine?,
    private val tableWidth: Float,
    private val tableHeight: Float
) : GameActor(engine) {

    init {
        drawPlayersRing()
    }

    //TODO: draw n circles for n players

    private fun drawPlayersRing() {
        engine?.entity {
            with<TransformComponent> {
                x = 0F
                y = 0F
            }
            with<EllipseRendererComponent> {
                width = tableWidth
                height = tableHeight
            }
        }

    }
}