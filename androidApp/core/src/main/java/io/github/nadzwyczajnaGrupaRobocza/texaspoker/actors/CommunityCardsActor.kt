package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.ashley.get
import ktx.collections.sortBy
import ktx.inject.Context
import ktx.log.debug
import ktx.log.logger

class CommunityCardsActor(
    object_pool: Context,
    assets: AssetManager,
    private val center_of_scene: Vector2
) : GameActor(object_pool) {

    companion object {

        private const val numberOfFrames = 5
        private const val cardWidth = 80f
        private const val cardHeight = 122f
    }

    private val log = logger<GameScreen>()
    private val frameRegion = assets[TextureAtlasAssets.Game].findRegion("frame")
    private val jHRegion = assets[TextureAtlasAssets.Game].findRegion("JH")

    private var clickId = 0
    private var frames = Array<Entity>(numberOfFrames)
    private var cards = Array<Entity?>(0)

    init {
        cards = Array(numberOfFrames)
        createCardFrames()
    }

    override fun update(delta: Float) {
        if (Gdx.input.justTouched()) {
            calculateNextCard()
            putNextCard()
            logCardDetails()
        }
    }

    private fun calculateNextCard() {
        clickId = (clickId + 1) % (numberOfFrames + 1)
    }

    private fun putNextCard() {
        if (clickId == 0) {
            takeOutAllCards()
        } else {
            drawCardAtFrame(clickId - 1)
        }
    }

    private fun takeOutAllCards() {
        for (card in cards) {
            engine?.removeEntity(card)
        }
        cards = Array(numberOfFrames)
    }

    private fun drawCardAtFrame(frameId: Int) {
        frames[frameId][TransformComponent.mapper]?.let { transform ->
            cards.add(
                createVisibleObject(
                    transform.x,
                    transform.y,
                    cardWidth,
                    cardHeight,
                    jHRegion
                )
            )
        }
    }

    private fun logCardDetails() {
        log.debug {
            "click: ${clickId}, frames: ${frames.size}, cards: ${cards?.size}"
        }
    }

    private fun createCardFrames() {
        var centerOfTableYWithCardOffset = center_of_scene.y - cardHeight / 2F
        val halfCardWidth = cardWidth / 2F
        var offset = halfCardWidth

        // If amount of cards is odd, place one at the center of table
        if (numberOfFrames % 2 == 1) {
            frames.add(
                createVisibleObject(
                    center_of_scene.x - halfCardWidth,
                    centerOfTableYWithCardOffset,
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
                    center_of_scene.x - offset - halfCardWidth,
                    centerOfTableYWithCardOffset,
                    cardWidth,
                    cardHeight,
                    frameRegion
                )
            )
            frames.add(
                createVisibleObject(
                    center_of_scene.x + offset - halfCardWidth,
                    centerOfTableYWithCardOffset,
                    cardWidth,
                    cardHeight,
                    frameRegion
                )
            )
            offset += cardWidth
        }

        // Sort cards to be render from the left to right
        frames.sortBy { it[TransformComponent.mapper]!!.x }
    }
}