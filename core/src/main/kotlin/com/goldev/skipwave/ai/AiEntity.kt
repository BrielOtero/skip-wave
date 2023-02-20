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

/**
 *  It's a temporary rectangle that is used to check if the player is in range of the enemy.
 */
val TMP_RECT = Rectangle()

/**
 *  It's a temporary rectangle that is used to check if the player is in range of the enemy.
 */
val TMP_RECT1 = Rectangle()

/**
 *  It's a temporary rectangle that is used to check if the player is in range of the enemy.
 */
val TMP_RECT2 = Rectangle()


/**
 * It's a data class that holds a reference to an entity and a world, and it has a bunch of component
 * mappers.
 *
 * @property entity The entity that this class is controlling.
 * @property world The world that the entity belongs to.
 * @property gameStage The stage that the game is being rendered on.
 * @property animationCmps Store entities with animation component in the world.
 * @property moveCmps Store entities with move component in the world.
 * @property attackCmps Store entities with attack component in the world.
 * @property stateCmps Store entities with state component in the world.
 * @property lifeCmps Store entities with life component in the world.
 * @property physicCmps Store entities with physic component in the world.
 * @property aiCmps Store entities with ai component in the world.
 * @property playerCmps Store entities with player component in the world.
 * @constructor Create an empty AiEntity.
 */
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
    /**
     *  It's a family that contains all the entities that have a player component.
     */
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    /**
     * It's a getter for the position of entity.
     *
     * @return The position of the entity.
     */
    val position: Vector2
        get() = physicCmps[entity].body.position

    /**
     *  It's a getter for the target of entity
     *
     *  @return The target of the entity.
     */
    val target: Entity
        get() = aiCmps[entity].target

    /**
     *  It's a getter that check if entity wants run
     *
     *  @return True if the entity wants to run.
     */
    val wantsToRun: Boolean
        get() {
            val moveCmp = moveCmps[entity]
            return moveCmp.cos != 0f || moveCmp.sin != 0f
        }

    /**
     *  It's a getter that check if entity wants attack
     *
     *  @return True if entity wants attack
     */
    val wantsToAttack: Boolean
        get() = attackCmps.getOrNull(entity)?.doAttack ?: false

    /**
     *  It's a getter for attack component
     *
     *  @return The attack component of the entity.
     */
    val attackCmp: AttackComponent
        get() = attackCmps[entity]

    /**
     *  It's a getter that check if the animation is done
     *
     *  @return True if the animation is done.
     */
    val isAnimationDone: Boolean
        get() = animationCmps[entity].isAnimationDone

    /**
     *  It's a getter for attack state
     *
     *  @return The attack state of the entity.
     */
    val attackState: AttackState
        get() = attackCmps[entity].state

    /**
     *  It's a getter that check entity is dead
     *
     *  @return True if the entity is dead.
     */
    val isDead: Boolean
        get() = lifeCmps[entity].isDead

    /**
     *  It's a getter that check entity can attack
     *  @return True if the entity can attack.
     */
    val canAttack: Boolean
        get() = attackCmps[entity].state == AttackState.READY


    /**
     * Change the animation of the entity.
     *
     * @param type The type of animation to play.
     * @param mode The mode in which the animation will be reproduced. LOOP by default.
     * @param resetAnimation If true, the animation will be reset to the beginning.
     */
    fun animation(
        type: AnimationType,
        mode: PlayMode = PlayMode.LOOP,
        resetAnimation: Boolean = false
    ) {
        with(animationCmps[entity]) {
            nextAnimation(type)
            playMode = mode
            if (resetAnimation) {
                stateTime = 0f
            }
        }
    }

    /**
     * It changes the state of the entity to the next state
     *
     * @param next The next state to change to.
     * @param inmediateChange If true, the state will be changed inmediately. If false, the state will
     * be changed at the end of the current frame.
     */
    fun state(next: EntityState, inmediateChange: Boolean = false) {
        with(stateCmps[entity]) {
            nextState = next
            if (inmediateChange) {
                stateMachine.changeState(nextState)
            }
        }
    }

    /**
     * If the entity has a state machine, set the global state to the default global state if enable is
     * true, otherwise set the global state to null
     *
     * @param enable Whether to enable the global state.
     */
    fun enableGlobalState(enable: Boolean) {
        with(stateCmps[entity]) {
            if (enable) {
                stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
            } else {
                stateMachine.globalState = null
            }
        }
    }

    /**
     * If the entity has a state component, set the next state to the previous state.
     */
    fun changeToPreviousState() {
        with(stateCmps[entity]) { nextState = stateMachine.previousState }
    }

    /**
     * If the entity has a move component, set its root property to the value of the enable
     * parameter.
     *
     * @param enable Whether or not to enable or disable the root.
     */
    fun root(enable: Boolean) {
        with(moveCmps[entity]) { root = enable }
    }

    /**
     * If the entity has an attack component, start the attack.
     */
    fun startAttack() {
        with(attackCmps[entity]) { startAttack() }
    }

    /**
     * If the entity has an attack component, set the doAttack flag to true and start the attack.
     */
    fun doAndStartAttack() {
        with(attackCmps[entity]) {
            doAttack = true
            startAttack()
        }
    }

    /**
     * If the entity has an AI component, set the target to the first player entity.
     *
     * @return A boolean value.
     */
    fun setPlayerForTarget(): Boolean {
        with(aiCmps[entity]) {
            target = playerEntities.first()
            return true
        }
    }

    /**
     * Given a target position, calculate the angle between the current position and the target
     * position, and set the cos and sin values of the move component to the cosine and sine of that
     * angle.
     *
     * @param targetPos The position to move to.
     */
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

    /**
     * Move to the target's position.
     */
    fun moveToTarget() {
        val aiCmp = aiCmps[entity]
        val targetPhysicCmp = physicCmps[aiCmp.target]
        moveTo(targetPhysicCmp.body.position)
    }


    /**
     * "If the entity has a target, return true if the entity is within range of the target."
     *
     * The function starts by checking if the entity has a target. If it doesn't, it returns true
     *
     * @param range The range of the target.
     * @return A boolean value.
     */
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

    /**
     * If the target is within the range of the source, return true.
     *
     * @param range The range of the attack.
     * @param targetPos The position of the target entity.
     * @return True if in range.
     */
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

    /**
     * If the entity has a movement component, set its cosine and sine to zero.
     */
    fun stopMovement() {
        with(moveCmps[entity]) {
            cos = 0f
            sin = 0f
        }
    }

    /**
     * If the attack component is ready, and there is an enemy within range, return true.
     *
     * @return True if can attack.
     */
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

    /**
     * Return a list of nearby entities that are players and not dead.
     *
     * @return A list of entities that are nearby, are players, and are not dead.
     */
    private fun nearbyEnemies(): List<Entity> {
        val aiCmp = aiCmps[entity]
        return aiCmp.nearbyEntitites
            .filter { it in playerCmps && !lifeCmps[it].isDead }
    }

    /**
     * Check if there are any nearby enemies
     * @return True if there are any nearby enemies
     */
    fun hasEnemyNearby() = nearbyEnemies().isNotEmpty()

    /**
     * This function takes an Event as a parameter, and then fires that event on the gameStage.
     *
     * @param entityAggroEvent The event that will be fired.
     */
    fun fireEvent(entityAggroEvent: Event) {
        gameStage.fire(entityAggroEvent)
    }


}
