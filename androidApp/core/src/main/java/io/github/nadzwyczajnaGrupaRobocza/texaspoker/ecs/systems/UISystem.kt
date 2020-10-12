package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.GameState
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.GameActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.log.logger
import ktx.scene2d.*
import ktx.style.button
import ktx.style.label
import ktx.style.skin
import ktx.style.textButton

class UISystem(object_pool: Context) : EntitySystem() {

    private val game_font: BitmapFont = object_pool.inject()
    private val assets: AssetManager = object_pool.inject()
    private val stage: Stage = object_pool.inject()
    private val log = logger<GameScreen>()

    // All classes that depends on textures cannot be created at construction time
    // Textures are load just before the show() function is called
    private var actors = arrayListOf<GameActor?>()
    private val game_state: GameState = object_pool.inject()
    private lateinit var touchButton: Button
    private lateinit var gameStateLabel: Label

    private var max_players_id = 9


    init {
        Scene2DSkin.defaultSkin = skin { s ->
            addRegions(assets[TextureAtlasAssets.Game])

            textButton {
                font = game_font
                fontColor = Color.WHITE
                up = s.getDrawable("black_button_normal")
                over = s.getDrawable("black_button_hover")
                down = s.getDrawable("black_button_down")
            }
            label {
                font = game_font
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

                touchButton = textButton("Fold") {
                    onClick {
                    }
                }
                touchButton = textButton("Check") {
                    onClick {
                    }
                }
                touchButton = textButton("Raise") {
                    onClick {
                    }
                }
                setFillParent(true)
                align(Align.bottom)
                pack()
            }
        }

        gameStateLabel += Actions.forever(
            Actions.sequence(
                Actions.fadeIn(0.5f) + Actions.fadeOut(
                    0.5f
                )
            )
        )
//        stage.isDebugAll = true
    }


    override fun update(deltaTime: Float) {
        gameStateLabel.setText("Current player: ${game_state.current_player_id}")

        stage.run {
            act()
            draw()
        }

    }
}