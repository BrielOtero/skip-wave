package com.goldev.skipwave.system

import com.goldev.skipwave.component.StateComponent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

/**
 * System that takes care of the states in the game.
 *
 * @constructor Create empty State system
 */
class StateSystem : IteratingSystem(
    family = family { all(StateComponent) }
) {

    /**
     * If the entity's next state is different from the current state, change the state machine's state
     * to the next state, then update the state machine
     *
     * @param entity The entity that the state machine is attached to.
     */
    override fun onTickEntity(entity: Entity) {
        with(entity[StateComponent]) {
            if (nextState != stateMachine.currentState) {
                stateMachine.changeState(nextState)
            }
            stateMachine.update()
        }
    }
}
