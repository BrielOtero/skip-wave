package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EnemyDeathEvent
import com.gabriel.event.EntityExperienceEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.*
import ktx.log.logger

@AllOf([ExperienceComponent::class])
class ExperienceSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,
) : EventListener, IteratingSystem() {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    override fun onTickEntity(entity: Entity) {
    }

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
        private val log = logger<ExperienceSystem>()
    }


}