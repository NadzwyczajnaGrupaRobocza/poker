package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.UILabelComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class PlayerActor(
    object_pool: Context,
    private val actor_x: Float,
    private val actor_y: Float,
    private val actor_width: Float,
    private val actor_height: Float,
    private val line_color: Color = Color.WHITE,
    private val line_width: Float = 3f,
    private val textureRegion: TextureRegion? = null
) : IGameActor(object_pool) {

    private val actor_left_down_corner_x = actor_x - actor_width / 2
    private val actor_left_down_corner_y = actor_y - actor_height / 2

    private val width_r: Float = actor_width * 2 / 3
    private val height_r: Float = actor_height * 2 / 3

    private val pos_x: Float = actor_left_down_corner_x
    private val pos_y: Float = actor_left_down_corner_y + actor_height / 3

    fun reCreate() {
        engine?.removeEntity(playerIconWithNameAndCoins)
        playerIconWithNameAndCoins = createPlayerIcon()
    }

    //private var playerBackground = createPlayerBackground()
    private var playerIconWithNameAndCoins = createPlayerIcon()
    private var playerStake = createPlayerStake()

    private fun createPlayerBackground(): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = actor_left_down_corner_x
                y = actor_left_down_corner_y
                z = 1F
            }
            with<UILabelComponent> {
                height = actor_height
                width = actor_width
                offsetX = 0.0F
                offsetY = 0.0F
            }
        }
    }

    private fun createPlayerIcon(): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = pos_x
                y = pos_y
                z = 1F
            }
            with<EllipseRendererComponent> {
                width = width_r
                height = height_r
                color = line_color
                lineWidth = line_width
            }
            textureRegion?.let { textureRegion ->
                with<SpriteRendererComponent> {
                    sprite.setRegion(textureRegion)
                    sprite.setSize(width_r, height_r)
                }
            }
            with<UILabelComponent> {
                height = actor_height / 3
                width = actor_width
                offsetX = 0.0F
                offsetY = -actor_height / 3
                texts["name: "] = "Kevin"
                texts["Coin: "] = "100$"
            }
        }
    }

    private fun createPlayerStake(): Entity {
        var width = actor_width / 3
        var height = actor_height / 3
        return addUiEntity(
            actor_left_down_corner_x + width_r, actor_left_down_corner_y + actor_height * 2 / 3,
            width, height, 0F, 0F
        )
    }

    private fun addUiEntity(
        pos_x: Float,
        pos_y: Float,
        ui_width: Float,
        ui_height: Float,
        ui_offsetX: Float,
        ui_offsetY: Float
    ): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = pos_x
                y = pos_y
                z = 1F
            }
            with<UILabelComponent> {
                height = ui_height
                width = ui_width
                offsetX = ui_offsetX
                offsetY = ui_offsetY
                texts[""] = "0$"
            }
        }
    }
}