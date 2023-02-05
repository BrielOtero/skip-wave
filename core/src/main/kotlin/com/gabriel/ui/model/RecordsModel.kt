package com.gabriel.ui.model


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.event.GamePauseEvent
import com.gabriel.event.PlayerDeathEvent
import com.gabriel.event.SkillEvent
import com.gabriel.event.fire
import com.gabriel.preferences.GamePreferences
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.RecordsView
import com.gabriel.ui.view.SkillUpgradeView
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.log.logger

class RecordsModel(
    world: World,
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

//    var skills by propertyNotify()

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {

        when (event) {
            is PlayerDeathEvent -> {
                log.debug { "SHOW RECORDS" }
                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<SkillUpgradeView>().first().isVisible=false
                uiStage.actors.filterIsInstance<GameView>().first().isVisible=false
                uiStage.actors.filterIsInstance<RecordsView>().first().isVisible=true
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SkillUpgradeModel>()
    }
}

