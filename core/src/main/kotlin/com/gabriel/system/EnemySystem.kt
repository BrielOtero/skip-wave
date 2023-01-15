package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.Survivor
import com.gabriel.component.EnemyComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EntityAddEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.math.vec2

class EnemySystem(
    @Qualifier("gameStage") private var gameStage: Stage,

) : IntervalSystem() {
    private val enemyEntities = world.family(allOf = arrayOf(EnemyComponent::class))

    override fun onTick() {
        if(enemyEntities.numEntities <= ENEMY_AMOUNT){
            gameStage.fire(
                EntityAddEvent(
                    vec2(200f, 200f),
                    "SKULL"
                )
            )
        }

    }

    companion object {
        private val ENEMY_AMOUNT: Int = 200
    }

}