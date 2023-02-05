package com.gabriel.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.SkipWave
import com.gabriel.SkipWave.Companion.ANIMATION_DURATION
import com.gabriel.event.*
import com.gabriel.ui.Buttons
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.RecordsModel
import com.gabriel.ui.model.Skills
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.widget.SkillSlot
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class RecordsView(
    model: RecordsModel,
    skin: Skin
) : KTable, Table(skin) {

    private val skillSlots = mutableListOf<SkillSlot>()

    init {
        isVisible = false

//        setPosition(-500f, 0f)
        //UI
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_FGD]
//You died!
            label(text = model.bundle["RecordsView.title"], style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.height(model.uiStage.height * 0.1f).top().row()
                lblCell.padTop(10f)
                setFontScale(0.4f)
                setColor(255f, 0f, 0f, 255f)
            }

            table { interiorCell ->
                background = skin[Drawables.FRAME_BGD]

                textButton(text = model.bundle["RecordsView.resume"], style = Buttons.RESUME.skinKey) { cell ->
                    cell.bottom().padTop(10f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()

                }


                label(
                    text = model.bundle["RecordsView.newRecord"],
                    style = Labels.FRAME.skinKey
                ) { lblCell ->
                    setColor(0f, 255f, 0f, 255f)
                    lblCell.expand().row()
                    lblCell.padTop(10f)
                    this.setFontScale(0.3f)
                }

                label(
                    text = model.bundle["RecordsView.waveRecord"],
                    style = Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.row()
                    lblCell.padTop(20f)
                    this.setFontScale(0.25f)
                }

                label(
                    text = "${model.gamePreferences.records.wave}",
                    style = Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.row()
                    lblCell.padTop(10f)
                    lblCell.padBottom(20f)
                    this.setFontScale(0.8f)
                }
                interiorCell.expand().fill().pad(10f, 10f, 10f, 10f).row()
            }

            textButton(text =model.bundle["RecordsView.button"], style = Buttons.DEFAULT.skinKey) { cell ->
                cell.bottom().expand().fill().maxHeight(25f).pad(25f, 10f, 6f, 10f).row()

                onClick {
                    log.debug { "Click on main menu button" }

//                    model.gameStage += Actions.fadeOut(ANIMATION_DURATION, Interpolation.circleOut).then(
//                        Actions.run(Runnable() {
//                            kotlin.run {
//                                model.gameStage.fire(MainMenuScreenEvent())
//
//                            }
//                        }),
//                    )
                    model.gameStage.fire(MainMenuScreenEvent())

                }
            }

            padBottom(10f)
            tableCell.expand().fill().maxWidth(model.uiStage.width * 0.9f).maxHeight(model.uiStage.height * 0.98f)
                .center()
        }


        // data binding
        model.onPropertyChange(SkillUpgradeModel::skills) { skills ->
            popup(skills)
            log.debug { "View OnPropertyChange Skills" }
        }

    }

    fun popup(skills: Skills) {


        if (this.alpha == 0f) {
            this.clearActions()
            this.setPosition(0f, 0f)
            this += Actions.sequence(Actions.fadeIn(0.2f))
        }
    }

    private fun Actor.resetFadeOutDelay() {
        this.actions
            .filterIsInstance<SequenceAction>()
            .lastOrNull()
            ?.let { sequence ->
                val delay = sequence.actions.last() as DelayAction
                delay.time = 0f
            }
    }


    companion object {
        private val log = logger<SkillUpgradeView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.recordsView(
    model: RecordsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: RecordsView.(S) -> Unit = {}
): RecordsView = actor(RecordsView(model, skin), init)