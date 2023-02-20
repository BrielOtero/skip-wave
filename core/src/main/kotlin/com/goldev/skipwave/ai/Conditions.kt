package com.goldev.skipwave.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task


/**
 *  This class is an abstract class that is used to create a condition that can be used in a behavior
 *  tree.
 */
abstract class Condition : LeafTask<AiEntity>() {
    /**
     * It's a getter for property AiEntity
     *
     * @return the object that is being used in the behavior tree.
     */
    val entity: AiEntity
        get() = `object`


    /**
     * The state of the condition
     *
     * @return return an boolean
     */
    abstract fun condition(): Boolean

    /**
     * If the condition is true, return SUCCEEDED, otherwise return FAILED.
     *
     * @return The status of the condition.
     */
    override fun execute(): Status {
        return when {
            condition() -> Status.SUCCEEDED
            else -> Status.FAILED
        }
    }

    /**
     * This function returns a copy of the task, with the same properties as the original task.
     *
     * @param task The task that is being copied.
     */
    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> = task
}

/**
 *  This class is a condition that returns true if the entity can attack.
 *
 *  @return true if the entity can attack
 */
class CanAttack : Condition() {
    /**
     * If the entity can attack, then return true.
     */
    override fun condition() = entity.canAttack()
}


/**
 * Check if the entity has an enemy nearby
 */
class IsEnemyNearby : Condition() {

    /**
     * Check if the entity has an enemy nearby
     *
     * @return If the entity has an enemy nearby, then return true.
     */
    override fun condition() = entity.hasEnemyNearby()
}

/**
 * Check if player can attack
 */
class CanPlayerAttack : Condition() {
    /**
     * If the entity can attack, then return true.
     *
     * @return true if can attack
     */
    override fun condition() = entity.canAttack
}