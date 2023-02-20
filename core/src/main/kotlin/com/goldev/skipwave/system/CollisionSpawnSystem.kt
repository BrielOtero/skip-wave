package com.goldev.skipwave.system

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
import com.goldev.skipwave.component.PhysicComponent
import com.goldev.skipwave.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.goldev.skipwave.component.CollisionComponent
import com.goldev.skipwave.component.TiledComponent
import com.goldev.skipwave.event.CollisionDespawnEvent
import com.goldev.skipwave.event.MapChangeEvent
import ktx.collections.GdxArray
import ktx.box2d.body
import ktx.tiled.*
import ktx.box2d.loop
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2


/**
 * System that takes care of the collision spawn of the game.
 *
 * @property phWorld The physic world.
 * @property physicCmps Entities with PhysicComponent in the world.
 * @constructor Create empty Collision spawn system.
 */
@AllOf([PhysicComponent::class, CollisionComponent::class])
class CollisionSpawnSystem(
    private val phWorld: World,
    private val physicCmps: ComponentMapper<PhysicComponent>,

) : EventListener, IteratingSystem() {

    /**
     *  Array of tiled layers.
     */
    private val tiledLayers = GdxArray<TiledMapTileLayer>()

    /**
     *  A set of cells that have already been processed.
     */
    private val processedCells = mutableSetOf<Cell>()

    /**
     * For each cell in the layer, starting at the given coordinates and going out to the given size,
     * call the given action with the cell, x, and y.
     *
     * @param startX The x coordinate of the center of the circle
     * @param startY The y coordinate of the cell to start at.
     * @param size The size of the area to search.
     * @param action The action to perform on each cell.
     */
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

    /**
     * For each cell in the spawn area, if the cell is not already processed, create an entity with a
     * physic component and a tiled component, and add the entity to the list of nearby entities.
     *
     * @param entity The entity that is currently being processed.
     */
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

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
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
        /**
         *  The size of the area to search for collision objects.
         */
        const val SPAWN_AREA_SIZE = 1
    }

}