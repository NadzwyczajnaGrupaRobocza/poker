package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.GameState
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

abstract class IGameActor(
    protected val object_pool : Context,
) {
    protected val engine: PooledEngine = object_pool.inject()
    protected  val game_state: GameState = object_pool.inject()

    open fun update(delta: Float) {
    }

    protected fun createVisibleObject(
        transform : TransformComponent,
        sprite_width: Float,
        sprite_height: Float,
        region: TextureRegion,
        index: Int = 0
    ): Entity? {
        return engine.entity {
            with<TransformComponent> { x = transform.x; y = transform.y; z = 5F; angle = transform.angle}
            with<SpriteRendererComponent> {
                sprite.setRegion(region)
                sprite.setSize(sprite_width, sprite_height)
                sprite.setPosition(transform.x, transform.y)
            }
        }
    }
    protected fun createVisibleObject(
        pos_x: Float,
        pos_y: Float,
        sprite_width: Float,
        sprite_height: Float,
        region: TextureRegion,
        index: Int = 0
    ): Entity? {
        return engine.entity {
            with<TransformComponent> { x = pos_x; y = pos_y; z = 5F}
            with<SpriteRendererComponent> {
                sprite.setRegion(region)
                sprite.setSize(sprite_width, sprite_height)
                sprite.setPosition(pos_x, pos_y)
            }
        }
    }

}