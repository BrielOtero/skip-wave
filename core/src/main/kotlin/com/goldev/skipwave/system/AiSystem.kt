package com.goldev.skipwave.system

import com.goldev.skipwave.component.AiComponent
import com.goldev.skipwave.component.DeadComponent
import com.github.quillraven.fleks.*


/**
 * System that takes care of AI in the game.
 *
 * @property aiCmps Entities with move component in the world.
 * @constructor Create empty Ai system.
 */
@AllOf([AiComponent::class])
@NoneOf([DeadComponent::class])
class AiSystem(
    private val aiCmps: ComponentMapper<AiComponent>,

) : IteratingSystem() {

    /**
     * For each entity with an AI component, run the behavior tree.
     *
     * @param entity The entity that the AI component is attached to.
     */
    override fun onTickEntity(entity: Entity) {
        with(aiCmps[entity]) {
            behaviorTree.step()
        }
    }
}