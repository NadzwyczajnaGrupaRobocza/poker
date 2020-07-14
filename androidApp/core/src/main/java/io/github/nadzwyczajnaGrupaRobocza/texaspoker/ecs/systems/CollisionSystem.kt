package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.SoundAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.BucketComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.CollisionComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class CollisionSystem(bucket: Entity, assets: AssetManager) : IteratingSystem(
    allOf(
        TransformComponent::class,
        CollisionComponent::class
    ).get()
) {

    private val dropSound = assets[SoundAssets.Drop]
    private val bucketBounds = bucket[TransformComponent.mapper]!!.bounds
    private val bucketCmp = bucket[BucketComponent.mapper]!!

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            if (transform.bounds.y < 0) {
                engine.removeEntity(entity)
            } else if (transform.bounds.overlaps(bucketBounds)) {
                ++bucketCmp.dropsGathered
                dropSound.play()
                engine.removeEntity(entity)
            }
        }
    }
}