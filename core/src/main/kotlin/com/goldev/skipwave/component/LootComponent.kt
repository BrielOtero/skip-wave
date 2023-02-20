package com.goldev.skipwave.component

import com.github.quillraven.fleks.Entity

/**
 *  This class is a component that will be attached to an entity that can be looted.
 */
class LootComponent {
    /**
     *  A variable that is used to store the entity that is interacting with the entity
     *  that has this component.
     */
    var interactEntity: Entity? = null
}
