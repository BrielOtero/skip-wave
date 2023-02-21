package com.goldev.skipwave.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Scaling
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.SkillModel
import ktx.actors.plusAssign
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.scene2d.*

/**
 * The SkillSlot widget.
 *
 * @property skin The skin of the widget.
 * @property uiStage The stage that the UI is being rendered on.
 * @property bundle The bundle with text to show in the UI.
 * @constructor Create empty Skill slot
 */
class SkillSlot(
    private val skin: Skin,
    private val uiStage: Stage,
    private val bundle: I18NBundle,
) : WidgetGroup(), KGroup, KtxInputAdapter {

    /**
     *  A variable that is used to store the background image.
     */
    private val background = Image(skin[Drawables.FRAME_FGD])

    /**
     *  A variable that is used to store the skill image.
     */
    private val skillImage = Image()

    /**
     *  A variable that is used to store the skill label.
     */
    private val skillName: Label = label("", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the skill level label.
     */
    private val skillLevel: Label = label("", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the skill value after label.
     */
    private val skillValueAfter: Label = label("", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the slot width.
     */
    private var slotWidth = uiStage.width * 0.8f

    /**
     *  A variable that is used to store the slot height.
     */
    private var slotHeight = uiStage.height * 0.25f

    /**
     *  A variable that is used to store the margin outer.
     */
    private var marginOuter = 6f

    /**
     *  A variable that is used to store the skill model.
     */
    var skillModel: SkillModel = SkillModel(-1, -1, "", "", 0, 0f)

    /**
     * Starts the view with its components
     */
    init {

        this += background.apply {
            setFillParent(true)
        }
        this += skillImage.apply {
            setSize(slotHeight * 0.3f, slotHeight * 0.3f)
            setPosition((slotWidth / 2) - (skillImage.width / 2), slotHeight - skillImage.height - marginOuter)
            setScaling(Scaling.stretch)
        }
        this += skillName.apply {
            setSize((slotWidth - marginOuter * 2), skillImage.height / 2)
            setPosition(marginOuter, slotHeight - skillImage.height - marginOuter * 2 - skillName.height)
            setFontScale(0.3f)
            toFront()
            setAlignment(Align.center)
        }
        this += skillLevel.apply {
            setSize((slotWidth - marginOuter * 4) / 2, skillImage.height / 2)
            setPosition(marginOuter * 2, skillName.y - skillLevel.height - marginOuter * 2)
            setFontScale(0.3f)
            toFront()
            setAlignment(Align.center)
        }
        this += skillValueAfter.apply {
            setSize((slotWidth - marginOuter * 4) / 2, skillImage.height / 2)
            setPosition(
                skillLevel.x + skillLevel.width + marginOuter,
                skillName.y - skillValueAfter.height - marginOuter * 2
            )
            setFontScale(0.3f)
            setColor(Color.GREEN)
            toFront()
            setAlignment(Align.center)
        }
    }

    /**
     * Gets the preferred width.
     */
    override fun getPrefWidth(): Float = background.drawable.minWidth

    /**
     * Gets the preferred height.
     */
    override fun getPrefHeight(): Float = background.drawable.minHeight

    /**
     * It sets new skill model of the SkillSlot.
     *
     * @param model The new skill model.
     */
    fun skill(model: SkillModel) {
        skillModel = model
        skillImage.drawable = skin.getDrawable(model.atlasKey)
        skillName.setText(model.skillName)
        if (model.onLevelUP > 0) {
            skillValueAfter.setText("+${model.onLevelUP}")
        } else {
            skillValueAfter.setText("${model.onLevelUP}")
        }
        skillLevel.setText("${bundle.get("SkillUpgradeView.level")} ${model.skillLevel}")
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private var log = logger<SkillSlot>()
    }
}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.skillSlot(
    skin: Skin = Scene2DSkin.defaultSkin,
    uiStage: Stage,
    bundle: I18NBundle,
    init: SkillSlot.(S) -> Unit = {}
): SkillSlot = actor(SkillSlot( skin,uiStage, bundle), init)