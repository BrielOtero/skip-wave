package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.WeaponComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.Qualifier

@AllOf([WeaponComponent::class])
class WeaponSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
    }
}