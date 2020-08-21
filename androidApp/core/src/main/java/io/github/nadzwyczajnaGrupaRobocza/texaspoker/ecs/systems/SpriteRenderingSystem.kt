package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class SpriteRenderingSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera
) : SortedIteratingSystem(
    allOf(
        TransformComponent::class,
        SpriteRendererComponent::class
    ).get(),
    compareBy { entity: Entity -> entity[SpriteRendererComponent.mapper]?.z }) {


    override fun update(deltaTime: Float) {
        forceSort()
        camera.update()

        batch.use(camera) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[SpriteRendererComponent.mapper]?.let { renderer2d ->
                batch.draw(
                    renderer2d.sprite, transform.x, transform.y
                )
            }
        }
    }
}
