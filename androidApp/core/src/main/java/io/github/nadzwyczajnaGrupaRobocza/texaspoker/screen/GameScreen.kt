package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.*
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderingSystem
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.style.label
import ktx.style.skin
import ktx.style.textButton


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
        }

        createGameScene()

        setupEntityComponentSystems()
    }

    override fun render(delta: Float) {
        updateGameScene(delta)
        engine.update(delta)

        batch.use(camera) { batch ->
            val str = "Waiting for player nr. ${game_state.current_player_id}"
            font.draw(
                batch,
                str,
                camera.viewportWidth / 2 - str.length,
                camera.viewportHeight * 99 / 100
            )
        }
        scene2d.table {
            label("Hello world!")
        }
    }


    private fun updateGameScene(delta: Float) {
        for (actor in actors) {
            actor?.update(delta)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            game_state.current_player_id = 0
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            game_state.current_player_id = 1
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            game_state.current_player_id = 2
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            game_state.current_player_id = 3
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            game_state.current_player_id = 4
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            game_state.current_player_id = 5
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            game_state.current_player_id = 6
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            game_state.current_player_id = 7
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            game_state.current_player_id = 8
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            game_state.current_player_id = 9
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
