package com.gabriel.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputListener
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
import ktx.actors.alpha
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
    var skillModel: SkillModel? = null

    init {
        this += background.apply {
            setBounds(0f, 0f, uiStage.width, 80f)
        }
        this += skillImage.apply {
            setBounds(
                10f,
                (background.height - background.height * 0.6f) / 2,
                background.height * 0.6f,
                background.height * 0.6f
            )
            log.debug { "Width ${background.width}" }
            setScaling(Scaling.stretch)
        }
        this += skillName.apply {
            setBounds(
                skillImage.x + skillImage.width + 5f,
                background.height - 40f,
                (uiStage.width - skillImage.width - 7)/2,
                20f
            )
            setFontScale(0.2f)
            toFront()
            setAlignment(Align.left)
        }
        this += skillLevel.apply {
            setBounds(
                skillImage.x + skillImage.width + 5f,
                background.height - 60f,
                (uiStage.width - skillImage.width - 7)/2,
                20f
            )
            setFontScale(0.2f)
            toFront()
            setAlignment(Align.left)
        }
        this += skillValueAfter.apply {
            setBounds(skillName.x+skillName.width, background.height-50f, (uiStage.width - skillImage.width - 7)/2, 20f)
            setFontScale(0.2f)
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
        skillName.setText(model.name)
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