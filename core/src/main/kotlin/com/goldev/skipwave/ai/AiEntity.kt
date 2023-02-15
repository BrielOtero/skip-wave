package com.goldev.skipwave.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.component.AiComponent.Companion.NO_TARGET
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.component.*
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

val TMP_RECT = Rectangle()
val TMP_RECT1 = Rectangle()
val TMP_RECT2 = Rectangle()

data class AiEntity(

    val entity: Entity,
    private val world: World,
    @Qualifier("gameStage") private val gameStage: Stage,
    private val animationCmps: ComponentMapper<AnimationComponent> = world.mapper(),
    private val moveCmps: ComponentMapper<MoveComponent> = world.mapper(),
    private val attackCmps: ComponentMapper<AttackComponent> = world.mapper(),
    private val stateCmps: ComponentMapper<StateComponent> = world.mapper(),
    private val lifeCmps: ComponentMapper<LifeComponent> = world.mapper(),
    private val physicCmps: ComponentMapper<PhysicComponent> = world.mapper(),
    private val aiCmps: ComponentMapper<AiComponent> = world.mapper(),
    private val playerCmps: ComponentMapper<PlayerComponent> = world.mapper(),
) {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    private val weaponEntities = world.family(allOf = arrayOf(WeaponComponent::class))
    val position: Vector2
        get() = physicCmps[entity].body.position

    val target: Entity
        get() = aiCmps[entity].target

    val wantsToRun: Boolean
        get() {
            val moveCmp = moveCmps[entity]
            return moveCmp.cos != 0f || moveCmp.sin != 0f
        }

    val wantsToAttack: Boolean
        get() = attackCmps.getOrNull(entity)?.doAttack ?: false

    val attackCmp: AttackComponent
        get() = attackCmps[entity]

    val isAnimationDone: Boolean
        get() = animationCmps[entity].isAnimationDone

    val attackState: AttackState
        get() = attackCmps[entity].state

    val isDead: Boolean
        get() = lifeCmps[entity].isDead
    val canAttack: Boolean
        get() = attackCmps[entity].state == AttackState.READY


    fun animation(type: AnimationType, mode: PlayMode = PlayMode.LOOP, resetAnimation: Boolean = false) {
        with(animationCmps[entity]) {
            nextAnimation(type)
            playMode = mode
            if (resetAnimation) {
                stateTime = 0f
            }
        }
    }

    fun state(next: EntityState, inmediateChange: Boolean = false) {
        with(stateCmps[entity]) {
            nextState = next
            if (inmediateChange) {
                stateMachine.changeState(nextState)
            }
        }
    }

    fun enableGlobalState(enable: Boolean) {
        with(stateCmps[entity]) {
            if (enable) {
                stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
            } else {
                stateMachine.globalState = null
            }
        }
    }

    fun changeToPreviousState() {
        with(stateCmps[entity]) { nextState = stateMachine.previousState }
    }

    fun root(enable: Boolean) {
        with(moveCmps[entity]) { root = enable }
    }

    fun startAttack() {
        with(attackCmps[entity]) { startAttack() }
    }

    fun doAndStartAttack() {
        with(attackCmps[entity]) {
            doAttack = true
            startAttack()
        }
    }

    fun setPlayerForTarget(): Boolean {
        with(aiCmps[entity]) {
            target = playerEntities.first()
            return true
        }
    }

    fun moveTo(targetPos: Vector2) {
        val (targetX, targetY) = targetPos
        val physicCmp = physicCmps[entity]
        val (sourceX, sourceY) = physicCmp.body.position
        with(moveCmps[entity]) {
            val angleRad = MathUtils.atan2(targetY - sourceY, targetX - sourceX)
            cos = MathUtils.cos(angleRad)
            sin = MathUtils.sin(angleRad)
        }
    }

    fun moveToTarget() {
        val aiCmp = aiCmps[entity]
//        if (aiCmp.target == NO_TARGET) {
//            with(moveCmps[entity]) {
//                cos = 0f
//                sin = 0f
//            }
//            return
//        }
        val targetPhysicCmp = physicCmps[aiCmp.target]
        moveTo(targetPhysicCmp.body.position)
    }


    fun inTargetRange(range: Float): Boolean {
        val aiCmp = aiCmps[entity]
        if (aiCmp.target == NO_TARGET) {
            println("NO TARGET")
            return true
        }

        val physicCmp = physicCmps[entity]
        val targetPhysicCmp = physicCmps[aiCmp.target]
        val (sourceX, sourceY) = physicCmp.body.position
        val (sourceOffX, sourceOffY) = physicCmp.offset
        var (sourceSizeX, sourceSizeY) = physicCmp.size
        sourceSizeX += range
        sourceSizeY += range
        val (targetX, targetY) = targetPhysicCmp.body.position
        val (targetOffX, targetOffY) = targetPhysicCmp.offset
        val (targetSizeX, targetSizeY) = targetPhysicCmp.size

        TMP_RECT1.set(
            sourceOffX + sourceX - sourceSizeX * 0.5f,
            sourceOffY + sourceY - sourceSizeY * 0.5f,
            sourceSizeX,
            sourceSizeY
        )
        TMP_RECT2.set(
            targetOffX + targetX - targetSizeX * 0.5f,
            targetOffY + targetY - targetSizeY * 0.5f,
            targetSizeX,
            targetSizeY
        )
        return TMP_RECT1.overlaps(TMP_RECT2)
    }

    fun inRange(range: Float, targetPos: Vector2): Boolean {
        val physicCmp = physicCmps[entity]
        val (sourceX, sourceY) = physicCmp.body.position
        val (offX, offY) = physicCmp.offset
        var (sizeX, sizeY) = physicCmp.size
        sizeX += range
        sizeY += range

        TMP_RECT.set(
            sourceX + offX - sizeX * 0.5f,
            sourceY + offY - sizeY * 0.5f,
            sizeX,
            sizeY,
        )

        return TMP_RECT.contains(targetPos)

    }

    fun stopMovement() {
        with(moveCmps[entity]) {
            cos = 0f
            sin = 0f
        }
    }

    fun canAttack(): Boolean {
        val attackCmp = attackCmps[entity]
        if (!attackCmp.isReady) {
            return false
        }

        val enemy = nearbyEnemies().firstOrNull() ?: return false
        val enemyPhysicCmp = physicCmps[enemy]
        val (sourceX, sourceY) = enemyPhysicCmp.body.position
        val (offX, offY) = enemyPhysicCmp.offset
        return inRange(1.5f + attackCmp.extraRange, vec2(sourceX + offX, sourceY + offY))

    }

    private fun nearbyEnemies(): List<Entity> {
        val aiCmp = aiCmps[entity]
        return aiCmp.nearbyEntitites
            .filter { it in playerCmps && !lifeCmps[it].isDead }
    }

    fun hasEnemyNearby() = nearbyEnemies().isNotEmpty()
    fun fireEvent(entityAggroEvent: Event) {
        gameStage.fire(entityAggroEvent)
    }


}
