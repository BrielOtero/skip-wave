package com.gabriel.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.event.StartMovementEvent
import com.gabriel.event.fire
import com.gabriel.ui.model.HUDModel
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import ktx.log.logger


class PlayerTouchInputProcessor(
    world: World,
    @Qualifier("uiStage") private val uiStage: Stage,
) : KtxInputAdapter {

    init {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(uiStage) // set your game input precessor as second
        multiplexer.addProcessor(this) // set stage as first input processor


        Gdx.input.inputProcessor = multiplexer

    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        uiStage.fire(StartMovementEvent(screenX.toFloat(), screenY.toFloat()))
        uiStage.touchDown(screenX, screenY, pointer, button)
        return true
    }

    companion object{
        private val log = logger<PlayerTouchInputProcessor>()
    }


}