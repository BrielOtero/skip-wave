package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.github.quillraven.fleks.*
import com.goldev.skipwave.component.PhysicComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.component.WeaponComponent

@AllOf([WeaponComponent::class])
class WeaponSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val physicCmps: ComponentMapper<PhysicComponent>

) : IteratingSystem() {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    override fun onTickEntity(entity: Entity) {

    }
}