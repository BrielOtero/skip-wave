package com.gabriel.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.AnimationModel
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.Skill
import com.gabriel.ui.model.SkillModel
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
class EntityAddEvent(val location: Vector2, val model: AnimationModel) : Event()
class EnemyAddEvent(val model: AnimationModel) : Event()
class MovementEvent() : Event()
class StartMovementEvent(val x: Float, val y: Float) : Event()
class EnemyDeathEvent(val experienceCmp: ExperienceComponent) : Event()
class PlayerDeathEvent():Event()
class EntityExperienceEvent(val entity: Entity) : Event()
class EntityLevelEvent(val entity: Entity) : Event()
class SkillEvent(val skill0: Skill, val skill1: Skill, val skill2: Skill) : Event()
class SkillApplyEvent(val skill:SkillModel):Event()
class GamePauseEvent ():Event()
class GameResumeEvent ():Event()
class SetGameScreenEvent():Event()
class MainMenuScreenEvent():Event()


class TestEvent():Event()
