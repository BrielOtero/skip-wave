package com.goldev.skipwave.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.graphics.g2d.Animation
import com.goldev.skipwave.component.AnimationType
import com.goldev.skipwave.component.AttackState
import com.goldev.skipwave.event.EntityAggroEvent

/**
 *  Action is a LeafTask that is used to represent an action that an AiEntity can perform
 */
abstract class Action : LeafTask<AiEntity>() {

    /**
     *  It's a getter for the AiEntity property.
     *
     *  @return AiEntity
     */
    val entity: AiEntity
        get() = `object`

    /**
     * This function returns a copy of the task, with the same properties as the original task.
     *
     * @param task The task that is being copied.
     */
    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> = task
}



/**
 * Perform the task of attacking a weapon entity
 */
class AttackWeaponTask : Action() {
    /**
     * If the weapon entity is not attacking, start attacking. If the entity is attacking, stop attacking. If
     * the entity is done attacking, return to idle
     *
     * @return The status of the action.
     */
    override fun execute(): Status {

        if (status != Status.RUNNING) {
            entity.doAndStartAttack()
            entity.animation(AnimationType.ATTACK)
            return Status.RUNNING
        }

        if (entity.attackState == AttackState.ATTACKING) {
            entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
            return Status.RUNNING;
        }

        if (entity.isAnimationDone) {
//            entity.animation(AnimationType.IDLE)
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}


/**
 *  Perform the task of attacking an entity
 */
class AttackTask : Action() {

    /**
     * If the entity is not running, then set the animation to attack, start the attack, and fire an
     * event. If the entity is running, then set the animation to run, stop the movement, and return
     * succeeded
     *
     * @return The status of the action.
     */
    override fun execute(): Status {
//        println("Attack")
        if (status != Status.RUNNING) {
            entity.animation(AnimationType.ATTACK)
            entity.doAndStartAttack()
            entity.fireEvent(EntityAggroEvent(entity.entity))
            return Status.RUNNING
        }

        if (entity.isAnimationDone) {
            entity.animation(AnimationType.RUN)
            entity.stopMovement()
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}

/**
 * This task makes the entity move to the target.
 *
 * @property range field is the distance from the target that the entity will stop moving
 */
class MoveTask(
    @JvmField
    @TaskAttribute(required = true)
    var range: Float = 0f
) : Action() {

    /**
     * If the entity is not running, set the animation to run and return running. If the entity is
     * running, set the player as the target and move to the target. If the entity is in range of the
     * target, return succeeded
     *
     * @return The status of the action.
     */
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationType.RUN)
            return Status.RUNNING
        }
        entity.setPlayerForTarget()
        entity.moveToTarget()
        if (entity.inTargetRange(range)) {
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }

    /**
     *  The task to copy to
     *
     * @param task The task to copy to.
     * @return A copy of the task.
     */
    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> {
        (task as MoveTask).range = range
        return task
    }
}

