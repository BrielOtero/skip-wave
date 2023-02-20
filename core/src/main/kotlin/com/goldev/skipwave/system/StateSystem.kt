package com.goldev.skipwave.system

import com.goldev.skipwave.component.StateComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

/**
 * System that takes care of the states in the game.
 *
 * @property stateCmps Entities with StateComponent in the world.
 * @constructor Create empty State system
 */
@AllOf([StateComponent::class])
class StateSystem(
    private val stateCmps: ComponentMapper<StateComponent>,
) : IteratingSystem() {

    /**
     * If the entity's next state is different from the current state, change the state machine's state
     * to the next state, then update the state machine
     *
     * @param entity The entity that the state machine is attached to.
     */
    override fun onTickEntity(entity: Entity) {
        with(stateCmps[entity]) {
            if (nextState != stateMachine.currentState) {
                stateMachine.changeState(nextState)
            }
            stateMachine.update()
        }
    }
}