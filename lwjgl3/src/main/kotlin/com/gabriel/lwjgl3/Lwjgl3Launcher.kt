@file:JvmName("Lwjgl3Launcher")

package com.gabriel.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.gabriel.SkipWave

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(SkipWave(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Survivor")
//        setWindowedMode(640, 480)
        setWindowedMode(415,900)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
