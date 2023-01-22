package com.gabriel.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.gabriel.ui.Drawables
import com.gabriel.ui.get
import com.gabriel.ui.model.SkillModel
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.scene2d.*

class SkillSlot(
    private val slotItemBgd: Drawables?,
    private val skin: Skin,
) : WidgetGroup(), KGroup {

    private val background = Image(skin[Drawables.FRAME_BGD])
    private val slotItemInfo: Image? = if (slotItemBgd == null) null else Image(skin[slotItemBgd])
    private val skillImage = Image()
    var skillModel: SkillModel? = null

    init {
        this += background.apply {
            setFillParent(true)
        }
        slotItemInfo?.let { info ->
            this += info.apply {
                alpha = 0.33f
                setPosition(0f, 0f)
                setSize(background.height, background.height)
                setScaling(Scaling.contain)
            }
        }
        this += skillImage.apply {
            setPosition(0f, 0f)
            setSize(background.height, background.height)
            setScaling(Scaling.contain)
        }
    }

    override fun getPrefWidth(): Float = background.drawable.minWidth

    override fun getPrefHeight(): Float = background.drawable.minHeight

    fun skill(model: SkillModel) {
        skillModel = model
        skillImage.drawable = skin.getDrawable(model.atlasKey)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.skillSlot(
    slotItemBgd: Drawables? = null,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillSlot.(S) -> Unit = {}
): SkillSlot = actor(SkillSlot(slotItemBgd, skin), init)