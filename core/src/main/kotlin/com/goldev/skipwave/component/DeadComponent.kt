package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

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
) : Component<DeadComponent> {
    override fun type() = DeadComponent

    companion object : ComponentType<DeadComponent>()
}
