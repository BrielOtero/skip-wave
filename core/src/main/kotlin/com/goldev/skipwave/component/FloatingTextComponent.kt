package com.goldev.skipwave.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import ktx.actors.plusAssign
import ktx.math.vec2

/**
 *  It's a component that holds a label and a target location for that label
 */
class FloatingTextComponent {
    /**
     *  It's a vector2 with the position for text.
     */
    val txtLocation = vec2()

    /**
     *  It's a vector2 with the target position for text.
     */
    var txtTarget = vec2()

    /**
     *  It's a variable with the life span for the text.
     */
    var lifeSpan = 0f

    /**
     *  It's a variable with the time.
     */
    var time = 0f

    /**
     *  It's a variable with the text to show.
     */
    lateinit var label: Label

    companion object {
        /* It adds a label to the UI stage, and then fades it out over a period of time */

        /**
         *  It listens for FloatingTextEvent that adds a label to the UI stage, and then fades it out over a period of time
         *
         *  @property  uiStage The stage that the ui is being rendered on.
         *  @constructor Creates an FloatingTextComponentListener
         */
        class FloatingTextComponentListener(
            @Qualifier("uiStage") private val uiStage: Stage
        ) : ComponentListener<FloatingTextComponent> {

            /**
             * When a FloatingTextComponent is added to an entity, add the label to the UI stage, fade
             * it out over the life span, and set the target location to a random location near the
             * original location.
             *
             *
             * @param entity The entity that the component is attached to.
             * @param component The component that was added to the entity
             */
            override fun onComponentAdded(entity: Entity, component: FloatingTextComponent) {
                uiStage.addActor(component.label)
                component.label += fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)
                component.txtTarget.set(
                    component.txtLocation.x + MathUtils.random(-1.5f, 1.5f),
                    component.txtLocation.y + 1f
                )
            }

            /**
             * When a FloatingTextComponent is removed from an entity, remove the label from the UI
             * stage.
             *
             * @param entity The entity that the component was removed from.
             * @param component The component that was added to the entity.
             */
            override fun onComponentRemoved(entity: Entity, component: FloatingTextComponent) {
                uiStage.root.removeActor(component.label)
            }
        }
    }
}