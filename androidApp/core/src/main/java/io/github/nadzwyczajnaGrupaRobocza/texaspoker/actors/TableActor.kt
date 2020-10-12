package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.assets.AssetManager
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.Game
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class TableActor(
    object_pool: Context,
    assets: AssetManager,
    private val tableWidth: Float,
    private val tableHeight: Float
) : GameActor(object_pool) {
    private val backgroundRegion = assets[TextureAtlasAssets.Game].findRegion("background")

    init {
        drawTable()
    }

    private fun drawTable() {
        engine?.entity {
            with<TransformComponent> {
                x = 0F;
                y = 0F;
                z = -1F;
            }
            with<SpriteRendererComponent> {
                sprite.setRegion(backgroundRegion)
                sprite.setSize(tableWidth, tableHeight)
            }
        }
    }
}