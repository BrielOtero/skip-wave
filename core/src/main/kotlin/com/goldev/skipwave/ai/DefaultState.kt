package com.goldev.skipwave.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.goldev.skipwave.component.AnimationType

/**
 *  It's a state machine that handles the different states of the AI entity
 */
enum class DefaultState : EntityState {
    /**
     * Represents that AiEntity is on idle state
     */
    IDLE {
        /**
         * When the entity enters the state, set the animation to idle.
         *
         * @param entity The entity that is being controlled by the AI.
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.IDLE)
        }

        /**
         * If the entity wants to attack, set the state to ATTACK, otherwise, if the entity wants to
         * run, set the state to RUN
         *
         * @param entity The entity that is being updated.
         */
        override fun update(entity: AiEntity) {
            when {
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.wantsToRun -> entity.state(RUN)
            }
        }
    },

    /**
     * Represents that AiEntity is on run state
     */
    RUN {
        /**
         * When the entity enters the state, set the animation to run.
         *
         * @param entity The entity that is running the state machine.
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.RUN)
        }

        /**
         * If the entity wants to attack, set the state to ATTACK, otherwise if the entity doesn't want
         * to run, set the state to IDLE
         *
         * @param entity The entity that is running this state.
         */
        override fun update(entity: AiEntity) {
            when {
                entity.wantsToAttack -> entity.state(ATTACK)
                !entity.wantsToRun -> entity.state(IDLE)
            }
        }
    },

    /**
     * Represents that AiEntity is on attack state
     */
    ATTACK {
        /**
         * It sets the entity's animation to the attack animation and sets the animation to play
         *
         * @param entity The entity that is being attacked.
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL)
            entity.root(false)
            entity.startAttack()
        }

        /**
         * If the entity is a root entity, then set its root flag to false.
         *
         * @param entity The entity that is running the behavior tree.
         */
        override fun exit(entity: AiEntity) {
            entity.root(false)
        }

        /**
         * If the attack is ready and the entity is not attacking, then change to the previous state.
         * If the attack is ready and the entity is attacking, then start another attack
         *
         * @param entity that is currently in this state
         */
        override fun update(entity: AiEntity) {
            val attackCmp = entity.attackCmp

            if (attackCmp.isReady && !attackCmp.doAttack) {
                entity.changeToPreviousState()
            } else if (attackCmp.isReady) {
                // start another attack
                entity.animation(AnimationType.ATTACK, Animation.PlayMode.LOOP, true)
                entity.startAttack()
            }
        }
    },

    /**
     * Represents that AiEntity is on dead state
     */
    DEAD {
        /**
         * This function is called when the entity enters the state. It tells the entity to root itself
         * to the ground
         *
         * @param entity The entity that is being controlled by the AI.
         */
        override fun enter(entity: AiEntity) {
            entity.root(true)
        }

        /**
         * If the entity is dead, play the death animation. If the entity is not dead, resurrect it
         *
         * @param entity the entity that is being updated.
         */
        override fun update(entity: AiEntity) {

            if (!entity.isDead) {
                entity.state(RESURRECT)
            } else {
                entity.animation(AnimationType.DEATH, Animation.PlayMode.NORMAL, false)
            }
        }
    },

    /**
     * Represents that AiEntity is on resurrect state
     */
    RESURRECT {
        /**
         * This function is called on the entity that is currently in the state. It tells the entity to
         * play the animation of type `DEATH` in reverse
         *
         * @param entity that is being controlled by the AI.
         */
        override fun enter(entity: AiEntity) {
            entity.enableGlobalState(true)
            entity.animation(AnimationType.DEATH, Animation.PlayMode.REVERSED, true)
        }

        /**
         * If the animation is done, set the state to idle and stop the root motion
         *
         * @param entity that is currently in this state.
         */
        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone) {
                entity.state(IDLE)
                entity.root(false)
            }
        }
    },
}

enum class DefaultGlobalState : EntityState {
    /**
     * Represents that AiEntity is on check alive state
     */
    CHECK_ALIVE {
        /**
         * If the entity is dead, disable the global state and set the entity's state to dead
         *
         * @param entity that the state is being applied to.
         */
        override fun update(entity: AiEntity) {
            if (entity.isDead) {
                entity.enableGlobalState(false)
                entity.state(DefaultState.DEAD, true)
            }
        }
    }
}