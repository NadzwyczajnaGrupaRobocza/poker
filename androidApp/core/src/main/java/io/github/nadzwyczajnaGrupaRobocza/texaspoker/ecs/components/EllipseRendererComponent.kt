package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import ktx.ashley.mapperFor

class EllipseRendererComponent : Component {
    companion object {
        val mapper = mapperFor<EllipseRendererComponent>()
    }

    var width = 0F
    var height = 0F
    var color = Color.WHITE
}
