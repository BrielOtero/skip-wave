package com.goldev.skipwave.ui.widget

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.Timer.Task
import com.goldev.skipwave.ui.Buttons
import com.goldev.skipwave.ui.Labels
import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.scene2d.*
import java.util.*


class ChangeValue(
    private val defaultValue: Float,
    private val uiStage: Stage,
    private val skin: Skin,
    private val bundle: I18NBundle,
) : WidgetGroup(), KGroup, KtxInputAdapter {

    private val btnLeft: Button = button(style = Buttons.LEFT.skinKey)
    private val btnRight: Button = button(style = Buttons.RIGHT.skinKey)
    private val lblValue: Label = label(defaultValue.toInt().toString(), style = Labels.FRAME.skinKey)
    private lateinit var timerLeft: java.util.Timer
    private lateinit var timerRight : java.util.Timer

    private var btnLeftTouchUp = false
    private var btnRightTouchUp = false


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
            btnLeftTouchUp = false
            timerLeft = java.util.Timer()
            timerLeft.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    synchronized(this) {
                        if (!btnLeftTouchUp) {
                            if (lblValue.text.toString().toInt() > 0) {
                                value--
                                lblValue.setText(value)
                                log.debug { "Delay" }
                            }else{
                                timerLeft.cancel();
                            }
                        } else {
                            timerLeft.cancel();
                        }
                    }
                }
            }, 0, 100)
        }

        btnLeft.onClick { btnLeftTouchUp = true }

        btnRight.onTouchDown {
            log.debug { "TOUCH RIGHT" }
            var value = lblValue.text.toString().toInt()
            btnRightTouchUp = false
            timerRight= java.util.Timer()
            timerRight.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    synchronized(this) {
                        if (!btnRightTouchUp) {
                            if (lblValue.text.toString().toInt() < 100) {
                                value++
                                lblValue.setText(value)
                                log.debug { "Delay" }
                            }else{
                                timerRight.cancel();
                            }
                        } else {
                            timerRight.cancel();
                        }
                    }
                }
            }, 0, 100)
        }

        btnRight.onClick { btnRightTouchUp = true }

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