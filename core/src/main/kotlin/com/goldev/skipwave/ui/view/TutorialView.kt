package com.goldev.skipwave.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.TutorialModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

/**
 * The view of the Tutorial
 *
 * @property model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Tutorial view
 */
class TutorialView(
    private val model: TutorialModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the continue button.
     */
    private val btnContinue: TextButton

    /**
     *  A variable that is used to store the life label.
     */
    private val lblLife: Label

    /**
     *  A variable that is used to store the experience label.
     */
    private val lblExperience: Label

    /**
     *  A variable that is used to store the wave label.
     */
    private val lblWave: Label

    /**
     *  A variable that is used to store the touchpad label.
     */
    private val lblTouchpad: Label

    /**
     *  A variable that is used to store the skills label.
     */
    private val lblSkills: Label

    /**
     *  A variable that is used to store the target label.
     */
    private val lblTarget: Label

    /**
     *  A variable that is used to store the step of the tutorial.
     */
    private var stepTutorial: Int = 0

    /**
     *  A variable that is used to store the touchpad table.
     */
    private val tbTouchpad: Table

    /**
     *  A variable that is used to store the skills table.
     */
    private val tbSkills: Table

    /**
     *  A variable that is used to store the target table.
     */
    private val tbTarget: Table

    /**
     * Starts the view with its components
     */
    init {
        //UI
        isVisible = false
        setFillParent(true)

        table { tableCell ->

            this@TutorialView.lblLife = label(
                this@TutorialView.model.bundle["TutorialView.life"], Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.pad(10f, 80f, 0f, 0f).width(95f).row()
                setAlignment(Align.left)
                setFontScale(0.2f)
                wrap = true
                isVisible = false
            }
            this@TutorialView.lblExperience = label(
                this@TutorialView.model.bundle["TutorialView.experience"],
                Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.padLeft(80f).padBottom(-7f).width(95f).row()
                setAlignment(Align.left)
                setFontScale(0.2f)
                wrap = true
                isVisible = false

            }
            this@TutorialView.lblWave = label(
                this@TutorialView.model.bundle["TutorialView.wave"],
                Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.padLeft(80f).width(95f).row()
                setAlignment(Align.left)
                setFontScale(0.2f)
                wrap = true
                isVisible = false
            }

            this@TutorialView.tbTouchpad = table { infoCell ->
                background = skin[Drawables.FRAME_FGD]

                this@TutorialView.lblTouchpad = label(
                    text = this@TutorialView.model.bundle["TutorialView.touchpad"],
                    Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.pad(5f, 5f, 5f, 5f).width(150f).row()
                    setAlignment(Align.center)
                    setFontScale(0.2f)
                    wrap = true
                }
                infoCell.padTop(30f).row()
                isVisible = false

            }

            this@TutorialView.tbSkills = table { infoCell ->
                background = skin[Drawables.FRAME_FGD]
                this@TutorialView.lblSkills = label(
                    text = this@TutorialView.model.bundle["TutorialView.skills"],
                    Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.pad(5f, 5f, 5f, 5f).width(150f).row()
                    setAlignment(Align.center)
                    setFontScale(0.2f)
                    wrap = true
                }
                infoCell.row()
                isVisible = false

            }

            this@TutorialView.tbTarget = table { infoCell ->
                background = skin[Drawables.FRAME_FGD]
                this@TutorialView.lblTarget = label(
                    text = this@TutorialView.model.bundle["TutorialView.target"],
                    Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.pad(5f, 5f, 5f, 5f).width(150f).row()
                    setAlignment(Align.center)
                    setFontScale(0.2f)
                    wrap = true
                }
                infoCell.row()
                isVisible = false
            }

            this@TutorialView.btnContinue = textButton(
                text = this@TutorialView.model.bundle["TutorialView.startTutorial"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.bottom().expand().fill().maxHeight(25f).pad(5f, 12f, 12f, 12f).row()
            }
            tableCell.expand().fill()
        }

        //EVENTS

        btnContinue.onTouchDown {
            log.debug { "BTN: CONTINUE" }
            model.gameStage.fire(ButtonPressedEvent())
        }

        btnContinue.onClick {

            if (stepTutorial < 7) {
                stepTutorial++
            }

            when (stepTutorial) {
                1 -> {
                    btnContinue.setText(this@TutorialView.model.bundle["TutorialView.continueTutorial"])
                    lblLife.isVisible = true
                }

                2 -> {
                    lblLife.isVisible = false
                    lblExperience.isVisible = true
                }

                3 -> {
                    lblExperience.isVisible = false
                    lblWave.isVisible = true
                }

                4 -> {
                    lblWave.isVisible = false
                    tbTouchpad.isVisible = true
                    model.uiStage.actors.filterIsInstance<TouchpadView>().first().touchable =
                        Touchable.disabled
                    with(model.uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                        this.model.disableTouchpad = false
                        isVisible = true
                    }
                    model.uiStage.fire(
                        StartMovementEvent(
                            model.uiStage.viewport.screenWidth.toFloat() / 2,
                            (model.uiStage.viewport.screenHeight.toFloat() / 2) + (model.uiStage.viewport.screenHeight.toFloat() * 0.2f)
                        )
                    )
                    with(model.uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                        this.model.disableTouchpad = true
                        isVisible = true
                    }

                }

                5 -> {
                    with(model.uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                        this.model.disableTouchpad = true
                        isVisible = false
                    }

                    tbTouchpad.isVisible = false
                    tbSkills.isVisible = true
                }

                6 -> {
                    tbSkills.isVisible = false
                    tbTarget.isVisible = true

                    btnContinue.setText(this@TutorialView.model.bundle["TutorialView.continueGame"])
                }

                7 -> {
                    model.gameStage.fire(HideTutorialViewEvent())

                }
            }
        }

        // DATA BINDING
    }


    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<TutorialView>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.tutorialView(
    model: TutorialModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TutorialView.(S) -> Unit = {}
): TutorialView = actor(TutorialView(model, skin), init)