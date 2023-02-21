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

/**
 * System that takes care of the enemies in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property waveCmps Entities with WaveComponent in the world.
 * @property gamePreferences The preferences of the game.
 * @constructor Create empty Enemy system
 */
class EnemySystem(
    @Qualifier("gameStage") private var gameStage: Stage,
    private var waveCmps: ComponentMapper<WaveComponent>,
    private val gamePreferences: GamePreferences

) : IntervalSystem() {
    /**
     *  Variable with all the entities with the EnemyComponent.
     */
    private val enemyEntities = world.family(allOf = arrayOf(EnemyComponent::class))

    /**
     * Variable with all the entities with the PlayerComponent.
     */
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    /**
     *  It's a variable that stores the last level that the player was.
     */
    private var lastLevel = 0

    /**
     *  It's a variable that stores the last level that the enemy change.
     */
    private var lastLevelChangeEnemy = -1

    /**
     *  It's a variable that stores the actual enemy that will be spawned.
     */
    private lateinit var actualEnemy: AnimationModel

    /**
     *  It's a variable that stores all animation model of enemies.
     */
    private var enemies =
        AnimationModel.values().filter {
            it != AnimationModel.UNDEFINED && it != AnimationModel.PLAYER && it != AnimationModel.CHEST && it != AnimationModel.SLASH_LEFT && it != AnimationModel.SLASH_RIGHT
        }

    /**
     * Spawn new enemies when they dead.
     */
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

        /**
         *  The amount of enemies that will be spawned.
         */
        private var ENEMY_AMOUNT: Int = 300

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<EnemySystem>()
    }

}