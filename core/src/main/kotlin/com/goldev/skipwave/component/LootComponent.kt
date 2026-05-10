package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

/**
 *  This class is a component that will be attached to an entity that can be looted.
 */
class LootComponent : Component<LootComponent> {
    /**
     *  A variable that is used to store the entity that is interacting with the entity
     *  that has this component.
     */
    var interactEntity: Entity? = null

    override fun type() = LootComponent

    companion object : ComponentType<LootComponent>()
}
