package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.goldev.skipwave.preferences.GamePreferences
import com.github.quillraven.fleks.IntervalSystem
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.EntityLevelEvent
import com.goldev.skipwave.event.MapChangeEvent
import ktx.log.logger

/**
 * System that takes care of the vibration in the game.
 *
 * @property gamePreferences The preferences of the game.
 * @constructor Create empty Vibrate system.
 */
class VibrateSystem(
    private val gamePreferences: GamePreferences
) : EventListener, IntervalSystem() {

    /**
     * Called every tick of VibrateSystem
     */
    override fun onTick() {
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is ButtonPressedEvent -> {
                log.debug { "VIBRATE" }
                vibrate(50)

            }

            is MapChangeEvent -> vibrate(100)
            is EntityLevelEvent -> vibrate(100)
        }
        return false
    }


    /**
     * If the user has enabled vibration, vibrate the device for the specified number of milliseconds.
     *
     * @param milliseconds The number of milliseconds to vibrate.
     */
    private fun vibrate(milliseconds: Int) {
        if (gamePreferences.settings.vibrator) {
            Gdx.input.vibrate(milliseconds)
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<VibrateSystem>()
    }
}