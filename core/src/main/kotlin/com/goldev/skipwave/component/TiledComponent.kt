package com.goldev.skipwave.component

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.github.quillraven.fleks.Entity

/**
 *  It's a component that holds a reference to a cell and a set of nearby entities
 */
class TiledComponent {
    /**
     *  It's a variable initialized later with the cell content.
     */
    lateinit var cell: Cell
    /**
     *  It's a variable initialized later with nearby entities.
     */
    val nearbyEntities = mutableSetOf<Entity>()
}