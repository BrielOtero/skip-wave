package com.goldev.skipwave.ui.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.*
import com.goldev.skipwave.ui.model.CreditsModel
import ktx.actors.*
import ktx.log.logger
import ktx.scene2d.*

class CreditsView(
    model: CreditsModel,
    skin: Skin
) : KTable, Table(skin) {
    private var btnExit: TextButton
    private var tbCredits = Table().apply {
        for (i in 1..8) {
            this.add(
                textButton(model.bundle["Credits.t${i}"], TextButtons.TITLE.skinKey) { txtButton ->
                    label.setFontScale(0.3f)
                }).width(150f).height(25f).padBottom(10f).row()

            this.add(
                label(
                    text = model.bundle["Credits.c${i}"],
                    style = Labels.FRAME.skinKey
                ) {
                    setFontScale(0.3f)
                    setAlignment(Align.center)
                    wrap = true
                }).width(150f).padBottom(10f).row()
        }
    }

    private var scrollPane = ScrollPane(tbCredits, skin).apply {
        width = 200f
        height = 800f
        background = skin[Drawables.FRAME_FGD]
        setScrollBarPositions(false, true)
    }
    private var tbCreditsView = Table().apply {
        this.add(
            label(
                text = model.bundle["Credits.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                setFontScale(0.4f)
            }).height(30f).padTop(10f).top().row()
        this.add(scrollPane).row()

        this@CreditsView.btnExit =
            textButton(text = model.bundle["Credits.mainMenu"], style = TextButtons.DEFAULT.skinKey)
        this.add(this@CreditsView.btnExit).padTop(5f).padBottom(6f).height(25f).width(110f).row()
    }

    init {

        //UI
        setFillParent(true)
//        isVisible=false
        background = skin[Drawables.FRAME_BGD]

        add(tbCreditsView)

//            tableCell.expand().padBottom(10f).fill().center()

        //EVENTS

        btnExit.onTouchDown {
            log.debug { "BTN: EXIT" }
            model.uiStage.fire(ButtonPressedEvent())
            model.gameStage.fire(ExitGameEvent())

            this@CreditsView.isVisible = false
            model.uiStage.actors.filterIsInstance<MainMenuView>().first().touchable = Touchable.enabled
        }
    }

    companion object {
        private val log = logger<MainMenuView>()
    }

}


@Scene2dDsl
fun <S> KWidget<S>.creditsView(
    model: CreditsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CreditsView.(S) -> Unit = {}
): CreditsView = actor(CreditsView(model, skin), init)