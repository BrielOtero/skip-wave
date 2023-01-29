package com.gabriel.component

import com.gabriel.ui.model.SkillModel


data class SkillComponent(
    val skillEntityId:Int,
    var name: String = "",
    var level: Int = 0,
    var onLevelUP:Int,
) {

}

enum class Skill(
    val skill: SkillComponent,
) {
    PLAYER_LIFE(SkillComponent(0,"Life", 0,100)),
    PLAYER_REGENERATION(SkillComponent(1,"Regeneration", 0,1)),
    PLAYER_SPEED(SkillComponent(2,"Speed", 0,1)),
    PLAYER_COOLDOWN(SkillComponent(3,"Cooldown", 0,-1)),
    PLAYER_DAMAGE(SkillComponent(4,"Damage", 0,1));

    var  atlasKey: String = this.toString().lowercase()
}