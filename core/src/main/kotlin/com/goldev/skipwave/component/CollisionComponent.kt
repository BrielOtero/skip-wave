package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 *  This class is a component that handles collisions.
 */
class CollisionComponent : Component<CollisionComponent> {
    override fun type() = CollisionComponent

    companion object : ComponentType<CollisionComponent>()
}
