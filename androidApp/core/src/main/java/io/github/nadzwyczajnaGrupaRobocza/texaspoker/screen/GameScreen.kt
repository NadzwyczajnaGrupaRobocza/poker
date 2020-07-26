package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.CollisionSystem
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


private val log = logger<GameScreen>()

class GameScreen(private val batch: Batch,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine) : KtxScreen {

    private  var card = Entity()

    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            if(cardCreated())
                removeCard()
            else
                createCard()
        }
        engine.update(delta)
    }

    fun cardCreated() : Boolean {
        return engine.entities.contains(card)
    }

    fun removeCard() {
        engine.removeEntity(card)
    }

    fun createCard() {
        card = engine.entity {
            with<TransformComponent>{ bounds.set(camera.viewportWidth/2, camera.viewportHeight/2, 100f, 152f) }
            with<RenderComponent>{ sprite.setRegion(assets[TextureAtlasAssets.Game].findRegion("JH"))}
        }
    }

    override fun show() {
        engine.entity {
            with<TransformComponent>{ bounds.set(0f, 0f, camera.viewportWidth, camera.viewportHeight) }
            with<RenderComponent>{
                sprite.setRegion(assets[TextureAtlasAssets.Game].findRegion("background"))
                z = -1
            }
        }
        engine.apply {
            addSystem(
                RenderSystem(
                    batch,
                    camera
                )
            )
            addSystem(
                CollisionSystem(
                    assets
                )
            )
        }
    }
}
