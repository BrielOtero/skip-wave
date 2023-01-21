package com.gabriel.ui.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.gabriel.ui.Drawables
import com.gabriel.ui.get
import ktx.actors.plusAssign
import ktx.scene2d.*

class PlayerInfo(
    private val skin: Skin,
) : WidgetGroup(), KGroup {

    private val lifeUnder: Image = Image(skin[Drawables.LIFE_UNDER])
    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])
    private val experienceUnder: Image = Image(skin[Drawables.LIFE_UNDER])
    private val experienceBar: Image = Image(skin[Drawables.LIFE_BAR])

    init {
        this += lifeUnder.apply {
            setPosition(700f, 125f)
        }
        this += lifeBar.apply {
            setPosition(700f, 125f)
        }
        this += experienceUnder.apply {
            setPosition(700f, 0f)
        }
        this += experienceBar.apply {
            setPosition(700f, 0f)
        }
    }

    override fun getPrefWidth() = lifeUnder.drawable.minWidth

    override fun getPrefHeight() = lifeUnder.drawable.minHeight

    fun life(percentage: Float, duration: Float = 0.75f) {
        lifeBar.clearActions()
        lifeBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }
    fun experience(percentage: Float, duration: Float = 0.75f) {
        experienceBar.clearActions()
        experienceBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

}

@Scene2dDsl
fun <S> KWidget<S>.playerInfo(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: PlayerInfo.(S) -> Unit = {}
): PlayerInfo = actor(PlayerInfo(skin), init)