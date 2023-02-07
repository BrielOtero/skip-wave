package com.gabriel.ui.view

import com.badlogic.gdx.math.Interpolation
import com.gabriel.ui.model.*

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.SkipWave
import com.gabriel.SkipWave.Companion.ANIMATION_DURATION
import com.gabriel.event.*
import com.gabriel.ui.Buttons
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.widget.SkillSlot
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class MainMenuView(
    model: MainMenuModel,
    skin: Skin
) : KTable, Table(skin) {

    private val skillSlots = mutableListOf<SkillSlot>()

    init {
        //UI
        setFillParent(true)
        background = skin[Drawables.FRAME_FGD]

        table { tableCell ->

            label(text = "SKIP WAVE", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.row()
                lblCell.padTop(10f)
                lblCell.padBottom(100f)
                this.setFontScale(0.4f)
            }

            textButton(text = model.bundle["MainMenuView.newGame"], style = Buttons.DEFAULT.skinKey) { cell ->
                cell.top().padTop(5f).padBottom(6f)
                    .height(25f).width(110f)
                    .colspan(2)
                    .row()

                onClick {
                    log.debug { "Click on new game button" }

//                    model.gameStage += Actions.fadeOut(ANIMATION_DURATION, Interpolation.circleOut).then(
//                        Actions.run(Runnable() {
//                            kotlin.run {
//                                model.gameStage.fire(SetGameScreenEvent())
//                            }
//                        }),
//                    )
                    model.gameStage.fire(SetGameScreenEvent())
                }
            }
            textButton(text = model.bundle["MainMenuView.settings"], style = Buttons.DEFAULT.skinKey) { cell ->
                cell.top().padTop(5f).padBottom(6f)
                    .height(25f).width(110f)
                    .colspan(2)
                    .row()
            }
            textButton(text = model.bundle["MainMenuView.credits"], style = Buttons.DEFAULT.skinKey) { cell ->
                cell.top().padTop(5f).padBottom(6f)
                    .height(25f).width(110f)
                    .colspan(2)
                    .row()
            }

            padBottom(10f)
            tableCell.expand().fill().center()
        }


        // data binding
//        model.onPropertyChange(SkillUpgradeModel::skills) { skills ->
//            popup(skills)
//            log.debug { "View OnPropertyChange Skills" }
//        }

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
        private val log = logger<MainMenuView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.mainMenuView(
    model: MainMenuModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: MainMenuView.(S) -> Unit = {}
): MainMenuView = actor(MainMenuView(model, skin), init)