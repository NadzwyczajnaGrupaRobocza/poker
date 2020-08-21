package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class ShapeRenderingSystem(
    private val renderer: ShapeRenderer
) : IteratingSystem(
    allOf(
        TransformComponent::class,
        EllipseRendererComponent::class
    ).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[EllipseRendererComponent.mapper]?.let { ellipse ->
                renderer.use(ShapeRenderer.ShapeType.Line) {renderer ->
                    renderer.ellipse(transform.x, transform.y, ellipse.width, ellipse.height)
                }
            }
        }
    }
}
