package com.gabriel.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.gabriel.component.ImageComponent
import com.gabriel.component.PhysicComponent
import com.github.quillraven.fleks.*
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import  com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.gabriel.component.CollisionComponent
import com.gabriel.component.TiledComponent

@AllOf([PhysicComponent::class, ImageComponent::class])
class PhysicSystem(
    private val phWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val tiledCmps: ComponentMapper<TiledComponent>,
    private val collisionCmps: ComponentMapper<CollisionComponent>,
) : ContactListener, IteratingSystem(interval = Fixed(1 / 60f)) {
    init {
        phWorld.setContactListener(this)
    }

    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "AutoClearForces must be set to false to guarantee a correct physic simulation." }
            phWorld.autoClearForces = false
        }
        super.onUpdate()
        phWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        phWorld.step(deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicCmp = physicCmps[entity]

        physicCmp.prevPos.set(physicCmp.body.position)

        if (!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }


    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val physicCmp = physicCmps[entity]
        val imageCmp = imageCmps[entity]

        val (prevX, prevY) = physicCmp.prevPos
        val (bodyX, bodyY) = physicCmp.body.position
        imageCmp.image.run {
            setPosition(
                MathUtils.lerp(prevX, bodyX, alpha) - width * 0.5f,
                MathUtils.lerp(prevY, bodyY, alpha) - height * 0.5f
            )
        }
    }

    private val Fixture.entity: Entity
        get() = this.body.userData as Entity

    override fun beginContact(contact: Contact) {
        val entityA: Entity = contact.fixtureA.entity
        val entityB: Entity = contact.fixtureB.entity
        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBCollisionFixture = entityB in collisionCmps && !contact.fixtureB.isSensor
        val isEntityACollisionFixture = entityA in collisionCmps && !contact.fixtureA.isSensor
        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor

        when {
            isEntityATiledCollisionSensor && isEntityBCollisionFixture -> {
                tiledCmps[entityA].nearbyEntities += entityB
            }

            isEntityBTiledCollisionSensor && isEntityACollisionFixture -> {
                tiledCmps[entityB].nearbyEntities += entityA
            }
        }
    }

    override fun endContact(contact: Contact) {
        val entityA: Entity = contact.fixtureA.entity
        val entityB: Entity = contact.fixtureB.entity
        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor

        when {
            isEntityATiledCollisionSensor && !contact.fixtureB.isSensor-> {
                tiledCmps[entityA].nearbyEntities -= entityB
            }

            isEntityBTiledCollisionSensor && !contact.fixtureA.isSensor -> {
                tiledCmps[entityB].nearbyEntities -= entityA
            }
        }
    }

    private fun Fixture.isStaticBody() = this.body.type == StaticBody

    private fun Fixture.isDinamicBody() = this.body.type == DynamicBody

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        contact.isEnabled = (contact.fixtureA.isStaticBody() && contact.fixtureB.isDinamicBody()) ||
                (contact.fixtureB.isStaticBody() && contact.fixtureA.isDinamicBody())
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit

    companion object {
        private val log = logger<PhysicSystem>()
    }
}