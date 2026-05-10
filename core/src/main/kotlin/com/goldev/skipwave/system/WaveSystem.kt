package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.goldev.skipwave.event.EntityLevelEvent
import com.goldev.skipwave.event.fire
import ktx.log.logger
import kotlin.math.pow

/**
 * System that takes care of the waves in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Wave system.
 */
class WaveSystem(
    private val gameStage: Stage = inject("gameStage"),
) : IteratingSystem(
    family = family { all(WaveComponent) }
) {

    /**
     * If the entity has enough experience to level up, then increase the wave number and set the
     * experience needed to level up to the current experience plus the wave number squared.
     *
     * @param entity Entity - The entity that is being ticked
     */
    override fun onTickEntity(entity: Entity) {
        with(entity[ExperienceComponent]) {

            if (experience >= experienceToNextWave) {
                with(entity[WaveComponent]) {
//                    log.debug { "Experience NEXT WAVE before ${experienceToNextLevel}" }
                    wave += 1
                    experienceToNextWave = experience + (wave + 2 / 0.2f).pow(2.04f)

//                    log.debug { "Experience NEXT WAVE after ${experienceToNextLevel}" }
                    gameStage.fire(EntityLevelEvent(entity))
                }
            }
        }
    }


    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<WaveSystem>()
    }


}
