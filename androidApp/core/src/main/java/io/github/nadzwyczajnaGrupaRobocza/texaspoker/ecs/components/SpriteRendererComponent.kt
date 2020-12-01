package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.mapperFor

class SpriteRendererComponent : Component {
    companion object {
        val mapper = mapperFor<SpriteRendererComponent>()
    }

    val sprite = Sprite()
    var hide : Boolean = false
}
