package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class GameState {
    var current_player_id = 0
}

abstract class GameActor(
    protected val engine: Engine?,
    protected val game_state: GameState
) {

    open fun update(delta: Float) {
    }

    protected fun createVisibleObject(
        pos_x: Float,
        pos_y: Float,
        sprite_width: Float,
        sprite_height: Float,
        region: TextureRegion,
        index: Int = 0
    ): Entity? {
        return engine?.entity {
            with<TransformComponent> { x = pos_x; y = pos_y; z = 0F}
            with<SpriteRendererComponent> {
                sprite.setRegion(region)
                sprite.setSize(sprite_width, sprite_height)
                sprite.setPosition(pos_x, pos_y)
            }
        }
    }

}