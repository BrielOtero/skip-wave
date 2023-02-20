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

/**
 * The view of the Settings
 *
 * @property model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Settings view
 */
class SettingsView(
    private val model: SettingsModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the value of the change value in music.
     */
    private var cvMusic: ChangeValue

    /**
     *  A variable that is used to store the value of the change value in effects.
     */
    private var cvEffects: ChangeValue

    /**
     *  A variable that is used to store the value of the TextButton apply changes.
     */
    private var btnApplyChanges: TextButton

    /**
     *  A variable that is used to store the value of the TextButton cancel changes.
     */
    private var btnCancelChanges: TextButton

    /**
     *  A variable that is used to store the value of the internal table.
     */
    private val internalTable: Table

    /**
     * Starts the view with its components
     */
    init {
        isVisible = false

        //UI
        setFillParent(true)

        internalTable = table { tableCell ->

            label(
                text = this@SettingsView.model.bundle["SettingsView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.height(this@SettingsView.model.uiStage.height * 0.1f).padTop(10f).top()
                    .row()
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
                    this@SettingsView.model.gameStage,
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
                    this@SettingsView.model.gameStage,
                    skin,
                    this@SettingsView.model.bundle
                ) { changeValueMusic ->
                    changeValueMusic.height(35f).width(110f).padBottom(6f).center().row()

                }

                audioCell.expand().width(140f).maxHeight(180f).padBottom(0f).fill().row()
            }

            this@SettingsView.btnApplyChanges = textButton(
                text = this@SettingsView.model.bundle["SettingsView.applyChanges"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.width(140f).padBottom(10f)
                    .height(25f).fill()
                    .row()
            }

            this@SettingsView.btnCancelChanges = textButton(
                text = this@SettingsView.model.bundle["SettingsView.cancelChanges"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.width(140f).padBottom(10f)
                    .height(25f).fill()
                    .row()
            }

            tableCell.fill().width(this@SettingsView.model.uiStage.width * 0.9f)
                .height(this@SettingsView.model.uiStage.height * 0.85f)
                .center()

        }

        //EVENTS
        btnApplyChanges.onTouchDown {
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnApplyChanges.onClick {
            this@SettingsView.model.musicVolume = cvMusic.getValue()
            this@SettingsView.model.effectsVolume = cvEffects.getValue()
            this@SettingsView.model.saveSettings()
            this@SettingsView.isVisible = false
            if (model.isMainMenuCall) {
                model.uiStage.actors.filterIsInstance<MainMenuView>().first().touchable =
                    Touchable.enabled
            }
        }
        btnCancelChanges.onTouchDown {
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnCancelChanges.onClick {
            this@SettingsView.isVisible = false
            if (model.isMainMenuCall) {
                model.uiStage.actors.filterIsInstance<MainMenuView>().first().touchable =
                    Touchable.enabled
            }
        }

        // DATA BINDING
        model.onPropertyChange(SettingsModel::isMainMenuCall) { isMainMenuCall ->
            changeBackground(isMainMenuCall)
        }
        model.onPropertyChange(SettingsModel::musicVolume) { musicVolume ->
            setMusicVolume(musicVolume)
        }
        model.onPropertyChange(SettingsModel::effectsVolume) { effectsVolume ->
            setEffectsVolume(effectsVolume)
        }
    }

    /**
     * If the main menu is being called, set the background of the frame to the skin's frame
     * background, otherwise set the background of the internal table to the skin's frame background
     *
     * @param isMainMenuCall If true, the background will be set to the frame background.
     */
    private fun changeBackground(isMainMenuCall: Boolean) {
        if (isMainMenuCall) {
            this.background = skin[Drawables.FRAME_BGD]
            internalTable.background = null
        } else {
            this.background = null
            internalTable.background = skin[Drawables.FRAME_BGD]
        }
    }

    /**
     * This function sets the music volume to the value passed in
     *
     * @param musicVolume The volume of the music.
     */
    private fun setMusicVolume(musicVolume: Int) {
        cvMusic.setValue(musicVolume)
    }

    /**
     * This function sets the effects volume to the value passed in
     *
     * @param effectsVolume The volume of the effects.
     */
    private fun setEffectsVolume(effectsVolume: Int) {
        cvEffects.setValue(effectsVolume)
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private var log = logger<SettingsView>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.settingsView(
    model: SettingsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(model, skin), init)