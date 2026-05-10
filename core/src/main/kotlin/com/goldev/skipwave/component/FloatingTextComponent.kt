package com.goldev.skipwave.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.actors.plusAssign
import ktx.math.vec2

/**
 *  It's a component that holds a label and a target location for that label
 */
class FloatingTextComponent : Component<FloatingTextComponent> {
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

    /**
     * When this component is added to an entity, the label is attached to the UI stage, fades
     * out over the life span, and the target location is set to a random location near the
     * original location.
     *
     * @param entity The entity that the component was added to.
     */
    override fun World.onAdd(entity: Entity) {
        val uiStage = inject<Stage>("uiStage")
        uiStage.addActor(label)
        label += fadeOut(lifeSpan, Interpolation.pow3OutInverse)
        txtTarget.set(
            txtLocation.x + MathUtils.random(-1.5f, 1.5f),
            txtLocation.y + 1f
        )
    }

    /**
     * When this component is removed from an entity, the label is detached from the UI stage.
     *
     * @param entity The entity that the component was removed from.
     */
    override fun World.onRemove(entity: Entity) {
        val uiStage = inject<Stage>("uiStage")
        uiStage.root.removeActor(label)
    }

    override fun type() = FloatingTextComponent

    companion object : ComponentType<FloatingTextComponent>()
}
