package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Scaling
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.model.MainMenuModel
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.CreditsModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class CreditsView(
    model: CreditsModel,
    skin: Skin
) : KTable, Table(skin) {

    private val btnExit: TextButton

    init {

        //UI
        setFillParent(true)
        background = skin[Drawables.FRAME_BGD]
        table { tableCell ->

            label(
                text = model.bundle["SettingsView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.height(model.uiStage.height * 0.1f).padTop(10f).top().row()
                setFontScale(0.4f)
            }
            this@CreditsView.btnExit =
                textButton(text = model.bundle["MainMenuView.exit"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            tableCell.expand().padBottom(10f).fill().center()
        }

        //EVENTS


        btnExit.onTouchDown {
            log.debug { "BTN: EXIT" }
            model.uiStage.fire(ButtonPressedEvent())
            model.gameStage.fire(ExitGameEvent())
        }
    }

    companion object {
        private val log = logger<MainMenuView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.creditsView(
    model: CreditsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CreditsView.(S) -> Unit = {}
): CreditsView = actor(CreditsView(model, skin), init)