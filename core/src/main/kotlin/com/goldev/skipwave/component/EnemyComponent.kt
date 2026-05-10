package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 *  This class is a component that represents an enemy.
 */
class EnemyComponent : Component<EnemyComponent> {
    override fun type() = EnemyComponent

    companion object : ComponentType<EnemyComponent>()
}
