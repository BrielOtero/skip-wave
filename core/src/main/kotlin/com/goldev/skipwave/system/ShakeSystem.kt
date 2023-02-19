package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.*
import com.goldev.skipwave.preferences.GamePreferences
import ktx.log.logger
import kotlin.math.abs


class ShakeSystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private val gamePreferences: GamePreferences
) : IntervalSystem() {
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    override fun onTick() {
        if (gamePreferences.settings.accelerometer) {
            checkShake(Gdx.input.accelerometerX, Gdx.input.accelerometerY, Gdx.input.accelerometerZ)
        }
    }

    private fun checkShake(x: Float, y: Float, z: Float) {
        val curTime = System.currentTimeMillis()
        // only allow one update every 100ms.
        if (curTime - lastUpdate > 100) {
            val diffTime: Long = curTime - lastUpdate
            lastUpdate = curTime

            val speed: Float = abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000
            if (speed > SHAKE_THRESHOLD) {
                gameStage.fire(ShakeEvent())
            }
            lastX = x
            lastY = y
            lastZ = z
        }
    }

    companion object {
        private var SHAKE_THRESHOLD = 6000
        private val log = logger<ShakeSystem>()
    }
}