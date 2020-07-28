package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.CollisionSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


private val log = logger<GameScreen>()

class GameScreen(
    private val batch: Batch,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {

    companion object {
        private const val numberOfFrames = 5
        private const val frameAssetName = "frame"
        private const val cardWidth = 100f
        private const val cardHeight = 152f
    }

    private val tableWidth = camera.viewportWidth
    private val tableHeight = camera.viewportHeight

    private var card = Entity()
    private var frames = Array<Entity>(numberOfFrames)

    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            log.debug { "camera w: ${camera.viewportWidth} h: ${camera.viewportHeight}" }
            if (cardCreated())
                removeCard()
            else
                createCard()
        }
        engine.update(delta)
    }

    fun createCardFrames() {
        val centerOfTable_x = tableWidth / 2
        val centerOfTable_y = tableHeight / 2 - cardHeight / 2

//        frames.add(
//            createVisibleObject(
//                centerOfTable_x,
//                centerOfTable_y,
//                cardWidth,
//                cardHeight,
//                frameAssetName
//            )
//        )
        for (frameId in 0..numberOfFrames) {
            val nextFrameOffset = (frameId - 3) * cardWidth
            frames.add(
                createVisibleObject(
                    centerOfTable_x + nextFrameOffset,
                    centerOfTable_y,
                    cardWidth,
                    cardHeight,
                    frameAssetName
                )
            )
        }
    }

    fun cardCreated(): Boolean {
        return engine.entities.contains(card)
    }

    fun removeCard() {
        engine.removeEntity(card)
    }

    fun createVisibleObject(
        pos_x: Float,
        pos_y: Float,
        width: Float,
        height: Float,
        assetName: String,
        index: Int = 0
    ): Entity {
        return engine.entity {
            with<TransformComponent> { bounds.set(pos_x, pos_y, width, height) }
            with<RenderComponent> {
                sprite.setRegion(
                    assets[TextureAtlasAssets.Game].findRegion(
                        assetName
                    )
                )
            }
        }
    }

    fun createCard() {
        card = createVisibleObject(
            camera.viewportWidth / 2,
            camera.viewportHeight / 2 - cardHeight / 2,
            100f,
            152f,
            "JH"
        )
    }

    fun createEmptyTable() {
        engine.entity {
            with<TransformComponent> {
                bounds.set(
                    0f,
                    0f,
                    camera.viewportWidth,
                    camera.viewportHeight
                )
            }
            with<RenderComponent> {
                sprite.setRegion(assets[TextureAtlasAssets.Game].findRegion("background"))
                z = -1
            }
        }
    }

    fun applyGameSystems() {
        engine.apply {
            addSystem(
                RenderSystem(
                    batch,
                    camera
                )
            )
            addSystem(
                CollisionSystem(
                    assets
                )
            )
        }
    }

    override fun show() {
        createEmptyTable()
        createCardFrames()

        applyGameSystems()
    }
}
