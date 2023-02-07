package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.gabriel.event.*
import com.gabriel.ui.Buttons
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.RecordsModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class RecordsView(
    private val model: RecordsModel,
    skin: Skin
) : KTable, Table(skin) {

    private lateinit var lblNewRecordOrReachWaveInfo: Label
    private lateinit var lblReachWaveInfo: Label
    private lateinit var lblReachWave: Label
    private lateinit var lblRecordWave: Label

    init {
        isVisible = false

        //UI
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_FGD]
            label(text = this@RecordsView.model.bundle["RecordsView.title"], style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.height(this@RecordsView.model.uiStage.height * 0.1f).top().row()
                lblCell.padTop(0f)
                setFontScale(0.4f)
                setColor(255f, 0f, 0f, 255f)
            }

            table { interiorCell ->
                background = skin[Drawables.FRAME_BGD]


                textButton(
                    text = this@RecordsView.model.bundle["RecordsView.resume"],
                    style = Buttons.RESUME.skinKey
                ) { cell ->
                    cell.bottom().padTop(10f).padBottom(6f)
                        .height(25f).width(110f)
                        .colspan(2)
                        .row()

                }

                table { gameCell ->
                    background = skin[Drawables.FRAME_FGD]

                    this@RecordsView.lblNewRecordOrReachWaveInfo = label(
                        text = this@RecordsView.model.bundle["RecordsView.newRecord"],
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.expand().width(this@RecordsView.model.uiStage.width * 0.7f).row()
                        lblCell.padTop(6f)
                        this.setFontScale(0.3f)
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

                   gameCell.expand().pad(6f,6f,6f,6f).fill().row()
                }


                table { recordCell ->
                    background = skin[Drawables.FRAME_FGD]

                    label(
                        text = this@RecordsView.model.bundle["RecordsView.waveRecord"],
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.width(this@RecordsView.model.uiStage.width * 0.7f).row()
                        lblCell.padTop(6f)
                        this.setFontScale(0.25f)
                        setAlignment(Align.center)
                        wrap = true

                    }

                    this@RecordsView.lblRecordWave = label(
                        text = "${this@RecordsView.model.gamePreferences.records.wave}",
                        style = Labels.FRAME.skinKey
                    ) { lblCell ->
                        lblCell.row()
                        lblCell.padTop(10f)
                        lblCell.padBottom(6f)
                        this.setFontScale(0.8f)
                        setAlignment(Align.center)
                    }
                    recordCell.expand().fill().pad(6f,6f,6f,6f).row()
                }

                interiorCell.expand().fill().pad(0f, 6f, 0f, 6f).row()
            }

            textButton(
                text = this@RecordsView.model.bundle["RecordsView.button"],
                style = Buttons.DEFAULT.skinKey
            ) { cell ->
                cell.bottom().expand().fill().maxHeight(25f).pad(5f, 10f, 6f, 10f).row()

                onClick {
                    log.debug { "Click on main menu button" }

//                    model.gameStage += Actions.fadeOut(ANIMATION_DURATION, Interpolation.circleOut).then(
//                        Actions.run(Runnable() {
//                            kotlin.run {
//                                model.gameStage.fire(MainMenuScreenEvent())
//
//                            }
//                        }),
//                    )
                    this@RecordsView.model.gameStage.fire(MainMenuScreenEvent())
                }
            }

            padBottom(10f)
            tableCell.expand().fill().maxWidth(this@RecordsView.model.uiStage.width * 0.9f)
                .maxHeight(this@RecordsView.model.uiStage.height * 0.95f)
                .center()
        }

        // data binding
        model.onPropertyChange(RecordsModel::isNewRecord) { isNewRecord ->
            showNewRecord(isNewRecord)
        }
        model.onPropertyChange(RecordsModel::reachWave) { reachWave ->
            lblReachWave.setText(reachWave)
        }
        model.onPropertyChange(RecordsModel::recordWave) { recordWave ->
            lblRecordWave.setText(recordWave)
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