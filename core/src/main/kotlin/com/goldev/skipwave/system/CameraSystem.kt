package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ImageComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.event.MapChangeEvent
import com.github.quillraven.fleks.*
import ktx.tiled.height
import ktx.tiled.width
import kotlin.math.max
import kotlin.math.min

/**
 * System that takes care of the camera of the game.
 *
 * @property imageCmps Entities with ImageComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Camera system.
 */
@AllOf([PlayerComponent::class, ImageComponent::class])
class CameraSystem(
    private val imageCmps: ComponentMapper<ImageComponent>,
    @Qualifier("gameStage") gameStage: Stage,

) : EventListener, IteratingSystem() {

    /**
     * Variable  that set the max width of the camera.
     */
    private var maxW = 0f

    /**
     *  Variable that set the max height of the camera.
     */
    private var maxH = 0f

    /**
     * Variable  that set the camera to the gameStage.
     */
    private val camera = gameStage.camera

    /**
     * Center the camera on the image, but we make sure that the camera doesn't go outside the
     * bounds of the map.
     *
     * @param entity The entity that the system is currently processing.
     */
    override fun onTickEntity(entity: Entity) {
        // we center on the image because it has an
        // interpolated position for rendering which makes
        // the game smoother

        with(imageCmps[entity]) {
            val viewW = camera.viewportWidth * 0.5f
            val viewH = camera.viewportHeight * 0.5f
            val camMinW = min(viewW, maxW - viewW)
            val camMaxW = max(viewW, maxW - viewW)
            val camMinH = min(viewH, maxH - viewH)
            val camMaxH = max(viewH, maxH - viewH)
            camera.position.set(
                image.x.coerceIn(camMinW, camMaxW),
                image.y.coerceIn(camMinH, camMaxH),
                camera.position.z
            )

        }
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {
            maxW = event.map.width.toFloat()
            maxH = event.map.height.toFloat()
            return true
        }
        return false
    }

}