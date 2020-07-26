package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.CollisionComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf

class CollisionSystem(assets: AssetManager) : IteratingSystem(
    allOf(
        TransformComponent::class,
        CollisionComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
    }
}
