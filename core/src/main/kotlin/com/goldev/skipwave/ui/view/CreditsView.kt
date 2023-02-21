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

/**
 * The view of the Credits
 *
 * @param model The model of the view
 * @param skin The skin of the view
 * @constructor Creates a empty Credits view
 */
class CreditsView(
    model: CreditsModel,
    skin: Skin
) : KTable, Table(skin) {

    /**
     *  A variable that is used to store the exit button.
     */
    private var btnExit: TextButton

    /**
     *  A variable that is used to store the credits table.
     */
    private var tbCredits = Table().apply {
        for (i in 1..8) {
            this.add(
                textButton(
                    model.bundle["CreditsView.t${i}"],
                    TextButtons.TITLE.skinKey
                ) { txtButton ->
                    label.setFontScale(0.25f)
                }).width(150f).height(20f).padBottom(10f).padRight(2f).row()

                println(model.bundle["CreditsView.c${i}"])
            this.add(
                label(
                    text = model.bundle["CreditsView.c${i}"],
                    style = Labels.FRAME.skinKey
                ) {
                    setFontScale(0.2f)
                    setAlignment(Align.center)
                    wrap = true
                }).width(140f).padBottom(10f).padRight(5f).row()
        }
    }

    /**
     *  A variable that is used to store the scroll pane.
     */
    private var scrollPane = ScrollPane(tbCredits, skin).apply {
        width = 220f
        height = 800f
        setScrollBarPositions(false, true)
        setScrollbarsVisible(true)
    }

    /**
     *  A variable that is used to store the credits view table.
     */
    private var tbCreditsView = Table().apply {
        this.add(
            label(
                text = model.bundle["CreditsView.title"],
                style = Labels.FRAME.skinKey
            ) { lblCell ->
                setFontScale(0.4f)
            }).height(30f).padTop(10f).top().row()
        this.add(scrollPane).width(model.uiStage.width * 0.90f).row()

        this@CreditsView.btnExit =
            textButton(
                text = model.bundle["CreditsView.mainMenu"],
                style = TextButtons.DEFAULT.skinKey
            )
        this.add(this@CreditsView.btnExit).padTop(5f).padBottom(10f).height(25f)
            .width(model.uiStage.width * 0.90f).row()
    }

    /**
     * Starts the view with its components
     */
    init {
        isVisible = false

        //UI
        setFillParent(true)
        background = skin[Drawables.FRAME_BGD]

        add(tbCreditsView)

        //EVENTS

        btnExit.onTouchDown {
            log.debug { "BTN: EXIT" }
            model.gameStage.fire(ButtonPressedEvent())

        }
        btnExit.onClick {
            this@CreditsView.isVisible = false
            model.uiStage.actors.filterIsInstance<MainMenuView>().first().touchable =
                Touchable.enabled
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
fun <S> KWidget<S>.creditsView(
    model: CreditsModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CreditsView.(S) -> Unit = {}
): CreditsView = actor(CreditsView(model, skin), init)