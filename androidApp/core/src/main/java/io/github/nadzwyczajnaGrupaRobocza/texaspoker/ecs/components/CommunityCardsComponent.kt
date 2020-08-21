package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Array
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Card
import ktx.ashley.mapperFor

class CommunityCardsComponent : Component {
    companion object {
        val mapper = mapperFor<CommunityCardsComponent>()
    }

    var cards = Array<Entity?>(0)
}
