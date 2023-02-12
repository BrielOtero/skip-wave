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

class VibrateSystem(

    private val gamePreferences: GamePreferences
) : EventListener, IntervalSystem() {

    override fun onTick() {

    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is ButtonPressedEvent -> {
                log.debug { "VIBRATE" }
                vibrate(100)

            }

            is MapChangeEvent -> vibrate(200)
            is EntityLevelEvent -> vibrate(100)
        }
        return false
    }


    private fun vibrate(milliseconds: Int) {
        if (gamePreferences.settings.vibrator) {
            Gdx.input.vibrate(milliseconds)
        }
    }

    companion object {
        private val log = logger<VibrateSystem>()
    }
}