package io.github.nadzwyczajnaGrupaRobocza.texaspoker.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.inject.Context

class PlayerHandActor(
    object_pool: Context,
) : IGameActor(object_pool) {

    private val assets: AssetManager = object_pool.inject()
    private val khRegion = assets[TextureAtlasAssets.Game].findRegion("KH")
    private val qhRegion = assets[TextureAtlasAssets.Game].findRegion("QH")
    private var cards = Array<CardActor?>(2)

    init {
        var transform = TransformComponent()
        transform.x = 380.0F
        transform.y = 70.0F
        transform.angle = 5F
        cards.add(
            CardActor(object_pool, transform, khRegion)
        )
        transform.x = 400.0F
        transform.y = 70.0F
        transform.angle = -15F
        cards.add(
            CardActor(object_pool, transform, qhRegion)
        )
    }
}