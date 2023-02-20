package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.EntityExperienceEvent
import com.goldev.skipwave.event.NewMapEvent
import com.goldev.skipwave.event.fire
import ktx.log.logger

class MapSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val waveCmps: ComponentMapper<WaveComponent>,
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
) : IntervalSystem(), EventListener {
    private var maps = MAPS.values().toList()
    private var lastWaveChange = 0
    override fun onTick() {
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is EntityExperienceEvent -> {

                val waveCmp = waveCmps[event.entity]
                val experienceCmp = experienceCmps[event.entity]

                if ((waveCmps[event.entity].wave % 5 == 0) && experienceCmp.experience >= (experienceCmp.experienceToNextWave) / 2) {
                    if (waveCmp.wave != lastWaveChange) {
                        gameStage.fire(NewMapEvent(maps.shuffled()[0].path))
                        log.debug { "MAP CHANGE ON ${waveCmp.wave} with ${experienceCmp.experience} exp" }
                        lastWaveChange = waveCmp.wave
                    }
                }

            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<MoveSystem>()
    }

}

enum class MAPS(
) {
    MAP_1,
    MAP_2,
    MAP_3,
    MAP_4,
    MAP_5,
    MAP_6;

    val path: String = Gdx.files.internal("maps/${this.toString().lowercase()}.tmx").path()
}


