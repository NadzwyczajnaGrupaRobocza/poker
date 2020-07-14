package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.BucketComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class RenderSystem(
    bucket: Entity,
    private val batch: Batch,
    private val font: BitmapFont,
    private val camera: OrthographicCamera
) : SortedIteratingSystem(
    allOf(
        TransformComponent::class,
        RenderComponent::class
    ).get(),
    compareBy { entity: Entity -> entity[RenderComponent.mapper]?.z }) {

    private val bucketCmp = bucket[BucketComponent.mapper]!!

    override fun update(deltaTime: Float) {
        forceSort()
        camera.update()

        batch.use(camera) {
            super.update(deltaTime)
            font.draw(batch, "Drops Collected: ${bucketCmp.dropsGathered}", 0f, 480f)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[RenderComponent.mapper]?.let { render ->
                batch.draw(
                    render.sprite, transform.bounds.x, transform.bounds.y,
                    transform.bounds.width, transform.bounds.height
                )
            }
        }
    }
}