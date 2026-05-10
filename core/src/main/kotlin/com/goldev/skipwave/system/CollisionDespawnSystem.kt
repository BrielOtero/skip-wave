package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.TiledComponent
import com.goldev.skipwave.event.CollisionDespawnEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

/**
 * System that takes care of the collision despawn of the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Collision despawn system
 */
class CollisionDespawnSystem(
    private val gameStage: Stage = inject("gameStage"),
) : IteratingSystem(
    family = family { all(TiledComponent) }
) {

    /**
     * If the entity has no nearby entities, then remove it from the world
     *
     * @param entity The entity that is being ticked.
     */
    override fun onTickEntity(entity: Entity) {
        with(entity[TiledComponent]) {
            if (nearbyEntities.isEmpty()) {
                gameStage.fire(CollisionDespawnEvent(cell))
                world -= entity
            }
        }
    }
}
