package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.oneOf
import ktx.graphics.use

class RenderingSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val renderer: ShapeRenderer
) : SortedIteratingSystem(
    allOf(
        TransformComponent::class
    )
        .get(),
    compareBy { entity: Entity -> entity[TransformComponent.mapper]?.z }) {


    override fun update(deltaTime: Float) {
        forceSort()
        camera.update()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        batch.use (camera){ batch ->
            entity[TransformComponent.mapper]?.let { transform ->
            entity[SpriteRendererComponent.mapper]?.let { renderer2d ->
                    batch.draw(
                        renderer2d.sprite,
                        transform.x,
                        transform.y,
                        renderer2d.sprite.width,
                        renderer2d.sprite.height
                    )
                }
            }
        }

        renderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            entity[TransformComponent.mapper]?.let { transform ->
            entity[EllipseRendererComponent.mapper]?.let {ellipse ->
                    renderer.ellipse(transform.x, transform.y, ellipse.width, ellipse.height)
                }
            }
        }
    }
}
