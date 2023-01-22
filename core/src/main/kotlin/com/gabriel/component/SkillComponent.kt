package com.gabriel.component

import com.gabriel.ui.model.SkillModel


data class SkillComponent(
    var name: String = "",
    var level: Int = 0,
) {

    var skillName: String = this.toString().lowercase()
}

enum class Skill(
    val skill: SkillComponent,
) {
    PLAYER_LIFE(SkillComponent("player_life", 0)),
    PLAYER_REGENERATION(SkillComponent("player_life", 0)),
    PLAYER_SPEED(SkillComponent("player_life", 0)),
    PLAYER_COOLDOWN(SkillComponent("player_life", 0)),
    PLAYER_DAMAGE(SkillComponent("player_life", 0));

    var skillName: String = this.toString().lowercase()
}