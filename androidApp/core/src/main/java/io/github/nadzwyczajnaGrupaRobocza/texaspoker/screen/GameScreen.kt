package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.MusicAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.BucketComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.MoveComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.CollisionSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.MoveSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.SpawnSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.logger

class GameScreen(private val batch: Batch,
                 private val font: BitmapFont,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine) : KtxScreen {

    companion object {
        private const val basicSpriteSize = 64f
    }

    private val bucket = engine.entity {
        with<BucketComponent>()
        with<TransformComponent>{ bounds.set(camera.viewportWidth / 2f - basicSpriteSize / 2, 20f,
            basicSpriteSize,
            basicSpriteSize
        ) }
        with<MoveComponent>()
        with<RenderComponent>()
    }
    private val touchPos = Vector3()

    override fun render(delta: Float) {
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket[TransformComponent.mapper]?.let { transform -> transform.bounds.x = touchPos.x - basicSpriteSize / 2f}
        }

        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> bucket[MoveComponent.mapper]?.let { move -> move.speed.x = -200f }
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> bucket[MoveComponent.mapper]?.let { move -> move.speed.x = 200f }
            else -> bucket[MoveComponent.mapper]?.let { move -> move.speed.x = 0f }
        }
        engine.update(delta)
    }

    override fun show() {
        //assets[MusicAssets.Rain].apply { isLooping = true }.play()
        engine.entity {
            with<TransformComponent>{ bounds.set(0f, 0f, camera.viewportWidth, camera.viewportHeight) }
            with<RenderComponent>{
                sprite.setRegion(assets[TextureAtlasAssets.Game].findRegion("concept"))
                z = -1
            }
        }
        bucket[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("bucket"))
        engine.apply {
            addSystem(SpawnSystem(assets))
            addSystem(MoveSystem())
            addSystem(
                RenderSystem(
                    bucket,
                    batch,
                    font,
                    camera
                )
            )
            addSystem(
                CollisionSystem(
                    bucket,
                    assets
                )
            )
        }
    }
}