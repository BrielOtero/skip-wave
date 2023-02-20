package com.goldev.skipwave.component

/**
 * A MoveComponent is a data class that contains the move data of entity.
 *
 * @property speed The speed of the entity.
 * @property cos The cosine of the angle of the direction of movement.
 * @property sin The sine of the angle of the direction of movement.
 * @property root If this is true, then the entity is the root of the tree.
 * @constructor Creates a MoveComponent with default values.
 */
data class MoveComponent(
    var speed: Float = 0f,
    var cos: Float = 0f,
    var sin: Float = 0f,
    var root: Boolean = false,
) {
}