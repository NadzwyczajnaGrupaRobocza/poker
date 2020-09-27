package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.EllipseRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.app.KtxInputAdapter
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.cos
import kotlin.math.sin

class PlayersRingActor(
    game_state: GameState,
    assets: AssetManager,
    engine: Engine?,
    tableWidth: Float,
    tableHeight: Float
) : GameActor(engine, game_state), KtxInputAdapter {

    private val circleR = 100F
    private val ellipseOffset = 300F
    private val ellipseWidth = tableWidth - ellipseOffset
    private val ellipseWidthR = ellipseWidth / 2
    private val ellipseHeight = tableHeight - ellipseOffset
    private val ellipseHeightR = ellipseHeight / 2
    private val ellipsePosX = tableWidth / 2
    private val ellipsePosY = tableHeight / 2

    private val playerIconTextureName = assets[TextureAtlasAssets.Game].findRegion("playerIcon")
    private val log = logger<GameScreen>()

    private var playerCount = 10
    private var maxPlayer = 10

    private var icons = Array<Entity?>(0)
    private var current_player_id = 0

    init {
        drawPlayersRing()
        drawPlayersIcons(playerCount)

        Gdx.input.inputProcessor = this
    }

    private fun drawPlayersRing() {
        addElipseEntity(
            ellipsePosX,
            ellipsePosY,
            ellipseWidth,
            ellipseHeight
        )

    }

    private fun degree(rad: Double): Double {
        return rad * 180 / Math.PI
    }

    private fun radian(degree: Double): Double {
        return degree / 180 * Math.PI
    }

    private fun getPlayerIconLineColor(playerId: Int): Color {
        return if (game_state.current_player_id == playerId)
            Color.BLUE
        else Color.WHITE
    }

    private fun getPlayerIconR(playerId: Int): Float {
        val offset = 10
        return if (game_state.current_player_id == playerId)
            circleR + offset
        else circleR - offset
    }

    private fun drawPlayersIcons(playerCount: Int) {

        val alphaStep = 360.0 / playerCount
        for (playerId in 0 until playerCount) {
            log.debug { "playerId: $playerId alphaStep: $alphaStep alpha: ${playerId * alphaStep}" }
            val alpha = radian(playerId * alphaStep).toFloat()
            val playerCircleR = getPlayerIconR(playerId)
            icons.add(
                addElipseEntity(
                    ellipseWidthR * cos(alpha) + ellipsePosX,
                    ellipseHeightR * sin(alpha) + ellipsePosY,
                    playerCircleR,
                    playerCircleR,
                    line_color = getPlayerIconLineColor(playerId),
                    playerIconTextureName
                )
            )
        }

    }

    private fun addElipseEntity(
        pos_x: Float,
        pos_y: Float,
        width_r: Float,
        height_r: Float,
        line_color: Color = Color.WHITE,
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
            }
            textureRegion?.let { textureRegion ->
                with<SpriteRendererComponent> {
                    sprite.setRegion(textureRegion)
                    sprite.setSize(width_r, height_r)
                }
            }
        }
    }

    override fun scrolled(amount: Int): Boolean {
        nextAnimation(amount)
        return true
    }

    override fun update(delta: Float) {
        if (Gdx.input.justTouched()) {
            nextAnimation(1)
        }

        if (current_player_id != game_state.current_player_id) {
            assert(game_state.current_player_id < playerCount)

            current_player_id = game_state.current_player_id
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