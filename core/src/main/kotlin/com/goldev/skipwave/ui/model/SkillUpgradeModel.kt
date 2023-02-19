package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.component.Skill
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.view.GameView
import com.goldev.skipwave.ui.view.TouchpadView
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.event.*
import ktx.log.logger

class SkillUpgradeModel(
    world: World,
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
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
                uiStage.actors.filterIsInstance<GameView>().first().isVisible=false
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()){
                    this.model.disableTouchpad=true
                    isVisible=false
                }
            }

            is SkillApplyEvent ->{
                gameStage.fire(ButtonPressedEvent())
                gameStage.fire(GameResumeEvent())
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = true
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = false
                    isVisible = true
                }
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SkillUpgradeModel>()
    }
}

