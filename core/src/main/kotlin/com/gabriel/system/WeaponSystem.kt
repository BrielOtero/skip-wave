package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.*
import com.github.quillraven.fleks.*
import ktx.math.vec2

@AllOf([WeaponComponent::class])
class WeaponSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val physicCmps: ComponentMapper<PhysicComponent>

) : IteratingSystem() {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    override fun onTickEntity(entity: Entity) {

    }
}