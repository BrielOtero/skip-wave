package com.goldev.skipwave.ui.model


import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.*
import com.goldev.skipwave.preferences.GamePreferences
import com.goldev.skipwave.ui.view.GameView
import com.goldev.skipwave.ui.view.RecordsView
import com.goldev.skipwave.ui.view.SkillUpgradeView
import com.goldev.skipwave.ui.view.TouchpadView
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.event.GamePauseEvent
import com.goldev.skipwave.event.PlayerDeathEvent
import com.goldev.skipwave.event.SavePreferencesEvent
import com.goldev.skipwave.event.fire
import ktx.log.logger

class RecordsModel(
    world: World,
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    private val waveCmps: ComponentMapper<WaveComponent> = world.mapper()

    var isNewRecord by propertyNotify(false)
    var reachWave by propertyNotify(0)
    var recordWave by propertyNotify(gamePreferences.game.wave)

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {

        when (event) {
            is PlayerDeathEvent -> {
                log.debug { "SHOW RECORDS" }

                val tempReachWave = waveCmps[event.entity].wave
                val tempRecordWave = gamePreferences.game.wave
                isNewRecord = tempReachWave > tempRecordWave
                if (isNewRecord) {
                    recordWave = tempReachWave
                    reachWave = tempReachWave
                } else {
                    recordWave = tempRecordWave
                    reachWave = tempReachWave
                }

                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<SkillUpgradeView>().first().isVisible = false
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = false
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()){
                    isVisible=false
                    this.model.disableTouchpad=true
                }

                uiStage.actors.filterIsInstance<RecordsView>().first().isVisible = true


                if (isNewRecord) {
                    gamePreferences.game.wave = reachWave
                    gameStage.fire(SavePreferencesEvent())
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

