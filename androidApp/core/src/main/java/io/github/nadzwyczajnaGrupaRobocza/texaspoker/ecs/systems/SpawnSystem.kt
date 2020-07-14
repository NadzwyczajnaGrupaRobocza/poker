package io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.MathUtils
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.TextureAtlasAssets
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.assets.get
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.CollisionComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.MoveComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.RenderComponent
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.ecs.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class SpawnSystem(assets: AssetManager) : IntervalSystem(1f) {
    private val dropRegion = assets[TextureAtlasAssets.Game].findRegion("drop")

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        updateInterval()
    }

    override fun updateInterval() {
        engine.entity {
            with<RenderComponent> {
                sprite.setRegion(dropRegion)
                z = 1
            }
            with<TransformComponent> { bounds.set(MathUtils.random(0f, 800 - 64f), 480f, 64f, 64f) }
            with<MoveComponent> { speed.set(0f, -200f) }
            with<CollisionComponent>()
        }
    }
}