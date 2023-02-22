package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.goldev.skipwave.event.ButtonPressedEvent
import com.goldev.skipwave.event.SetMainMenuScreenEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.ui.TextButtons
import com.goldev.skipwave.ui.Drawables
import com.goldev.skipwave.ui.Labels
import com.goldev.skipwave.ui.get
import com.goldev.skipwave.ui.model.RecordsModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

/**
 * The view of the Records
 *
 * @property model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Records view
 */
class RecordsView(
    private val model: RecordsModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the label that is used to show the new record or reach wave info.
     */
    private val lblNewRecordOrReachWaveInfo: Label

    /**
     *  A variable that is used to store the label that is used to show the reach wave info.
     */
    private val lblReachWave: Label

    /**
     *  A variable that is used to store the label that is used to show the record wave.
     */
    private val lblRecordWave: Label

    /**
     *  A variable that is used to store the button that is used to go to the main menu.
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
            label(
                text = this@RecordsView.model.bundle["RecordsView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                lblCell.height(this@RecordsView.model.uiStage.height * 0.1f).top().row()
                setColor(255f, 0f, 0f, 255f)
                setFontScale(0.4f)
            }

            table { interiorCell ->

                table { gameCell ->
                    background = skin[Drawables.FRAME_FGD]

                    this@RecordsView.lblNewRecordOrReachWaveInfo = label(
                        text = this@RecordsView.model.bundle["RecordsView.newRecord"],
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.expand().width(this@RecordsView.model.uiStage.width * 0.7f).row()
                        lblCell.padTop(6f)
                        setFontScale(0.3f)
                        setAlignment(Align.center)
                        wrap = true
                    }

                    this@RecordsView.lblReachWave = label(
                        text = "",
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.expand().row()
                        lblCell.padTop(10f)
                        this.setFontScale(0.8f)
                        setAlignment(Align.center)
                    }

                    gameCell.expand().pad(0f, 6f, 6f, 6f).fill().row()
                }

                table { recordCell ->
                    background = skin[Drawables.FRAME_FGD]

                    label(
                        text = this@RecordsView.model.bundle["RecordsView.waveRecord"],
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.width(this@RecordsView.model.uiStage.width * 0.7f).padTop(6f).row()
                        setFontScale(0.25f)
                        setAlignment(Align.center)
                        wrap = true

                    }
                    this@RecordsView.lblRecordWave = label(
                        text = "${this@RecordsView.model.gamePreferences.game.wave}",
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.padTop(10f).padBottom(6f).row()
                        setFontScale(0.8f)
                        setAlignment(Align.center)
                    }

                    recordCell.expand().fill().pad(6f, 6f, 0f, 6f).row()
                }

                interiorCell.expand().fill().pad(0f, 6f, 0f, 6f).row()
            }

            this@RecordsView.btnMainMenu = textButton(
                text = this@RecordsView.model.bundle["RecordsView.goToMainMenu"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.bottom().expand().fill().maxHeight(25f).pad(5f, 12f, 12f, 12f).row()
            }

            tableCell.expand().fill().maxWidth(this@RecordsView.model.uiStage.width * 0.9f)
                .maxHeight(this@RecordsView.model.uiStage.height * 0.65f)
                .center()
        }

        //EVENTS

        btnMainMenu.onClick {
            log.debug { "BTN: MAIN MENU" }
            model.gameStage.fire(ButtonPressedEvent())
        }
        btnMainMenu.onClick {
            model.gameStage.fire(SetMainMenuScreenEvent())
        }

        // DATA BINDING

        model.onPropertyChange(RecordsModel::isNewRecord) { isNewRecord ->
            showNewRecord(isNewRecord)
        }
        model.onPropertyChange(RecordsModel::reachWave) { reachWave ->
            setReachWave(reachWave)
        }
        model.onPropertyChange(RecordsModel::recordWave) { recordWave ->
            setRecordWave(recordWave)
        }
    }

    /**
     * Set's the content of label depending on if is new record.
     *
     * @param isNewRecord True if is new record.
     */
    private fun showNewRecord(isNewRecord: Boolean) {
        if (isNewRecord) {
            lblNewRecordOrReachWaveInfo.setColor(0f, 255f, 0f, 255f)
            lblNewRecordOrReachWaveInfo.setText(model.bundle["RecordsView.newRecord"])
        } else {
            lblNewRecordOrReachWaveInfo.setColor(255f, 255f, 255f, 255f)
            lblNewRecordOrReachWaveInfo.setText(model.bundle["RecordsView.reachWaveInfo"])
        }
    }

    /**
     * Sets the reach wave to the value passed in.
     *
     * @param reachWave The reach wave to set.
     */
    private fun setReachWave(reachWave: Int) {
        lblReachWave.setText(reachWave)
    }

    /**
     * Sets the record wave to the value passed in.
     *
     * @param recordWave The reach record to set.
     */
    private fun setRecordWave(recordWave: Int) {
        lblRecordWave.setText(recordWave)
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeView>()
    }

}

/**
 * Acts as a view builder by creating it directly in an actor in stages
 */
@Scene2dDsl
fun <S> KWidget<S>.recordsView(
    model: RecordsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: RecordsView.(S) -> Unit = {}
): RecordsView = actor(RecordsView(model, skin), init)