package com.gabriel.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.AnimationModel
import com.github.quillraven.fleks.Entity

fun Stage.fire(event: Event) {
    this.root.fire(event)
}

data class MapChangeEvent(val map: TiledMap) : Event()
class CollisionDespawnEvent(val cell: Cell) : Event()
class EntityAttackEvent(val model: AnimationModel) : Event()
class EntityDeathEvent(val model: AnimationModel) : Event()
class EntityLootEvent(val model: AnimationModel) : Event()
class EntityDamageEvent(val entity: Entity) : Event()
class EntityAggroEvent(val entity: Entity) : Event()
class MovementEvent() : Event()
class StartMovementEvent(val x: Float, val y: Float) : Event()
