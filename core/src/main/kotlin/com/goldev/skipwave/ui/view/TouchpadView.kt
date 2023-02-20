package com.goldev.skipwave.ui.view

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.goldev.skipwave.event.MovementEvent
import com.goldev.skipwave.ui.model.TouchpadModel
import ktx.actors.*
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.*


/**
 * The view of the Touchpad
 *
 * @property model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Touchpad view
 */
class TouchpadView(
    val model: TouchpadModel,
    skin: Skin,
) : Table(skin), KTable {
    /**
     *  A variable that is used to store the position of the touchpad on the x axis.
     */
    private var positionX: Float = 0f

    /**
     * A variable that is used to store the position of the touchpad on the y axis.
     */
    private var positionY: Float = 0f

    /**
     *  Used to scale the touchpad to the screen size.
     */
    private var scale: Float = 0f
        get() = (this@TouchpadView.stage.width / this@TouchpadView.stage.viewport.screenWidth)

    /**
     * Starts the view with its components
     */
    init {
        // UI
        alpha = 0f
        setPosition(-400f, 0f)

        touchpad(0f) { cell ->
            cell.size(70f, 70f)
        }.onChangeEvent {
            log.debug { "X $knobPercentX Y $knobPercentY" }
            this@TouchpadView.model.knobPercentX = knobPercentX
            this@TouchpadView.model.knobPercentY = knobPercentY
            this@TouchpadView.fire(MovementEvent())

            this@TouchpadView.model.isTouch = isTouched

            if (!isTouched && knobPercentX == 0f && knobPercentY == 0f) {
                this@TouchpadView.alpha = 0f
                this@TouchpadView.model.touchpadLocation = vec2(knobPercentX, knobPercentY - 400f)
            }
        }

        // DATA BINDING
        model.onPropertyChange(TouchpadModel::touchpadLocation) { touchpadLocation ->
            setTouchpad(touchpadLocation)
        }

        model.onPropertyChange(TouchpadModel::opacity) { opacity ->
            setTouchpadVisible(opacity)
        }
    }

    /**
     * It sets the position of the touchpad.
     *
     * @param touchpadLocation The location of the touchpad.
     */
    private fun setTouchpad(touchpadLocation: Vector2) {
        positionX = touchpadLocation.x * scale
        positionY = touchpadLocation.y * scale
        setPosition(positionX, this@TouchpadView.stage.height - positionY)
    }

    /**
     * This function sets the alpha value of the touchpad to the value of the opacity parameter
     *
     * @param opacity The opacity of the touchpad.
     */
    private fun setTouchpadVisible(opacity: Float) {
        this.alpha = opacity
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<TouchpadModel>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.touchpadView(
    model: TouchpadModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TouchpadView.(S) -> Unit = {}
): TouchpadView = actor(TouchpadView(model, skin), init)