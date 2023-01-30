package com.gabriel.component

import com.gabriel.ui.model.SkillModel


class SkillComponent {
}

enum class Skill(
    val skillEntityId: Int,
    var skillName: String = "",
    var skillLevel: Int = 0,
    var onLevelUP: Int,
) {
    PLAYER_LIFE(0, "Life", 0, 100),
    PLAYER_REGENERATION(1, "Regeneration", 0, 1),
    PLAYER_SPEED(2, "Speed", 0, 1),
    PLAYER_COOLDOWN(3, "Cooldown", 0, -1),
    PLAYER_DAMAGE(4, "Damage", 0, 1);

    var atlasKey: String = this.toString().lowercase()
}