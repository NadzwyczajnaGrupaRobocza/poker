package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.GameState
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.IGameActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.UILabelComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen.GameScreen
import ktx.actors.*
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger
import ktx.scene2d.*

class UISystem(object_pool: Context) : SortedIteratingSystem(
    allOf(TransformComponent::class, UILabelComponent::class).get(),
    compareBy { entity: Entity -> entity[TransformComponent.mapper]?.z }) {

    private val game_font: BitmapFont = object_pool.inject()
    private val assets: AssetManager = object_pool.inject()
    private val stage: Stage = object_pool.inject()
    private val camera: OrthographicCamera = object_pool.inject()
    private val log = logger<GameScreen>()

    // All classes that depends on textures cannot be created at construction time
    // Textures are load just before the show() function is called
    private var actors = arrayListOf<IGameActor?>()
    private val game_state: GameState = object_pool.inject()
    private lateinit var touchButton: Button
    private lateinit var gameStateLabel: Label
    private lateinit var riseField : TextField

    private var max_players_id = 9

    init {
        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("data/ui.json"))
        Scene2DSkin.defaultSkin.getFont("default-font").data.setScale(1.5F)

        Gdx.input.inputProcessor = stage

        stage.actors {
/*
            table {
                gameStateLabel = label("Current player")
                background("square_button")
                //setFillParent(true)
                align(Align.center)
                setPosition(200, 100)
                this.width = 200F
                this.height = 100F
                //pack()
            }
*/
            var button_width = 200F
            var button_height = 50F

            textButton("Check") {
                width = button_width
                height = button_height
                onClick {
                }
            }
            touchButton = textButton("Rise") {
                width = button_width
                height = button_height
                setPosition(Gdx.graphics.width - 200F, 0F)
                onClick {
                }
            }
            textButton("Fold") {
                width = button_width
                height = button_height
                setPosition(Gdx.graphics.width / 2 - 100F, 0F)
                onClick {
                }
            }
            var riseSlider = slider(min = 10F, max = 1000F, step = 1F, vertical = true){
                height = Gdx.graphics.height - (touchButton.height * 2)
                setPosition(touchButton.x + (touchButton.width * 3 / 4), touchButton.y + (touchButton.height * 1.5F))
                style.knob.minHeight = style.knob.minHeight * 4
                style.knob.minWidth = style.knob.minWidth * 1.5F

                onChange {
                    riseField.text = "rise: $${value}"
                }
            }
            riseField = textField("rise: $${riseSlider.minValue}"){
                setPosition(touchButton.x - 10, touchButton.y + (touchButton.height * 1.5F))
                touchable = Touchable.disabled
            }
        }

/*
        gameStateLabel += Actions.forever(
            Actions.sequence(
                Actions.fadeIn(0.5f) + Actions.fadeOut(
                    0.5f
                )
            )
        )
*/
        //stage.isDebugAll = true
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

//        gameStateLabel.setText("Current player: ${game_state.current_player_id}")

        stage.run {
            act()
            draw()
        }
    }

    override fun entityAdded(entity: Entity) {
        super.entityAdded(entity)

        entity[TransformComponent.mapper]?.let { transform ->
            entity[UILabelComponent.mapper]?.let { ui ->
                stage.addActor(table {
                    background(ui.imageName)
                    //setFillParent(true)
                    align(Align.center)

                    var pos =
                        Vector3(transform.x + ui.offsetX, transform.y + ui.offsetY, transform.z)

                    camera.project(pos)
                    var widthAndHeight = Vector3(ui.width, ui.height, 0F)
                    camera.project(widthAndHeight)
                    setPosition(pos.x, pos.y)
                    this.width = widthAndHeight.x
                    this.height = widthAndHeight.y

                    for ((key, value) in ui.texts) {
                        label("$key$value")
                        row()
                    }
                })
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        //
    }
}