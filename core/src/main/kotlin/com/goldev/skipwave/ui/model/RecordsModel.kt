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

/**
 * The model of the Records
 *
 * @param world The entities world.
 * @property bundle The bundle with text to show in the UI.
 * @property gamePreferences The preferences of the game.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor
 *
 */
class RecordsModel(
    world: World,
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    /**
     *  Component mapper with the entities with WaveComponent
     */
    private val waveCmps: ComponentMapper<WaveComponent> = world.mapper()

    /**
     *  Notifiable property with new record.
     */
    var isNewRecord by propertyNotify(false)

    /**
     *  Notifiable property with the reach wave.
     */
    var reachWave by propertyNotify(0)

    /**
     *  Notifiable property with the record wave.
     */
    var recordWave by propertyNotify(gamePreferences.game.wave)

    init {
        gameStage.addListener(this)
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
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
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    isVisible = false
                    this.model.disableTouchpad = true
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
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeModel>()
    }
}

