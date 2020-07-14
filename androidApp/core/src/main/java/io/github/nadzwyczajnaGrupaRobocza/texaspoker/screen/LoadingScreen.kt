package io.github.nadzwyczajnaGrupaRobocza.texaspoker.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.MusicAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.SoundAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.load
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(private val game: Game,
                    private val batch: Batch,
                    private val font: BitmapFont,
                    private val assets: AssetManager,
                    private val camera: OrthographicCamera) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        camera.update();
    }

    override fun render(delta: Float) {
        assets.update()

        batch.use(camera) { batch ->
            font.draw(batch, "Welcome to Drop!!! ", 100f, 150f)
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
        MusicAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }
        TextureAtlasAssets.values().forEach { assets.load(it) }
    }
}