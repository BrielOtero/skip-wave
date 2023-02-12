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

@AllOf([ImageComponent::class])
class RenderSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    @Qualifier("uiStage") private val uiStage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>
) : EventListener, IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
) {

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val fgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, gameStage.batch)
    private val orthoCam = gameStage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

        with(gameStage) {
            viewport.apply()
            orthoCam.zoom = 1.6f
//            orthoCam.zoom = 5.0f
            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthoCam)
            if (bgdLayers.isNotEmpty()) {
                gameStage.batch.use(orthoCam.combined) {
                    bgdLayers.forEach { mapRenderer.renderTileLayer(it) }
                }
            }

            act(deltaTime)
            draw()

            if (fgdLayers.isNotEmpty()) {
                gameStage.batch.use(orthoCam.combined) {
                    fgdLayers.forEach { mapRenderer.renderTileLayer(it) }
                }
            }
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
                fgdLayers.clear()


                event.map.forEachLayer<TiledMapTileLayer> { layer ->
                    if (layer.name.startsWith("fgd_")) {
                        fgdLayers.add(layer)
                    } else {
                        bgdLayers.add(layer)
                    }
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