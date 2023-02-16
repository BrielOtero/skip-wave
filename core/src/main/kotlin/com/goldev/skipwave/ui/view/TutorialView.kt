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
import com.goldev.skipwave.ui.model.TutorialModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class TutorialView(
    private val model: TutorialModel,
    skin: Skin
) : KTable, Table(skin) {

    private val btnMainMenu: TextButton

    init {
        //UI
        isVisible = false
        setFillParent(true)

        table { tableCell ->

            this@TutorialView.btnMainMenu = textButton(
                text = this@TutorialView.model.bundle["RecordsView.goToMainMenu"],
                style = TextButtons.DEFAULT.skinKey
            ) { cell ->
                cell.bottom().expand().fill().maxHeight(25f).pad(5f, 12f, 12f, 12f).row()
            }

            tableCell.expand().fill().maxWidth(this@TutorialView.model.uiStage.width * 0.9f)
                .maxHeight(this@TutorialView.model.uiStage.height * 0.95f)
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
    }


    companion object {
        private val log = logger<SkillUpgradeView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.tutorialView(
    model: TutorialModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TutorialView.(S) -> Unit = {}
): TutorialView = actor(TutorialView(model, skin), init)