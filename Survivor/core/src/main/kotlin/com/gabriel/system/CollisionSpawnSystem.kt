package com.gabriel.system

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.ComponentMapper
import com.gabriel.component.PhysicComponent
import com.gabriel.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.gabriel.component.CollisionComponent
import com.gabriel.component.TiledComponent
import com.gabriel.event.CollisionDespawnEvent
import com.gabriel.event.MapChangeEvent
import ktx.collections.GdxArray
import ktx.box2d.body
import ktx.box2d.circle
import ktx.tiled.*
import ktx.box2d.loop
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2


@AllOf([PhysicComponent::class, CollisionComponent::class])
class CollisionSpawnSystem(
    private val phWorld: World,
    private val physicCmps: ComponentMapper<PhysicComponent>,
) : EventListener, IteratingSystem() {

    private val tiledLayers = GdxArray<TiledMapTileLayer>()
    private val processedCells = mutableSetOf<Cell>()

    private fun TiledMapTileLayer.forEachCell(
        startX: Int,
        startY: Int,
        size: Int,
        action: (Cell, Int, Int) -> Unit
    ) {
        for (x in startX - size..startX + size) {
            for (y in startY - size..startY + size) {
                this.getCell(x, y)?.let { action(it, x, y) }
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        val (entityX, entityY) = physicCmps[entity].body.position

        tiledLayers.forEach { layer ->
            layer.forEachCell(entityX.toInt(), entityY.toInt(), SPAWN_AREA_SIZE) { cell, x, y ->
                if (cell.tile.objects.isEmpty()) {
                    // cell is not linked to a collision object -> no nothing
                    return@forEachCell
                }
                if (cell in processedCells) {
                    return@forEachCell
                }

                processedCells.add(cell)
                cell.tile.objects.forEach { mapObject ->
                    world.entity {
                        physicCmpFromShape2D(phWorld, x, y, mapObject.shape)
                        add<TiledComponent> {
                            this.cell = cell
                            nearbyEntities.add(entity)
                        }
                    }
                }
            }
        }
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
                event.map.layers.getByType(TiledMapTileLayer::class.java, tiledLayers)

                // world boundary chain shape to restrict movement within tiled map
                world.entity {
                    val w = event.map.width.toFloat()
                    val h = event.map.height.toFloat()

                    add<PhysicComponent> {
                        body = phWorld.body(StaticBody) {
                            position.set(0f, 0f)
                            fixedRotation = true
                            allowSleep = false
                            loop(
                                vec2(0f, 0f),
                                vec2(w, 0f),
                                vec2(w, h),
                                vec2(0f, h),
                            )
                        }
                    }
                }
                return true
            }

            is CollisionDespawnEvent -> {
                processedCells.remove(event.cell)
                return true
            }

            else -> return false
        }
    }

    companion object {
        const val SPAWN_AREA_SIZE = 3
    }

}