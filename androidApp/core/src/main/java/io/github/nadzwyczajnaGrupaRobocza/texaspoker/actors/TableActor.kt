package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.ArcRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class TableActor(
    object_pool: Context,
    assets: AssetManager,
    center_of_scene: Vector2,
    private val tableWidth: Float,
    private val tableHeight: Float
) : GameActor(object_pool) {
    private val backgroundRegion = assets[TextureAtlasAssets.Game].findRegion("background")

    private val ellipsePosX = center_of_scene.x
    private val ellipsePosY = center_of_scene.y

    init {
        drawTable()
        drawTableBorder()
    }

    private fun drawTable() {
        engine?.entity {
            with<TransformComponent> {
                x = 0F;
                y = 0F;
                z = -1F;
            }
            with<SpriteRendererComponent> {
                sprite.setRegion(backgroundRegion)
                sprite.setSize(tableWidth, tableHeight)
            }
        }
    }

    private fun drawTableBorder() {
        addArcEntity(
            ellipsePosX,
            ellipsePosY,
        )
    }

    private fun addArcEntity(
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