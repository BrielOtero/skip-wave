package com.goldev.skipwave.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

/**
 * The EntityState class is an interface that defines the methods that must be implemented by all
 * concrete states.
 */
interface EntityState : State<AiEntity> {
    /**
     * Called when it enters in the state
     *
     * @param entity That is entering the state.
     * @return The unit
     */
    override fun enter(entity: AiEntity) = Unit

    /**
     * Is called as long as the status is set
     *
     * @param entity That the AI is attached to.
     * @return The unit
     */
    override fun update(entity: AiEntity) = Unit

    /**
     * Is called when the entity exits the state.
     *
     * @param entity The entity that the AI is attached to.
     * @return the unit
     */
    override fun exit(entity: AiEntity) = Unit

    /**
     * The function is called by the message dispatcher when a message is received
     *
     * @param entity The entity that received the message.
     * @param telegram The message that is being sent.
     * @return false
     */
    override fun onMessage(entity: AiEntity, telegram: Telegram) = false
}