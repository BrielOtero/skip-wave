package com.gabriel.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.SkillModel
import ktx.actors.plusAssign
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.scene2d.*

class SkillSlot(
    private val slotItemBgd: Drawables?,
    private val uiStage: Stage,
    private val skin: Skin,
) : WidgetGroup(), KGroup, KtxInputAdapter {

    private val background = Image(skin[Drawables.FRAME_BGD])
    private val skillImage = Image()
    val skillName: Label = label("Cooldown", style = Labels.FRAME.skinKey)
    private val skillLevel: Label = label("Level 2", style = Labels.FRAME.skinKey)
    private val skillValueAfter: Label = label("+25", style = Labels.FRAME.skinKey)
    var skillModel: SkillModel = SkillModel(-1, -1, "", "", 0, 0)
    private var slotWidth = uiStage.width * 0.8f
    private var slotHeight = uiStage.height * 0.25f
    private var marginOuter = 6f

    init {

        this += background.apply {
            setFillParent(true)
//            setBounds(0f, 0f, uiStage.width, 80f)
        }
        this += skillImage.apply {
            setSize(slotHeight * 0.3f, slotHeight * 0.3f)
            setPosition((slotWidth / 2) - (skillImage.width / 2), slotHeight - skillImage.height - marginOuter)
            setScaling(Scaling.stretch)
        }
        this += skillName.apply {
            setSize((slotWidth - marginOuter * 2), skillImage.height/2)
            setPosition(marginOuter, slotHeight - skillImage.height - marginOuter*2 - skillName.height)
            setFontScale(0.3f)
            toFront()
            setAlignment(Align.center)
        }
        this += skillLevel.apply {
            setSize((slotWidth - marginOuter * 4) / 2, skillImage.height/2)
            setPosition(marginOuter * 2, skillName.y-skillLevel.height - marginOuter*2)
            setFontScale(0.3f)
            toFront()
            setAlignment(Align.center)
        }
        this += skillValueAfter.apply {
            setSize((slotWidth - marginOuter * 4) / 2, skillImage.height/2)
            setPosition(skillLevel.x + skillLevel.width + marginOuter, skillName.y-skillValueAfter.height - marginOuter*2)
            setFontScale(0.3f)
            setColor(Color.GREEN)
            toFront()
            setAlignment(Align.center)
        }

    }

    override fun getPrefWidth(): Float = background.drawable.minWidth

    override fun getPrefHeight(): Float = background.drawable.minHeight

    fun skill(model: SkillModel) {
        skillModel = model
        skillImage.drawable = skin.getDrawable(model.atlasKey)
        skillName.setText(model.skillName)
        if (model.onLevelUP > 0) {
            skillValueAfter.setText("+${model.onLevelUP}")
        } else {
            skillValueAfter.setText(model.onLevelUP)
        }
        skillLevel.setText("Level ${model.skillLevel}")
    }

    companion object {
        private var log = logger<SkillSlot>()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.skillSlot(
    slotItemBgd: Drawables? = null,
    uiStage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillSlot.(S) -> Unit = {}
): SkillSlot = actor(SkillSlot(slotItemBgd, uiStage, skin), init)