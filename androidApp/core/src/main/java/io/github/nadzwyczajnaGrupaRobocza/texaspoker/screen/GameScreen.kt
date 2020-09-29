package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.*
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderingSystem
import ktx.actors.*
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.*
import ktx.style.button
import ktx.style.label
import ktx.style.skin
import ktx.style.textButton
import java.lang.Math.max
import java.lang.Math.min


class GameScreen(
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine,
    private val shape_renderer: ShapeRenderer
) : KtxScreen {

    private val log = logger<GameScreen>()

    // All classes that depends on textures cannot be created at construction time
    // Textures are load just before the show() function is called
    private var actors = arrayListOf<GameActor?>()
    private val game_state = GameState()
    private val stage = stage(batch)
    private lateinit var touchButton: Button
    private lateinit var gameStateLabel: Label

    private var max_players_id = 9

    override fun show() {
        Scene2DSkin.defaultSkin = skin {
            textButton {
                font = BitmapFont()
                fontColor = Color.WHITE
            }
            label {
                font = BitmapFont()
                fontColor = Color.WHITE
            }
            button {

            }
        }

        Gdx.input.inputProcessor = stage

        stage.actors {
            table {
                gameStateLabel = label("Current player")
                setFillParent(true)
                align(Align.top)
                pack()
            }
            table {

                touchButton = button {
                    label("Prev")
                    onClick {
                        --game_state.current_player_id
                        if (game_state.current_player_id < 0)
                            game_state.current_player_id = max_players_id
                    }
                }
                setFillParent(true)
                align(Align.bottomLeft)
                pack()
            }
            table {
                button {
                    label("Next")
                    onClick {
                        ++game_state.current_player_id
                        if (game_state.current_player_id > max_players_id)
                            game_state.current_player_id = 0
                    }
                }

                setFillParent(true)
                align(Align.bottomRight)
                pack()
            }
        }

        gameStateLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
        stage.isDebugAll = true

        createGameScene()

        setupEntityComponentSystems()
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
    }

    override fun render(delta: Float) {
        updateGameScene(delta)
        engine.update(delta)

        gameStateLabel.setText("Current player: ${game_state.current_player_id}")

        stage.run {
            act()
            draw()
        }
    }


    private fun updateGameScene(delta: Float) {
        for (actor in actors) {
            actor?.update(delta)
        }
    }

    private fun createGameScene() {
        log.debug {
            "create game with screen width: ${camera.viewportWidth} height: ${camera.viewportHeight}"
        }
        actors.add(
            TableActor(
                game_state,
                assets,
                engine,
                camera.viewportWidth,
                camera.viewportHeight
            )
        )
        actors.add(
            CommunityCardsActor(
                game_state,
                assets,
                engine,
                camera.viewportWidth,
                camera.viewportHeight
            )
        )
        actors.add(
            PlayersRingActor(
                game_state,
                assets,
                engine,
                camera.viewportWidth,
                camera.viewportHeight
            )
        )
    }

    private fun setupEntityComponentSystems() {
        engine.apply {
            addSystem(
                RenderingSystem(
                    batch,
                    camera,
                    shape_renderer
                )
            )
        }
    }
}
