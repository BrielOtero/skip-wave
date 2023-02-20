package com.goldev.skipwave.system

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.FloatingTextComponent
import com.goldev.skipwave.event.SkillEvent
import com.github.quillraven.fleks.*
import com.goldev.skipwave.event.ShowPauseViewEvent
import ktx.math.vec2

/**
 * System that takes care of the floating text in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @property textCmps Entities with TextComponent in the world.
 * @constructor Create empty Floating text system
 */
@AllOf([FloatingTextComponent::class])
class FloatingTextSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val textCmps: ComponentMapper<FloatingTextComponent>,

    ) : IteratingSystem(), EventListener {

    /**
     *  A family of entities that have the FloatingTextComponent.
     */
    private val textEntities = world.family(allOf = arrayOf(FloatingTextComponent::class))

    /**
     *  The ui location for text
     */
    private val uiLocation = vec2()

    /**
     *  The ui target location for text
     */
    private val uiTarget = vec2()

    /**
     * It converts a Vector2 from game coordinates to UI coordinates
     *
     * @param from The Vector2 to copy the coordinates from.
     */
    private fun Vector2.toUiCoordinates(from: Vector2) {
        this.set(from)
        gameStage.viewport.project(this)
        uiStage.viewport.unproject(this)
    }

    /**
     * Convert the game coordinates to UI coordinates, interpolate between the two, and set the
     * label's position."
     *
     * @param entity The entity that is being processed.
     */
    override fun onTickEntity(entity: Entity) {
        with(textCmps[entity]) {
            if (time >= lifeSpan) {
                world.remove(entity)
                return
            }

            time += deltaTime

            /**
             * convert game coordinates to UI coordinates
             * 1) project = stage to screen coordinates
             * 2) unproject = screen to stage coordinates
             */
            uiLocation.toUiCoordinates(txtLocation)
            uiTarget.toUiCoordinates(txtTarget)
            uiLocation.interpolate(
                uiTarget,
                (time / lifeSpan).coerceAtMost(1f),
                Interpolation.smooth2
            )
            label.setPosition(uiLocation.x, uiStage.viewport.worldHeight - uiLocation.y)
        }
    }

    /**
     * It removes all the text entities from the world
     */
    private fun clearText() {
        textEntities.forEach { entity ->
            world.remove(entity)
        }
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {

        when (event) {
            is SkillEvent -> clearText()

            is ShowPauseViewEvent -> clearText()

            else -> return false
        }
        return true
    }
}