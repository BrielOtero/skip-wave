package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.*
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.MovementEvent
import com.goldev.skipwave.event.StartMovementEvent
import com.github.quillraven.fleks.World
import com.goldev.skipwave.component.MoveComponent
import com.goldev.skipwave.component.PlayerComponent
import ktx.log.logger
import ktx.math.vec2


/**
 * The model of the Touchpad
 *
 * @property world The entities world.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Touchpad model
 */
class TouchpadModel(
    private val world: World,
    val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    /**
     *  Temporary vector.
     */
    private val tmpVec = vec2()

    /**
     *  A family of entities with the PlayerComponent.
     */
    private val playerEntities = world.family { all(PlayerComponent) }

    /**
     *  A variable that is used to store the cosine of the angle of the touchpad.
     */
    private var playerCos = 0f

    /**
     *  A variable that is used to store the sine of the angle of the touchpad.
     */
    private var playerSin = 0f

    /**
     *  A variable that is used to store the knob percent x of the touchpad.
     */
    var knobPercentX = 0f

    /**
     *  A variable that is used to store the knob percent y of the touchpad.
     */
    var knobPercentY = 0f

    /**
     *  Notifiable property with the touchpad location.
     */
    var touchpadLocation by propertyNotify(vec2(0f, -50f))

    /**
     *  Notifiable property with the touchpad opacity.
     */
    var opacity by propertyNotify(0f)

    /**
     *  A variable that is used to check if the touchpad is being touched.
     */
    var isTouch: Boolean = false

    /**
     *  Used to disable the touchpad when the player is dead.
     */
    var disableTouchpad: Boolean = false

    init {
        uiStage.addListener(this)
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
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


    /**
     * If the knob is moved more than 45% in any direction, set the player's movement to that
     * direction. Otherwise, set the player's movement to 0
     */
    private fun updatePlayerMovement() {
        val death = 0.45f

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
        with(world) {
            playerEntities.forEach { player ->
                with(player[MoveComponent]) {
                    cos = tmpVec.x
                    sin = tmpVec.y
                }
            }
        }
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<TouchpadModel>()
    }
}
