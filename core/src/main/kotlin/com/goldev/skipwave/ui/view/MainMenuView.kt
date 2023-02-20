package com.goldev.skipwave.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.goldev.skipwave.SkipWave.Companion.ANIMATION_DURATION
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.model.MainMenuModel
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*
import java.util.jar.Manifest

/**
 * The view of the MainMenu
 *
 * @param model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty MainMenu view
 */
class MainMenuView(
    model: MainMenuModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the new game button.
     */
    private val btnNewGame: TextButton

    /**
     *  A variable that is used to store the settings button.
     */
    private val btnSettings: TextButton

    /**
     *  A variable that is used to store the credits button.
     */
    private val btnCredits: TextButton

    /**
     *  A variable that is used to store the exit button.
     */
    private val btnExit: TextButton

    /**
     *  A variable that stores the version of the game.
     */
    private val versionName: String = "1.5"

    /**
     * Starts the view with its components
     */
    init {
        //UI
        setFillParent(true)
        background = skin[Drawables.FRAME_BGD]
        table { tableCell ->

            label(text = "SKIP WAVE", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.height(50f).width(150f).padBottom(10f).padTop(70f).row()
                setAlignment(Align.center)
                this.setFontScale(0.4f)
            }

            image(skin[Drawables.PLAYER]) { imageCell ->
                setScaling(Scaling.stretch)
                imageCell.height(50f).width(50f).padBottom(10f).row()
            }

            this@MainMenuView.btnNewGame =
                textButton(
                    text = model.bundle["MainMenuView.newGame"],
                    style = TextButtons.DEFAULT.skinKey
                ) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@MainMenuView.btnSettings =
                textButton(
                    text = model.bundle["MainMenuView.settings"],
                    style = TextButtons.DEFAULT.skinKey
                ) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@MainMenuView.btnCredits =
                textButton(
                    text = model.bundle["MainMenuView.credits"],
                    style = TextButtons.DEFAULT.skinKey
                ) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }
            this@MainMenuView.btnExit =
                textButton(
                    text = model.bundle["MainMenuView.exit"],
                    style = TextButtons.DEFAULT.skinKey
                ) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }
            label(
                text = "${model.bundle["MainMenuView.version"]}: ${this@MainMenuView.versionName}",
                style = Labels.FRAME.skinKey
            ) { cell ->
                cell.bottom().expand().padTop(5f).padBottom(0f).row()
                this.setFontScale(0.2f)
            }
            label(
                text = model.bundle["MainMenuView.madeBy"],
                style = Labels.FRAME.skinKey
            ) { cell ->
                cell.bottom().expand().padTop(1f).padBottom(6f).row()
                this.setFontScale(0.2f)
            }

            tableCell.expand().padBottom(10f).fill().center()
        }

        //EVENTS
        btnNewGame.onTouchDown {
            log.debug { "BTN: NEW GAME" }
            model.uiStage.fire(ButtonPressedEvent())
        }
        btnNewGame.onClick {
            model.gameStage += Actions.fadeOut(ANIMATION_DURATION, Interpolation.elasticOut).then(
                Actions.run(Runnable() {
                    kotlin.run {
                        model.gameStage.fire(SetGameScreenEvent())
                    }
                }),
            )
        }

        btnSettings.onTouchDown {
            log.debug { "BTN: SETTINGS" }
            model.uiStage.fire(ButtonPressedEvent())

        }
        btnSettings.onClick {
            this@MainMenuView.touchable = Touchable.disabled
            model.gameStage.fire(ShowSettingsViewEvent(true))
        }
        btnCredits.onTouchDown {
            log.debug { "BTN: CREDITS" }
            model.uiStage.fire(ButtonPressedEvent())
        }
        btnCredits.onClick {
            this@MainMenuView.touchable = Touchable.disabled
            model.gameStage.fire(ShowCreditsViewEvent())
        }
        btnExit.onTouchDown {
            log.debug { "BTN: EXIT" }
        }
        btnExit.onClick {
            model.uiStage.fire(ButtonPressedEvent())
            model.gameStage.fire(ExitGameEvent())
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<MainMenuView>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.mainMenuView(
    model: MainMenuModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: MainMenuView.(S) -> Unit = {}
): MainMenuView = actor(MainMenuView(model, skin), init)