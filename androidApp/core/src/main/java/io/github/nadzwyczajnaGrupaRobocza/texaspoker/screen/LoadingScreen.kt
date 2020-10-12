package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.load
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.inject.Context

class LoadingScreen(object_pool: Context) : KtxScreen {

    private val game: Game = object_pool.inject()
    private val batch: Batch = object_pool.inject()
    private val font: BitmapFont = object_pool.inject()
    private val assets: AssetManager = object_pool.inject()
    private val camera: OrthographicCamera = object_pool.inject()

    override fun resize(width: Int, height: Int) {
        camera.update();
    }

    override fun render(delta: Float) {
        assets.update()

        batch.use(camera) { batch ->
            font.draw(batch, "Welcome to TexasPoker!!! ", 100f, 150f)
            if (assets.isFinished) {
                font.draw(batch, "Tap anywhere to begin!", 100f, 100f)
            } else {
                font.draw(batch, "Loading assets...", 100f, 100f)
            }
        }

        if (assets.isFinished && Gdx.input.isTouched) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<GameScreen>()
        }
    }

    override fun show() {
        TextureAtlasAssets.values().forEach { assets.load(it) }
    }
}
