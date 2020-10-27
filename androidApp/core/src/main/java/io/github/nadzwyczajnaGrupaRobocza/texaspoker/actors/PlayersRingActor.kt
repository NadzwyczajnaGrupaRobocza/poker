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

class PlayersRingActor(
    object_pool: Context,
    assets: AssetManager,
    tableWidth: Float,
    tableHeight: Float,
    center_of_scene: Vector2
) : GameActor(object_pool), KtxInputAdapter {

    private val circleR = 80F
    private val ellipseOffsetX = 280F
    private val ellipseOffsetY = 200F
    private val ellipseWidth = tableWidth - ellipseOffsetX
    private val ellipseWidthR = ellipseWidth / 2
    private val ellipseHeight = tableHeight - ellipseOffsetY
    private val ellipseHeightR = ellipseHeight / 2
    private val ellipsePosX = center_of_scene.x
    private val ellipsePosY = center_of_scene.y

    private val playerIconTextureName = assets[TextureAtlasAssets.Game].findRegion("playerIcon")
    private val tableTexture = assets[TextureAtlasAssets.Game].findRegion("background")
    private val log = logger<GameScreen>()

    private var playerCount = 10
    private var maxPlayer = 10

    private var icons = Array<Entity?>(0)
    private var current_player_id = 0

    private var defaultLineWidth = 3f
    private var selectedPlayerLineWidth = 17f

    init {
        drawTableBorder()
        drawPlayersIcons(playerCount)
    }

    private fun drawTableBorder() {
/*
        addElipseEntity(
            ellipsePosX,
            ellipsePosY,
            ellipseWidth,
            ellipseHeight
        )
*/
        addArcEntity(
            ellipsePosX,
            ellipsePosY,
            ellipseWidth,
            ellipseHeight,
        )
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
                addElipseEntityWithUi(
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

    private fun addArcEntity(
        pos_x: Float,
        pos_y: Float,
        width_r: Float,
        height_r: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
        textureRegion: TextureRegion? = null
    ) {
        engine?.entity {
            with<TransformComponent> {
                x = pos_x - 300F
                y = pos_y
                z = 1F
            }
            with<ArcRendererComponent> {
                radius = 200F
                start = 90F
                degrees = 180F
                color = line_color
                lineWidth = line_width
            }
            textureRegion?.let { textureRegion ->
                with<SpriteRendererComponent> {
                    sprite.setRegion(textureRegion)
                    sprite.setSize(width_r, height_r)
                }
            }
        }
        engine?.entity {
            with<TransformComponent> {
                x = pos_x + 300F
                y = pos_y
                z = 1F
            }
            with<ArcRendererComponent> {
                radius = 200F
                start = -90F
                degrees = 180F
                color = line_color
                lineWidth = line_width
            }
            textureRegion?.let { textureRegion ->
                with<SpriteRendererComponent> {
                    sprite.setRegion(textureRegion)
                    sprite.setSize(width_r, height_r)
                }
            }
        }
    }
    private fun addElipseEntityWithUi(
        pos_x: Float,
        pos_y: Float,
        width_r: Float,
        height_r: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
        textureRegion: TextureRegion? = null
    ): Entity? {
        return engine?.entity {
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
    private fun addElipseEntity(
        pos_x: Float,
        pos_y: Float,
        width_r: Float,
        height_r: Float,
        line_color: Color = Color.WHITE,
        line_width: Float = 3f,
        textureRegion: TextureRegion? = null
    ): Entity? {
        return engine?.entity {
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
            engine?.removeEntity(icon)
        }
        icons = Array(maxPlayer)

        drawPlayersIcons(playerCount)
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