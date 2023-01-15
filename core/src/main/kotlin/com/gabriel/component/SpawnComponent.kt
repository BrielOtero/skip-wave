package com.gabriel.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.utils.Scaling
import ktx.math.vec2

const val DEFAULT_SPEED = 3f
const val DEFAULT_ATTACK_DAMAGE = 5
const val DEFAULT_LIFE = 15


enum class EntityType {
    PLAYER, ENEMY, WEAPON, SPAWN, NOTHING
}

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
    val dropExperience:Float = 0f,
)

data class SpawnComponent(
    var type: String = "",
    var location: Vector2 = vec2()
) {
}