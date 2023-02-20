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

/**
 * The view of the Pause
 *
 * @param model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Pause view
 */
class PauseView(
    model: PauseModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the resume TextButton.
     */
    private val btnResume: TextButton

    /**
     *  A variable that is used to store the settings TextButton.
     */
    private val btnSettings: TextButton

    /**
     *  A variable that is used to store the main menu TextButton.
     */
    private val btnMainMenu: TextButton

    /**
     * Starts the view with its components
     */
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
        btnResume.onTouchDown {
            log.debug { "BTN: RESUME" }
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnResume.onClick {
            model.gameStage.fire(HidePauseViewEvent())
        }

        btnSettings.onTouchDown{
            log.debug { "BTN: SETTINGS" }
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnSettings.onClick {
            model.gameStage.fire(ShowSettingsViewEvent(false))
        }

        btnMainMenu.onTouchDown {
            log.debug { "BTN: MAIN MENU" }
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnMainMenu.onClick {
            model.gameStage.fire(SetMainMenuScreenEvent())
        }

        // DATA BINDING

    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<PauseView>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.pauseView(
    model: PauseModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: PauseView.(S) -> Unit = {}
): PauseView = actor(PauseView(model, skin), init)