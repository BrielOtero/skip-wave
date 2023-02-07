package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
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

    override fun onTick() {
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is EntityLevelEvent -> {
                log.debug { "Wave ${waveCmps[event.entity].wave}" }
                when (waveCmps[event.entity].wave) {
                    MAPS.MAP_1.wave -> {
                        gameStage.fire(NewMapEvent(MAPS.MAP_1.path))
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
    val wave: Int
) {
    MAP_0(0),
    MAP_1(2),
    MAP_2(50);

    val path: String = Gdx.files.internal("maps/${this.toString().lowercase()}.tmx").path()
}


