package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 *  This class is a component that represents a weapon.
 */
class WeaponComponent : Component<WeaponComponent> {
    override fun type() = WeaponComponent

    companion object : ComponentType<WeaponComponent>()
}
