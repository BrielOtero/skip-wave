package com.goldev.skipwave.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EntityAttackEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.system.EntitySpawnSystem.Companion.HIT_BOX_SENSOR
import com.github.quillraven.fleks.*
import com.goldev.skipwave.component.*
import ktx.box2d.query
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2


/**
 * System that takes care of the attacks in the game.
 *
 * @property attackCmps Entities with AttackComponent in the world.
 * @property physicCmps Entities with PhysicComponent in the world.
 * @property imgCmps Entities with ImageComponent in the world.
 * @property lifeCmps Entities with LifeComponent in the world.
 * @property playerCmps Entities with PlayerComponent in the world.
 * @property weaponCmps Entities with WeaponComponent in the world.
 * @property lootCmps Entities with LootComponent in the world.
 * @property animationCmps Entities with AnimationComponent in the world.
 * @property phWorld The physics world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Attack system
 */
@AllOf([AttackComponent::class, PhysicComponent::class, ImageComponent::class])
class AttackSystem(
    private val attackCmps: ComponentMapper<AttackComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val imgCmps: ComponentMapper<ImageComponent>,
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
    private val weaponCmps: ComponentMapper<WeaponComponent>,
    private val lootCmps: ComponentMapper<LootComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val phWorld: World,
    @Qualifier("gameStage") private val gameStage: Stage,

) : IteratingSystem() {

    /**
     * If the entity is ready to attack, start the attack, otherwise if the attack is in progress, deal
     * damage to nearby enemies
     *
     * @param entity The entity that is being processed.
     */
    override fun onTickEntity(entity: Entity) {
        val attackCmp = attackCmps[entity]

        if (attackCmp.isReady && !attackCmp.doAttack) {
            // entity does not want to attack and is not executing an attack -> do nothing
            return
        }

        if (attackCmp.isPrepared && attackCmp.doAttack) {
            // attack intention and is ready to attack -> start attack
            attackCmp.doAttack = false
            attackCmp.state = AttackState.ATTACKING
            attackCmp.cooldown = attackCmp.maxCooldown
//            gameStage.fire(EntityAttackEvent(animationCmps[entity].model))
            if (attackCmp.cooldown > 0f) {
                return
            }
        }

        attackCmp.cooldown -= deltaTime
        if (attackCmp.cooldown <= 0f && attackCmp.isAttacking) {
            // deal damage to nearby enemies
            gameStage.fire(EntityAttackEvent(animationCmps[entity].model))
            attackCmp.state = AttackState.DEAL_DAMAGE

            val image = imgCmps[entity].image
            val physicCmp = physicCmps[entity]
            val attackLeft = image.flipX
            val (x, y) = physicCmp.body.position
            val (offX, offY) = physicCmp.offset
            val (w, h) = physicCmp.size
            val halfW = w * 0.5f
            val halfH = h * 0.5f

            if (attackLeft) {
                AABB_RECT.set(
                    x + offX - halfW - attackCmp.extraRange,
                    y + offY - halfH,
                    x + offX + halfW,
                    y + offY + halfH,
                )
            } else {
                AABB_RECT.set(
                    x + offX - halfW,
                    y + offY - halfH,
                    x + offX + halfW + attackCmp.extraRange,
                    y + offY + halfH,
                )
            }

            phWorld.query(AABB_RECT.x, AABB_RECT.y, AABB_RECT.width, AABB_RECT.height) { fixture ->
                if (fixture.userData != HIT_BOX_SENSOR) {
                    return@query true
                }

                val fixtureEntity = fixture.entity
                if (fixtureEntity == entity) {
                    // we don't want to attack ourselves!
                    return@query true
                }

                // turn off friendly fire
                val isAttackerPlayer = entity in playerCmps
                val isAttackerWeapon = entity in weaponCmps
                if (isAttackerPlayer && fixtureEntity in playerCmps) {
                    return@query true
                } else if (!isAttackerPlayer && !isAttackerWeapon && fixtureEntity !in playerCmps) {
                    return@query true
                } else if (isAttackerWeapon && fixtureEntity in playerCmps) {
                    return@query true
                }

                configureEntity(fixtureEntity) {
                    lifeCmps.getOrNull(it)?.let { lifeCmp ->
                        lifeCmp.takeDamage += attackCmp.damage * MathUtils.random(0.9f, 1.1f)
                    }
                    if (isAttackerPlayer) {
                        lootCmps.getOrNull(it)?.let { lootCmp ->
                            lootCmp.interactEntity = entity
                        }
                    }
                }

                return@query true
            }
        }

        val isDone = animationCmps.getOrNull(entity)?.isAnimationDone ?: true
        if (isDone) {
            attackCmp.state = AttackState.READY
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<AttackSystem>()

        /**
         *  It's a rectangle that is used to check for collisions.
         */
        val AABB_RECT = Rectangle()
    }
}