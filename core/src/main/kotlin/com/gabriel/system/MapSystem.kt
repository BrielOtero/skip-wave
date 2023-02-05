package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.LevelComponent
import com.gabriel.event.*
import com.gabriel.ui.model.RecordsModel
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.RecordsView
import com.gabriel.ui.view.SkillUpgradeView
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class MapSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val levelCmps: ComponentMapper<LevelComponent>,
) : IntervalSystem(), EventListener {

    override fun onTick() {
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is EntityLevelEvent -> {
                log.debug { "Level ${levelCmps[event.entity].level}" }
                when (levelCmps[event.entity].level) {
                    MAPS.MAP_1.level -> {
                        val currentMap = TmxMapLoader().load(MAPS.MAP_1.path)
                        gameStage.fire(MapChangeEvent(currentMap!!))
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
    val level: Int
) {
    MAP_1(2), MAP_2(50);

    val path: String = Gdx.files.internal("maps/${this.toString().lowercase()}.tmx").path()
}

