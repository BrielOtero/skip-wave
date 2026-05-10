package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.event.EnemyDeathEvent
import com.goldev.skipwave.event.EntityExperienceEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.log.logger

/**
 * System that takes care of the experience in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Experience system
 */
class ExperienceSystem(
    private val gameStage: Stage = inject("gameStage"),
) : EventListener, IteratingSystem(
    family = family { all(ExperienceComponent) }
) {
    /**
     *  A family of entities that have the PlayerComponent.
     */
    private val playerEntities = world.family { all(PlayerComponent) }

    /**
     * This function is called every tick for every entity in the world.
     *
     * @param entity The entity that is being ticked.
     */
    override fun onTickEntity(entity: Entity) {
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is EnemyDeathEvent -> {
                //Adds experience to player when an enemy has died
//                log.debug { "Experience before ${experienceCmps[playerEntities.first()].experience}" }

                with(world) {
                    playerEntities.first()[ExperienceComponent].experience += event.experienceCmp.dropExperience
                }

//                log.debug { "Experience after ${experienceCmps[playerEntities.first()].experience}" }
                gameStage.fire(EntityExperienceEvent(playerEntities.first()))
            }

            else -> return false
        }
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<ExperienceSystem>()
    }


}
