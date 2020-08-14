package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.CommunityCardsComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.collections.sortBy
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class TableAnimationSystem(
    assets: AssetManager,
    private val tableWidth: Float,
    private val tableHeight: Float
) : EntitySystem() {

    companion object {
        private const val numberOfFrames = 5
        private const val cardWidth = 100f
        private const val cardHeight = 152f
    }

    private var clickId = 0
    //private var cards = Array<Entity>(numberOfFrames)
    private var frames = Array<Entity>(numberOfFrames)
    private val backgroundRegion = assets[TextureAtlasAssets.Game].findRegion("background")
    private val frameRegion = assets[TextureAtlasAssets.Game].findRegion("frame")
    private val jHRegion = assets[TextureAtlasAssets.Game].findRegion("JH")

    private var table : Entity? = null

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)

        engine?.let { engine ->
            this.table = engine.entity {
                with<CommunityCardsComponent> {
                    cards = Array(numberOfFrames)
                }
            }
        }

        createEmptyTable()
        createCardFrames()
    }

    override fun update(delta: Float) {
        if (Gdx.input.justTouched()) {
            clickId = (clickId + 1) % (numberOfFrames + 1)

            updateCards()

            log.debug { "click: ${clickId}, frames: ${frames.size}, cards: ${table?.get(CommunityCardsComponent.mapper)?.cards?.size}" }
        }
    }

    private fun updateCards() {
        table?.get(CommunityCardsComponent.mapper)?.let { communityCardsComponent ->
            if(clickId == 0) {
                for (card in communityCardsComponent.cards)
                {
                    engine.removeEntity(card)
                }
                communityCardsComponent.cards = Array(numberOfFrames)
            }
            else {

                table?.get(CommunityCardsComponent.mapper)?.let { communityCardsComponent ->
                    frames[clickId - 1][TransformComponent.mapper]?.let { transform ->
                        communityCardsComponent.cards.add (
                            createVisibleObject(
                                transform.bounds.x,
                                transform.bounds.y,
                                cardWidth,
                                cardHeight,
                                jHRegion
                            )
                        )
                    }
                }
            }
        }
    }

    private fun createEmptyTable() {
        engine.entity {
            with<TransformComponent> {
                bounds.set(
                    0f,
                    0f,
                    tableWidth,
                    tableHeight
                )
            }
            with<RenderComponent> {
                sprite.setRegion(backgroundRegion)
                z = -1
            }
        }
    }

    private fun createVisibleObject(
        pos_x: Float,
        pos_y: Float,
        width: Float,
        height: Float,
        region: TextureRegion,
        index: Int = 0
    ): Entity {
        return engine.entity {
            with<TransformComponent> { bounds.set(pos_x, pos_y, width, height) }
            with<RenderComponent> {
                sprite.setRegion(
                    region
                )
            }
        }
    }

    private fun createCardFrames() {
        val centerOfTableX = tableWidth / 2F
        val centerOfTableY = tableHeight / 2F - cardHeight / 2F
        val halfCardWidth = cardWidth / 2F
        var offset = halfCardWidth

        // If amount of cards is odd, place one at the center of table
        if (numberOfFrames % 2 == 1) {
            frames.add(
                createVisibleObject(
                    centerOfTableX - halfCardWidth,
                    centerOfTableY,
                    cardWidth,
                    cardHeight,
                    frameRegion
                )
            )
            offset = cardWidth
        }

        // Put cards around center pointer
        var halfOfCards = numberOfFrames / 2
        for (frameId in 1..halfOfCards) {
            frames.add(
                createVisibleObject(
                    centerOfTableX - offset - halfCardWidth,
                    centerOfTableY,
                    cardWidth,
                    cardHeight,
                    frameRegion
                )
            )
            frames.add(
                createVisibleObject(
                    centerOfTableX + offset - halfCardWidth,
                    centerOfTableY,
                    cardWidth,
                    cardHeight,
                    frameRegion
                )
            )
            offset += cardWidth
        }

        // Sort cards to be render from the left to right
        frames.sortBy { it[TransformComponent.mapper]!!.bounds.x }
    }

}