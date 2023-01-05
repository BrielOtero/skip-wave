package com.gabriel.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.ai.AiEntity
import com.gabriel.ai.DefaultState
import com.gabriel.ai.EntityState
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

data class StateComponent(
    var nextState: EntityState = DefaultState.IDLE,
    val stateMachine: DefaultStateMachine<AiEntity, EntityState> = DefaultStateMachine()
) {
    companion object {
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
