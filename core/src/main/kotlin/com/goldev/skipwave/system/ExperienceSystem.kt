package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.event.EnemyDeathEvent
import com.goldev.skipwave.event.EntityExperienceEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*
import ktx.log.logger

/**
 * System that takes care of the experience in the game.
 *
 * @property experienceCmps Entities with ExperienceComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Experience system
 */
@AllOf([ExperienceComponent::class])
class ExperienceSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : EventListener, IteratingSystem() {
    /**
     *  A family of entities that have the PlayerComponent.
     */
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

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

                experienceCmps[playerEntities.first()].experience += event.experienceCmp.dropExperience

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