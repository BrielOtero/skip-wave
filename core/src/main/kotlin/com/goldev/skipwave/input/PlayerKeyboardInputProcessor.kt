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

/**
 *  It listens for keyboard input and updates the player's movement and attack components
 *  @param world The world of the game.
 *  @property uiStage The stage that the UI is being rendered on.
 *  @property moveCmps Store entities with move component in the world
 *  @constructor Creates an empty PlayerKeyboardInputProcessor
 */
class PlayerKeyboardInputProcessor(
    world: World,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val moveCmps: ComponentMapper<MoveComponent> = world.mapper(),
) : KtxInputAdapter {

    /**
     *  The property sine of the player with the angle of the direction of movement.
     */
    private var playerSin = 0f

    /**
     *  The property cosine of the player with the angle of the direction of movement.
     */
    private var playerCos = 0f

    /**
     *  Property with a temporary vector.
     */
    private val tmpVec = vec2()

    /**
     *  Property with all the entities with the PlayerComponent.
     */
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))


    init {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(uiStage) // set your game input processor as second
        multiplexer.addProcessor(this) // set stage as first input processor

        Gdx.input.inputProcessor = multiplexer
    }

    /**
     * If the key is up, down, right, or left, then it's a movement key.
     *
     * @return True if is a movement key.
     */
    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }

    /**
     * It updates the movement component of each player entity with the cosine and sine of the player's
     * current angle
     */
    private fun updatePlayerMovement() {
        tmpVec.set(playerCos, playerSin).nor()
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = tmpVec.x
                sin = tmpVec.y
            }
        }
    }

    /**
     * If the key pressed is a movement key, then set the playerSin and playerCos variables to the
     * appropriate values, and update the player movement.
     *
     * @param keycode The keycode of the key that was pressed.
     * @return True if it has been executed.
     */
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
        }
        return false
    }

    /**
     * If the key pressed is a movement key, then set the playerSin and playerCos variables to the
     * appropriate values, and then update player movement.
     *
     * @param keycode The keycode of the key that was pressed.
     * @return true if it has been executed.
     */
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