package com.goldev.skipwave.ui.widget

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.Timer.Task
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.Buttons
import com.goldev.skipwave.ui.Labels
import ktx.actors.*
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.scene2d.*
import java.util.*


/**
 * The change value widget.
 *
 * @property defaultValue The default value of change.
 * @property gameStage The stage that the game is being rendered on.
 * @property skin The skin of the widget.
 * @property bundle The bundle with text to show in the UI.
 * @constructor Create empty Change value.
 */
class ChangeValue(
    private val defaultValue: Int,
    private val gameStage: Stage,
    private val skin: Skin,
    private val bundle: I18NBundle,

    ) : WidgetGroup(), KGroup, KtxInputAdapter {

    /**
     *  A variable that is used to store the left button.
     */
    private val btnLeft: Button = button(style = Buttons.LEFT.skinKey)

    /**
     *  A variable that is used to store the right button.
     */
    private val btnRight: Button = button(style = Buttons.RIGHT.skinKey)

    /**
     *  A variable that is used to store the value label.
     */
    private val lblValue: Label = label(defaultValue.toString(), style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the value of left timer.
     */
    private lateinit var timerLeft: java.util.Timer

    /**
     *  A variable that is used to store the value of right timer.
     */
    private lateinit var timerRight: java.util.Timer

    /**
     *  A variable that is used to store if button left is touch down.
     */
    private var btnLeftTouchDown = false

    /**
     *  A variable that is used to store if button left is touch up.
     */
    private var btnLeftTouchUp = false

    /**
     *  A variable that is used to store if button right is touch down.
     */
    private var btnRightTouchDown = false

    /**
     *  A variable that is used to store if button right is touch up.
     */
    private var btnRightTouchUp = false

    /**
     * Starts the view with its components
     */
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
            log.debug { "BTN: TOUCH LEFT" }

            if (!btnRightTouchDown) {
                btnRight.isDisabled = true
                btnLeftTouchDown = true

                btnLeftTouchUp = false
                gameStage.fire(ButtonPressedEvent())
                timerLeft = java.util.Timer()
                timerLeft.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        synchronized(this) {
                            if (!btnLeftTouchUp) {
                                var value = lblValue.text.toString().toInt()
                                if (lblValue.text.toString().toInt() > 0) {
                                    value--
                                    lblValue.setText(value)
                                } else {
                                    timerLeft.cancel();
                                }
                            } else {
                                timerLeft.cancel();
                            }
                        }
                    }
                }, 0, 100)
            }

        }

        btnLeft.onClick {
            btnLeftTouchUp = true
            btnLeftTouchDown = false
            btnRight.isDisabled = false
        }
        btnLeft.onExit {
            btnLeftTouchUp = true
            btnLeftTouchDown = false
            btnRight.isDisabled = false

        }

        btnRight.onTouchDown {
            log.debug { "BTN: TOUCH RIGHT" }

            if (!btnLeftTouchDown) {
                btnLeft.isDisabled = true
                btnRightTouchDown = true

                gameStage.fire(ButtonPressedEvent())
                btnRightTouchUp = false
                timerRight = java.util.Timer()
                timerRight.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        synchronized(this) {
                            var value = lblValue.text.toString().toInt()
                            if (!btnRightTouchUp) {
                                if (lblValue.text.toString().toInt() < 100) {
                                    value++
                                    lblValue.setText(value)
                                } else {
                                    timerRight.cancel();
                                }
                            } else {
                                timerRight.cancel();
                            }
                        }
                    }
                }, 0, 100)
            }

        }

        btnRight.onClick {
            btnRightTouchUp = true
            btnRightTouchDown = false
            btnLeft.isDisabled = false
        }
        btnRight.onExit {
            btnRightTouchUp = true
            btnRightTouchDown = false
            btnLeft.isDisabled = false
        }
    }

    /**
     * Gets the value of the change value.
     */
    fun getValue() = lblValue.text.toString().toInt()

    /**
     * It sets the value of the change value.
     *
     * @param value The value to set in change value.
     */
    fun setValue(value: Int) {
        lblValue.setText(value.toString())
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private var log = logger<ChangeValue>()
    }
}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.changeValue(
    defaultValue: Int,
    uiStage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
    bundle: I18NBundle,
    init: ChangeValue.(S) -> Unit = {}
): ChangeValue = actor(ChangeValue(defaultValue, uiStage, skin, bundle), init)