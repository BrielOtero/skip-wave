package com.goldev.skipwave.ui.widget

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.ui.Buttons
import com.goldev.skipwave.ui.Labels
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.scene2d.*

class ChangeValue(
    private val defaultValue: Float,
    private val uiStage: Stage,
    private val skin: Skin,
    private val bundle: I18NBundle,
) : WidgetGroup(), KGroup, KtxInputAdapter {

    private val btnLeft: Button = button(style = Buttons.LEFT.skinKey)
    private val btnRight: Button = button(style = Buttons.RIGHT.skinKey)
    private val lblValue: Label = label(defaultValue.toInt().toString(), style = Labels.FRAME.skinKey)


    init {

        this += btnLeft.apply {
            setSize(30f, 30f)
            setPosition(0f, 0f)
        }
        this += lblValue.apply {
            setSize(30f, 30f)
            setPosition(40f, 0f)
            setAlignment(Align.center)
            setFontScale(0.4f)

        }
        this += btnRight.apply {
            setSize(30f, 30f)
            setPosition(80f, 0f)
        }

        //MANAGE EVENTS

        btnLeft.onTouchDown {
            log.debug { "TOUCH LEFT" }
            var value = lblValue.text.toString().toInt()
            if (lblValue.text.toString().toInt() > 0) {
                value--
                lblValue.setText(value)
            }
        }

        btnRight.onTouchDown {
            log.debug { "TOUCH RIGHT" }
            var value = lblValue.text.toString().toInt()
            if (lblValue.text.toString().toInt() < 100) {
                value++
                lblValue.setText(value)
            }
        }
    }
    fun getValue() = lblValue.text.toString().toFloat()
    companion object {
        private var log = logger<ChangeValue>()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.changeValue(
    defaultValue: Float,
    uiStage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
    bundle: I18NBundle,
    init: ChangeValue.(S) -> Unit = {}
): ChangeValue = actor(ChangeValue(defaultValue, uiStage, skin, bundle), init)