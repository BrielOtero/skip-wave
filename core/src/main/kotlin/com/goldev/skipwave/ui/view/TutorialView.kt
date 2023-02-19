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

class TutorialView(
    private val model: TutorialModel,
    skin: Skin
) : KTable, Table(skin) {

    private val btnContinue: TextButton
    private val lblLife: Label
    private val lblExperience: Label
    private val lblWave: Label
    private val lblTouchpad: Label
    private val lblSkills: Label
    private val lblTarget: Label
    private var stepTutorial: Int = 0
    private val tbTouchpad: Table
    private val tbSkills: Table
    private val tbTarget: Table


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
            this@TutorialView.lblExperience = label( this@TutorialView.model.bundle["TutorialView.experience"], Labels.FRAME.skinKey) { lblCell ->
                lblCell.padLeft(80f).padBottom(-7f).width(95f).row()
                setAlignment(Align.left)
                setFontScale(0.2f)
                wrap = true
                isVisible = false

            }
            this@TutorialView.lblWave = label( this@TutorialView.model.bundle["TutorialView.wave"], Labels.FRAME.skinKey) { lblCell ->
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
                    model.uiStage.actors.filterIsInstance<TouchpadView>().first().touchable = Touchable.disabled
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
        private val log = logger<TutorialView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.tutorialView(
    model: TutorialModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TutorialView.(S) -> Unit = {}
): TutorialView = actor(TutorialView(model, skin), init)