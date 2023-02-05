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
import com.gabriel.event.*
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.SkillModel
import com.gabriel.ui.model.Skills
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.model.TouchpadModel
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
            label(text = model.bundle.get("SkillUpgradeView.title"), style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.row()
                lblCell.padTop(10f)
                this.setFontScale(0.4f)
            }

            for (i in 1..3) {
                this@SkillUpgradeView.skillSlots += skillSlot(skin = skin, uiStage = model.uiStage, bundle = model.bundle
                ) { skillCell ->
                    skillCell.expand().width(model.uiStage.width * 0.8f).height(model.uiStage.height * 0.25f).center().row()
                    skillCell.pad(0f, 10f, 4f, 10f)

                    onTouchDown {
                        this.setPosition(-500f, 0f)
                        this@SkillUpgradeView += Actions.sequence(Actions.fadeOut(0.2f))

                        model.gameStage.fire(GameResumeEvent())

                        log.debug { "Resume" }

                        model.gameStage.fire(SkillApplyEvent(skillModel))

                        model.uiStage.actors.filterIsInstance<GameView>().first().isVisible = true

                        with(model.uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                            this.model.disableTouchpad = false
                            isVisible = true
                        }
                    }

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

        with(skills.skill1) {
            skill(SkillModel(0, skillEntityId, atlasKey, skillName, skillLevel, onLevelUP))
        }
        with(skills.skill2) {
            skill(SkillModel(1, skillEntityId, atlasKey, skillName, skillLevel, onLevelUP))
        }
        with(skills.skill3) {
            skill(SkillModel(2, skillEntityId, atlasKey, skillName, skillLevel, onLevelUP))
        }

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
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillUpgradeView.(S) -> Unit = {}
): SkillUpgradeView = actor(SkillUpgradeView(model, skin), init)