package com.goldev.skipwave.android

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.goldev.skipwave.SkipWave

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(SkipWave(), AndroidApplicationConfiguration().apply {
            // Configure your application here.

        })
    }
}
