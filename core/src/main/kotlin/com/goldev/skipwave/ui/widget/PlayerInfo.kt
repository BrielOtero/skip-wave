package com.goldev.skipwave.ui.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

/**
 * The PlayerInfo widget.
 *
 * @property skin The skin of the widget.
 * @property bundle The bundle with text to show in the UI.
 * @constructor Create empty Player info.
 */
class PlayerInfo(
    private val skin: Skin,
    val bundle: I18NBundle,

) : WidgetGroup(), KGroup {

    /**
     *  A variable that is used to store the life under image.
     */
    private val lifeUnder: Image = Image(skin[Drawables.LIFE_UNDER])

    /**
     *  A variable that is used to store the life bar image.
     */
    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])

    /**
     *  A variable that is used to store the life text label.
     */
    private val lifeText: Label = label("", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the experience under image.
     */
    private val experienceUnder: Image = Image(skin[Drawables.EXPERIENCE_UNDER])

    /**
     *  A variable that is used to store the experience bar image.
     */
    private val experienceBar: Image = Image(skin[Drawables.EXPERIENCE_BAR])

    /**
     *  A variable that is used to store the experience text label.
     */
    private val experienceText: Label = label("0/50", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the level text label.
     */
    private val levelText: Label = label("${bundle.get("GameView.wave")} 0", style = Labels.FRAME.skinKey)

    /**
     *  A variable that is used to store the player UI background image.
     */
    private val playerUIBackground: Image = Image(skin[Drawables.FRAME_BGD])

    /**
     *  A variable that is used to store the life.
     */
    private var life = 0

    /**
     *  A variable that is used to store the max life.
     */
    private var lifeMax = 0

    /**
     *  A variable that is used to store the experience.
     */
    private var experience = 0

    /**
     *  A variable that is used to store the experience to next level.
     */
    private var experienceToNextLevel = 50

    /**
     * Starts the view with its components
     */
    init {
        this += playerUIBackground.apply {
            setPosition(0f, 0f)
            setSize(experienceUnder.width + 12f, 40f + lifeUnder.height + 8f)
        }
        this += lifeUnder.apply {
            setPosition(6f, 40f)
        }
        this += lifeBar.apply {
            setPosition(6f, 40f)
        }
        this += lifeText.apply {
            setSize(lifeUnder.width, lifeUnder.height)
            setPosition(6f, 40f)
            setAlignment(Align.center)
            toFront()
        }
        this += experienceUnder.apply {
            setPosition(6f, 22f)
        }
        this += experienceBar.apply {
            setPosition(6f, 22f)
        }
        this += experienceText.apply {
            setSize(experienceUnder.width, experienceUnder.height)
            setPosition(6f, 22f)
            setAlignment(Align.center)
            toFront()
        }

        this += levelText.apply {
            setSize(experienceUnder.width, experienceUnder.height)
            setPosition(6f, 4f)
            setAlignment(Align.center)
            toFront()
        }

        playerExperienceBar(0f, 0f)
    }

    /**
     * Gets the preferred width.
     */
    override fun getPrefWidth() = lifeUnder.drawable.minWidth

    /**
     * Gets the preferred height.
     */
    override fun getPrefHeight() = lifeUnder.drawable.minHeight

    /**
     * It sets the new life of the player.
     *
     * @param newLife The new life of the player.
     */
    fun playerLife(newLife: Float) {
        life = newLife.toInt()
        lifeText.setText("${life}/${lifeMax}")
    }

    /**
     * It sets the new max life of the player.
     *
     * @param newLifeMax The new max life of the player.
     */
    fun playerLifeMax(newLifeMax: Float) {
        lifeMax = newLifeMax.toInt()
        lifeText.setText("${life}/${lifeMax}")
    }

    /**
     * It sets the life bar of the player.
     *
     * @param percentage The percentage of the life bar.
     * @param duration The duration of the animation.
     */
    fun playerLifeBar(percentage: Float, duration: Float = 0.75f) {
        lifeBar.clearActions()
        lifeBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

    /**
     * It sets the new experience of the player.
     *
     * @param newExperience The new experience of the player.
     */
    //Experience
    fun playerExperience(newExperience: Float) {
        experience = newExperience.toInt()
        experienceText.setText("${experience}/${experienceToNextLevel}")
    }

    /**
     * It sets the new experience to next level of the player.
     *
     * @param newExperienceToNextLevel The new experience to next level of the player.
     */
    fun playerExperienceToNextLevel(newExperienceToNextLevel: Float) {
        experienceToNextLevel = newExperienceToNextLevel.toInt()
        experienceText.setText("${experience}/${experienceToNextLevel}")
    }

    /**
     * It sets the experience bar of the player.
     *
     * @param percentage The percentage of the experience bar.
     * @param duration The duration of the animation.
     */
    fun playerExperienceBar(percentage: Float, duration: Float = 0.75f) {
        experienceBar.clearActions()
        experienceBar += scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }

    /**
     * It sets the wave of the player.
     *
     * @param newWave The wave of the player.
     */
    fun playerWave(newWave: Int) {
        levelText.setText("${bundle.get("GameView.wave")} $newWave")
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<PlayerInfo>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.playerInfo(
    skin: Skin = Scene2DSkin.defaultSkin,
    bundle: I18NBundle,
    init: PlayerInfo.(S) -> Unit = {}
): PlayerInfo = actor(PlayerInfo(skin, bundle), init)