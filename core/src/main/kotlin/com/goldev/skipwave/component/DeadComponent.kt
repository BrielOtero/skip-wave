package com.goldev.skipwave.component

/**
 * This component is used to mark an entity as dead, and to specify how long it should wait before
 * reviving.
 *
 * @property reviveTime The revive time for entity.
 * @property waitForAnimation If entity is waiting for animation.
 * @constructor Created DeadComponent with default values
 */
class DeadComponent(
    var reviveTime: Float = 0f,
    var waitForAnimation: Boolean = false,
) {
}