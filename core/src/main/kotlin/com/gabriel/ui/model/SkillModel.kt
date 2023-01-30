package com.gabriel.ui.model

import com.gabriel.component.Skill

data class SkillModel (
    var slotIdx:Int,
    val skillEntityId:Int,
    val atlasKey:String,
    val skillName:String,
    var skillLevel:Int,
    var onLevelUP:Int,
)

data class Skills(
    val skill1: Skill,
    val skill2: Skill,
    val skill3: Skill,
)



