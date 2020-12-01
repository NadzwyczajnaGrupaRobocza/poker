package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class UILabelComponent : Component {
    companion object {
        val mapper = mapperFor<UILabelComponent>()
    }

    var texts = mutableMapOf<String, String>()

    var width : Float = 100F
    var height : Float = 50F
    var offsetX : Float = 0.0F
    var offsetY : Float = 0.0F
    var imageName : String = "square_button"
}