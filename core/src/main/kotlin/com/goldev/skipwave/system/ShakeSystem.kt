package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.*
import com.goldev.skipwave.preferences.GamePreferences
import ktx.log.logger
import kotlin.math.abs


/**
 * System that takes care of the shake in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property gamePreferences The preferences of the game.
 * @constructor Create empty Shake system.
 */
class ShakeSystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private val gamePreferences: GamePreferences,

    ) : IntervalSystem() {

    /**
     *  A variable that is used to keep track of the last time the accelerometer was checked.
     */
    private var lastUpdate: Long = 0

    /**
     * Keeping track of the last x value.
     */
    private var lastX = 0f

    /**
     *  Keeping track of the last y value.
     */
    private var lastY = 0f

    /**
     *  Keeping track of the last z value.
     */
    private var lastZ = 0f

    /**
     * If the accelerometer is enabled, check the accelerometer values for a shake
     */
    override fun onTick() {
        if (gamePreferences.settings.accelerometer) {
            checkShake(Gdx.input.accelerometerX, Gdx.input.accelerometerY, Gdx.input.accelerometerZ)
        }
    }

    /**
     * Check if is ShakeEvent
     *
     * @param x Acceleration minus Gx on the x-axis
     * @param y Float, z: Float - These are the values of the accelerometer.
     * @param z Float - The acceleration force along the z axis, that is, the force applied
     * perpendicular to the screen, in m/s2.
     */
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
        /**
         *  This is the threshold for the shake.
         */
        private var SHAKE_THRESHOLD = 6000

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<ShakeSystem>()
    }
}