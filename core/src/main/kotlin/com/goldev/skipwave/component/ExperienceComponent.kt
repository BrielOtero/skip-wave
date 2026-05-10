package com.goldev.skipwave.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 * ExperienceComponent is a data class that holds the data for experience.
 *
 * @property experience The current experience of the entity.
 * @property dropExperience The amount of experience that will be dropped when the entity dies.
 * @property experienceToNextWave The amount of experience needed to wave up.
 * @constructor Creates an ExperienceComponent with defaults values
 */
data class ExperienceComponent(
    var experience: Float = 0f,
    var dropExperience: Float = 0f,
    var experienceToNextWave: Float = 0f,
) : Component<ExperienceComponent> {
    override fun type() = ExperienceComponent

    companion object : ComponentType<ExperienceComponent>()
}
