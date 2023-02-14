package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.SettingsModel
import com.goldev.skipwave.ui.widget.ChangeValue
import com.goldev.skipwave.ui.widget.changeValue
import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.actors.onTouchUp
import ktx.log.logger
import ktx.scene2d.*

class SettingsView(
    private val model: SettingsModel,
    skin: Skin
) : KTable, Table(skin) {

    private var cvMusic: ChangeValue
    private var cvEffects: ChangeValue
    private var btnApplyChanges: TextButton
    private val internalTable: Table


    init {
        isVisible = false

        //UI
        setFillParent(true)

        internalTable = table { tableCell ->

            label(
                text = this@SettingsView.model.bundle["SettingsView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.height(this@SettingsView.model.uiStage.height * 0.1f).padTop(10f).top().row()
                setFontScale(0.4f)
            }

            table { audioCell ->
                background = skin[Drawables.FRAME_FGD_DARK]

                textButton(
                    text = this@SettingsView.model.bundle["SettingsView.audio"],
                    style = TextButtons.TITLE.skinKey
                ) { cell ->
                    cell.width(125f).pad(5f, 0f, 6f, 0f)
                        .height(25f).fill()
                        .row()
                }

                label(
                    text = this@SettingsView.model.bundle["SettingsView.music"],
                    style = Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.height(25f).fill().row()
                    setFontScale(0.25f)
                }

                this@SettingsView.cvMusic = changeValue(
                    this@SettingsView.model.musicVolume,
                    this@SettingsView.model.uiStage,
                    skin,
                    this@SettingsView.model.bundle
                ) { cell ->
                    cell.height(35f).width(110f).padBottom(6f).center().row()
                }


                label(
                    text = this@SettingsView.model.bundle["SettingsView.effects"],
                    style = Labels.FRAME.skinKey
                ) { lblCell ->
                    lblCell.height(25f).fill().row()
                    setFontScale(0.25f)
                }

                this@SettingsView.cvEffects = changeValue(
                    this@SettingsView.model.effectsVolume,
                    this@SettingsView.model.uiStage,
                    skin,
                    this@SettingsView.model.bundle
                ) { changeValueMusic ->
                    changeValueMusic.height(35f).width(110f).padBottom(6f).center().row()

                }

                audioCell.expand().top().width(140f).maxHeight(180f).padBottom(10f).fill().row()
            }

            this@SettingsView.btnApplyChanges = textButton(
                text = this@SettingsView.model.bundle["SettingsView.applyChanges"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.width(140f).padBottom(10f)
                    .height(25f).fill()
                    .row()
            }

            tableCell.fill().width(this@SettingsView.model.uiStage.width * 0.9f)
                .height(this@SettingsView.model.uiStage.height * 0.75f)
                .center()

//            tableCell.expand().fill().maxWidth(this@SettingsView.model.uiStage.width * 0.9f)
//                .maxHeight(this@SettingsView.model.uiStage.height * 0.95f)
//                .center()
        }

        //EVENTS
//        cvMusic.onTouchDown {
//            this@SettingsView.model.gameStage.fire(ButtonPressedEvent())
//        }
        cvMusic.onClick {
            this@SettingsView.model.musicVolume = getValue()
        }
//        cvEffects.onTouchDown {
//            this@SettingsView.model.gameStage.fire(ButtonPressedEvent())
//        }
        cvEffects.onClick {
            this@SettingsView.model.effectsVolume = getValue()
        }
        btnApplyChanges.onTouchDown {
            model.gameStage.fire(ButtonPressedEvent())
            this@SettingsView.model.saveSettings()
            this@SettingsView.isVisible = false
            if (model.isMainMenuCall) {
                model.uiStage.actors.filterIsInstance<MainMenuView>().first().touchable = Touchable.enabled
            }
        }

        // DATA BINDING
        model.onPropertyChange(SettingsModel::isMainMenuCall) { isMainMenuCall ->
            changeBackground(isMainMenuCall)
        }
    }

    private fun changeBackground(isMainMenuCall: Boolean) {
        if (isMainMenuCall) {
            this.background = skin[Drawables.FRAME_BGD]
            internalTable.background = null
        } else {
            this.background = null
            internalTable.background = skin[Drawables.FRAME_BGD]
        }
    }


    companion object {
        private var log = logger<SettingsView>()
    }

}

@Scene2dDsl
fun <S> KWidget<S>.settingsView(
    model: SettingsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(model, skin), init)