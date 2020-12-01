package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors.IGameActor
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context

class BackgroundActor(
    object_pool: Context,
    backgroundSprite: TextureAtlas.AtlasRegion,
    private val tableWidth: Float,
    private val tableHeight: Float,

    ) : IGameActor(object_pool) {

    private val backgroundEntity =
        engine?.entity {
            with<TransformComponent> {
                x = 0F;
                y = 0F;
                z = -1F;
            }
            with<SpriteRendererComponent> {
                sprite.setRegion(backgroundSprite)
                sprite.setSize(tableWidth, tableHeight)
            }
        }
}