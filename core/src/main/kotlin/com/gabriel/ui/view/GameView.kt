package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.gabriel.ui.Labels
import com.gabriel.ui.model.GameModel
import com.gabriel.ui.widget.PlayerInfo
import com.gabriel.ui.widget.playerInfo
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.scene2d.*

class GameView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {
    private val playerInfo: PlayerInfo

    init {
        // UI

        setFillParent(true)
        table {
            it.top().fill().row()
            this@GameView.playerInfo = playerInfo(skin, model.bundle) { cell ->
                setScale(0.8f)
                cell.expand().minHeight(35f).left().padTop(10f).padLeft(2f)
            }
//            it.fill().top().row()
        }
        table {
            it.expand().row()
        }


        // data binding

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

}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)