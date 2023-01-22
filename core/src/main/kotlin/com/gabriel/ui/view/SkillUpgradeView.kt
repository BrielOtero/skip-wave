package com.gabriel.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.gabriel.ui.Drawables
import com.gabriel.ui.Labels
import com.gabriel.ui.get
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.widget.SkillSlot
import com.gabriel.ui.widget.skillSlot
import ktx.scene2d.*

class SkillUpgradeView(
    private val model: SkillUpgradeModel,
    skin: Skin
) : KTable, Table(skin) {

    private val skillSlots = mutableListOf<SkillSlot>()

    init {
        //UI
        setFillParent(true)

        table { tableCell ->
            background = skin[Drawables.FRAME_BGD]
            label(text = "Level UP!", style = Labels.FRAME.skinKey) { lblCell ->
                lblCell.fill().row()
                lblCell.padBottom(5f)
            }
            for (i in 1..4) {
                this@SkillUpgradeView.skillSlots += skillSlot(skin = skin) { skillCell ->
                    skillCell.padBottom(2f)
                    skillCell.left()
                    skillCell.row()
                }
            }
            tableCell.expand().width(90f).height(120f).center()
        }
        // data binding
    }
}

@Scene2dDsl
fun <S> KWidget<S>.skillUpgradeView(
    model: SkillUpgradeModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SkillUpgradeView.(S) -> Unit = {}
): SkillUpgradeView = actor(SkillUpgradeView(model, skin), init)