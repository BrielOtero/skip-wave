package com.goldev.skipwave.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import  com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.goldev.skipwave.component.*
import com.goldev.skipwave.system.EntitySpawnSystem.Companion.AI_SENSOR

/**
 *  A property that returns the entity that the fixture belongs to.
 */
val Fixture.entity: Entity
    get() = this.body.userData as Entity

/**
 * System that takes care of the physic in the game.
 *
 * @property phWorld The physic world.
 * @constructor Create empty Physic system.
 */
class PhysicSystem(
    private val phWorld: World = inject(),
) : ContactListener, IteratingSystem(
    family = family { all(PhysicComponent, ImageComponent) },
    interval = Fixed(1 / 30f)
) {

    init {
        phWorld.setContactListener(this)
    }

    /**
     * For each update of the system, the previously applied forces are cleaned.
     */
    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "AutoClearForces must be set to false to guarantee a correct physic simulation." }
            phWorld.autoClearForces = false
        }
        super.onUpdate()
        phWorld.clearForces()
    }

    /**
     * In each system frame the physics world is updated.
     */
    override fun onTick() {
        super.onTick()
        phWorld.step(deltaTime, 6, 2)
    }

    /**
     * This function is called every frame for every entity that has a physic component
     *
     * @param entity The entity that is being processed.
     */
    override fun onTickEntity(entity: Entity) {

        val physicCmp = entity[PhysicComponent]

        physicCmp.prevPos.set(physicCmp.body.position)

        if (!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }

    }

    /**
     * Interpolate the position of the entity's image between the previous position and the current
     * position.
     *
     * @param entity The entity that is being interpolated.
     * @param alpha The alpha value of the interpolation.
     */
    override fun onAlphaEntity(entity: Entity, alpha: Float) {

        val physicCmp = entity[PhysicComponent]
        val imageCmp = entity[ImageComponent]

        val (prevX, prevY) = physicCmp.prevPos
        val (bodyX, bodyY) = physicCmp.body.position
        imageCmp.image.run {
            setPosition(
                MathUtils.lerp(prevX, bodyX, alpha) - width * 0.5f,
                MathUtils.lerp(prevY, bodyY, alpha) - height * 0.5f
            )
        }
    }


    /**
     * If a collision sensor is touching a collision fixture, add the fixture's entity to the sensor's
     * nearbyEntities list
     *
     * @param contact  The contact object that contains information about the collision.
     */
    override fun beginContact(contact: Contact) {
        with(world) {
            val entityA: Entity = contact.fixtureA.entity
            val entityB: Entity = contact.fixtureB.entity
            val isEntityATiledCollisionSensor = entityA has TiledComponent && contact.fixtureA.isSensor
            val isEntityBCollisionFixture = entityB has CollisionComponent && !contact.fixtureB.isSensor
            val isEntityACollisionFixture = entityA has CollisionComponent && !contact.fixtureA.isSensor
            val isEntityBTiledCollisionSensor = entityB has TiledComponent && contact.fixtureB.isSensor
            val isEntityAAiSensor =
                entityA has AiComponent && contact.fixtureA.isSensor && contact.fixtureA.userData == AI_SENSOR
            val isEntityBAiSensor =
                entityB has AiComponent && contact.fixtureB.isSensor && contact.fixtureB.userData == AI_SENSOR


            when {
                isEntityATiledCollisionSensor && isEntityBCollisionFixture -> {
                    entityA[TiledComponent].nearbyEntities += entityB
                }

                isEntityBTiledCollisionSensor && isEntityACollisionFixture -> {
                    entityB[TiledComponent].nearbyEntities += entityA
                }

                isEntityAAiSensor && isEntityBCollisionFixture -> {
                    entityA[AiComponent].nearbyEntitites += entityB
                }

                isEntityBAiSensor && isEntityACollisionFixture -> {
                    entityB[AiComponent].nearbyEntitites += entityA
                }
            }
        }
    }

    /**
     * If a sensor is no longer touching another entity, remove that entity from the sensor's list of
     * nearby entities
     *
     * @param contact The contact object that contains the two fixtures that are colliding.
     */
    override fun endContact(contact: Contact) {
        with(world) {
            val entityA: Entity = contact.fixtureA.entity
            val entityB: Entity = contact.fixtureB.entity
            val isEntityATiledCollisionSensor = entityA has TiledComponent && contact.fixtureA.isSensor
            val isEntityBTiledCollisionSensor = entityB has TiledComponent && contact.fixtureB.isSensor
            val isEntityAAiSensor =
                entityA has AiComponent && contact.fixtureA.isSensor && contact.fixtureA.userData == AI_SENSOR
            val isEntityBAiSensor =
                entityB has AiComponent && contact.fixtureB.isSensor && contact.fixtureB.userData == AI_SENSOR

            when {
                isEntityATiledCollisionSensor && !contact.fixtureB.isSensor -> {
                    entityA[TiledComponent].nearbyEntities -= entityB
                }

                isEntityBTiledCollisionSensor && !contact.fixtureA.isSensor -> {
                    entityB[TiledComponent].nearbyEntities -= entityA
                }

                isEntityAAiSensor && !contact.fixtureB.isSensor -> {
                    entityA[AiComponent].nearbyEntitites -= entityB
                }

                isEntityBAiSensor && !contact.fixtureA.isSensor -> {
                    entityB[AiComponent].nearbyEntitites -= entityA
                }
            }
        }
    }

    /**
     * If the body of the fixture is a static body, then return true.
     */
    private fun Fixture.isStaticBody() = this.body.type == StaticBody

    /**
     * If the fixture's body is a dynamic body, return true.
     */
    private fun Fixture.isDynamicBody() = this.body.type == DynamicBody

    /**
     * If the contact is between a static body and a dynamic body, then enable the contact. Otherwise,
     * if the contact is between two dynamic bodies, then enable the contact only if both bodies are
     * enemies
     *
     * @param contact The contact that is about to be solved.
     * @param oldManifold The contact manifold from the previous time step.
     */
    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        with(world) {
            if (contact.fixtureA.isStaticBody() && contact.fixtureB.isDynamicBody() || contact.fixtureB.isStaticBody() && contact.fixtureA.isDynamicBody()) {
                contact.isEnabled = true
            } else {
                contact.isEnabled =
                    (contact.fixtureA.entity has EnemyComponent && contact.fixtureB.entity has EnemyComponent)
            }
        }
    }

    /**
     * Is called after a collision has been resolved.
     *
     * @param contact The contact that was solved.
     * @param impulse The impulse applied to the body.
     * @return The Unit.
     */
    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<PhysicSystem>()
    }
}
