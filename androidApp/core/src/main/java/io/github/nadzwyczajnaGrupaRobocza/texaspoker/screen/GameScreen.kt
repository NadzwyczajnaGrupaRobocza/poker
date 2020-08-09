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
import ktx.ashley.get
import ktx.ashley.with
import ktx.collections.sortBy
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

    private var cards = Array<Entity>(numberOfFrames)
    private var frames = Array<Entity>(numberOfFrames)
    private var clickId = 0

    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            log.debug { "camera w: ${camera.viewportWidth} h: ${camera.viewportHeight}" }
            clickId = (clickId + 1) % (numberOfFrames + 1)
            updateCards()
            //if (cardCreated())
            //    removeCard()
            // else
            //     createCard()
        }
        engine.update(delta)
    }

    fun updateCards() {
        if (clickId == 0) {
            for (card in cards) {
                engine.removeEntity(card)

            }
            return
        } else {
            frames[clickId-1][TransformComponent.mapper]?.let { transform ->
                cards.add(
                    createVisibleObject(
                        transform.bounds.x,
                        transform.bounds.y,
                        cardWidth,
                        cardHeight,
                        "JH"
                    )
                )
            }
        }
    }

    fun createCardFrames() {
        val centerOfTable_x = tableWidth / 2
        val centerOfTable_y = tableHeight / 2 - cardHeight / 2

        val halfCardWidth = cardWidth / 2
        val lastCardId = numberOfFrames - 1

        var offset = halfCardWidth

        if (numberOfFrames % 2 == 1) {
            frames.add(
                createVisibleObject(
                    centerOfTable_x - halfCardWidth,
                    centerOfTable_y,
                    cardWidth,
                    cardHeight,
                    frameAssetName
                )
            )
            offset = cardWidth
        }

        var halfOfCards = numberOfFrames / 2
        for (frameId in 1..halfOfCards) {
            frames.add(
                createVisibleObject(
                    centerOfTable_x - offset - halfCardWidth,
                    centerOfTable_y,
                    cardWidth,
                    cardHeight,
                    frameAssetName
                )
            )
            frames.add(
                createVisibleObject(
                    centerOfTable_x + offset - halfCardWidth,
                    centerOfTable_y,
                    cardWidth,
                    cardHeight,
                    frameAssetName
                )
            )
            offset += cardWidth
        }
        frames.sortBy { it[TransformComponent.mapper]!!.bounds.x }
    }

    //fun cardCreated(): Boolean {
    //    return engine.entities.contains(card)
    //}

    //fun removeCard() {
    //    engine.removeEntity(card)
    //}

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

//    fun createCard() {
    //       card = createVisibleObject(
    //          camera.viewportWidth / 2 - cardWidth / 2,
    //         camera.viewportHeight / 2 - cardHeight / 2,
    ///        100f,
    //      152f,
    //     "JH"
    //)
    //}

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
