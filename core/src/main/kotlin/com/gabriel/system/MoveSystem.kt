package com.gabriel.system

import com.badlogic.gdx.math.Vector2
import com.gabriel.SkipWave
import com.gabriel.component.*
import com.gabriel.event.EntityAddEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.math.component1
import ktx.math.component2
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.*

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
            refreshWeaponPosition(entity, physicCmp)

            return
        }

        physicCmp.impulse.set(
            mass * (moveCmp.speed * moveCmp.cos - velX),
            mass * (moveCmp.speed * moveCmp.sin - velY)
        )

        refreshWeaponPosition(entity, physicCmp)

        imageCmps.getOrNull(entity)?.let { imageCmp ->
            if (moveCmp.cos != 0f) {
                imageCmp.image.flipX = moveCmp.cos < 0
            }
        }
    }

    private fun refreshWeaponPosition(entity: Entity, physicCmp: PhysicComponent) {


//        EntityAddEvent(
//            vec2((location.x / SkipWave.UNIT_SCALE) - 25, (location.y / SkipWave.UNIT_SCALE) - 5),
//            AnimationModel.SLASH_LEFT
//        )
        if (entity in playerCmps) {
            var pos: Vector2
            weaponEntities.forEach { weapon ->
                if (imageCmps[weapon].image.flipX) {
                    pos = vec2(physicCmp.body.position.x - 1.5f, physicCmp.body.position.y - 0.3f)
                } else {
                    pos = vec2(physicCmp.body.position.x + 1.5f, physicCmp.body.position.y - 0.3f)
                }

//                imageCmps[weapon].image.setPosition(pos.x, pos.y)
//                imageCmps[weapon].image.moveBy(pos.x, pos.y)
                physicCmps[weapon].body.setTransform(pos.x,pos.y, physicCmps[weapon].body.angle)
//                physicCmps[weapon].body.setLinearVelocity(physicCmps[entity].body.linearVelocity)
            }
        }
    }


    companion object {
        private val log = logger<MoveSystem>()
    }

//    }
}