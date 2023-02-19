package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EnemyAddEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.component.AnimationModel
import com.goldev.skipwave.component.EnemyComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.ShowTutorialViewEvent
import com.goldev.skipwave.preferences.GamePreferences
import ktx.log.logger

class EnemySystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private var waveCmps: ComponentMapper<WaveComponent>,
    private val gamePreferences: GamePreferences

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

    private var showTutorial = true
    override fun onTick() {
        val currentWave = waveCmps[playerEntities.first()].wave

        //AMOUNT ENEMIES
        if (currentWave > 1 && currentWave != lastLevel) {
            if (ENEMY_AMOUNT <= 500) {
                ENEMY_AMOUNT += 5
                lastLevel = currentWave
            }
        }

        //ACTUAL ENEMIES
        if (currentWave <= enemies.size && currentWave != lastLevelChangeEnemy) {
            actualEnemy = enemies[currentWave]
            log.debug { "ENEMY ADDED ${enemies[currentWave].name}" }
            lastLevelChangeEnemy = currentWave
        }

        //NOTIFY ENEMY SPAWN
        if (enemyEntities.numEntities < ENEMY_AMOUNT) {

            if (currentWave <= enemies.size) {
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
        } else {
            if (!gamePreferences.game.tutorialComplete) {
                gameStage.fire(ShowTutorialViewEvent())
            }
        }
    }

    companion object {
        private var ENEMY_AMOUNT: Int = 30
        private val log = logger<EnemySystem>()
    }

}