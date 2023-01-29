package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.event.MovementEvent
import com.gabriel.ui.model.TouchpadModel
import ktx.actors.*
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.*


class TouchpadView(
     val model: TouchpadModel,
    skin: Skin,
) : Table(skin), KTable {
    private var positionX: Float = 0f
    private var positionY: Float = 0f
    private var scale: Float = 0f
        get() = (this@TouchpadView.stage.width / this@TouchpadView.stage.viewport.screenWidth)


    init {
        alpha = 0f

        // UI
        touchpad(0f) { cell ->
            cell.size(70f, 70f)
        }.onChangeEvent {
            log.debug { "X $knobPercentX Y $knobPercentY" }
            this@TouchpadView.model.knobPercentX = knobPercentX
            this@TouchpadView.model.knobPercentY = knobPercentY
            this@TouchpadView.fire(MovementEvent())

            this@TouchpadView.model.isTouch = isTouched

            if (!isTouched && knobPercentX == 0f && knobPercentY == 0f) {
                this@TouchpadView.alpha = 1f
                this@TouchpadView.model.touchpadLocation = vec2(knobPercentX, knobPercentY - 200f)
            }
        }

        //Data binding
        model.onPropertyChange(TouchpadModel::touchpadLocation) { touchpadLocation ->
            positionX = touchpadLocation.x * scale
            positionY = touchpadLocation.y * scale
            setPosition(positionX, this@TouchpadView.stage.height - positionY)
        }

        model.onPropertyChange(TouchpadModel::opacity) { opacity ->
            this.alpha = opacity
        }
        setPosition(-100f, 0f)
    }

    companion object {
        private val log = logger<TouchpadModel>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.touchpadView(
    model: TouchpadModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TouchpadView.(S) -> Unit = {}
): TouchpadView = actor(TouchpadView(model, skin), init)