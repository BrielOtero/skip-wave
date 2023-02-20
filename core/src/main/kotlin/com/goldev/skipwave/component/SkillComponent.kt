package com.goldev.skipwave.component


/**
 *  This class is a component that represents an Skill.
 */
class SkillComponent {
}

/**
 *  It's an enum class that holds all the player's skills.
 *
 *  @property skillEntityId The id of skill.
 *  @property skillName The name of skill.
 *  @property skillLevel The level of skill.
 *  @property onLevelUP The value to apply on level up.
 *  @constructor Creates Skill with default values
 */
enum class Skill(
    val skillEntityId: Int = 0,
    var skillName: String = "",
    var skillLevel: Int = 0,
    var onLevelUP: Float = 0f,
) {
    PLAYER_LIFE(0, "Life", 0, 100f),
    PLAYER_REGENERATION(1, "Regeneration", 0, 2f),
    PLAYER_SPEED(2, "Speed", 0, 1f),
    PLAYER_COOLDOWN(3, "Cooldown", 0, -1f),
    PLAYER_DAMAGE(4, "Damage", 0, 10f);

    /**
     * Resets the skillLevel variable to 0
     */
    fun resetSkillLevel() {
        skillLevel = 0
    }
    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    var atlasKey: String = this.toString().lowercase()
}