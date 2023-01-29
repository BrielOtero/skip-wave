package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.gabriel.event.GamePauseEvent
import com.gabriel.event.GameResumeEvent
import com.gabriel.event.TestEvent
import com.gabriel.event.fire
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.SkillModel
import com.gabriel.ui.model.Skills
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.widget.SkillSlot
import com.gabriel.ui.widget.skillSlot
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class SkillUpgradeView(
    model: SkillUpgradeModel,
    gameStage:Stage,
    uiStage: Stage,
    skin: Skin
) : KTable, Table(skin) {

    private val skillSlots = mutableListOf<SkillSlot>()

    init {
        this.alpha = 0f
        setPosition(-500f, 0f)
        //UI
        setFillParent(true)

        table { tableCell ->

            background = skin[Drawables.FRAME_BGD]
            label(text = "Level UP!", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.row()
                lblCell.padTop(20f)
                lblCell.padBottom(5f)
            }
            for (i in 1..3) {
                this@SkillUpgradeView.skillSlots += skillSlot(skin = skin, uiStage = uiStage) { skillCell ->
                    skillCell.expand().fill().row()
//                    skillCell.padBottom(5f)
                    skillCell.pad(10f, 10f, 10f, 10f)
                    setScale(0.8f)

                    onTouchDown {
                       gameStage.fire(TestEvent())
                        log.debug { "TOUCHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH ${skillName} " }
                        this@SkillUpgradeView.alpha = 0f
                        this.setPosition(-500f, 0f)
                        gameStage.fire(GameResumeEvent())
                    }

                }
            }
            tableCell.expand().fill().maxWidth(uiStage.width * 0.9f).maxHeight(uiStage.height * 0.7f).center()
        }


        // data binding
        model.onPropertyChange(SkillUpgradeModel::skills) { skills ->
            popup(skills)
            log.debug { "View OnPropertyChange Skills" }
        }

    }

    fun popup(skills: Skills) {

        with(skills.skill1) {
            skill(SkillModel(skill.skillEntityId, atlasKey, skill.name, 0))
        }
        with(skills.skill2) {
            skill(SkillModel(skill.skillEntityId, atlasKey, skill.name, 1))
        }
        with(skills.skill3) {
            skill(SkillModel(skill.skillEntityId, atlasKey, skill.name, 2))
        }

        if (this.alpha == 0f) {
            log.debug { "Alpha ${alpha}" }
            this.clearActions()
            this.setPosition(0f, 0f)
            this += Actions.sequence(Actions.fadeIn(0.2f))
        } else {
//            this.setPosition(-500f, 0f)
//            this.resetFadeOutDelay()
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

    fun skill(skillModel: SkillModel) {
        skillSlots[skillModel.slotIdx].skill(skillModel)
    }

    companion object {
        private val log = logger<SkillUpgradeView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.skillUpgradeView(
    model: SkillUpgradeModel,
    gameStage: Stage,
    uiStage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillUpgradeView.(S) -> Unit = {}
): SkillUpgradeView = actor(SkillUpgradeView(model, gameStage,uiStage, skin), init)