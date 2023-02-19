package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.*
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.MovementEvent
import com.goldev.skipwave.event.StartMovementEvent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.component.MoveComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.component.WeaponComponent
import ktx.log.logger
import ktx.math.vec2


class TouchpadModel(
    world: World,
    @Qualifier("gameStage") val stage: Stage,
    private val moveCmps: ComponentMapper<MoveComponent> = world.mapper(),
) : PropertyChangeSource(), EventListener {

    private val tmpVec = vec2()
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    private var playerCos = 0f
    private var playerSin = 0f
    var knobPercentX = 0f
    var knobPercentY = 0f

    var touchpadLocation by propertyNotify(vec2(0f, -50f))
    var opacity by propertyNotify(0f)
    var isTouch: Boolean = false
    var disableTouchpad: Boolean = false


    init {
        stage.addListener(this)
    }

    override fun handle(event: Event): Boolean {
//        log.debug { "Event ${event}" }
        when (event) {
            is StartMovementEvent -> {
                log.debug { "Start Movement" }
                if (!isTouch && !disableTouchpad) {
                    touchpadLocation = vec2(event.x, event.y)
                    opacity = 0.4f
                    log.debug { "Start Movement INSIDE" }
                }
            }

            is MovementEvent -> updatePlayerMovement()
            else -> return false
        }
        return true
    }


    private fun updatePlayerMovement() {
        val death = 0.25f

        playerCos = when {
            knobPercentX > death -> 1f
            knobPercentX < -death -> -1f
            else -> 0f
        }

        playerSin = when {
            knobPercentY > death -> 1f
            knobPercentY < -death -> -1f
            else -> 0f
        }

        tmpVec.set(playerCos, playerSin).nor()
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = tmpVec.x
                sin = tmpVec.y
            }
        }
    }

    companion object {
        private val log = logger<TouchpadModel>()
    }
}