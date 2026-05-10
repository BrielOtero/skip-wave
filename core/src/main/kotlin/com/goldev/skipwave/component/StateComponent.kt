package com.goldev.skipwave.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.ai.AiEntity
import com.goldev.skipwave.ai.DefaultState
import com.goldev.skipwave.ai.EntityState
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
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
) : Component<StateComponent> {

    /**
     * When this component is added to an entity, the state machine's owner is set to an AiEntity
     * built around that entity.
     *
     * @param entity The entity that the component was added to.
     */
    override fun World.onAdd(entity: Entity) {
        val gameStage = inject<Stage>("gameStage")
        stateMachine.owner = AiEntity(entity, this, gameStage)
    }

    override fun type() = StateComponent

    companion object : ComponentType<StateComponent>()
}
