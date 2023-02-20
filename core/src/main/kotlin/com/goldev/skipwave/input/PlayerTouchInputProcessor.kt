package com.goldev.skipwave.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.view.TouchpadView
import com.goldev.skipwave.ui.view.TutorialView
import ktx.app.KtxInputAdapter
import ktx.log.logger


/**
 *  It's an input processor that listens for touch events and fires a StartMovementEvent when it receives one
 *
 *  @property uiStage The stage that the UI is being rendered on.
 *  @constructor Creates a empty PlayerTouchInputProcessor
 */
class PlayerTouchInputProcessor(
    @Qualifier("uiStage") private val uiStage: Stage,
) : KtxInputAdapter {

    init {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(uiStage) // set your game input processor as second
        multiplexer.addProcessor(this) // set stage as first input processor

        Gdx.input.inputProcessor = multiplexer
    }


    /**
     * When the user touches the screen, fire a StartMovementEvent and pass the touch coordinates to
     * the UI Stage
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer The index of the pointer that triggered the event.
     * @param button The button that was pressed.
     * @return True if it has been executed.
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        log.debug { "EVENT: TouchDown touchpad" }
        uiStage.fire(StartMovementEvent(screenX.toFloat(), screenY.toFloat()))
        uiStage.touchDown(screenX, screenY, pointer, button)
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<PlayerTouchInputProcessor>()
    }


}