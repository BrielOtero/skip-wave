package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.ShowPauseViewEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.Buttons
import com.goldev.skipwave.ui.model.GameModel
import com.goldev.skipwave.ui.widget.PlayerInfo
import com.goldev.skipwave.ui.widget.playerInfo
import ktx.actors.onTouchDown
import ktx.log.logger
import ktx.scene2d.*

class GameView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {
    private val playerInfo: PlayerInfo
    private val btnPause: Button

    init {
        // UI

        setFillParent(true)
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
            model.gameStage.fire(ShowPauseViewEvent())
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

        model.onPropertyChange(GameModel::playerExperienceToNextLevel) { playerExperienceToNextLevel ->
            playerExperienceToNextLevel(playerExperienceToNextLevel)
        }

        model.onPropertyChange(GameModel::playerExperienceBar) { playerExperienceBar ->
            playerExperienceBar(playerExperienceBar)
        }

        //Level
        model.onPropertyChange(GameModel::playerLevel) { playerLevel ->
            playerLevel(playerLevel)
        }

    }

    fun playerLife(newLife: Float) = playerInfo.playerLife(newLife)
    fun playerLifeMax(newLifeMax: Float) = playerInfo.playerLifeMax(newLifeMax)
    fun playerLifeBar(percentage: Float) = playerInfo.playerLifeBar(percentage)
    fun playerExperience(newExperience: Float) = playerInfo.playerExperience(newExperience)
    fun playerExperienceToNextLevel(newExperienceToNextLevel: Float) =
        playerInfo.playerExperienceToNextLevel(newExperienceToNextLevel)

    fun playerExperienceBar(percentage: Float) = playerInfo.playerExperienceBar(percentage, 0.25f)
    fun playerLevel(newLevel: Int) = playerInfo.playerLevel(newLevel)

    companion object{
        private val log = logger<GameView>()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)