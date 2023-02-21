package com.goldev.skipwave.component

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
) {
}