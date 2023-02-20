package com.goldev.skipwave.ui.model

import com.goldev.skipwave.component.Skill

/**
 *  A SkillModel is a data class that has the information about an skill.
 *
 * @property slotIdx The index of the slot in the skill bar.
 * @property skillEntityId This is the id of the skill entity.
 * @property atlasKey The key of the skill icon in the atlas.
 * @property skillName The name of the skill.
 * @property skillLevel The current level of the skill.
 * @property onLevelUP This is the amount of experience that is required to level up the skill.
 */
data class SkillModel(
    var slotIdx: Int,
    val skillEntityId: Int,
    val atlasKey: String,
    val skillName: String,
    var skillLevel: Int,
    var onLevelUP: Float,
)

/**
 * Skills is a data class that contains three Skills to show in the SkillUpgradeView.
 *
 * @property skill1 The first Skill to show
 * @property  skill2 The second Skill to show
 * @property skill3 The third Skill to show
 */
data class Skills(
    val skill1: Skill,
    val skill2: Skill,
    val skill3: Skill,
)



