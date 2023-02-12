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
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class MainMenuView(
    model: MainMenuModel,
    skin: Skin
) : KTable, Table(skin) {

    private val btnNewGame: TextButton
    private val btnSettings: TextButton
    private val btnCredits: TextButton
    private val btnExit: TextButton

    init {

        //UI
        setFillParent(true)
        background = skin[Drawables.FRAME_BGD]
        table { tableCell ->

            label(text = "SKIP WAVE", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.row()
                lblCell.padTop(10f)
                lblCell.padBottom(10f)
                this.setFontScale(0.4f)
            }


            image(skin[Drawables.PLAYER]) { imageCell ->
                setScaling(Scaling.stretch)
                imageCell.height(50f).width(50f).padBottom(10f).row()
            }

            this@MainMenuView.btnNewGame =
                textButton(text = model.bundle["MainMenuView.newGame"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@MainMenuView.btnSettings =
                textButton(text = model.bundle["MainMenuView.settings"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            this@MainMenuView.btnCredits =
                textButton(text = model.bundle["MainMenuView.credits"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }
            this@MainMenuView.btnExit =
                textButton(text = model.bundle["MainMenuView.exit"], style = TextButtons.DEFAULT.skinKey) { cell ->
                    cell.top().padTop(5f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()
                }

            tableCell.expand().padBottom(10f).fill().center()
        }

        //EVENTS
        btnNewGame.onTouchDown {
            log.debug { "BTN: NEW GAME" }
            model.uiStage.fire(ButtonPressedEvent())
            model.gameStage.fire(SetGameScreenEvent())
//            model.gameStage += Actions.fadeOut(ANIMATION_DURATION, Interpolation.circleOut).then(
//                        Actions.run(Runnable() {
//                            kotlin.run {
//                                model.gameStage.fire(SetGameScreenEvent())
//                            }
//                        }),
//                    )
        }

        btnSettings.onTouchDown {
            log.debug { "BTN: SETTINGS" }
            model.uiStage.fire(ButtonPressedEvent())
            model.gameStage.fire(ShowSettingsViewEvent(true))
        }
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
fun <S> KWidget<S>.mainMenuView(
    model: MainMenuModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: MainMenuView.(S) -> Unit = {}
): MainMenuView = actor(MainMenuView(model, skin), init)