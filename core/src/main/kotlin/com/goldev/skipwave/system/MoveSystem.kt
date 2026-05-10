package com.goldev.skipwave.system

import com.badlogic.gdx.math.Vector2
import com.goldev.skipwave.component.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.math.component1
import ktx.math.component2
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.*

/**
 * System that takes care of the movement in the game.
 *
 * @constructor Create empty Move system
 */
class MoveSystem : IteratingSystem(
    family = family { all(MoveComponent, PhysicComponent) }
) {

    /**
     *  A list of all entities with the WeaponComponent.
     */
    private val weaponEntities = world.family { all(WeaponComponent) }

    /**
     * If the entity is rooted, stop it immediately. Otherwise, apply an impulse to the entity to
     * make it move
     *
     * @param entity The entity that is being processed.
     */
    override fun onTickEntity(entity: Entity) {

        val moveCmp = entity[MoveComponent]
        val physicCmp = entity[PhysicComponent]
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

        entity.getOrNull(ImageComponent)?.let { imageCmp ->
            if (moveCmp.cos != 0f) {
                imageCmp.image.flipX = moveCmp.cos < 0
            }
        }
    }

    /**
     * It refreshes the position of the weapon entity to be in front of the player entity
     *
     * @param entity The entity that has the PhysicComponent
     * @param physicCmp PhysicComponent - The physic component of the entity
     */
    private fun refreshWeaponPosition(entity: Entity, physicCmp: PhysicComponent) {
        if (entity has PlayerComponent) {
            var pos: Vector2
            weaponEntities.forEach { weapon ->
                if (weapon[ImageComponent].image.flipX) {
                    pos = vec2(physicCmp.body.position.x - 1.5f, physicCmp.body.position.y - 0.3f)
                } else {
                    pos = vec2(physicCmp.body.position.x + 1.5f, physicCmp.body.position.y - 0.3f)
                }

                weapon[PhysicComponent].body.setTransform(pos.x, pos.y, weapon[PhysicComponent].body.angle)
            }
        }
    }


    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<MoveSystem>()
    }

}
