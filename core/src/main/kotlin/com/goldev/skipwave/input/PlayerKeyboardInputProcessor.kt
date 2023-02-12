package com.goldev.skipwave.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.AttackComponent
import com.goldev.skipwave.component.MoveComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.component.WeaponComponent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import ktx.math.vec2

class PlayerKeyboardInputProcessor(
    world: World,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val moveCmps: ComponentMapper<MoveComponent> = world.mapper(),
    private val attackCmps: ComponentMapper<AttackComponent> = world.mapper(),
) : KtxInputAdapter {

    private var playerSin = 0f
    private var playerCos = 0f
    private val tmpVec = vec2()
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    private val weaponEntities = world.family(allOf = arrayOf(WeaponComponent::class))


    init {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(uiStage) // set your game input precessor as second
        multiplexer.addProcessor(this) // set stage as first input processor

        Gdx.input.inputProcessor = multiplexer
    }

    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }

    private fun updatePlayerMovement() {
        tmpVec.set(playerCos, playerSin).nor()
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = tmpVec.x
                sin = tmpVec.y
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = 1f
                DOWN -> playerSin = -1f
                RIGHT -> playerCos = 1f
                LEFT -> playerCos = -1f
            }
            updatePlayerMovement()
            return true
        } else if (keycode == SPACE) {
//            weaponEntities.forEach {
//                with(attackCmps[it]) {
//                    doAttack = true
//                }
//            }
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }
}