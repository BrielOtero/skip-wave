package com.gabriel.ui.model

import com.gabriel.component.Skill

data class SkillModel (
    val skillEntityId:Int,
    val atlasKey:String,
    val name:String,
    var slotIdx:Int,
)

data class Skills(
    val skill1: Skill,
    val skill2: Skill,
    val skill3: Skill,
)



