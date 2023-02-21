package com.goldev.skipwave.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import ktx.math.vec2

/**
 *  It's a constant value with the default entity speed.
 */
const val DEFAULT_SPEED = 3f

/**
 *  It's a constant value with the default entity attack damage.
 */
const val DEFAULT_ATTACK_DAMAGE = 5

/**
 *  It's a constant value with the default entity life.
 */
const val DEFAULT_LIFE = 15


/**
 * It's a class that has a bunch of constants that are used to identify the type of entity
 *
 * @constructor Create empty Entity type
 */
enum class EntityType {
    PLAYER, ENEMY, WEAPON, SPAWN, NOTHING
}

/**
 * SpawnCfg is a data class that contains a bunch of properties for the entity spawn.
 *
 * @property model The animation model to use for this entity.
 * @property entityType The type of entity that will be spawned.
 * @property bodyType The type of body to create for the entity.
 * @property isFlip If the entity should be flipped horizontally
 * @property canAttack If the entity can attack or not.
 * @property lootable If true, the entity will drop loot when killed.
 * @property aiTreePath The path to the AI tree to use for this entity.
 * @property physicScaling The scaling of the physic body.
 * @property physicOffset The offset of the physic body from the center of the entity.
 * @property speedScaling The speed of the entity.
 * @property attackScaling The attack damage scaling.
 * @property attackDelay The time between two attacks.
 * @property attackExtraRange This is the extra range that the entity will have when attacking.
 * @property lifeScaling The amount of life the entity will have.
 * @property dropExperience The amount of experience the player will get when killing this entity.
 * @constructor Creates a SpawnCfg with default values
 */
data class SpawnCfg(
    val model: AnimationModel,
    val entityType: EntityType = EntityType.NOTHING,
    val bodyType: BodyType = BodyType.DynamicBody,
    val isFlip: Boolean = false,
    val canAttack: Boolean = true,
    val lootable: Boolean = false,
    val aiTreePath: String = "",
    val physicScaling: Vector2 = vec2(1f, 1f),
    val physicOffset: Vector2 = vec2(0f, 0f),

    // Stats
    val speedScaling: Float = 1f,
    val attackScaling: Float = 1f,
    val attackDelay: Float = 0.2f,
    val attackExtraRange: Float = 0f,
    val lifeScaling: Float = 1f,
    val dropExperience: Float = 0f,
)

/**
 * SpawnComponent is a data class that contains the data of Spawn.
 *
 * @property type The type of the entity. This is used to determine what kind of entity to spawn.
 * @property  model The model to use for the entity.
 * @property location The location of the spawn point.
 * @constructor Creates SpawnComponent with default values
 */
data class SpawnComponent(
    var type: String = "",
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var location: Vector2 = vec2()
) {
}