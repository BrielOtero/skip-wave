package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.TiledComponent
import com.goldev.skipwave.event.CollisionDespawnEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*

@AllOf([TiledComponent::class])
class CollisionDespawnSystem(
    private val tiledCmps: ComponentMapper<TiledComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(tiledCmps[entity]) {
            if (tiledCmps[entity].nearbyEntities.isEmpty()) {
                gameStage.fire(CollisionDespawnEvent(cell))
                world.remove(entity)
            }
        }
    }
}