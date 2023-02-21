package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.goldev.skipwave.event.SkillApplyEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.SkillModel
import com.goldev.skipwave.ui.model.Skills
import com.goldev.skipwave.ui.model.SkillUpgradeModel
import com.goldev.skipwave.ui.widget.SkillSlot
import com.goldev.skipwave.ui.widget.skillSlot
import ktx.actors.alpha
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.log.logger
import ktx.scene2d.*

/**
 * The view of the SkillUpgrade
 *
 * @property model The model of the view
 * @param skin The skin of the view
 * @constructor Creates an empty Skill Upgrade view
 */
class SkillUpgradeView(
    val model: SkillUpgradeModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  It's a list of skill slots.
     */
    private val skillSlots = mutableListOf<SkillSlot>()

    /**
     * Starts the view with its components
     */
    init {
        //UI
        this.alpha = 0f
        this.isVisible = false
        setPosition(-500f, 0f)
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_BGD]
            label(
                text = this@SkillUpgradeView.model.bundle["SkillUpgradeView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.padTop(10f).padBottom(8f).row()
                this.setFontScale(0.4f)
            }

            for (i in 1..3) {
                this@SkillUpgradeView.skillSlots += skillSlot(
                    skin = skin,
                    uiStage = this@SkillUpgradeView.model.uiStage,
                    bundle = this@SkillUpgradeView.model.bundle
                ) { skillCell ->
                    skillCell.expand().width(this@SkillUpgradeView.model.uiStage.width * 0.8f)
                        .height(this@SkillUpgradeView.model.uiStage.height * 0.25f).center()
                        .row()
                    skillCell.pad(0f, 10f, 4f, 10f)

                    onTouchDown {
                        this@SkillUpgradeView.setPosition(-500f, 0f)
                        this@SkillUpgradeView += Actions.sequence(Actions.fadeOut(0.2f)).then(
                            Actions.run(Runnable() {
                                kotlin.run {
                                    this@SkillUpgradeView.isVisible = false
                                }
                            }),
                        )
                        this@SkillUpgradeView.model.gameStage.fire(SkillApplyEvent(skillModel))

                    }

                }
            }
            padBottom(10f)
            tableCell.expand().maxWidth(this@SkillUpgradeView.model.uiStage.width * 0.9f)
                .maxHeight(this@SkillUpgradeView.model.uiStage.height * 0.98f).center()
        }

        // DATA BINDING
        model.onPropertyChange(SkillUpgradeModel::skills) { skills ->
            popupSkills(skills)
        }

    }

    /**
     * Translate the skills
     *
     * @param skillEntityId The skillEntity to translate.
     * @return The text.
     */
    fun translateSkill(skillEntityId: Int): String {
        when (skillEntityId) {
            0 -> return model.bundle["Skills.life"]
            1 -> return model.bundle["Skills.regeneration"]
            2 -> return model.bundle["Skills.speed"]
            3 -> return model.bundle["Skills.cooldown"]
            4 -> return model.bundle["Skills.damage"]
        }

        return ""
    }

    /**
     * It populates the skill upgrade view with the skills of the player.
     *
     * @param skills The skills object that contains the skills to be displayed
     */
    fun popupSkills(skills: Skills) {

        with(skills.skill1) {
            skill(SkillModel(0, skillEntityId, atlasKey, translateSkill(skillEntityId), skillLevel, onLevelUP))
        }
        with(skills.skill2) {
            skill(SkillModel(1, skillEntityId, atlasKey, translateSkill(skillEntityId), skillLevel, onLevelUP))
        }
        with(skills.skill3) {
            skill(SkillModel(2, skillEntityId, atlasKey, translateSkill(skillEntityId), skillLevel, onLevelUP))
        }

        if (this.alpha == 0f) {
            this.clearActions()
            this.setPosition(0f, 0f)
            this@SkillUpgradeView.isVisible = true
            this += Actions.sequence(Actions.fadeIn(0.2f))
        }
    }

    /**
     * Creates new skill
     *
     * @param skillModel The skill model that you want to use.
     */
    fun skill(skillModel: SkillModel) {
        skillSlots[skillModel.slotIdx].skill(skillModel)
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeView>()
    }
}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.skillUpgradeView(
    model: SkillUpgradeModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillUpgradeView.(S) -> Unit = {}
): SkillUpgradeView = actor(SkillUpgradeView(model, skin), init)