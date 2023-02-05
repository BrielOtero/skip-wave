package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.event.SetGameScreenEvent
import com.gabriel.event.SkillEvent
import com.gabriel.event.TestEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class MainMenuModel(
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

//    var skills by propertyNotify()

    init {
        uiStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {

        when (event) {

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SkillUpgradeModel>()
    }
}

