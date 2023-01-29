package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.Skill
import com.gabriel.event.SkillEvent
import com.gabriel.event.TestEvent
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.log.logger

class SkillUpgradeModel(
    world: World,
    @Qualifier("gameStage") val gameStage: Stage,
) : PropertyChangeSource(), EventListener {

    var skills by propertyNotify(Skills(Skill.PLAYER_COOLDOWN, Skill.PLAYER_COOLDOWN, Skill.PLAYER_COOLDOWN))

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {

        when (event) {
            is SkillEvent -> {
                log.debug { "Skill Event on model" }
                skills = Skills(event.skill0, event.skill1, event.skill2)
            }

            is TestEvent -> {
                log.debug { "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" }
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SkillUpgradeModel>()
    }
}

