package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import ktx.ashley.mapperFor

class ArcRendererComponent : Component {
    companion object {
        val mapper = mapperFor<ArcRendererComponent>()
    }

    var radius = 0F
    var start = 0F
    var degrees = 0F
    var color = Color.WHITE
    var lineWidth = 1F
}
