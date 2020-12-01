package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.graphics.Color
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.IGameActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.ArcRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class TableBorderActor(
    object_pool: Context,
    ellipsePosX: Float,
    ellipsePosY: Float
) : IGameActor(object_pool) {

    private val tableLeftArcEntity = addLeftArcEntity(ellipsePosX, ellipsePosY)
    private val tableRightArcEntity = addRightArcEntity(ellipsePosX, ellipsePosY)

    private fun addLeftArcEntity(
        pos_x: Float,
        pos_y: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
    ) {
        engine?.entity {
            with<TransformComponent> {
                x = pos_x - 300F
                y = pos_y
                z = 1F
            }
            with<ArcRendererComponent> {
                radius = 220F
                start = 90F
                degrees = 180F
                color = line_color
                lineWidth = line_width
            }
        }
    }

    private fun addRightArcEntity(
        pos_x: Float,
        pos_y: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
    ) {
        engine?.entity {
            with<TransformComponent> {
                x = pos_x + 300F
                y = pos_y
                z = 1F
            }
            with<ArcRendererComponent> {
                radius = 220F
                start = -90F
                degrees = 180F
                color = line_color
                lineWidth = line_width
            }
        }
    }
}