package com.gabriel.ui.model


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.component.LifeComponent
import com.gabriel.component.WaveComponent
import com.gabriel.event.*
import com.gabriel.preferences.GamePreferences
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.RecordsView
import com.gabriel.ui.view.SkillUpgradeView
import com.github.quillraven.fleks.ComponentMapper
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

    private val waveCmps: ComponentMapper<WaveComponent> = world.mapper()

    var isNewRecord by propertyNotify(false)
    var reachWave by propertyNotify(0)
    var recordWave by propertyNotify(gamePreferences.records.wave)

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {

        when (event) {
            is PlayerDeathEvent -> {
                log.debug { "SHOW RECORDS" }

                val tempReachWave = waveCmps[event.entity].wave
                val tempRecordWave = gamePreferences.records.wave
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
                uiStage.actors.filterIsInstance<RecordsView>().first().isVisible = true

                if (isNewRecord) {
                    gamePreferences.records.wave = reachWave
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

