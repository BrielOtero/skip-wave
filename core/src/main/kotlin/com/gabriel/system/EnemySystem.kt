package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.*
import com.gabriel.event.EnemyAddEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class EnemySystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private var waveCmps: ComponentMapper<WaveComponent>,

    ) : IntervalSystem() {
    private val enemyEntities = world.family(allOf = arrayOf(EnemyComponent::class))
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    private var lastLevel = 0
    private var lastLevelChangeEnemy = -1
    private lateinit var actualEnemy: AnimationModel
    private var enemies =
        AnimationModel.values().filter {
            it != AnimationModel.UNDEFINED && it != AnimationModel.PLAYER && it != AnimationModel.CHEST && it != AnimationModel.SLASH_LEFT && it != AnimationModel.SLASH_RIGHT
        }


    override fun onTick() {
        val actualLevel = waveCmps[playerEntities.first()].wave

        //AMOUNT ENEMIES
        if (actualLevel > 1 && actualLevel != lastLevel) {
            if (ENEMY_AMOUNT <= 250) {
                ENEMY_AMOUNT += 2
                lastLevel = actualLevel
            }
        }

        //ACTUAL ENEMIES
        if (actualLevel <= enemies.size && actualLevel % 2 == 0 && actualLevel != lastLevelChangeEnemy) {
            if (actualLevel == 0) {
                actualEnemy = enemies[actualLevel]
                log.debug { "ENEMY ADDED ${enemies[actualLevel].name}" }
            } else {
                actualEnemy = enemies[actualLevel - 1]
                log.debug { "ENEMY ADDED ${enemies[actualLevel-1].name}" }
            }
            lastLevelChangeEnemy = actualLevel
        }

        //NOTIFY ENEMY SPAWN
        if (enemyEntities.numEntities < ENEMY_AMOUNT) {

            if (actualLevel <= enemies.size) {
                gameStage.fire(
                    EnemyAddEvent(
                        actualEnemy
                    )
                )
            } else {
                gameStage.fire(
                    EnemyAddEvent(
                        enemies.shuffled()[0]
                    )
                )
            }
        }
    }

    companion object {
        private var ENEMY_AMOUNT: Int = 30
        private val log = logger<EnemySystem>()
    }

}