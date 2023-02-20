package com.goldev.skipwave.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.SkipWave.Companion.UNIT_SCALE
import com.goldev.skipwave.component.ImageComponent
import com.goldev.skipwave.event.MapChangeEvent
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer

/**
 * System that takes care of the render in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @property imageCmps Entities with ImageComponent in the world.
 * @constructor Create empty Render system
 */
@AllOf([ImageComponent::class])
class RenderSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>

) : EventListener, IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
) {

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()

    /**
     *  OrthogonalTiledMapRenderer with the map renderer.
     */
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, gameStage.batch)

    /**
     *  Camera of the gameStage as an OrthographicCamera.
     */
    private val orthoCam = gameStage.camera as OrthographicCamera

    /**
     * For each execution of the system, renders the game stage and the UI stage, and their actors.
     */
    override fun onTick() {
        super.onTick()

        with(gameStage) {
            viewport.apply()
            orthoCam.zoom = 1.6f
//            orthoCam.zoom = 4.0f
            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthoCam)
            if (bgdLayers.isNotEmpty()) {
                gameStage.batch.use(orthoCam.combined) {
                    bgdLayers.forEach { mapRenderer.renderTileLayer(it) }
                }
            }

            act(deltaTime)
            draw()

        }

        // render UI
        with(uiStage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }


    }

    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        when {
            event is MapChangeEvent -> {
                bgdLayers.clear()

                event.map.forEachLayer<TiledMapTileLayer> { layer ->
                    bgdLayers.add(layer)
                }
                return true
            }
        }
        return false
    }

    override fun onDispose() {
        mapRenderer.disposeSafely()
    }

}