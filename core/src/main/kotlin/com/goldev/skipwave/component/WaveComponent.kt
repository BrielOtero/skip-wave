package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 * WaveComponent is a data class that holds the data of wave.
 *
 * @property wave The current wave number.
 * @constructor Creates a WaveComponent with default values.
 */
data class WaveComponent(
    var wave: Int = 0
) : Component<WaveComponent> {
    override fun type() = WaveComponent

    companion object : ComponentType<WaveComponent>()
}
