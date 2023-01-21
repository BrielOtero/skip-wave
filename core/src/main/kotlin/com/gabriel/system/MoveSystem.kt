package com.gabriel.system

import com.badlogic.gdx.math.Vector2
import com.gabriel.component.*
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.debug
import ktx.math.component1
import ktx.math.component2
import ktx.log.logger
import ktx.math.vec2

@AllOf([MoveComponent::class, PhysicComponent::class])
class MoveSystem(
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val weaponCmps: ComponentMapper<WeaponComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
) : IteratingSystem() {
    private val weaponEntities = world.family(allOf = arrayOf(WeaponComponent::class))
    var last = vec2(0f, 0f)

    override fun onTickEntity(entity: Entity) {

        val moveCmp = moveCmps[entity]
        val physicCmp = physicCmps[entity]
        val mass = physicCmp.body.mass
        val (velX, velY) = physicCmp.body.linearVelocity


        if ((moveCmp.cos == 0f && moveCmp.sin == 0f) || moveCmp.root) {
            // no direction specified or rooted-> stop entity immediately
            physicCmp.impulse.set(
                mass * (0f - velX),
                mass * (0f - velY),
            )
            refresh(entity, physicCmp)

            return
        }

        physicCmp.impulse.set(
            mass * (moveCmp.speed * moveCmp.cos - velX),
            mass * (moveCmp.speed * moveCmp.sin - velY)
        )

        refresh(entity, physicCmp)

        imageCmps.getOrNull(entity)?.let { imageCmp ->
            if (moveCmp.cos != 0f) {
                imageCmp.image.flipX = moveCmp.cos < 0
            }
        }
    }

    private fun refresh(entity: Entity, physicCmp: PhysicComponent) {

        if (entity in playerCmps) {
            weaponEntities.forEach { weapon ->
                physicCmps[weapon].body.setLinearVelocity(physicCmps[entity].body.linearVelocity)
            }
        }
    }


    companion object {
        private val log = logger<MoveSystem>()
    }

//    }
}