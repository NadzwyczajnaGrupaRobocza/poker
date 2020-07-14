package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BucketComponent : Component {
    companion object {
        val mapper = mapperFor<BucketComponent>()
    }

    var dropsGathered = 0
}