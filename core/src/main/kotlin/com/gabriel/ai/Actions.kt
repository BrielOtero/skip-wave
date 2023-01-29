package com.gabriel.ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.ai.utils.random.FloatDistribution
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.gabriel.component.AnimationType
import com.gabriel.component.AttackState
import com.gabriel.event.EntityAggroEvent
import ktx.math.vec2

abstract class Action : LeafTask<AiEntity>() {
    val entity: AiEntity
        get() = `object`

    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> = task
}

class AttackTask : Action() {

    override fun execute(): Status {
//        println("Attack")
        if (status != Status.RUNNING) {
            entity.doAndStartAttack()
            entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
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

class AttackWeaponTask : Action() {
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.doAndStartAttack()
            println("doAndStartAttack")
            return Status.RUNNING
        }




        when (entity.attackState) {
            AttackState.READY -> {
                entity.animation(AnimationType.IDLE)
//                println("READY")
                return Status.SUCCEEDED
            }

            AttackState.ATTACKING -> {
//                println("ATTACKING")
                entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, false)
                return Status.RUNNING
            }

            AttackState.DEAL_DAMAGE -> {
//                println("DAMAGE")
                return Status.RUNNING
            }

            else -> return Status.SUCCEEDED
        }
    }
}

class MoveTask(
    @JvmField
    @TaskAttribute(required = true)
    var range: Float = 0f
) : Action() {
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

    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> {
        (task as MoveTask).range = range
        return task
    }
}


class IdleTask(
    @JvmField
    @TaskAttribute(required = true)
    var duration: FloatDistribution? = null

) : Action() {
    private var currentDuration = 0f

    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationType.IDLE)
            currentDuration = duration?.nextFloat() ?: 1f
            return Status.RUNNING
        }

        currentDuration -= GdxAI.getTimepiece().deltaTime
        if (currentDuration <= 0f) {
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }

    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> {
        (task as IdleTask).duration = duration
        return task
    }

}

class WanderTask : Action() {

    private val startPos = vec2()
    private val targetPos = vec2()

    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationType.RUN)

            if (startPos.isZero) {
                startPos.set(entity.position)
            }

            targetPos.set(startPos)
            targetPos.x += MathUtils.random(-3f, 3f)
            targetPos.y += MathUtils.random(-3f, 3f)
            entity.moveTo(targetPos)
            return Status.RUNNING
        }

        if (entity.inRange(0.5f, targetPos)) {
            entity.stopMovement()
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}

