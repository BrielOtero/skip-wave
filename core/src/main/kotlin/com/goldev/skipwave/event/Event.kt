package com.goldev.skipwave.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.AnimationModel
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.system.SkillUpgradeSystem.*
import com.goldev.skipwave.ui.model.SkillModel
import com.github.quillraven.fleks.Entity

fun Stage.fire(event: Event) {
    this.root.fire(event)
}

/**
 * MapChangeEvent is an event that is fired when the map changes.
 *
 * @property map The map that was changed.
 * @constructor Creates a MapChangeEvent.
 */
data class MapChangeEvent(val map: TiledMap) : Event()

/**
 * NewMapEvent is an event that is fired when there are new map.
 *
 * @property path The path to the map file.
 * @constructor Creates a NewMapEvent.
 */
data class NewMapEvent(val path: String) : Event()

/**
 *  It's an event that is fired when a cell collides with another cell and is destroyed.
 *
 *  @property cell The cell.
 *  @constructor Creates a CollisionDespawnEvent.
 */
class CollisionDespawnEvent(val cell: Cell) : Event()

/**
 *  EntityAttackEvent is an event that is fired when an entity attacks.
 *
 *  @property model The AnimationModel from attack entity.
 *  @constructor Creates an EntityAttackEvent.
 */
class EntityAttackEvent(val model: AnimationModel) : Event()

/**
 *  EntityDeathEvent is an event that is fired when an entity dies.
 *
 *  @property model The AnimationModel from death entity.
 *  @constructor Creates an EntityDeathEvent.
 */
class EntityDeathEvent(val model: AnimationModel) : Event()

/**
 *  EntityLootEvent is an event that is fired when an entity loots an item.
 *
 *  @property model The AnimationModel from loot entity.
 *  @constructor Creates an EntityLootEvent.
 */
class EntityLootEvent(val model: AnimationModel) : Event()

/**
 *  EntityDamageEvent is an event that is fired when an entity is damaged.
 *
 *  @property entity The entity that fired the event.
 *  @constructor Creates an EntityDamageEvent.
 */
class EntityDamageEvent(val entity: Entity) : Event()

/**
 *  EntityAggroEvent is an event that is fired when an entity is aggroed.
 *
 *  @property entity The entity that fired the event.
 *  @constructor Creates an EntityAggroEvent.
 */
class EntityAggroEvent(val entity: Entity) : Event()

/**
 *  It's an event that is fired when an entity is added to the game world.
 *
 *  @property location The location to spawn entity.
 *  @property model The AnimationModel to spawn.
 *  @constructor Creates an EntityDamageEvent.
 */
class EntityAddEvent(val location: Vector2, val model: AnimationModel) : Event()

/**
 *  It's an event that's sent when an enemy is added to the game.
 *
 *  @property model The AnimationModel to add.
 *  @constructor Creates an EnemyAddEvent.
 */
class EnemyAddEvent(val model: AnimationModel) : Event()

/**
 *  It's an event that's sent when player is move.
 */
class MovementEvent : Event()

/**
 *  StartMovementEvent is an Event that contains an x and y coordinate to start movement.
 *
 *  @property x The x position to start movement.
 *  @property y The y position to start movement.
 *  @constructor Creates a StartMovementEvent.
 */
class StartMovementEvent(val x: Float, val y: Float) : Event()

/**
 *  It's an event that's fired when an enemy dies.
 *
 *  @property experienceCmp The ExperienceComponent of the died entity.
 *  @constructor Creates an EnemyDeathEvent.
 */
class EnemyDeathEvent(val experienceCmp: ExperienceComponent) : Event()

/**
 *  PlayerDeathEvent is an event that is fired when a player dies.
 *
 *  @property entity The player entity.
 *  @constructor Creates a PlayerDeathEvent.
 */
class PlayerDeathEvent(val entity: Entity) : Event()

/**
 *  EntityExperienceEvent is an event that is fired when an entity gains experience.
 *
 *  @property entity The entity that gains experience.
 *  @constructor Creates an EntityExperienceEvent.
 */
class EntityExperienceEvent(val entity: Entity) : Event()

/**
 *  EntityLevelEvent is an event that is fired when an entity enters or leaves a level.
 *
 *  @property entity The entity that gains level.
 *  @constructor Creates an EntityLevelEvent.
 */
class EntityLevelEvent(val entity: Entity) : Event()

/**
 *  SkillEvent is an event that if fired when skill panel shows.
 *
 *  @property skill0 The first skill to show.
 *  @property skill1 The second skill to show.
 *  @property skill2 The third skill to show.
 *  @constructor Creates a SkillEvent.
 */
class SkillEvent(val skill0: Skill, val skill1: Skill, val skill2: Skill) : Event()

/**
 * SkillApplyEvent is an event that is fired when a skill is applied.
 *
 * @property skill The skill to be applied
 * @constructor Creates a SkillApplyEvent
 */
class SkillApplyEvent(val skill: SkillModel) : Event()

/**
 *  GamePauseEvent is an event that fired to pause the game.
 */
class GamePauseEvent : Event()

/**
 *  GameResumeEvent is an event that fired to resume the game.
 */
class GameResumeEvent : Event()

/**
 *  SavePreferencesEvent is an event that fired to save preferences.
 */
class SavePreferencesEvent : Event()

/**
 *  ExitGameEvent is an event that fired to exit the game.
 */
class ExitGameEvent : Event()

/**
 *  ButtonPressedEven is an event that fired when button is pressed.
 */
class ButtonPressedEvent : Event()

/**
 *  ShakeEvent is an event that fired when phone is shaked.
 */
class ShakeEvent : Event()

// CHANGE SCREENS

/**
 *  SetGameScreenEvent is an event that is fired when the game screen should be shown.
 */
class SetGameScreenEvent : Event()

/**
 *  It's an event that tells the game to set the main menu screen.
 */
class SetMainMenuScreenEvent : Event()

// SHOW-HIDE VIEWS

/**
 *   It's an event that tells the game to show the settings view.
 *   @property isMainMenuCall The property that tells us if the event was called by main menu.
 *   @constructor Creates a ShowSettingsViewEvent.
 */
class ShowSettingsViewEvent(val isMainMenuCall: Boolean) : Event()

/**
 *  It's an event fired to show the pause view.
 */
class ShowPauseViewEvent : Event()

/**
 *  It's an event fired to hide the pause view.
 */
class HidePauseViewEvent : Event()

/**
 *  It's an event fired to show the tutorial.
 */
class ShowTutorialViewEvent : Event()

/**
 *  It's an event fired to hide the tutorial.
 */
class HideTutorialViewEvent : Event()

/**
 *  It's an event fired to show the main menu.
 */
class ShowMainMenuViewEvent : Event()

/**
 *  It's an event fired to show the credits.
 */
class ShowCreditsViewEvent : Event()


