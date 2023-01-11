package com.gabriel.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.event.MovementEvent
import com.gabriel.ui.model.HUDModel
import com.gabriel.ui.model.propertyNotify
import ktx.actors.*
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.*


class HUDView(
    private val model: HUDModel,
    skin: Skin,
) : Table(skin), KTable {
    private var positionX: Float = 0f
    private var positionY: Float = 0f
    private var scale: Float = 0f
        get() = (this@HUDView.stage.width / this@HUDView.stage.viewport.screenWidth)


    init {
        alpha = 0f

        // UI
        touchpad(0f) { cell ->
            cell.size(70f, 70f)
        }.onChangeEvent {
            log.debug { "X $knobPercentX Y $knobPercentY" }
            this@HUDView.model.knobPercentX = knobPercentX
            this@HUDView.model.knobPercentY = knobPercentY
            this@HUDView.fire(MovementEvent())

            this@HUDView.model.isTouch = isTouched

            if (!isTouched && knobPercentX == 0f && knobPercentY == 0f) {
                this@HUDView.alpha = 1f
                this@HUDView.model.touchpadLocation = vec2(knobPercentX, knobPercentY - 200f)
            }
        }

        //Data binding
        model.onPropertyChange(HUDModel::touchpadLocation) { touchpadLocation ->
            positionX = touchpadLocation.x * scale
            positionY = touchpadLocation.y * scale
            setPosition(positionX, this@HUDView.stage.height - positionY)
        }

        model.onPropertyChange(HUDModel::opacity) { opacity ->
            this.alpha = opacity
        }
        setPosition(-100f, 0f)
    }

    companion object {
        private val log = logger<HUDModel>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.hudView(
    model: HUDModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: HUDView.(S) -> Unit = {}
): HUDView = actor(HUDView(model, skin), init)