package com.goldev.skipwave.system

import com.goldev.skipwave.component.AiComponent
import com.goldev.skipwave.component.DeadComponent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family


/**
 * System that takes care of AI in the game.
 *
 * @constructor Create empty Ai system.
 */
class AiSystem : IteratingSystem(
    family = family { all(AiComponent).none(DeadComponent) }
) {

    /**
     * For each entity with an AI component, run the behavior tree.
     *
     * @param entity The entity that the AI component is attached to.
     */
    override fun onTickEntity(entity: Entity) {
        with(entity[AiComponent]) {
            behaviorTree.step()
        }
    }
}
