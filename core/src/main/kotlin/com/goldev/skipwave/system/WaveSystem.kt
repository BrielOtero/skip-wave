package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.*
import com.goldev.skipwave.event.EntityLevelEvent
import com.goldev.skipwave.event.fire
import ktx.log.logger
import kotlin.math.pow

/**
 * System that takes care of the waves in the game.
 *
 * @property experienceCmps Entities with ExperienceComponent in the world.
 * @property waveCmps Entities with WaveComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Wave system.
 */
@AllOf([WaveComponent::class])
class WaveSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val waveCmps: ComponentMapper<WaveComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    /**
     * If the entity has enough experience to level up, then increase the wave number and set the
     * experience needed to level up to the current experience plus the wave number squared.
     *
     * @param entity Entity - The entity that is being ticked
     */
    override fun onTickEntity(entity: Entity) {
        with(experienceCmps[entity]) {

            if (experience >= experienceToNextWave) {
                with(waveCmps[entity]) {
//                    log.debug { "Experience NEXT WAVE before ${experienceToNextLevel}" }
                    wave += 1
                    experienceToNextWave = experience + (wave / 0.2f).pow(2f)

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