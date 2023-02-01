package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.event.*
import com.gabriel.ui.Buttons
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.RecordsModel
import com.gabriel.ui.model.Skills
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.widget.SkillSlot
import ktx.actors.alpha
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class RecordsView(
    model: RecordsModel,
    skin: Skin
) : KTable, Table(skin) {

    private val skillSlots = mutableListOf<SkillSlot>()

    init {
        isVisible=false

//        setPosition(-500f, 0f)
        //UI
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_BGD]
            label(text = "You died!", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.row()
                lblCell.padTop(10f)
                this.setFontScale(0.4f)
            }
            textButton(text = "MAIN MENU", style = Buttons.DEFAULT.skinKey) { cell ->
                cell.top().padTop(5f).padBottom(6f)
                    .height(25f).width(110f)
                    .colspan(2)
                    .row()
                onTouchDown {
                    model.gameStage.fire(MainMenuScreenEvent())
                    this@RecordsView.isVisible=false

//                    gameStage.fire(NewGameEvent())

//                   uiStage.actors.filterIsInstance<GameView>().first().isVisible = true

//                   with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
//                       this.model.disableTouchpad = false
//                       isVisible = true
//                   }
                }
            }


            padBottom(10f)
            tableCell.expand().maxWidth(model.uiStage.width * 0.9f).maxHeight(model.uiStage.height * 0.98f).center()
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