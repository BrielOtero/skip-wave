package com.gabriel.ui.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.gabriel.ui.Drawables
import com.gabriel.ui.get
import ktx.actors.plusAssign
import ktx.scene2d.*
import ktx.style.skin

class CharacterInfo(
    private val skin: Skin,
) : WidgetGroup(), KGroup {

    private val background: Image = Image(skin[Drawables.LIFE_UNDER])
    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])

    init {
        this += background
        this += lifeBar.apply { setPosition(0f, 0f) }
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight

    fun life(percentage: Float, duration: Float = 0.75f) {
        lifeBar.clearActions()
        lifeBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

}

@Scene2dDsl
fun <S> KWidget<S>.characterInfo(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CharacterInfo.(S) -> Unit = {}
): CharacterInfo = actor(CharacterInfo(skin), init)