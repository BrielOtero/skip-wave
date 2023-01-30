package com.gabriel.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.Survivor
import com.gabriel.Survivor.Companion.UNIT_SCALE
import com.gabriel.component.EnemyComponent
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.LevelComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EnemyAddEvent
import com.gabriel.event.EntityAddEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger
import ktx.math.vec2

class EnemySystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private var levelCmps: ComponentMapper<LevelComponent>,

    ) : IntervalSystem() {
    private val enemyEntities = world.family(allOf = arrayOf(EnemyComponent::class))
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    override fun onTick() {

        if (levelCmps[playerEntities.first()].level > 5) {
            ENEMY_AMOUNT = 100
        }else if(levelCmps[playerEntities.first()].level > 10){
            ENEMY_AMOUNT = 200
        }

        if (enemyEntities.numEntities < ENEMY_AMOUNT) {
            gameStage.fire(
                EnemyAddEvent(
                    "CYCLOPE_SHINY"
                )
            )
        }

    }

    companion object {
        private var ENEMY_AMOUNT: Int = 50
        private val log = logger<EnemySystem>()
    }

}