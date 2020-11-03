package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.*
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.math.radian
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.app.KtxInputAdapter
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context
import ktx.log.debug
import ktx.log.logger
import kotlin.math.cos
import kotlin.math.sin

class PokerPlayerActor(
    object_pool: Context,
    private val pos_x: Float,
    private val pos_y: Float,
    private val width_r: Float,
    private val height_r: Float,
    private val line_color: Color = Color.WHITE,
    private val line_width: Float = 3f,
    private val textureRegion: TextureRegion? = null
) : GameActor(object_pool) {

    fun reCreate() {
        engine?.removeEntity(playerIconWithNameAndCoins)
        playerIconWithNameAndCoins = createPlayerIcon()
    }

    private var playerIconWithNameAndCoins = createPlayerIcon()

    private fun createPlayerIcon(): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = pos_x - width_r / 2
                y = pos_y - height_r / 2
                z = 1F
            }
            with<EllipseRendererComponent> {
                width = width_r
                height = height_r
                color = line_color
                lineWidth = line_width
            }
            textureRegion?.let { textureRegion ->
                with<SpriteRendererComponent> {
                    sprite.setRegion(textureRegion)
                    sprite.setSize(width_r, height_r)
                }
            }
            with<UILabelComponent> {
                height = 40F
                width = 125F
                offsetX = 0F
                offsetY = -height
                texts["name"] = "Kevin"
                texts["Coin"] = "100$"
            }
        }
    }


    private fun addUiEntity(
        pos_x: Float,
        pos_y: Float,
        ui_width: Float,
        ui_height: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
        textureRegion: TextureRegion? = null
    ): Entity? {
        return engine?.entity {
            with<TransformComponent> {
                x = pos_x
                y = pos_y
                z = 1F
            }
            with<UILabelComponent> {
                height = ui_height
                width = ui_width
                offsetX = 0F
                offsetY = 0F
            }
        }
    }
}

class PlayersRingActor(
    object_pool: Context,
    assets: AssetManager,
    tableWidth: Float,
    tableHeight: Float,
    center_of_scene: Vector2
) : GameActor(object_pool), KtxInputAdapter {

    private val circleR = 55F
    private val ellipseOffsetX = 200F
    private val ellipseOffsetY = 180F
    private val ellipseWidth = tableWidth - ellipseOffsetX
    private val ellipseWidthR = ellipseWidth / 2
    private val ellipseHeight = tableHeight - ellipseOffsetY
    private val ellipseHeightR = ellipseHeight / 2
    private val ellipsePosX = center_of_scene.x
    private val ellipsePosY = center_of_scene.y

    private val playerIconTextureName = assets[TextureAtlasAssets.Game].findRegion("playerIcon")
    private val log = logger<GameScreen>()

    private var playerCount = 10
    private var maxPlayer = 10

    private var icons = Array<PokerPlayerActor?>(0)
    private var current_player_id = 0

    private var defaultLineWidth = 3f

    init {
        drawPlayersIcons(playerCount)
    }

    private fun getPlayerIconLineColor(playerId: Int): Color {
        return if (game_state.current_player_id == playerId)
            Color.VIOLET
        else Color.WHITE
    }

    private fun getPlayerIconR(playerId: Int): Float {
        val offset = 10
        return circleR
        return if (game_state.current_player_id == playerId)
            circleR + offset
        else circleR
    }

    private fun getPlayerIconLineWidth(playerId: Int): Float {
/*
        if (game_state.current_player_id == playerId) {
            return selectedPlayerLineWidth
        }
*/
        return defaultLineWidth
    }

    private fun drawPlayersIcons(playerCount: Int) {

        val alphaStep = 360.0 / playerCount
        for (playerId in 0 until playerCount) {
            log.debug { "playerId: $playerId alphaStep: $alphaStep alpha: ${playerId * alphaStep}" }
            val alpha = radian(playerId * alphaStep).toFloat()
            val playerCircleR = getPlayerIconR(playerId)
            icons.add(
                PokerPlayerActor(
                    object_pool,
                    ellipseWidthR * cos(alpha) + ellipsePosX,
                    ellipseHeightR * sin(alpha) + ellipsePosY,
                    playerCircleR,
                    playerCircleR,
                    line_color = getPlayerIconLineColor(playerId),
                    line_width = getPlayerIconLineWidth(playerId),
                    playerIconTextureName
                )
            )
        }

    }
    override fun update(delta: Float) {

        if (current_player_id != game_state.current_player_id) {
            assert(game_state.current_player_id < playerCount)

            //current_player_id = game_state.current_player_id
            redrawPlayerIcons()
        }

    }

    private fun redrawPlayerIcons() {
        for (icon in icons) {
            icon?.reCreate()
        }
    }

    private fun nextAnimation(amount: Int) {
        playerCount += amount

        if (playerCount < 0) {
            playerCount = maxPlayer
        } else if (playerCount > maxPlayer) {
            playerCount = 0
        }

        log.debug { "scrolled: $amount playerCount: $playerCount" }
        redrawPlayerIcons()
    }
}