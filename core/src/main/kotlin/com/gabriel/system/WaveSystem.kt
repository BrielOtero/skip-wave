package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.WaveComponent
import com.gabriel.event.*
import com.github.quillraven.fleks.*
import ktx.log.logger

@AllOf([WaveComponent::class])
class WaveSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val levelCmps: ComponentMapper<WaveComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(experienceCmps[entity]) {
            if ((levelCmps[entity].wave % 2 == 0) && experience >= (experienceToNextLevel) / 2) {
                gameStage.fire(HalfOfNextWaveExperienceEvent(entity))
            }

            if (experience >= experienceToNextLevel) {
                with(levelCmps[entity]) {

//                    log.debug { "ExperienceToNextLevel before ${experienceToNextLevel}" }
                    wave += 1
                    experienceToNextLevel = experience + 500f

                    log.debug { "ExperienceToNextLevel ${experienceToNextLevel}" }
//                    log.debug { "ExperienceToNextLevel after ${experienceToNextLevel}" }
                    gameStage.fire(EntityLevelEvent(entity))
                }
            }
        }
    }


    companion object {
        private val log = logger<WaveSystem>()
    }


}