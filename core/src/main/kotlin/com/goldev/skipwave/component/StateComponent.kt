package com.goldev.skipwave.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.ai.AiEntity
import com.goldev.skipwave.ai.DefaultState
import com.goldev.skipwave.ai.EntityState
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

/**
 * It's a component that holds a state machine and a next state.
 *
 * @property nextState The next state that the entity will transition to.
 * @property stateMachine This is the state machine that will be used to manage the entity's state.
 * @constructor Creates StateComponent with default values
 */
data class StateComponent(
    var nextState: EntityState = DefaultState.IDLE,
    val stateMachine: DefaultStateMachine<AiEntity, EntityState> = DefaultStateMachine()
) {
    companion object {
        /**
         * It's a listener that listens for the addition of a StateComponent to an entity, and when it
         * finds one, it sets the owner of the state machine to an AiEntity
         *
         *  @property world The world that the entity belongs to.
         *  @property  gameStage The stage that the game is being rendered on.
         *  @constructor Creates an StateComponentListener
         */
        class StateComponentListener(
            private val world: World,
            @Qualifier("gameStage") private val gameStage: Stage,
        ) : ComponentListener<StateComponent> {

            override fun onComponentAdded(entity: Entity, component: StateComponent) {
                component.stateMachine.owner = AiEntity(entity, world, gameStage)
            }

            override fun onComponentRemoved(entity: Entity, component: StateComponent) = Unit

        }
    }
}
