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

class RecordsView(
    private val model: RecordsModel,
    skin: Skin
) : KTable, Table(skin) {

    private val lblNewRecordOrReachWaveInfo: Label
    private val lblReachWave: Label
    private val lblRecordWave: Label
    private val btnMainMenu: TextButton

    init {
        //UI
        isVisible = false
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_BGD]
            label(text = this@RecordsView.model.bundle["RecordsView.title"], style = Labels.FRAME.skinKey) { lblCell ->
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
                .maxHeight(this@RecordsView.model.uiStage.height * 0.95f)
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

    private fun showNewRecord(isNewRecord: Boolean) {
        if (isNewRecord) {
            lblNewRecordOrReachWaveInfo.setColor(0f, 255f, 0f, 255f)
            lblNewRecordOrReachWaveInfo.setText(model.bundle["RecordsView.newRecord"])
        } else {
            lblNewRecordOrReachWaveInfo.setColor(255f, 255f, 255f, 255f)
            lblNewRecordOrReachWaveInfo.setText(model.bundle["RecordsView.reachWaveInfo"])
        }
    }
    private fun setReachWave(reachWave:Int){
        lblReachWave.setText(reachWave)
    }
    private fun setRecordWave(recordWave:Int){
        lblRecordWave.setText(recordWave)
    }

    companion object {
        private val log = logger<SkillUpgradeView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.recordsView(
    model: RecordsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: RecordsView.(S) -> Unit = {}
): RecordsView = actor(RecordsView(model, skin), init)