package com.gabriel.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.GameModel
import com.gabriel.ui.model.HUDModel
import com.gabriel.ui.widget.CharacterInfo
import com.gabriel.ui.widget.characterInfo
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.collections.filter
import ktx.scene2d.*
import ktx.style.skin

class GameView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {

    private val playerInfo: CharacterInfo
    private val popupLabel: Label

    init {
        // UI


        setFillParent(true)


        table {
            background = skin[Drawables.FRAME_BGD]

            this@GameView.popupLabel = label(text = "", style = Labels.FRAME.skinKey) { lblCell ->
                this.setAlignment(Align.topLeft)
                this.wrap = true
                lblCell.expand().fill().pad(14f)
            }

            this.alpha = 0f
            it.expand().width(130f).height(90f).top().row()
        }
        playerInfo = characterInfo()


        // data binding
        model.onPropertyChange(GameModel::playerLife) { playerLife ->
            playerLife(playerLife)
            setPosition(100f, this@GameView.stage.height - 700f)
        }

        model.onPropertyChange(GameModel::lootText) { lootInfo ->
            popup(lootInfo)
        }
//
//        model.onPropertyChange(GameModel::playerPosition) { playerPosition ->
//            var scale = this@GameView.stage.width / this@GameView.stage.viewport.screenWidth
//            setPosition(playerPosition.x * scale, this@GameView.stage.height - (playerPosition.y * scale))
//        }
    }

//    override fun setPosition(x: Float, y: Float) {
//        super.setPosition(x, y)
//    }

    fun playerLife(percentage: Float) = playerInfo.life(percentage)

    private fun Actor.resetFadeOutDelay() {
        this.actions
            .filterIsInstance<SequenceAction>()
            .lastOrNull()
            ?.let { sequence ->
                val delay = sequence.actions.last() as DelayAction
                delay.time = 0f
            }
    }


    fun popup(infoText: String) {
        popupLabel.txt = infoText
        if (popupLabel.parent.alpha == 0f) {
            popupLabel.parent.clearActions()
            popupLabel.parent += sequence(fadeIn(0.2f), delay(4f, fadeOut(0.75f)))
        } else {
            popupLabel.parent.resetFadeOutDelay()
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)