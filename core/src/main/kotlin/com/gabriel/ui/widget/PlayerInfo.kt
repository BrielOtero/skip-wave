package com.gabriel.ui.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class PlayerInfo(
    private val skin: Skin,
) : WidgetGroup(), KGroup {
    private val lifeUnder: Image = Image(skin[Drawables.LIFE_UNDER])
    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])
    private val lifeText: Label = label("", style = Labels.FRAME.skinKey)
    private val experienceUnder: Image = Image(skin[Drawables.EXPERIENCE_UNDER])
    private val experienceBar: Image = Image(skin[Drawables.EXPERIENCE_BAR])
    private val experienceText: Label = label("0/50", style = Labels.FRAME.skinKey)
    private val levelText: Label = label("Level 0", style = Labels.FRAME.skinKey)

    private var life = 0
    private var lifeMax = 0
    private var experience = 0
    private var experienceToNextLevel = 50

    init {

        this += lifeUnder.apply {
            setPosition(0f, 36f)
        }
        this += lifeBar.apply {
            setPosition(0f, 36f)
        }
        this += lifeText.apply {
            setSize(lifeUnder.width, lifeUnder.height)
            setPosition(0f, 36f)
            setAlignment(Align.center)
            toFront()
        }

        this += experienceUnder.apply {
            setPosition(0f, 18f)
        }
        this += experienceBar.apply {
            setPosition(0f, 18f)
        }
        this += experienceText.apply {
            setSize(experienceUnder.width, experienceUnder.height)
            setPosition(0f, 18f)
            setAlignment(Align.center)
            toFront()
        }

        this += levelText.apply {
            setPosition(0f, 0f)
        }

        playerExperienceBar(0f, 0f)
    }

    override fun getPrefWidth() = lifeUnder.drawable.minWidth

    override fun getPrefHeight() = lifeUnder.drawable.minHeight

    //Life
    fun playerLife(newLife: Float) {
        life = newLife.toInt()
        lifeText.setText("${life}/${lifeMax}")
    }

    fun playerLifeMax(newLifeMax: Float) {
        lifeMax = newLifeMax.toInt()
        lifeText.setText("${life}/${lifeMax}")
    }

    fun playerLifeBar(percentage: Float, duration: Float = 0.75f) {
        lifeBar.clearActions()
        lifeBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

    //Experience
    fun playerExperience(newExperience: Float) {
        experience = newExperience.toInt()
        experienceText.setText("${experience}/${experienceToNextLevel}")
    }

    fun playerExperienceToNextLevel(newExperienceToNextLevel: Float) {
        experienceToNextLevel = newExperienceToNextLevel.toInt()
        experienceText.setText("${experience}/${experienceToNextLevel}")
    }

    fun playerExperienceBar(percentage: Float, duration: Float = 0.75f) {
        experienceBar.clearActions()
        experienceBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

    fun playerLevel(newLevel: Int) {
        levelText.setText("${"Level"} ${newLevel}")
    }

    companion object {
        private val log = logger<PlayerInfo>()
    }

}

@Scene2dDsl
fun <S> KWidget<S>.playerInfo(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: PlayerInfo.(S) -> Unit = {}
): PlayerInfo = actor(PlayerInfo(skin), init)