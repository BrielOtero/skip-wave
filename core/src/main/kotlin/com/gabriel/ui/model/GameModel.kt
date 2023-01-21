package com.gabriel.ui.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.AnimationComponent
import com.gabriel.component.LifeComponent
import com.gabriel.component.PhysicComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EntityAggroEvent
import com.gabriel.event.EntityDamageEvent
import com.gabriel.event.EntityLootEvent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.math.vec2

class GameModel(
    world: World,
    @Qualifier("gameStage") val gameStage: Stage,
) : PropertyChangeSource(), EventListener {

    private val playerCmps: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeCmps: ComponentMapper<LifeComponent> = world.mapper()
    private val animationCmps: ComponentMapper<AnimationComponent> = world.mapper()

    var playerLife by propertyNotify(1f)
    var playerExperience by propertyNotify(0f)
    var lootText by propertyNotify("")


    init {
        gameStage.addListener(this)
    }


    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityDamageEvent -> {
                val isPlayer = event.entity in playerCmps
                val lifeCmp = lifeCmps[event.entity]
                if (isPlayer) {
                    playerLife = lifeCmp.life / lifeCmp.max
                }

            }

            is EntityLootEvent -> {
                lootText = "You found something [#00ff00]useful[] !"
            }

            else -> return false
        }
        return true
    }
}