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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class PokerPlayerActor(
    object_pool: Context,
    private val actor_x: Float,
    private val actor_y: Float,
    private val actor_width: Float,
    private val actor_height: Float,
    private val line_color: Color = Color.WHITE,
    private val line_width: Float = 3f,
    private val textureRegion: TextureRegion? = null
) : GameActor(object_pool) {

    private val actor_left_down_corner_x = actor_x - actor_width / 2
    private val actor_left_down_corner_y = actor_y - actor_height / 2

    private val width_r: Float = actor_width * 2 / 3
    private val height_r: Float = actor_height * 2 / 3

    private val pos_x: Float = actor_left_down_corner_x
    private val pos_y: Float = actor_left_down_corner_y + actor_height / 3

    fun reCreate() {
        engine?.removeEntity(playerIconWithNameAndCoins)
        playerIconWithNameAndCoins = createPlayerIcon()
    }

    //private var playerBackground = createPlayerBackground()
    private var playerIconWithNameAndCoins = createPlayerIcon()
    private var playerStake = createPlayerStake()

    private fun createPlayerBackground(): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = actor_left_down_corner_x
                y = actor_left_down_corner_y
                z = 1F
            }
            with<UILabelComponent> {
                height = actor_height
                width = actor_width
                offsetX = 0.0F
                offsetY = 0.0F
            }
        }
    }

    private fun createPlayerIcon(): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = pos_x
                y = pos_y
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
                height = actor_height / 3
                width = actor_width
                offsetX = 0.0F
                offsetY = -actor_height / 3
                texts["name: "] = "Kevin"
                texts["Coin: "] = "100$"
            }
        }
    }

    private fun createPlayerStake(): Entity {
        var width = actor_width / 3
        var height = actor_height / 3
        return addUiEntity(
            actor_left_down_corner_x + width_r, actor_left_down_corner_y + actor_height * 2 / 3,
            width, height, 0F, 0F
        )
    }

    private fun addUiEntity(
        pos_x: Float,
        pos_y: Float,
        ui_width: Float,
        ui_height: Float,
        ui_offsetX: Float,
        ui_offsetY: Float
    ): Entity {
        return engine.entity {
            with<TransformComponent> {
                x = pos_x
                y = pos_y
                z = 1F
            }
            with<UILabelComponent> {
                height = ui_height
                width = ui_width
                offsetX = ui_offsetX
                offsetY = ui_offsetY
                texts[""] = "0$"
            }
        }
    }
}

class PlayersRingActor(
    object_pool: Context,
    assets: AssetManager,
    tableWidth: Float,
    tableHeight: Float,
    private val center_of_scene: Vector2
) : GameActor(object_pool), KtxInputAdapter {

    private val circleR = 100F
    private val ellipseOffsetX = 300F
    private val ellipseOffsetY = 220F
    private val ellipseWidth = tableWidth - ellipseOffsetX
    private val ellipseWidthR = ellipseWidth / 2
    private val ellipseHeight = tableHeight - ellipseOffsetY
    private val ellipseHeightR = ellipseHeight / 2
    private val ellipsePosX = center_of_scene.x
    private val ellipsePosY = center_of_scene.y

    private val playerIconTextureName = assets[TextureAtlasAssets.Game].findRegion("playerIcon")
    private val log = logger<GameScreen>()

    private var playerCount = 8
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
                    (ellipseWidthR * cos(alpha) + ellipsePosX),
                    (ellipseHeightR * sin(alpha) + ellipsePosY),
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