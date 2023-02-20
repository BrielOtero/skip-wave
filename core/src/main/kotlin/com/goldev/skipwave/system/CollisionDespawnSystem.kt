package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.TiledComponent
import com.goldev.skipwave.event.CollisionDespawnEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*

/**
 * System that takes care of the collision despawn of the game.
 *
 * @property tiledCmps Entities with TiledComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Collision despawn system
 */
@AllOf([TiledComponent::class])
class CollisionDespawnSystem(
    private val tiledCmps: ComponentMapper<TiledComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    /**
     * If the entity has no nearby entities, then remove it from the world
     *
     * @param entity The entity that is being ticked.
     */
    override fun onTickEntity(entity: Entity) {
        with(tiledCmps[entity]) {
            if (tiledCmps[entity].nearbyEntities.isEmpty()) {
                gameStage.fire(CollisionDespawnEvent(cell))
                world.remove(entity)
            }
        }
    }
}