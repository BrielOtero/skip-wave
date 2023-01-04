package com.gabriel.system

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.FloatingTextComponent
import com.github.quillraven.fleks.*
import ktx.math.vec2

@AllOf([FloatingTextComponent::class])
class FloatingTextSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val textCmps: ComponentMapper<FloatingTextComponent>,
) : IteratingSystem() {

    private val uiLocation = vec2()
    private val uiTarget = vec2()

    private fun Vector2.toUiCoordinates(from: Vector2) {
        this.set(from)
        gameStage.viewport.project(this)
        uiStage.viewport.unproject(this)
    }

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
            uiLocation.interpolate(uiTarget, (time / lifeSpan).coerceAtMost(1f), Interpolation.smooth2)
            label.setPosition(uiLocation.x, uiStage.viewport.worldHeight - uiLocation.y)
        }
    }
}