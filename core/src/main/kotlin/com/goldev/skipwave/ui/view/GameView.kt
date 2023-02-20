package com.goldev.skipwave.ui.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.ShowPauseViewEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.Buttons
import com.goldev.skipwave.ui.model.GameModel
import com.goldev.skipwave.ui.widget.PlayerInfo
import com.goldev.skipwave.ui.widget.playerInfo
import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.log.logger
import ktx.scene2d.*

/**
 * The bundle with text to show in the UI.
 *
 * @param model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Game view
 */
class GameView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {

    /**
     *  A variable that is used to store the player info widget.
     */
    private val playerInfo: PlayerInfo

    /**
     *  A variable that is used to store the pause button.
     */
    val btnPause: Button

    /**
     * Starts the view with its components
     */
    init {
        // UI
        setFillParent(true)

        if (!skin.has(PIXMAP_KEY, TextureRegionDrawable::class.java)) {
            skin.add(PIXMAP_KEY, TextureRegionDrawable(
                Texture(
                    Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
                        this.drawPixel(0, 0, Color.rgba8888(0.1f, 0.1f, 0.1f, 0.7f))
                    }
                )
            ))
        }

        table {
            it.top().fill().row()
            this@GameView.playerInfo = playerInfo(skin, model.bundle) { cell ->
                setScale(0.8f)
                cell.expand().minHeight(35f).left().padTop(18f).padLeft(2f)
            }
            this@GameView.btnPause = button(style = Buttons.PAUSE.skinKey, skin = skin) { cell ->
                cell.expand().top().right().padTop(4f).padRight(4f)
            }
        }
        table {
            it.expand().row()
        }

        //EVENTS

        btnPause.onTouchDown {
            log.debug { "BTN: PAUSE" }
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnPause.onClick {
            if (!model.uiStage.actors.filterIsInstance<SkillUpgradeView>().first().isVisible) {
                model.gameStage.fire(ShowPauseViewEvent())
            }
        }

        // DATA BINDING

        //Life
        model.onPropertyChange(GameModel::playerLife) { playerLife ->
            playerLife(playerLife)
        }
        model.onPropertyChange(GameModel::playerLifeMax) { playerLifeMax ->
            playerLifeMax(playerLifeMax)
        }
        model.onPropertyChange(GameModel::playerLifeBar) { playerLifeBar ->
            playerLifeBar(playerLifeBar)
        }

        //Experience
        model.onPropertyChange(GameModel::playerExperience) { playerExperience ->
            playerExperience(playerExperience)
        }

        model.onPropertyChange(GameModel::playerExperienceToNextWave) { playerExperienceToNextLevel ->
            playerExperienceToNextWave(playerExperienceToNextLevel)
        }

        model.onPropertyChange(GameModel::playerExperienceBar) { playerExperienceBar ->
            playerExperienceBar(playerExperienceBar)
        }

        //Level
        model.onPropertyChange(GameModel::playerWave) { playerLevel ->
            playerWave(playerLevel)
        }

    }

    /**
     * It sets the player's life to the value of newLife.
     *
     * @param newLife The new life of the player.
     */
    fun playerLife(newLife: Float) = playerInfo.playerLife(newLife)

    /**
     * Sets the player's maximum life to the value passed in.
     *
     * @param newLifeMax The new maximum life of the player.
     */
    fun playerLifeMax(newLifeMax: Float) = playerInfo.playerLifeMax(newLifeMax)

    /**
     * Sets the player's life bar to the value passed in.
     *
     * @param percentage The percentage of the player's life.
     */
    fun playerLifeBar(percentage: Float) = playerInfo.playerLifeBar(percentage)

    /**
     * Sets the player's experience to the value passed in.
     *
     * @param newExperience The new experience of the player.
     */
    fun playerExperience(newExperience: Float) = playerInfo.playerExperience(newExperience)

    /**
     * Sets the player's experience to next wave to the value passed in.
     *
     * @param newExperienceToNextWave The new experience to next wave.
     */
    fun playerExperienceToNextWave(newExperienceToNextWave: Float) =
        playerInfo.playerExperienceToNextLevel(newExperienceToNextWave)

    /**
     * Sets the player's experience bar to the value passed in.
     *
     * @param percentage The percentage of the bar to fill.
     */
    fun playerExperienceBar(percentage: Float) = playerInfo.playerExperienceBar(percentage, 0.25f)

    /**
     * Sets the player's wave to the value passed in.
     *
     * @param newWave The new wave of the player.
     */
    fun playerWave(newWave: Int) = playerInfo.playerLevel(newWave)

    /**
     * If the show parameter is true, then set the background of the GameView to the pixmap
     *
     * @param show Whether to show the background or not
     */
    fun showBackground(show: Boolean) {
        if (show) {
            this@GameView.background = skin.get(PIXMAP_KEY, TextureRegionDrawable::class.java)
        } else {
            this@GameView.background = null
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<GameView>()

        /**
         *  It's a constant that is used to store the key of the pixmap.
         */
        private const val PIXMAP_KEY = "pauseTexture"
    }
}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)