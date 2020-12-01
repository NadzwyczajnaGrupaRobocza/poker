package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.ArcRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.inject.Context

class MyShapeRenderer : ShapeRenderer() {
    /** Draws an arc using [ShapeType.Line] or [ShapeType.Filled].  */
    override fun arc(
        x: Float,
        y: Float,
        radius: Float,
        start: Float,
        degrees: Float,
        segments: Int
    ) {
        var segments2 = segments * 2
        require(segments > 0) { "segments must be > 0." }
        val colorBits: Float = color.toFloatBits()
        val theta = 2 * MathUtils.PI * (degrees / 360.0f) / segments2
        val cos = MathUtils.cos(theta)
        val sin = MathUtils.sin(theta)
        var cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians)
        var cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians)
        for (i in 0 until segments2) {
            renderer.color(colorBits)
            renderer.vertex(x + cx, y + cy, 0f)
            val temp = cx
            cx = cos * cx - sin * cy
            cy = sin * temp + cos * cy
            renderer.color(colorBits)
            renderer.vertex(x + cx, y + cy, 0f)
        }
        renderer.color(colorBits)
        renderer.vertex(x + cx, y + cy, 0f)
        val temp = cx
        cx = 0f
        cy = 0f
    }
}

class RenderingSystem(object_pool: Context) : SortedIteratingSystem(
    allOf(TransformComponent::class).get(),
    compareBy { entity: Entity -> entity[TransformComponent.mapper]?.z }) {

    private val batch: Batch = object_pool.inject()
    private val camera: OrthographicCamera = object_pool.inject()
    private val renderer: MyShapeRenderer = object_pool.inject()

    private val defaultLineWidth = 1F

    override fun update(deltaTime: Float) {
        forceSort()
        camera.update()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            drawSprites(entity, transform)
            drawEllipses(entity, transform)
        }
        Gdx.gl.glLineWidth(defaultLineWidth)
    }

    private fun drawSprites(entity: Entity, transform: TransformComponent) {
        batch.use(camera) { batch ->
            entity[SpriteRendererComponent.mapper]?.let { renderer2d ->
                if (!renderer2d.hide) {
//                    batch.draw(
//                        renderer2d.sprite,
//                        transform.x,
//                        transform.y,
//                        renderer2d.sprite.width,
//                        renderer2d.sprite.height
//                    )

                    batch.draw(
                        renderer2d.sprite,
                        transform.x,
                        transform.y,
                        0.0F,
                        0.0F,
                        renderer2d.sprite.width,
                        renderer2d.sprite.height,
                        1.0F,
                        1.0F,
                        transform.angle
                    )
                }
            }
        }
    }

    private fun drawEllipses(entity: Entity, transform: TransformComponent) {
        renderer.projectionMatrix = batch.projectionMatrix;
        renderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            entity[EllipseRendererComponent.mapper]?.let { ellipse ->
                Gdx.gl.glLineWidth(ellipse.lineWidth)
                renderer.color = ellipse.color
                renderer.ellipse(transform.x, transform.y, ellipse.width, ellipse.height)
            }
            entity[ArcRendererComponent.mapper]?.let { arc ->
                Gdx.gl.glLineWidth(arc.lineWidth)
                renderer.color = arc.color
                renderer.arc(transform.x, transform.y, arc.radius, arc.start, arc.degrees)
            }
        }
    }

}
