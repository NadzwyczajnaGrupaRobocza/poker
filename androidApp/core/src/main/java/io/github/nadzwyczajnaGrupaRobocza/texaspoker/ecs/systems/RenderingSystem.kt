package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.ArcRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.inject.Context
import ktx.log.logger

private val log = logger<GameScreen>()


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
        require(segments > 0) { "segments must be > 0." }
        val colorBits: Float = color.toFloatBits()
        val theta = 2 * MathUtils.PI * (degrees / 360.0f) / segments
        val cos = MathUtils.cos(theta)
        val sin = MathUtils.sin(theta)
        var cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians)
        var cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians)
        for (i in 0 until segments) {
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
        batch.use(camera) { batch ->
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
        renderer.projectionMatrix = batch.projectionMatrix;
        renderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            entity[TransformComponent.mapper]?.let { transform ->
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

        Gdx.gl.glLineWidth(defaultLineWidth)
    }

}
