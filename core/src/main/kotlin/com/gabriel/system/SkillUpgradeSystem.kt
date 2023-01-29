package com.gabriel.system

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.Skill
import com.gabriel.component.SkillComponent
import com.gabriel.event.*
import com.gabriel.ui.model.SkillModel
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.collections.GdxArray
import ktx.log.logger
import kotlin.math.log

class SkillUpgradeSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
) : IntervalSystem(), EventListener {
    private val skillsModel = Skill.values()
    val numSkill: Int = 3


    init {
    }

    override fun onTick() {

    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityLevelEvent -> {
                log.debug { "Skill System Level Event" }
                val shuffledSkills = (0..skillsModel.size-1).shuffled().take(numSkill)
                gameStage.fire(TestEvent())
                gameStage.fire(
                    SkillEvent(
                        skillsModel[shuffledSkills[0]],
                        skillsModel[shuffledSkills[1]],
                        skillsModel[shuffledSkills[2]]
                    )
                )
                gameStage.fire(GamePauseEvent())
                
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SkillUpgradeSystem>()
    }
}

