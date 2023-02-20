package com.goldev.skipwave.component

/**
 * LifeComponent is a data class that holds the life data for entity
 *
 * @property life The current life of the entity.
 * @property max The maximum amount of life the entity can have.
 * @property regeneration The amount of life that is regenerated every second.
 * @property takeDamage This is the amount of damage that the entity will take.
 * @constructor Creates a LifeComponent with default values
 */
data class LifeComponent(
    var life: Float = 30f,
    var max: Float = 30f,
    var regeneration: Float = 1f,
    var takeDamage: Float = 0f,
) {
    /**
     * It's a getter for isDead property.
     * @return true if entity is dead.
     */
    val isDead: Boolean
        get() = life <= 0f
}