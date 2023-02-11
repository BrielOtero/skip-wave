package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.event.*
import com.gabriel.preferences.GamePreferences
import com.github.quillraven.fleks.IntervalSystem
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