package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import ktx.inject.Context

class TableActor(
    object_pool: Context,
    centerOfScene: Vector2,
    tableWidth: Float,
    tableHeight: Float
) : IGameActor(object_pool) {
    private val assets: AssetManager = object_pool.inject()
    private val backgroundSprite = assets[TextureAtlasAssets.Game].findRegion("background")

    private val ellipsePosX = centerOfScene.x
    private val ellipsePosY = centerOfScene.y

    private val tableBackground =
        BackgroundActor(object_pool, backgroundSprite, tableWidth, tableHeight)
    private val tableArc = TableBorderActor(object_pool, ellipsePosX, ellipsePosY)

    private val communityCards = CommunityCardsActor(
        object_pool,
        assets,
        centerOfScene
    )
    private val playersRing =
        PlayersRingActor(
            object_pool.inject(),
            assets,
            tableWidth,
            tableHeight,
            centerOfScene
        )
    private val prizePool = PrizePoolActor(object_pool, centerOfScene)
    private val playerHand = PlayerHandActor(object_pool)

    override fun update(delta: Float) {
        tableBackground.update(delta)
        tableArc.update(delta)
        communityCards.update(delta)
        playersRing.update(delta)
        prizePool.update(delta)
    }
}