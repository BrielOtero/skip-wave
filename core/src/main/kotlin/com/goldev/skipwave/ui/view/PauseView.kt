package com.goldev.skipwave.ui.view


import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.model.PauseModel
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class PauseView(
    model: PauseModel,
    skin: Skin
) : KTable, Table(skin) {

    private val btnResume: TextButton
    private val btnSettings: TextButton
    private val btnMainMenu: TextButton

    init {
        //UI
        isVisible = false
        setFillParent(true)
        table { tableCell ->
            background = skin[Drawables.FRAME_BGD]

            label(text = model.bundle["PauseView.title"], style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.height(model.uiStage.height * 0.1f).width(110f).top().row()
                this.setFontScale(0.4f)
                setAlignment(Align.center)
            }

            this@PauseView.btnResume =
                textButton(text = model.bundle["PauseView.resume"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().pad(5f, 6f, 6f, 6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@PauseView.btnSettings =
                textButton(text = model.bundle["PauseView.settings"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().pad(5f, 6f, 6f, 6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@PauseView.btnMainMenu =
                textButton(text = model.bundle["PauseView.mainMenu"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().pad(5f, 6f, 16f, 6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            tableCell.expand().center()
        }

        //EVENTS
        btnResume.onClick {
            log.debug { "BTN: RESUME" }
            model.gameStage.fire(ButtonPressedEvent())
            model.gameStage.fire(HidePauseViewEvent())
        }

        btnSettings.onClick {
            log.debug { "BTN: SETTINGS" }
            model.gameStage.fire(ButtonPressedEvent())
            model.gameStage.fire(ShowSettingsViewEvent(false))
        }

        btnMainMenu.onClick {
            log.debug { "BTN: MAIN MENU" }
            model.gameStage.fire(ButtonPressedEvent())
            model.gameStage.fire(SetMainMenuScreenEvent())
        }

        // DATA BINDING

    }

    companion object {
        private val log = logger<PauseView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.pauseView(
    model: PauseModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: PauseView.(S) -> Unit = {}
): PauseView = actor(PauseView(model, skin), init)