package com.gabriel.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.event.MovementEvent
import com.gabriel.ui.model.HUDModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*


class HUDView(
    private val model: HUDModel,
    skin: Skin,
) : Table(skin), KTable {
    private var positionX: Float = 0f
    private var positionY: Float = 0f
    private var scale: Float = 0f

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
            if (!isTouched) {
                log.debug { "TouchUP" }
                this@HUDView.alpha=0f
            }
        }

        //Data binding
        model.onPropertyChange(HUDModel::touchpadX) { touchpadX ->
            scale = this@HUDView.stage.width / this@HUDView.stage.viewport.screenWidth
            positionX = touchpadX * scale
        }

        model.onPropertyChange(HUDModel::touchpadY) { touchpadY ->
            positionY = touchpadY * scale
            setPosition(positionX, this@HUDView.stage.height - positionY)
        }

        model.onPropertyChange(HUDModel::opacity) { opacity ->
            this.alpha = opacity
        }
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
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