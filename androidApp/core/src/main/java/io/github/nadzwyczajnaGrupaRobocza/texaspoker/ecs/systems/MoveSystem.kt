package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.MoveComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem : IteratingSystem(
    allOf(
        TransformComponent::class,
        MoveComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[MoveComponent.mapper]?.let { move ->
                transform.bounds.x =
                    MathUtils.clamp(
                        transform.bounds.x + move.speed.x * deltaTime,
                        0f, 800f - 64f
                    )
                transform.bounds.y += move.speed.y * deltaTime
            }
        }
    }
}