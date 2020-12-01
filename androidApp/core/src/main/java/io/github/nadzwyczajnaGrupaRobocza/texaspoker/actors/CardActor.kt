package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.SpriteRendererComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.get
import ktx.inject.Context

class CardActor(
    object_pool: Context,
    transform: TransformComponent,
    sprite: TextureAtlas.AtlasRegion
) :
    IGameActor(object_pool) {

    companion object {
        private const val cardWidth = 60f
        private const val cardHeight = 91.5f
    }

    private val cardBody = createVisibleObject(
        transform,
        cardWidth,
        cardHeight,
        sprite
    )

    fun setVisibility(visibility: Boolean) {
        cardBody?.let { entity ->
            entity[SpriteRendererComponent.mapper]?.hide = !visibility
        }
    }
}