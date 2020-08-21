package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TransformComponent : Component {
    companion object {
        val mapper = mapperFor<TransformComponent>()
    }

    var x = 0.0F
    var y = 0.0F
}
