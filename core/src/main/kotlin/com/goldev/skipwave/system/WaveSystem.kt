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

@AllOf([WaveComponent::class])
class WaveSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val waveCmps: ComponentMapper<WaveComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(experienceCmps[entity]) {

            if (experience >= experienceToNextWave) {
                with(waveCmps[entity]) {
//                    log.debug { "Experience NEXT WAVE before ${experienceToNextLevel}" }
                    wave += 1
                    experienceToNextWave = experience + (wave/0.2f).pow(2f)

//                    log.debug { "Experience NEXT WAVE after ${experienceToNextLevel}" }
                    gameStage.fire(EntityLevelEvent(entity))
                }
            }
        }
    }


    companion object {
        private val log = logger<WaveSystem>()
    }


}