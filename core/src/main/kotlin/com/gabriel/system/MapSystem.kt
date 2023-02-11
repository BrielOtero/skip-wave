package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.AnimationModel
import com.gabriel.component.WaveComponent
import com.gabriel.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class MapSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val waveCmps: ComponentMapper<WaveComponent>,
) : IntervalSystem(), EventListener {
    private var maps = MAPS.values().toList()
    private var lastWaveChange = 0
    override fun onTick() {
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is HalfOfNextWaveExperienceEvent -> {
                val waveCmp = waveCmps[event.entity]
                if (waveCmp.wave != lastWaveChange) {
                    gameStage.fire(NewMapEvent(maps.shuffled()[0].path))
                    log.debug { "Wave ${waveCmp.wave}" }
                    lastWaveChange = waveCmp.wave
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


